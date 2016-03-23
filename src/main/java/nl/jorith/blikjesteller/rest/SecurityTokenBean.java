package nl.jorith.blikjesteller.rest;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.UUID;

@SessionScoped
public class SecurityTokenBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String token;
	
	public void createToken() {
		this.token = UUID.randomUUID().toString();
	}

	public String getToken() {
		return token;
	}
}
