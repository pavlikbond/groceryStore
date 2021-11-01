package GroceryStore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class Shipment implements Serializable {

	private static int orderCounter = 1;
	private Product product;
	private int orderedQuantity;
	private int orderNumber;
	private LocalDate date;

	public Shipment(Product product) {
		this.product = product;
		this.orderedQuantity = product.getReorderLevel() * 2;
		orderNumber = orderCounter++;
		date = LocalDate.now();
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

	public static void save(ObjectOutputStream output) throws IOException {
		output.writeObject(orderCounter);
	}

	public static void retrieve(ObjectInputStream input) throws IOException, ClassNotFoundException {
		orderCounter = (int) input.readObject();
	}
}
