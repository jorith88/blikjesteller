package nl.jorith.blikjesteller.bd.type;

public class OrderItem {
	private String blikjeId;
	private String amount;
	
	public String getBlikjeId() {
		return blikjeId;
	}
	public void setBlikjeId(String blikjeId) {
		this.blikjeId = blikjeId;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
