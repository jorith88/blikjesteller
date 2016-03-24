package nl.jorith.blikjesteller.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import nl.jorith.blikjesteller.rest.type.Blikje;

public class ConfigTest {
	
	@Inject Configuration config = new Configuration();
	
	@Test
	public void checkOrderEmail() {
		ApplicationConfig appConfig = config.getApplicationConfig();
		
		Assert.assertTrue("Order email address must be set", StringUtils.isNotBlank(appConfig.getOrderEmail()));
	}
	
	
	@Test
	public void checkOrderEmailBcc() {
		ApplicationConfig appConfig = config.getApplicationConfig();
		
		Assert.assertTrue("Order email BCC address must be set", StringUtils.isNotBlank(appConfig.getOrderEmailBcc()));
	}
	
	
	@Test
	public void checkDebugOrderEmail() {
		ApplicationConfig appConfig = config.getApplicationConfig();
		
		Assert.assertTrue("Debug order email address must be set", StringUtils.isNotBlank(appConfig.getDebugOrderEmail()));
	}
	
	@Test
	public void checkBlikjesConfig() {
		ApplicationConfig appConfig = config.getApplicationConfig();
		
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
