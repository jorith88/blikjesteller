package nl.jorith.blikjesteller.facade;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;

import nl.jorith.blikjesteller.config.ApplicationConfig;
import nl.jorith.blikjesteller.config.Configuration;
import nl.jorith.blikjesteller.rest.type.Blikje;

@Stateless
public class EmailFacade {
	private static final Logger LOGGER = Logger.getLogger(EmailFacade.class.getName());
	
	@Inject
	private Configuration config;
	
	public void sendBlikjesOrder(List<Blikje> blikjes, boolean debugMode) {

		String mailBody = getEmailBody("order", velocityContext -> {
			velocityContext.put("blikjes", blikjes);
	    });

		ApplicationConfig applicationConfig = config.getApplicationConfig();

		String emailTo = debugMode ?
				applicationConfig.getDebugOrderEmail() : applicationConfig.getOrderEmail();

		String emailBcc = applicationConfig.getOrderEmailBcc();

		LOGGER.log(Level.INFO, "Send blikjes order to " + emailTo + ". BCC to " + emailBcc);

		sendMail(emailTo, emailBcc, "blikje@jorith.nl", "Blikjesbestelling", mailBody);
	}

	private void sendMail(String to, String bcc, String from, String subject, String message) {
		Properties props = new Properties();
        props.put("mail.smtp.host", "192.168.150.2");
        props.put("mail.smtp.port", "25");

        Session mailSession = Session.getDefaultInstance(props);
        Message msg = new MimeMessage(mailSession);

        InternetAddress fromAddress = null;
        try {
			fromAddress = new InternetAddress(from);

			msg.setFrom(fromAddress);

			msg.addRecipient(RecipientType.TO, new InternetAddress(to));
			msg.addRecipient(RecipientType.BCC, new InternetAddress(bcc));

			msg.setSubject(subject);

			msg.setContent(message, "text/html");

			Transport.send(msg);
		} catch (MessagingException e) {
			LOGGER.log(Level.SEVERE, "Error Sending Message", e);
			throw new RuntimeException(e);
		}
	}

	private String getEmailBody(String templateName, Consumer<VelocityContext> setupVelocityContext) {

		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();

		VelocityContext context = new VelocityContext();

		// First, add some generic viewtools
		context.put("numberTool", new NumberTool());
		context.put("mathTool", new MathTool());

		// Then, dynamically add additional objects to the context
		setupVelocityContext.accept(context);

		Template template = ve.getTemplate( "/email-templates/" + templateName + ".vtl" );

		String emailBody;
		try (StringWriter writer = new StringWriter()) {

			template.merge( context, writer );
			emailBody = writer.toString();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error while merging Velocity template", e);
			throw new RuntimeException(e);
		}

		return emailBody;
	}
}
