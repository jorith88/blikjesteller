package nl.jorith.blikjesteller.facade;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class SecurityFacade implements Serializable {
	private static final long serialVersionUID = 1L;

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
