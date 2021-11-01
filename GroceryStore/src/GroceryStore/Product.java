package GroceryStore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Product implements Serializable {
	private static int idCounter = 1;
	private String name;
	private int productID;
	private int currentStock;
	private double price;
	private int reorderLevel;

	public Product(String name, int currentStock, double price, int reorderLevel) {
		this.name = name;
		this.productID = idCounter++;
		this.currentStock = currentStock;
		this.price = price;
		this.reorderLevel = reorderLevel;
	}

	// getters and setters, didn't do all since some of them probably don't need to
	// be changed i.e. id, name, reorderlevel
	public String getName() {
		return name;
	}

	public int getProductID() {
		return productID;
	}

	public int getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getReorderLevel() {
		return reorderLevel;
	}

	@Override
	public String toString() {
		return "Product name: " + name + ", price: $" + price;
	}

	//saves idcounter
	public static void save(ObjectOutputStream output) throws IOException {
		output.writeObject(idCounter);
	}

	//retrieves idcounter
	public static void retrieve(ObjectInputStream input) throws IOException, ClassNotFoundException {
		idCounter = (int) input.readObject();
	}
}
