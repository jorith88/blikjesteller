package nl.jorith.dotcms.blikjesteller.email;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.dotmarketing.util.Logger;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;

public class EmailFacade {

	public void sendBlikjesOrder(List<Blikje> blikjes) {
		StringBuilder mailBody = new StringBuilder();
		
		mailBody.append("Blikjesteller bestelling:\n\n");
		
		for (Blikje blikje : blikjes) {
			mailBody.append(blikje.getName() + " - " + blikje.getPrice() + ": " + blikje.getAmount() + "\n");
		}
		
		sendMail("blikje@jorith.nl", "Bestelling", mailBody.toString(), "jorith@gmail.com");
	}
	
	private void sendMail(String from, String subject, String message, String to) {
		Logger.info(this, "Sending mail to " + to);
		Logger.info(this, message);
		
		Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "25");

        Session mailSession = Session.getDefaultInstance(props);
        Message msg = new MimeMessage(mailSession);

        InternetAddress fromAddress = null;
        InternetAddress toAddress = null;

        try {
			fromAddress = new InternetAddress(from);

			msg.setFrom(fromAddress);

			toAddress = new InternetAddress(to);
			msg.addRecipient(RecipientType.TO, toAddress);

			msg.setSubject(subject);

			message = message.replaceAll("\n", "<br/>");
			msg.setContent(message, "text/html");

			Transport.send(msg);
		} catch (MessagingException e) {
			String errorMessage = "Fout bij verzenden van e-mail";
			Logger.error(this, errorMessage,e);
			throw new RuntimeException(e);
		}
	}
}
