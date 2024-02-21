package beans;

import java.time.LocalDate;

public class Cart {

	private int customerId; // Usput i ID od carta
	private int price;
	private int deleted;
	private LocalDate fromDate;
	private LocalDate untilDate;

	public Cart() {

	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(LocalDate untilDate) {
		this.untilDate = untilDate;
	}

	public String returnSerializableFormat() {
		return customerId + "|" + price + "|" + fromDate + "|" + untilDate + "|" + deleted + "\n";
	}

}
