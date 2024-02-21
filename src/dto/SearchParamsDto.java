package dto;

public class SearchParamsDto {
	private String location;
	private String shop;
	private String vehicleType;
	private String rating;
	
	public SearchParamsDto() {
		
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "SearchParamsDto [location=" + location + ", shop=" + shop + ", vehicleType=" + vehicleType + ", rating="
				+ rating + "]";
	}
	
}
