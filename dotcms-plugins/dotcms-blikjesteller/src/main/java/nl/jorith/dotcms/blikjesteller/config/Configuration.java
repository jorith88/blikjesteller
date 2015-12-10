package nl.jorith.dotcms.blikjesteller.config;

import nl.jorith.dotcms.blikjesteller.util.ContentletQuery;

public class Configuration {

	private static String get(String key) {
		ContentletQuery query = new ContentletQuery("Configuration");
		query.addFieldLimitation(true, "key", key);

		return query.executeSafe()
				.stream()
				.findFirst()
				.get()
				.getStringProperty("value");
	}

	public static String getOrderEmail() {
		return get("blikjesteller.order-email");
	}

	public static String getOrderEmailBcc() {
		return get("blikjesteller.order-email-bcc");
	}
}
