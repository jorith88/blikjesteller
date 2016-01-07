package nl.jorith.blikjesteller.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class Configuration {
	private static final Gson GSON = new Gson();
	private static final Logger LOGGER = Logger.getLogger(Configuration.class.getName()); 
	
	private static ApplicationConfig applicationConfig;

	public static ApplicationConfig getApplicationConfig() {

		if (applicationConfig == null) {
			try (InputStream in = Configuration.class.getResourceAsStream("/config/ApplicationConfig.json");
				 InputStreamReader reader = new InputStreamReader(in);) {

				applicationConfig = GSON.fromJson(reader, ApplicationConfig.class);

			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Error while loading configuration", e);
				throw new RuntimeException(e);
			}
		}

		return applicationConfig;
	}
}
