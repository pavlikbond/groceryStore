package GroceryStore;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import DataTransfer.Request;
import DataTransfer.Result;

public class GroceryStore implements Serializable {
	private ArrayList<Member> memberList;
	private ArrayList<Product> productList;
	private ArrayList<Shipment> shipmentList;
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

	//searches through productlist by Id and returns product if found
	public Product getProduct(int productId) {
		for (Product product : productList) {
			if (product.getProductID() == productId) {
				return product;
			}
		}
		return null;
	}

	//searches through shipment list and returns shipment if found
	public Shipment getShipment(int orderId) {
		for (Shipment shipment : shipmentList) {
			if (shipment.getOrderNumber() == orderId) {
				return shipment;
			}
		}
		return null;
	}

	//searches through memeberlist and returns memeber if found
	public Member getMember(int memberID) {
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
		Member member = getMember(request.getMemberID());
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
		//make sure there are no products with same name
		for (Product product : productList) {
			if (product.getName().toLowerCase().equals(request.getProductName().toLowerCase())) {
				result.setResultCode(Result.SAME_NAME_EXISTS);
				return result;
			}
		}
		Product product = new Product(request.getProductName(), request.getCurrentStock(), request.getPrice(),
				request.getReorderLevel());
		if (productList.add(product)) {
			//automatically order 2x product
			shipmentList.add(new Shipment(product));
			result.setResultCode(Result.OPERATION_COMPLETED);
			result.setProductFields(product);
			return result;
		} else {
			result.setResultCode(Result.OPERATION_FAILED);
			return result;
		}
	}

	//checkout out items, fails if qty is greater than stock or product not found
	public Result checkOutItems(Request request) {
		Result result = new Result();
		LocalDate date = LocalDate.now();
		Product product = getProduct(request.getProductID());
		if (product != null) {
			result.setProduct(product);
			result.setProductFields(product);
			//make sure quantity entered is not more than current stock
			if (product.getCurrentStock() - request.getQuantity() < 0) {
				result.setResultCode(Result.QUANTITY_EXCEEDS_STOCK);
				return result;
			}
			product.setCurrentStock(product.getCurrentStock() - request.getQuantity());
			result.setResultCode(Result.OPERATION_COMPLETED);
			return result;
		} else {
			result.setResultCode(Result.PRODUCT_NOT_FOUND);
			return result;
		}
	}

	//returns list of shipments for checked out items if qty < reorderLevel
	public ArrayList<Shipment> orderProducts(Transaction transaction) {
		ArrayList<Shipment> shipments = transaction.orderProducts();
		shipmentList.addAll(shipments);
		return shipments;
	}

	//adds transaction to member after checkout
	public void addTransaction(Request request) {
		Member member = getMember(request.getMemberID());
		member.addTransaction(request.getTransaction());
	}

	//uses id to look through list, if id matches, then the price is changed
	public Result changePrice(Request request) {
		Result result = new Result();
		for (Product product : productList) {
			if (product.getProductID() == request.getProductID()) {
				product.setPrice(request.getPrice());
				result.setResultCode(Result.OPERATION_COMPLETED);
				result.setPrice(product.getPrice());
				return result;
			}
		}
		result.setResultCode(Result.PRODUCT_NOT_FOUND);
		return result;
	}

	//returns copy of shipment list
	public ArrayList<Shipment> getShipments() {
		return new ArrayList<>(shipmentList);
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

	//method to process shipment, adds to current stock of product
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

	//returns a list of transactions that match the member Id and is withing specified dates
	public ArrayList<Transaction> getTransactions(Request request) {
		ArrayList<Transaction> list = new ArrayList<>();
		Result result = new Result();
		Member member = getMember(request.getMemberID());
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

	//saves the GroceryStore object
	public static boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("GroceryStoreData");
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(instance);
			Member.save(output);
			Product.save(output);
			Shipment.save(output);
			Transaction.save(output);
			file.close();
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	//looks for and retrieves GroceryStore object
	public static GroceryStore retrieve() {
		try {
			FileInputStream file = new FileInputStream("GroceryStoreData");
			ObjectInputStream input = new ObjectInputStream(file);
			instance = (GroceryStore) input.readObject();
			Member.retrieve(input);
			Product.retrieve(input);
			Shipment.retrieve(input);
			Transaction.retrieve(input);
			return instance;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}