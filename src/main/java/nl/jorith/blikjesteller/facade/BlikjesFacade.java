package nl.jorith.blikjesteller.facade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.jorith.blikjesteller.bd.type.Blikje;
import nl.jorith.blikjesteller.config.ApplicationConfig;
import nl.jorith.blikjesteller.config.Configuration;

public class BlikjesFacade {
	
	private ApplicationConfig applicationConfig = Configuration.getApplicationConfig();

	public List<Blikje> getAllBlikjes() {

		return applicationConfig.getBlikjes().stream()
				.peek(blikje -> blikje.setAmount(0))
				.collect(Collectors.toList());
	}

	public List<Blikje> getOrderedBlikjes(Map<String,Integer> order) {

		return applicationConfig.getBlikjes().stream()
			.filter (blikje -> order.keySet().contains(blikje.getId())) // Only get blikjes that are ordered
			.filter (blikje -> order.get(blikje.getId()) > 0) // Only get blikjes that have an amount > 0
			.peek   (blikje -> blikje.setAmount(order.get(blikje.getId())))
			.collect(Collectors.toList());
	}
}
