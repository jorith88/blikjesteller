package nl.jorith.dotcms.blikjesteller.rest.type;

import com.dotmarketing.portlets.contentlet.model.Contentlet;

public class Blikje {
	private String id;
	private String name;
	private Float price;
	private Integer amount;

	public static Blikje fromContentlet(Contentlet c) {
		Blikje blikje = new Blikje();
		blikje.setId(c.getIdentifier());
		blikje.setName(c.getStringProperty("naam"));
		blikje.setPrice(c.getFloatProperty("prijs"));
		blikje.setAmount(0);

		return blikje;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer defaultAmount) {
		this.amount = defaultAmount;
	}
}
