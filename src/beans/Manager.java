package beans;

public class Manager extends User {
	private int rentACarObjectId;

	public Manager() {
		super();
	}

	public Manager(String username, String password, String firstName, String lastName, Gender gender, Role role,
			String dateOfBirth, int rentACarObjectId, boolean blocked) {
		super(username, password, firstName, lastName, gender, role, dateOfBirth);
		// TODO Auto-generated constructor stub
		this.rentACarObjectId = rentACarObjectId;
		this.isBlocked = blocked;
	}

	public int getRentACarObjectId() {
		return rentACarObjectId;
	}

	public void setRentACarObjectId(int rentACarObjectId) {
		this.rentACarObjectId = rentACarObjectId;
	}

	public String returnSerializableFormat() {
		return Integer.toString(id) + "|" + username + "|" + password + "|" + firstName + "|" + lastName + "|" + gender
				+ "|" + dateOfBirth + "|" + role + "|" + Integer.toString(rentACarObjectId) + "|"
				+ Boolean.toString(isBlocked) + "\n";
	}
}
