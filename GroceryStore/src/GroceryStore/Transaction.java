package GroceryStore;

import java.time.LocalDate;
import java.util.ArrayList;

public class Transaction {
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
	}

	public double getTotalAmount() {
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

	public ArrayList<Shipment> orderProducts() {
		ArrayList<Shipment> shipments = new ArrayList<>();
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).getCurrentStock() - quantityList.get(i) < productList.get(i).getReorderLevel()) {
				Shipment shipment = new Shipment(productList.get(i));
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
		string += "\nTotal: " + total + "\nDate: " + date + "\n";
		return string;
	}

}