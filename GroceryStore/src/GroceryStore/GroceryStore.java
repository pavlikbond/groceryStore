package GroceryStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class GroceryStore {
	private ArrayList<Member> memberList;
	private ArrayList<Product> productList;
	private ArrayList<Transaction> transactionList;
	private ArrayList<Shipment> shipmentList;

	// singleton design pattern
	private static GroceryStore instance = null;

	private GroceryStore() {
		this.memberList = new ArrayList<Member>();
		this.productList = new ArrayList<Product>();
		this.transactionList = new ArrayList<Transaction>();
		this.shipmentList = new ArrayList<Shipment>();
	};

	public static GroceryStore getInstance() {
		if (instance == null) {
			instance = new GroceryStore();
		}
		return instance;
	}

	public Transaction getTransaction(int transactionId) {
		return transactionList.get(transactionId);
	}

	public Product getProduct(int productId) {
		return productList.get(productId);
	}

	public Shipment getShipment(int orderId) {
		return shipmentList.get(orderId);
	}

	// create date object, create member object, add member to list
	public boolean enrollMember(String name, String address, String phoneNumber, LocalDate dateJoined, double feePaid) {
		LocalDate date = LocalDate.now();
		Member newMember = new Member(name, address, phoneNumber, date, feePaid);
		return memberList.add(newMember);
	}

	// remove member from list using member ID
	public String removeMember(int memberID) {
		for (Member member : memberList) {
			if (member.getMemberID() == memberID) {
				// we could create a list of removed members and just transfer it?
				memberList.remove(member);
				return "Member removed";
			}
		}
		return "Member doesn't exist";
	}

	// Add a product to the product list with product name, stock, price and reorder
	// value
	public boolean addProduct(String product_name, int stock, double price, int reorder) {
		Product product = new Product(product_name, stock, price, reorder);

		return productList.add(product);
	}

	// Checks out a member once they're done shopping. Creates a transaction with
	// total price and product list and requests a shipment if product stock is
	// below reorder level.
	// TO DO: add quantities to transactions somehow
	public void checkOutItems(int memId, LocalDate date) {
		Scanner reader = new Scanner(System.in);

		Transaction transaction = new Transaction(memId, 0, date);
		String pro;
		int quantity;
		double total = 0;

		while (true) {
			System.out.println("Enter Product ID and quantity Ex. 'a 5'. Enter 0 to quit:");

			// user enters data
			pro = reader.next();
			if (pro.compareTo("0") == 0) {
				break;
			}
			quantity = reader.nextInt();

			// Find matching product in list
			for (Product product : productList) {
				if (product.getName().compareToIgnoreCase(pro) == 0) {
					// TO DO add quantity to transaction
					product.setCurrentStock(product.getCurrentStock() - quantity);
					transaction.addProduct(product);
					total += product.getPrice() * quantity;

					// make reorder if necessary
					if (product.getCurrentStock() <= product.getReorderLevel()) {
						shipmentList.add(new Shipment(product));
						System.out.println("New order has been placed for: " + product.getName());
					}

					break;
				}
			}
		}

		// Print products in transaction
		for (Product product : transaction.getProductList()) {
			System.out.println(product.getName());
		}

		// Set total and add transaction to the list
		transaction.setTotal(total);
		transactionList.add(transaction);
		reader.close();

	}

	//uses id to look through list, if id matches, then the price is changed
	public boolean changePrice(int productID, double newPrice) {
		for (Product product : productList) {
			if (product.getProductID() == productID) {
				product.setPrice(newPrice);
				return true;
			}
		}
		return false;
	}

	//searches through list of products for name and returns list of products that match criteria
	public ArrayList<Product> getProductInfo(String search) {
		ArrayList<Product> results = new ArrayList<>();
		search = search.toLowerCase();
		for (Product product : productList) {
			//instead of contains maybe something else could be used?
			if (product.getName().toLowerCase().contains(search)) {
				results.add(product);
			}
		}
		return results;
	}

	//searches memberList and if the name contains search substring then returns list of members who fit criteria
	public ArrayList<Member> getMemberInfo(String search) {
		ArrayList<Member> results = new ArrayList<>();
		search = search.toLowerCase();
		for (Member member : memberList) {
			if (member.getName().toLowerCase().contains(search)) {
				results.add(member);
			}
		}
		return results;
	}
}
