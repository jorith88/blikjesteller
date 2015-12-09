package nl.jorith.dotcms.blikjesteller.facade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;
import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

public class BlikjesFacade {

	public List<Blikje> getAllBlikjes() {
		ContentletQuery query = new ContentletQuery("Blikje");
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);

		return query.executeSafe()
			.stream()
			.map(Blikje::fromContentlet)
			.collect(Collectors.toList());
	}

	public List<Blikje> getOrderedBlikjes(Map<String,Integer> order) {

		String[] identifiers = order
			.keySet()
			.stream()
			.toArray(String[]::new);

		ContentletQuery query = new ContentletQuery("Blikje");
		query.addIdentifierLimitations(true, identifiers);
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);

		return query.executeSafe()
				.stream()
				.filter(contentlet -> order.get(contentlet.getIdentifier()) > 0) // ordered amount is greater than 0
				.map(Blikje::fromContentlet)
				.collect(Collectors.toList());
	}
}
