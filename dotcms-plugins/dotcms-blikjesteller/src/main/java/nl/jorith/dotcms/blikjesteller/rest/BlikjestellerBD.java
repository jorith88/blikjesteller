package nl.jorith.dotcms.blikjesteller.rest;

import java.util.LinkedList;
import java.util.List;

import nl.jorith.dotcms.blikjesteller.email.EmailFacade;
import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;
import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

import com.dotcms.repackage.com.google.gson.Gson;
import com.dotcms.repackage.javax.ws.rs.GET;
import com.dotcms.repackage.javax.ws.rs.POST;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.rest.WebResource;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import com.google.gson.reflect.TypeToken;

@Path("/blikjesteller")
public class BlikjestellerBD extends WebResource {

	private static final Gson gson = new Gson();
	private static final EmailFacade emailFacade = new EmailFacade();

	@GET @Path("/blikjes")
	public String getBlikjesJSON() {
		ContentletQuery query = new ContentletQuery("Blikje");
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);

		List<Contentlet> result = query.executeSafe();

		List<Blikje> blikjes = new LinkedList<>();

		for (Contentlet contentlet : result){
			Blikje blikje = new Blikje();
			blikje.setName(contentlet.getStringProperty("naam"));
			blikje.setPrice(contentlet.getFloatProperty("prijs"));
			blikje.setAmount(0);
			blikjes.add(blikje);
		}

		return gson.toJson(blikjes);
	}

	@POST @Path("send-order")
	public void sendOrder(String blikjesJSON) {
		List<Blikje> blikjes = gson.fromJson(blikjesJSON, new TypeToken<List<Blikje>>(){}.getType());

		if (blikjes.size() > 0) {
			emailFacade.sendBlikjesOrder(blikjes);
		}
	}
}
