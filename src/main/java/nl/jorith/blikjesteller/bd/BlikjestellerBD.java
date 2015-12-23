package nl.jorith.blikjesteller.bd;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import nl.jorith.blikjesteller.bd.type.Blikje;
import nl.jorith.blikjesteller.config.Configuration;
import nl.jorith.blikjesteller.facade.BlikjesFacade;
import nl.jorith.blikjesteller.facade.EmailFacade;

@Path("/blikjesteller")
@Produces(MediaType.APPLICATION_JSON)
public class BlikjestellerBD {

	private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName());
	private static final String TOKEN_KEY = "nl.jorith.blikjesteller.token";
	
	private static final Gson gson = new Gson();
	private static final EmailFacade emailFacade = new EmailFacade();
	private static final BlikjesFacade blikjesFacade = new BlikjesFacade();
	
	@GET @Path("/blikjes")
	public String getBlikjes(@Context HttpServletRequest request) {
		
		// Set a token to prevent calls to /send-order only
		request.getSession().setAttribute(TOKEN_KEY, UUID.randomUUID().toString());

		return gson.toJson(blikjesFacade.getAllBlikjes());
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
			LOGGER.log(Level.WARNING, "Received an empty order...");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		// Only allow orders when a token has been set in the session
		String token = (String)request.getSession().getAttribute(TOKEN_KEY);
		if (StringUtils.isBlank(token)) {
			LOGGER.log(Level.WARNING, "Received an order without token...");
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		List<Blikje> orderedBlikjes = blikjesFacade.getOrderedBlikjes(order);
		
		emailFacade.sendBlikjesOrder(orderedBlikjes);
	}
}
