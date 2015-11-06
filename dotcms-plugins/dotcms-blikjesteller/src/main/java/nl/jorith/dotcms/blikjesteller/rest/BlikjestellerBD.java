package nl.jorith.dotcms.blikjesteller.rest;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.dotcms.repackage.com.google.gson.Gson;
import com.dotcms.repackage.javax.ws.rs.GET;
import com.dotcms.repackage.javax.ws.rs.POST;
import com.dotcms.repackage.javax.ws.rs.Path;
import com.dotcms.repackage.javax.ws.rs.WebApplicationException;
import com.dotcms.repackage.javax.ws.rs.core.Context;
import com.dotcms.repackage.javax.ws.rs.core.Response.Status;
import com.dotcms.repackage.org.apache.commons.lang.StringUtils;
import com.dotcms.rest.WebResource;
import com.dotmarketing.portlets.contentlet.model.Contentlet;
import com.dotmarketing.util.Logger;
import com.google.gson.reflect.TypeToken;

import nl.jorith.dotcms.blikjesteller.email.BlikjesFacade;
import nl.jorith.dotcms.blikjesteller.email.EmailFacade;
import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;
import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

@Path("/blikjesteller")
public class BlikjestellerBD extends WebResource {

	private static final String TOKEN_KEY = "nl.jorith.blikjesteller.token";
	
	private static final Gson gson = new Gson();
	private static final EmailFacade emailFacade = new EmailFacade();
	private static final BlikjesFacade blikjesFacade = new BlikjesFacade();
	
	@GET @Path("/blikjes")
	public String getBlikjesJSON(@Context HttpServletRequest request) {
		ContentletQuery query = new ContentletQuery("Blikje");
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);

		List<Contentlet> result = query.executeSafe();

		List<Blikje> blikjes = new LinkedList<>();
		
		result.forEach(c -> {
			Blikje blikje = new Blikje();
			blikje.setId(c.getIdentifier());
			blikje.setName(c.getStringProperty("naam"));
			blikje.setPrice(c.getFloatProperty("prijs"));
			blikje.setAmount(0);
			blikjes.add(blikje);
		});
		
		// Set a token to prevent calls to /send-order only
		request.getSession().setAttribute(TOKEN_KEY, UUID.randomUUID().toString());

		return gson.toJson(blikjes);
	}

	@POST @Path("send-order")
	public void sendOrder(String orderJSON, @Context HttpServletRequest request) {
		Map<String,Integer> order = gson.fromJson(orderJSON, new TypeToken<Map<String,Integer>>(){}.getType());
		
		long numberOfNotZeroBlikjes = order.values()
									.stream()
									.filter(i -> i > 0)
									.count();
		
		// We don't accept empty orders
		if (numberOfNotZeroBlikjes == 0) {
			Logger.error(this, "Received an empty order...");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		// Only allow orders when a token has been set in the session
		String token = (String)request.getSession().getAttribute(TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			Logger.error(this, "Received an order without token...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		List<Blikje> orderedBlikjes = blikjesFacade.getOrderedBlikjes(order);
		emailFacade.sendBlikjesOrder(orderedBlikjes);
	}
}
