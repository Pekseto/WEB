package beans;

public class Comment {
	
	public enum Status {
		Approved, Denied, Pending;
	}
	
	private int id;
	private int customerId;
	private int rentACarObjectId;
	private String text;
	private int rating;
	private Status status;

	public Comment() {
		
	}
	
	public Comment(int objectId, int customerId, String text, int rating) {
		this.customerId = customerId;
		this.rentACarObjectId = objectId;
		this.text = text;
		this.rating = rating;
		this.status = Status.Pending;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getRentACarObjectId() {
		return rentACarObjectId;
	}

	public void setRentACarObjectId(int rentACarObjectId) {
		this.rentACarObjectId = rentACarObjectId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String returnSerializableFormat() {
		return id + "|" + customerId + "|" + rentACarObjectId + "|" + text + "|" + rating + "|" + status + "\n";
	}
	
}
