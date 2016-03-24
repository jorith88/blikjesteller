package nl.jorith.blikjesteller.facade;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import nl.jorith.blikjesteller.config.Configuration;
import nl.jorith.blikjesteller.exception.SendOrderNotAllowedException;
import nl.jorith.blikjesteller.rest.type.Blikje;
import nl.jorith.blikjesteller.rest.type.SendOrderAccessBean;

@Stateless
public class BlikjesFacade {
	private static final Logger logger = Logger.getLogger(BlikjesFacade.class.getName());
	
	@Inject
	private Configuration config;
	
	@Inject
	private EmailFacade emailFacade;
	
	@Inject
	private SendOrderAccessBean sendOrderAccess;

	public List<Blikje> getAllBlikjes() {
		sendOrderAccess.init();
		
		List<Blikje> blikjes = config.getApplicationConfig().getBlikjes();

		return blikjes.stream()
				.peek(blikje -> blikje.setAmount(0))
				.collect(Collectors.toList());
	}

	public void sendOrder(Map<String, Integer> order, boolean debugMode) throws SendOrderNotAllowedException {
		
		sendOrderAccess.checkAccess();
		
		long numberOfNotZeroBlikjes = order.values().stream()
											.filter(i -> i > 0)
											.count();
		
		// We don't accept empty orders
		if (numberOfNotZeroBlikjes == 0) {
			logger.log(Level.WARNING, "Received an empty order...");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		List<Blikje> orderedBlikjes = getOrderedBlikjes(order);
		
		emailFacade.sendBlikjesOrder(orderedBlikjes, debugMode);
	}
	
	private List<Blikje> getOrderedBlikjes(Map<String,Integer> order) {
		
		List<Blikje> blikjes = config.getApplicationConfig().getBlikjes();
		
		return blikjes.stream()
				.filter (blikje -> order.keySet().contains(blikje.getId())) // Only get blikjes that are ordered
				.filter (blikje -> order.get(blikje.getId()) > 0) // Only get blikjes that have an amount > 0
				.peek   (blikje -> blikje.setAmount(order.get(blikje.getId())))
				.collect(Collectors.toList());
	}
}
