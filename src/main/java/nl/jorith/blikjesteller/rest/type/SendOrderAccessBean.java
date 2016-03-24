package nl.jorith.blikjesteller.rest.type;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import nl.jorith.blikjesteller.exception.SendOrderNotAllowedException;

@SessionScoped
public class SendOrderAccessBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean allowed;

	@PostConstruct
	public void init() {
		this.allowed = true;
	}

	public void checkAccess() throws SendOrderNotAllowedException {
		if (!allowed) {
			throw new SendOrderNotAllowedException();
		}
	}
}
