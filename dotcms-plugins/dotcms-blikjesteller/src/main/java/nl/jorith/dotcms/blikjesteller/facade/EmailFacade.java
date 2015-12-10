package nl.jorith.dotcms.blikjesteller.facade;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import nl.jorith.dotcms.blikjesteller.config.Configuration;
import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;

import com.dotmarketing.util.Logger;
import com.liferay.util.JNDIUtil;

public class EmailFacade {

	public void sendBlikjesOrder(List<Blikje> blikjes) {
		StringBuilder mailBody = new StringBuilder();
		Locale locale = new Locale("nl", "NL");
	    NumberFormat nf = NumberFormat.getNumberInstance(locale);
	    nf.setMinimumFractionDigits(2);
	    nf.setMaximumFractionDigits(2);

		mailBody.append("<h3>Bestelling</h3>\n");

		mailBody.append("<table>\n");
		float totalPrice = 0;
		for (Blikje blikje : blikjes) {
			if (blikje.getAmount() > 0) {
				totalPrice += (blikje.getPrice() * blikje.getAmount());
				mailBody.append(String.format("<tr><td width=\"140px\">%s</td><td width=\"70px\">&euro; %s</td><td width=\"50px\">%s</td><td>&euro; %s</td></tr>\n", blikje.getName(), nf.format(blikje.getPrice()), blikje.getAmount(), nf.format(blikje.getAmount() * blikje.getPrice())));
			}
		}

		mailBody.append("<tr><td colspan=\"4\">&nbsp;</td></tr>\n");
		mailBody.append(String.format("<tr><td colspan=\"3\"><strong>Totaal</strong></td><td><strong>&euro; %s</strong></td></tr>\n", nf.format(totalPrice)));

		String emailTo = Configuration.getOrderEmail();
		String emailBcc = Configuration.getOrderEmailBcc();

		Logger.info(this, "Send blikjes order to " + emailTo + ". BCC to " + emailBcc);

		sendEmail(emailTo, emailBcc, "blikje@jorith.nl", "Blikjesbestelling", mailBody.toString(), true);
	}

	private String sendEmail(String to, String bcc, String from, String subject,
			String message, Boolean html) {
		Session s = null;
		try {
			Context ctx = new InitialContext();
			s = (Session) JNDIUtil.lookup(ctx, "mail/MailSession");
		} catch (NamingException e1) {
			Logger.error(this, e1.getMessage(), e1);
		}

		if (s == null) {
			Logger.debug(this, "No Mail Session Available.");
			return "";
		}
		String smtpServer = s.getProperty("mail.smtp.host");

		Properties props = System.getProperties();
		props.put("mail.host", smtpServer);
		props.setProperty("mail.transport.protocol", "smtp");
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to, false));
			msg.setRecipients(Message.RecipientType.BCC,
					InternetAddress.parse(bcc, false));
		} catch (Exception e) {
			Logger.error(this, "Error Assigning To and From Addresses", e);
			return "Invalid To and/or From Address: " + e.getMessage();
		}
		try {
			msg.setSubject(subject);
		} catch (Exception e) {
			Logger.error(this, "Error Assigning Subject", e);
			return "Invalid Subject: " + e.getMessage();
		}

		if (html.booleanValue())
			try {
				msg.setContent(message, "text/html");
			} catch (Exception e) {
				Logger.error(this, "Error Setting Content", e);
				return "Invalid Message: " + e.getMessage();
			}
		else {
			try {
				msg.setText(message);
			} catch (Exception e) {
				Logger.error(this, "Error Assigning Text", e);
				return "Invalid Message: " + e.getMessage();
			}

		}

		try {
			msg.setHeader("X-Mailer", "DotCMSSimpleMailer");
			msg.setSentDate(new Date());

			Transport transport = session.getTransport();
			transport.connect();
			transport.sendMessage(msg,
					msg.getRecipients(Message.RecipientType.TO));

			transport.close();
		} catch (Exception e) {
			Logger.error(this, "Error Sending Message", e);
			return "Unable to Send Message: " + e.getMessage();
		}

		return "";
	}
}
