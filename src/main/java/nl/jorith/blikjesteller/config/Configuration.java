package nl.jorith.blikjesteller.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;

import com.google.gson.Gson;

@Singleton
public class Configuration {
	private static final Logger logger = Logger.getLogger(Configuration.class.getName()); 
	
	private ApplicationConfig applicationConfig;

	public ApplicationConfig getApplicationConfig() {

		if (applicationConfig == null) {
			try (InputStream in = Configuration.class.getResourceAsStream("/config/ApplicationConfig.json");
				 InputStreamReader reader = new InputStreamReader(in);) {

				applicationConfig = new Gson().fromJson(reader, ApplicationConfig.class);

			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while loading configuration", e);
				throw new RuntimeException(e);
			}
		}

		return applicationConfig;
	}
}
