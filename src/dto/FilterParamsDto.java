package dto;

public class FilterParamsDto {
	private String avgRating;
	private String fuelFilter;
	private String gearshiftFilter;
	private boolean onlyOpen;

	public FilterParamsDto() {
		
	}

	public String getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(String avgRating) {
		this.avgRating = avgRating;
	}

	public String getFuelFilter() {
		return fuelFilter;
	}

	public void setFuelFilter(String fuelFilter) {
		this.fuelFilter = fuelFilter;
	}

	public String getGearshiftFilter() {
		return gearshiftFilter;
	}

	public void setGearshiftFilter(String gearshiftFilter) {
		this.gearshiftFilter = gearshiftFilter;
	}

	public boolean isOnlyOpen() {
		return onlyOpen;
	}

	public void setOnlyOpen(boolean onlyOpen) {
		this.onlyOpen = onlyOpen;
	}

	@Override
	public String toString() {
		return "FilterParamsDto [avgRating=" + avgRating + ", fuelFilter=" + fuelFilter + ", gearshiftFilter="
				+ gearshiftFilter + ", onlyOpen=" + onlyOpen + "]";
	}

}
