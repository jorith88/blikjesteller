package nl.jorith.blikjesteller.rest;

import nl.jorith.blikjesteller.util.CookieEnum;

public enum BlikjestellerCookie implements CookieEnum {

	DEVICE_ID("did", 1000 * DAY, "/");
	
	private String name;
	private int maxAge;
	private String path;
	
	private BlikjestellerCookie(String name, int maxAge, String path) {
		this.name = name;
		this.maxAge = maxAge;
		this.path = path;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public int getMaxAge() {
		return this.maxAge;
	}

	@Override
	public String getPath() {
		return this.path;
	}
	
	
}
