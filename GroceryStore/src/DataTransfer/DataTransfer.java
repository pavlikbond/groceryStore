package DataTransfer;

import java.time.LocalDate;

import GroceryStore.Member;
import GroceryStore.Product;
import GroceryStore.Transaction;

public abstract class DataTransfer {
	private String productName;
	private int productID;
	private int currentStock;
	private double price;
	private int reorderLevel;
	private int memberID;
	private String memberName;
	private String address;
	private String phoneNumber;
	private LocalDate date;
	private double feePaid;
	private int orderNumber;
	private Transaction transaction;
	private int quantity;

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Transaction getTransaction() {
		return this.transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
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

	public void setReorderLevel(int reorderLevel) {
		this.reorderLevel = reorderLevel;
	}

	public int getMemberID() {
		return this.memberID;
	}

	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}

	public String getMemberName() {
		return this.memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public double getFeePaid() {
		return feePaid;
	}

	public void setFeePaid(double feePaid) {
		this.feePaid = feePaid;
	}

	public void setMemberFields(Member member) {
		this.memberID = member.getMemberID();
		this.memberName = member.getName();
		this.phoneNumber = member.getPhoneNumber();
		this.address = member.getAddress();
	}

	public void setProductFields(Product product) {
		this.productName = product.getName();
		this.productID = product.getProductID();
		this.currentStock = product.getCurrentStock();
		this.reorderLevel = product.getReorderLevel();
		this.price = product.getPrice();
	}

}
