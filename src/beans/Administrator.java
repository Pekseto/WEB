package beans;

public class Administrator extends User {

	public Administrator() {
		super();
	}

	public Administrator(int id, String username, String password, String name, String surname, Gender gender,
			Role role, String dateOfBirth) {
		super(username, password, name, surname, gender, role, dateOfBirth);
	}
	
	public String returnSerializableFormat() {
		return Integer.toString(id) + "|" + username + "|" + password + "|" + firstName + "|" + lastName + "|" + gender
				+ "|" + dateOfBirth + "|" + role + "\n";
	}
}
