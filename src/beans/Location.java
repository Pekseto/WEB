package beans;

public class Location {
	private int id;
	private double longitude;
	private double latitude;
	private String street;
	private String city;
	private int postalCode;

	public Location() {
		// TODO Auto-generated constructor stub
	}

	public Location(double longitude, double latitude, String street, String city, int zipCode) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
		this.street = street;
		this.city = city;
		this.postalCode = zipCode;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(int zipCode) {
		this.postalCode = zipCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSerializableFormat() {
		return Integer.toString(id) + "|" + Double.toString(longitude) + "|" + Double.toString(latitude) + "|" + street
				+ "|" + city + "|" + Integer.toString(postalCode) + "\n";
	}
}
