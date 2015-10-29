package nl.jorith.dotcms.blikjesteller.email;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.dotmarketing.viewtools.MailerTool;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;

public class EmailFacade {

	public void sendBlikjesOrder(List<Blikje> blikjes) {
		StringBuilder mailBody = new StringBuilder();
		Locale locale = new Locale("nl", "NL");      
	    NumberFormat nf = NumberFormat.getNumberInstance(locale);
	    nf.setMinimumFractionDigits(2);
	    nf.setMaximumFractionDigits(2);

		mailBody.append("<p>Blikjesteller bestelling:</p>");

		mailBody.append("<table>");
		float totalPrice = 0;
		for (Blikje blikje : blikjes) {
			if (blikje.getAmount() > 0) {
				totalPrice += (blikje.getPrice() * blikje.getAmount());
				mailBody.append(String.format("<tr><td width=\"100px\">%s</td><td width=\"100px\">&euro; %s</td><td width=\"50px\">%s</td><td>&euro; %s</td></tr>", blikje.getName(), nf.format(blikje.getPrice()), blikje.getAmount(), nf.format(blikje.getAmount() * blikje.getPrice())));
			}
		}
		
		mailBody.append("<tr><td colspan=\"4\">&nbsp;</td></tr>");
		mailBody.append(String.format("<tr><td colspan=\"3\"><strong>Totaal</strong></td><td><strong>&euro; %s</strong></td></tr>", nf.format(totalPrice)));

		MailerTool mailer = new MailerTool();
		mailer.sendEmail("jorith@gmail.com", "blikje@jorith.nl", "Bestelling", mailBody.toString(), true);
	}
}
