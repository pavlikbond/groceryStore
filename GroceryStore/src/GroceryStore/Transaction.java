package GroceryStore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Transaction implements Serializable {
	private static int idCounter = 1;
	private int transactionID;
	private ArrayList<Product> productList;
	private ArrayList<Integer> quantityList;
	private double total;
	private LocalDate date;

	public Transaction(double total, LocalDate date) {
		this.transactionID = idCounter++;
		this.total = total;
		this.date = date;
		this.productList = new ArrayList<Product>();
		this.quantityList = new ArrayList<>();
	}

	public void addProduct(Product product, int qunatity) {
		productList.add(product);
		quantityList.add(qunatity);
		this.total = calcTotalAmount();
	}

	//calculates total amount from all products
	private double calcTotalAmount() {
		double total = 0;

		for (int i = 0; i < productList.size(); i++) {
			double price = (productList.get(i).getPrice()) * quantityList.get(i);
			total += price;
		}
		return total;
	}

	public static int getIdCounter() {
		return idCounter;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public int getQuantity(int index) {
		return quantityList.get(index);
	}

	public double getTotal() {
		return total;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	//creates a shipment for any item that needs to be ordered and returns it as a list
	public ArrayList<Shipment> orderProducts() {
		ArrayList<Shipment> shipments = new ArrayList<>();
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).getCurrentStock() < productList.get(i).getReorderLevel()) {
				Shipment shipment = new Shipment(productList.get(i));
				shipments.add(shipment);
			}
		}
		return shipments;
	}

	@Override
	public String toString() {
		String string = "Transaction ID: " + transactionID + "\n";
		for (int i = 0; i < productList.size(); i++) {
			string += productList.get(i).toString() + " quantity: " + quantityList.get(i) + "\n";

		}
		string += "\nTotal: $" + total + "\nDate: " + date + "\n";
		return string;
	}

	public static void save(ObjectOutputStream output) throws IOException {
		output.writeObject(idCounter);
	}

	public static void retrieve(ObjectInputStream input) throws IOException, ClassNotFoundException {
		idCounter = (int) input.readObject();
	}
}