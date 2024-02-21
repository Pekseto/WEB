package dto;

public class CartAndVehicleDto {

	private int cartId;
	private int vehicleId;

	public CartAndVehicleDto() {

	}

	public int getCartId() {
		return cartId;
	}

	public void setCartId(int cartId) {
		this.cartId = cartId;
	}

	public int getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String returnSerializableFormat() {
		return cartId + "|" + vehicleId + "\n";
	}

}
