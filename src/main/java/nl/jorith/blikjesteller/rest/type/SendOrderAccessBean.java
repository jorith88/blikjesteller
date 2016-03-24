package nl.jorith.blikjesteller.rest.type;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class SendOrderAccessBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean allowed;

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}

	public boolean isAllowed() {
		return allowed;
	}
}
