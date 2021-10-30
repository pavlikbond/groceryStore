package GroceryStore;

import java.time.LocalDate;

public class Shipment {

	private static int orderCounter = 1;
	private Product product;
	private int orderedQuantity;
	private int orderNumber;
	private LocalDate date;
	private boolean processed = false;

	public Shipment(Product product) {
		this.product = product;
		this.orderedQuantity = product.getReorderLevel() * 2;
		orderNumber = orderCounter++;
		date = LocalDate.now();
	}

	// Processes the shipment that hasn't been processed already.
	public void process() {
		if (processed == true) {
			System.out.println("Error: Shipment has already been processed!");
			return;
		}

		processed = true;

		product.setCurrentStock(product.getCurrentStock() + orderedQuantity);

		System.out.println("Shipment " + orderNumber + " has been processed for " + product.getName());
	}

	public Product getProduct() {
		return product;
	}

	public int getOrderedQuantity() {
		return orderedQuantity;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public LocalDate getDate() {
		return date;
	}

	public boolean isProcessed() {
		return processed;
	}

}
