package beans;

import java.time.LocalDate;

public class OrderCancellation {
	private int customerId;
	private LocalDate cancellationDate;
		
	public OrderCancellation(int customerId, LocalDate cancellationDate) {
		this.customerId = customerId;
		this.cancellationDate = cancellationDate;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public LocalDate getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(LocalDate cancellationDate) {
		this.cancellationDate = cancellationDate;
	}
	
	public String returnSerializableFormat() {
		return customerId + "|" + cancellationDate + "\n";
	}

}
