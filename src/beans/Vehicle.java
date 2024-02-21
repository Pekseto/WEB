package beans;

public class Vehicle {
	public enum Type {
		Car, Van, MobileHome;
	}

	public enum Kind {
		Manual, Automatic;
	}

	public enum FuelType {
		Diesel, Gasoline, Hybrid, Electric
	}

	private int id;
	private String brand;
	private String model;
	private int price;
	private Type type;
	private Kind kind;
	private FuelType fuelType;
	private double consumption;
	private int numberOfDoors;
	private int personCapacity;
	private String description;
	private String image;
	private int rentACarObjectId;
	private boolean status;
	private int isDeleted;

	public Vehicle() {

	}

	public Vehicle(String brand, String model, int price, Type type, Kind kind, FuelType fuelType, double consumption,
			int numberOfDoors, int personCapacity, String description, String image, boolean status) {
		super();
		this.brand = brand;
		this.model = model;
		this.price = price;
		this.type = type;
		this.kind = kind;
		this.fuelType = fuelType;
		this.consumption = consumption;
		this.numberOfDoors = numberOfDoors;
		this.personCapacity = personCapacity;
		this.description = description;
		this.image = image;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getRentACarObjectId() {
		return rentACarObjectId;
	}

	public void setRentACarObjectId(int rentACarObjectId) {
		this.rentACarObjectId = rentACarObjectId;
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public FuelType getFuelType() {
		return fuelType;
	}

	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	public double getConsumption() {
		return consumption;
	}

	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	public int getNumberOfDoors() {
		return numberOfDoors;
	}

	public void setNumberOfDoors(int numberOfDoors) {
		this.numberOfDoors = numberOfDoors;
	}

	public int getPersonCapacity() {
		return personCapacity;
	}

	public void setPersonCapacity(int personCapacity) {
		this.personCapacity = personCapacity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String returnSerializableFormat() {
		return id + "|" + brand + "|" + model + "|" + price + "|" + type + "|" + kind + "|" + fuelType + "|"
				+ consumption + "|" + numberOfDoors + "|" + personCapacity + "|" + description + "|" + image + "|"
				+ rentACarObjectId + "|" + status + "|" + isDeleted + "\n";
	}

}
