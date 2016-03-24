package nl.jorith.blikjesteller.rest;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import nl.jorith.blikjesteller.exception.SendOrderNotAllowedException;
import nl.jorith.blikjesteller.facade.BlikjesFacade;
import nl.jorith.blikjesteller.rest.type.Blikje;

@Path("/blikjesteller")
@Produces(MediaType.APPLICATION_JSON)
public class BlikjestellerBd {
	
	private static final Logger logger = Logger.getLogger(BlikjestellerBd.class.getName());

	@Inject
	private BlikjesFacade blikjesFacade;

	@GET
	@Path("/blikjes")
	public List<Blikje> getBlikjes(@Context HttpServletRequest request, @HeaderParam("Debug") boolean debugMode) {
		return blikjesFacade.getAllBlikjes();
	}

	@POST
	@Path("send-order")
	public Response sendOrder(Map<String, Integer> order, @Context HttpServletRequest request, @HeaderParam("Debug") boolean debugMode) {

		try {
			blikjesFacade.sendOrder(order, debugMode);
		} catch (SendOrderNotAllowedException e) {
			logger.log(Level.WARNING, "Received an order without token...");
			return Response.status(Status.FORBIDDEN).build();
		}
		
		return Response.noContent().build();
	}
}
