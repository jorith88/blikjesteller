package nl.jorith.dotcms.blikjesteller.email;

import java.util.List;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;

import com.dotmarketing.viewtools.MailerTool;

public class EmailFacade {

	public void sendBlikjesOrder(List<Blikje> blikjes) {
		StringBuilder mailBody = new StringBuilder();

		mailBody.append("Blikjesteller bestelling:<br/><br/>");

		for (Blikje blikje : blikjes) {
			mailBody.append(blikje.getName() + " - " + blikje.getPrice() + ": " + blikje.getAmount() + "<br/>");
		}

		MailerTool mailer = new MailerTool();
		mailer.sendEmail("jorith@gmail.com", "blikje@jorith.nl", "Bestelling", mailBody.toString(), true);
	}
}
