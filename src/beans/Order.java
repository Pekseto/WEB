package beans;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
	public enum Status {
		Processing, Approved, Retrieved, Returned, Rejected, Canceled
	}

	private String id;
	private int rentACarObjectId;
	private RentACarObject rentACarObject;
	private LocalDateTime beginDate;
	private int rentDuration;
	private LocalDateTime endDate;
	private double price;
	private int customerId;
	private Customer customer;
	private User user;

	private Status status;

	public Order() {
	}

	public Order(int rentACarObjectId, LocalDateTime dateTime, int rentDuration, int price, int customerId,
			Status status) {
		super();
		this.beginDate = dateTime;
		this.rentDuration = rentDuration;
		this.price = price;
		this.status = status;
		this.rentACarObjectId = rentACarObjectId;
		this.customerId = customerId;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getRentACarObjectId() {
		return rentACarObjectId;
	}

	public void setRentACarObjectId(int rentACarShopId) {
		this.rentACarObjectId = rentACarShopId;
	}

	public LocalDateTime getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(LocalDateTime dateTime) {
		this.beginDate = dateTime;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate() {
		this.endDate = beginDate.plusDays(rentDuration);
	}

	public int getRentDuration() {
		return rentDuration;
	}

	public void setRentDuration(int rentDuration) {
		this.rentDuration = rentDuration;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	
	public RentACarObject getRentACarObject() {
		return rentACarObject;
	}

	public void setRentACarObject(RentACarObject rentACarObject) {
		this.rentACarObject = rentACarObject;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String returnSerializableFormat() {
		return id + "|" + rentACarObjectId + "|" + beginDate + "|" + rentDuration + "|" + price + "|" + customerId + "|"
				+ status + "\n";
	}

}
