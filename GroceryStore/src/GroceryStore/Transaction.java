package GroceryStore;

import java.util.ArrayList;
import java.util.Date;

public class Transaction {
	private static int idCounter = 1;
	private int transactionID;
	private int memeberID;
	private ArrayList<Product> productList;
	private double total;
	private Date date;

	public Transaction(int memeberID, double total, Date date) {
		this.transactionID = idCounter++;
		this.memeberID = memeberID;
		this.total = total;
		this.date = date;
		this.productList = new ArrayList<Product>();
	}

	public void addProduct(Product product) {
		productList.add(product);
	}

	public static int getIdCounter() {
		return idCounter;
	}

	public int getTransactionID() {
		return transactionID;
	}

	public int getMemberID() {
		return memeberID;
	}

	public ArrayList<Product> getProductList() {
		return productList;
	}

	public double getTotal() {
		return total;
	}

	public Date getDate() {
		return date;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return " "; // TODO: format string
	}

}
