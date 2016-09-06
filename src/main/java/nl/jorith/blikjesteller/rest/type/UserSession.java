package nl.jorith.blikjesteller.rest.type;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

@SessionScoped
public class UserSession implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean sendOrderAllowed;
	private String deviceId;

	public boolean isSendOrderAllowed() {
		return sendOrderAllowed;
	}

	public void setSendOrderAllowed(boolean allowed) {
		this.sendOrderAllowed = allowed;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
