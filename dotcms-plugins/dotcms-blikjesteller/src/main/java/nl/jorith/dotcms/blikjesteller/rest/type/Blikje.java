package nl.jorith.dotcms.blikjesteller.rest.type;

public class Blikje {
	private String id;
	private String name;
	private Float price;
	private Integer amount;
	
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
