package beans;

public class Customer extends User {

	private double points;
	private CustomerType type;
	private boolean isSuspicious;

	public Customer() {

	}

	public Customer(String username, String password, String name, String surname, Gender gender, Role role,
			String dateOfBirth, int points, CustomerType type) {
		super(username, password, name, surname, gender, role, dateOfBirth);
		this.points = points;
		this.type = type;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public CustomerType getType() {
		return type;
	}

	public void setType(CustomerType type) {
		this.type = type;
	}

	public boolean isSuspicious() {
		return isSuspicious;
	}

	public void setSuspicious(boolean isSuspicious) {
		this.isSuspicious = isSuspicious;
	}

	public String returnSerializableFormat() {
		return Integer.toString(id) + "|" + username + "|" + password + "|" + firstName + "|" + lastName + "|" + gender
				+ "|" + dateOfBirth + "|" + role + "|" + points + "|" + Boolean.toString(isBlocked) + "|" + isSuspicious  + "\n";
	}

}
