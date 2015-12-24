package nl.jorith.blikjesteller.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import nl.jorith.blikjesteller.bd.type.Blikje;

public class ConfigTest {
	
	@Test
	public void checkOrderEmail() {
		ApplicationConfig appConfig = Configuration.getApplicationConfig();
		
		Assert.assertTrue("Order email address must be set", StringUtils.isNotBlank(appConfig.getOrderEmail()));
	}
	
	@Test
	public void checkBlikjesConfig() {
		ApplicationConfig appConfig = Configuration.getApplicationConfig();
		
		List<Blikje> blikjes = appConfig.getBlikjes();
		Set<String> blikjeIds = new HashSet<>();
		
		blikjes.forEach(blikje -> {
			Assert.assertTrue("Name of blikje " + blikje.getId() + " is blank", StringUtils.isNotBlank(blikje.getName()));
			Assert.assertTrue("Invalid price for blikje " + blikje.getName() + ": " + blikje.getPrice(), blikje.getPrice() > 0);
			
			Assert.assertFalse("Duplicate id: " + blikje.getId(), blikjeIds.contains(blikje.getId()));
			blikjeIds.add(blikje.getId());
		});
	}
}
