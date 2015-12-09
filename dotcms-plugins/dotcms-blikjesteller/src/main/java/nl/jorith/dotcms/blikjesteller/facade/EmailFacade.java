package nl.jorith.dotcms.blikjesteller.facade;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import com.dotcms.repackage.org.apache.commons.lang.StringUtils;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import com.dotmarketing.viewtools.MailerTool;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;
import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

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

		ContentletQuery query = new ContentletQuery("Configuration");
		query.addFieldLimitation(true, "key", "blikjesteller.order-email");
		List<Contentlet> result = query.executeSafe();
		
		if (result.size() > 0) {
			String orderEmail = result.get(0).getStringProperty("value");
			
			if (StringUtils.isNotEmpty(orderEmail)) {
				Logger.info(this, "Send blikjes order to " + orderEmail);
				MailerTool mailer = new MailerTool();
				mailer.sendEmail(orderEmail, "blikje@jorith.nl", "Blikjesbestelling", mailBody.toString(), true);
			}
		}
		
	}
}
