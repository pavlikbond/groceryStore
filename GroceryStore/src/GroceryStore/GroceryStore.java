package GroceryStore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import DataTransfer.Request;
import DataTransfer.Result;

public class GroceryStore {
	private ArrayList<Member> memberList;
	private ArrayList<Product> productList;
	private ArrayList<Shipment> shipmentList;
	Scanner reader = new Scanner(System.in);
	// singleton design pattern
	private static GroceryStore instance = null;

	private GroceryStore() {
		this.memberList = new ArrayList<Member>();
		this.productList = new ArrayList<Product>();
		this.shipmentList = new ArrayList<Shipment>();
	};

	public static GroceryStore getInstance() {
		if (instance == null) {
			instance = new GroceryStore();
		}
		return instance;
	}

	public Product getProduct(int productId) {
		for (Product product : productList) {
			if (product.getProductID() == productId) {
				return product;
			}
		}
		return null;
	}

	public Shipment getShipment(int orderId) {
		for (Shipment shipment : shipmentList) {
			if (shipment.getOrderNumber() == orderId) {
				return shipment;
			}
		}
		return null;
	}

	public Member verifyMember(int memberID) {
		for (Member member : memberList) {
			if (member.getMemberID() == memberID) {
				return member;
			}
		}
		return null;
	}

	// create date object, create member object, add member to list
	public Result enrollMember(Request request) {
		Result result = new Result();
		LocalDate date = LocalDate.now();
		Member newMember = new Member(request.getMemberName(), request.getAddress(), request.getPhoneNumber(), date,
				request.getFeePaid());
		if (memberList.add(newMember)) {
			result.setResultCode(Result.OPERATION_COMPLETED);
			result.setMemberFields(newMember);
			return result;
		}
		result.setResultCode(Result.OPERATION_FAILED);
		return result;
	}

	// remove member from list using member ID
	public Result removeMember(Request request) {
		Result result = new Result();
		Member member = verifyMember(request.getMemberID());
		if (member != null) {
			if (memberList.remove(member)) {
				result.setResultCode(Result.OPERATION_COMPLETED);
				return result;
			} else {
				result.setResultCode(Result.OPERATION_FAILED);
				return result;
			}
		} else {
			result.setResultCode(Result.MEMBER_NOT_FOUND);
			return result;
		}
	}

	// Add a product to the product list with product name, stock, price and reorder
	// value
	public Result addProduct(Request request) {
		Result result = new Result();
		Product product = new Product(request.getProductName(), request.getCurrentStock(), request.getPrice(),
				request.getReorderLevel());
		if (productList.add(product)) {
			result.setResultCode(Result.OPERATION_COMPLETED);
			result.setProductFields(product);
			return result;
		} else {
			result.setResultCode(Result.OPERATION_FAILED);
			return result;
		}
	}

	//Still working on it, do not delete.
	public Result checkOutItems1(Request request) {
		Result result = new Result();
		Transaction transaction = new Transaction(0, request.getDate());
		Product product = getProduct(request.getProductID());
		//member already verified, this is just to get the member object
		Member member = verifyMember(request.getMemberID());
		if (product != null) {
			result.setProduct(product);
			result.setProductFields(product);
			result.setResultCode(Result.OPERATION_COMPLETED);
			return result;
		} else {
			result.setResultCode(Result.PRODUCT_NOT_FOUND);
			return result;
		}
	}

	//Still working on it do not delete
	public ArrayList<Shipment> orderProducts(Transaction transaction) {
		ArrayList<Shipment> shipments = transaction.orderProducts();
		shipmentList.addAll(shipments);
		return shipments;
	}

	// Checks out a member once they're done shopping. Creates a transaction with
	// total price and product list and requests a shipment if product stock is
	// below reorder level.
	public void checkOutItems(Request request) {
		Result result = new Result();
		Transaction transaction = new Transaction(0, request.getDate());
		Member member = verifyMember(request.getMemberID());
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
					transaction.addProduct(product, quantity);
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
		int i = 0;
		for (Product product : transaction.getProductList()) {
			System.out.println(product.getName() + " " + transaction.getQuantity(i) + " $" + product.getPrice() + " $"
					+ product.getPrice() * transaction.getQuantity(i));
		}

		// Set total and add transaction to the list
		transaction.setTotal(total);
		member.addTransaction(transaction);
		reader.close();

	}

	//uses id to look through list, if id matches, then the price is changed
	public Result changePrice(Request request) {
		Result result = new Result();
		for (Product product : productList) {
			if (product.getProductID() == request.getProductID()) {
				product.setPrice(request.getPrice());
				result.setResultCode(Result.OPERATION_COMPLETED);
				return result;
			}
		}
		result.setResultCode(Result.PRODUCT_NOT_FOUND);
		return result;
	}

	public ArrayList<Shipment> getShipments() {
		return shipmentList;
	}

	//helper method to validate shipment exists
	public Shipment findShipment(int orderNum) {
		for (Shipment shipment : shipmentList) {
			if (shipment.getOrderNumber() == orderNum) {
				return shipment;
			}
		}
		return null;
	}

	public Result processShipment(Request request) {
		Result result = new Result();
		Product product;
		Shipment shipment = findShipment(request.getOrderNumber());
		if (shipment != null) {
			product = shipment.getProduct();
			product.setCurrentStock(product.getCurrentStock() + shipment.getOrderedQuantity());
			shipmentList.remove(shipment);
			result.setResultCode(Result.OPERATION_COMPLETED);
			result.setProductFields(product);
			return result;
		} else {
			result.setResultCode(Result.SHIPMENT_NOT_FOUND);
			return result;
		}
	}

	public ArrayList<Transaction> getTransactions(Request request) {
		ArrayList<Transaction> list = new ArrayList<>();
		Result result = new Result();
		Member member = verifyMember(request.getMemberID());
		if (member != null) {
			return member.getTransactionList(request.getStartDate(), request.getEndDate());
		} else {
			return null;
		}
	}

	//searches through list of products for name and returns list of products that match criteria
	public ArrayList<Product> getProductInfo(Request request) {
		ArrayList<Product> results = new ArrayList<>();
		String search = request.getProductName().toLowerCase();
		for (Product product : productList) {
			if (product.getName().toLowerCase().startsWith(search)) {
				results.add(product);
			}
		}
		return results;
	}

	//searches memberList and if the name contains search substring then returns list of members who fit criteria
	public ArrayList<Member> getMemberInfo(Request request) {
		ArrayList<Member> results = new ArrayList<>();
		String search = request.getMemberName().toLowerCase();
		for (Member member : memberList) {
			if (member.getName().toLowerCase().startsWith(search)) {
				results.add(member);
			}
		}
		return results;
	}

	//This should return all the members
	//This is step 11
	public ArrayList<Member> getAllMemberInfo() {
		//this makes a copy and returns it without doing the for loop
		return new ArrayList<>(memberList);
	}

	//This should return all the products on hand
	//This is for step 12
	public ArrayList<Product> getAllProducts() {
		//this makes a copy and returns it without doing the for loop
		return new ArrayList<>(productList);
	}
}