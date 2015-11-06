package nl.jorith.dotcms.blikjesteller.email;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nl.jorith.dotcms.blikjesteller.rest.type.Blikje;
import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

public class BlikjesFacade {
	
	public List<Blikje> getAllBlikjes() {
		ContentletQuery query = new ContentletQuery("Blikje");
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);

		List<Blikje> blikjes = new LinkedList<>();
		
		query.executeSafe().forEach(c -> {
			Blikje blikje = new Blikje();
			blikje.setId(c.getIdentifier());
			blikje.setName(c.getStringProperty("naam"));
			blikje.setPrice(c.getFloatProperty("prijs"));
			blikje.setAmount(0);
			blikjes.add(blikje);
		});

		return blikjes;
	}
	
	public List<Blikje> getOrderedBlikjes(Map<String,Integer> order) {
		List<Blikje> orderedBlikjes = new LinkedList<>();
		
		String[] identifiers = order
							.keySet()
							.stream()
							.toArray(String[]::new);
		
		ContentletQuery query = new ContentletQuery("Blikje");
		query.addIdentifierLimitations(true, identifiers);
		query.addLive(true);
		query.addSorting("Blikje", "naam", true);
		
		query.executeSafe().forEach(c -> {
			int amount = order.get(c.getIdentifier());

			if (amount > 0) {
				Blikje blikje = new Blikje();
				blikje.setId(c.getIdentifier());
				blikje.setName(c.getStringProperty("naam"));
				blikje.setPrice(c.getFloatProperty("prijs"));
				blikje.setAmount(amount);
				orderedBlikjes.add(blikje);
			}
		});
		
		return orderedBlikjes;
		
	}
}
