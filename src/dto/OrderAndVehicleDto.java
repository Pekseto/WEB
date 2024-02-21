package dto;

public class OrderAndVehicleDto {

	private String orderId;
	private int vehicleId;

	public OrderAndVehicleDto() {

	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String returnSerializableFormat() {
		return orderId + "|" + vehicleId + "\n";
	}

}
