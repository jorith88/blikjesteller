package nl.jorith.blikjesteller.config;

import java.util.List;

import nl.jorith.blikjesteller.bd.type.Blikje;

public class ApplicationConfig {
	private String orderEmail;
	private String orderEmailBcc;
	private List<Blikje> blikjes;
	
	public String getOrderEmail() {
		return orderEmail;
	}
	public void setOrderEmail(String orderEmail) {
		this.orderEmail = orderEmail;
	}
	public String getOrderEmailBcc() {
		return orderEmailBcc;
	}
	public void setOrderEmailBcc(String orderEmailBcc) {
		this.orderEmailBcc = orderEmailBcc;
	}
	public List<Blikje> getBlikjes() {
		return blikjes;
	}
	public void setBlikjes(List<Blikje> blikjes) {
		this.blikjes = blikjes;
	}
	
	
}
