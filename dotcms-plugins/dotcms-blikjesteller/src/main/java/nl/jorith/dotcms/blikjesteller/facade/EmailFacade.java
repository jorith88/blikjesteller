package nl.jorith.dotcms.blikjesteller.facade;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import nl.jorith.dotcms.blikjesteller.config.Configuration;
import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;

import com.dotmarketing.util.Logger;

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

		sendMail(emailTo, emailBcc, "blikje@jorith.nl", "Blikjesbestelling", mailBody.toString());
	}

	private void sendMail(String to, String bcc, String from, String subject, String message) {
		Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
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
			Logger.error(this, "Error Sending Message", e);
			throw new RuntimeException(e);
		}

	}
}
