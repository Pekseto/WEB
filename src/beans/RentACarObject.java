package beans;

public class RentACarObject {
	private int id;
	private String name;
	private String workingHours;
	private boolean isOpen;
	private int locationId;
	private Location location;
	private int rating;
	private String logo;

	public RentACarObject() {

	}

	public RentACarObject(String name, String bussinesHours, boolean status, int rating, String logo) {
		super();
		this.name = name;
		this.workingHours = bussinesHours;
		this.isOpen = status;
		this.rating = rating;
		this.logo = logo;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String bussinesHours) {
		this.workingHours = bussinesHours;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating2) {
		this.rating = rating2;
	}

	public String getSerializableFormat() {
		return Integer.toString(id) + "|" + name + "|" + workingHours + "|" + Boolean.toString(isOpen) + "|"
				+ Integer.toString(locationId) + "|" + Integer.toString(rating) + "|" + logo + "\n";
	}

}
