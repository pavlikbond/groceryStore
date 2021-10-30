package UI;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import DataTransfer.Request;
import DataTransfer.Result;
import GroceryStore.GroceryStore;
import GroceryStore.Member;
import GroceryStore.Product;
import GroceryStore.Shipment;
import GroceryStore.Transaction;

public class UserInterface {
	private static UserInterface userInterface;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private Scanner scanner = new Scanner(System.in);
	private static GroceryStore groceryStore;
	private static final int EXIT = 0;
	private static final int ENROLL_MEMBER = 1;
	private static final int REMOVE_MEMBER = 2;
	private static final int ADD_PRODUCT = 3;
	private static final int CHECK_OUT = 4;
	private static final int PROCESS_SHIPMENT = 5;
	private static final int CHANGE_PRICE = 6;
	private static final int GET_PRODUCT_INFO = 7;
	private static final int GET_MEMBER_INFO = 8;
	private static final int PRINT_TRANSACTIONS = 9;
	private static final int LIST_OUTSTANDING_ORDERS = 10;
	private static final int LIST_MEMBERS = 11;
	private static final int LIST_PRODUCTS = 12;
	private static final int SAVE = 13;
	private static final int HELP = 14;

	private UserInterface() {
		groceryStore = GroceryStore.getInstance();
	}

	public static UserInterface instance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		} else {
			return userInterface;
		}
	}

	public void help() {
		System.out.println("Enter a number between 0 and 14 as explained below:");
		System.out.println(EXIT + " to Exit\n");
		System.out.println(ENROLL_MEMBER + " to enroll a new member");
		System.out.println(REMOVE_MEMBER + " to remove a member");
		System.out.println(ADD_PRODUCT + " to add a new product");
		System.out.println(CHECK_OUT + " to check out the items");
		System.out.println(PROCESS_SHIPMENT + " to process a new shipment");
		System.out.println(CHANGE_PRICE + " to change the price of a product");
		System.out.println(GET_PRODUCT_INFO + " to get product information");
		System.out.println(GET_MEMBER_INFO + " to get member information");
		System.out.println(PRINT_TRANSACTIONS + " to print member's transactions");
		System.out.println(LIST_OUTSTANDING_ORDERS + " to see all outstanding orders");
		System.out.println(LIST_MEMBERS + " to print all members");
		System.out.println(LIST_PRODUCTS + " to print all products");
		System.out.println(SAVE + " to  save data");
		System.out.println(HELP + " for help");
	}

	// Here is step 13, it doesn't require we load so here is only save
	// This should save data to the disk
	public static boolean save() {
		try {
			FileOutputStream file = new FileOutputStream("LibraryData");
			ObjectOutputStream output = new ObjectOutputStream(file);
			output.writeObject(groceryStore);
			Member.save(output);
			file.close();
			return true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}
	}

	//Method to close scanners
	public void close() {
		scanner.close();
	}

	public int getNumberFromUser(String prompt) {
		System.out.println(prompt);
		String userInput = scanner.nextLine();
		int toInt = Integer.parseInt(userInput); // This changes the String to an int
		return toInt;
	}

	public double getPriceFromUser(String prompt) {
		System.out.println(prompt);
		String userInput = scanner.nextLine();
		double toDouble = Double.parseDouble(userInput); // This changes the String to an int
		return toDouble;
	}

	public String getInput(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				return line;
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);

	}

	public LocalDate getDate(String prompt) {
		do {
			try {
				String date = getInput(prompt);
				DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				return LocalDate.parse(date, format);
			} catch (Exception fe) {
				System.out.println("Format incorrect");
			}
		} while (true);
	}

	public void enrollMember() {
		Request request = new Request();
		request.setMemberName(getInput("Enter member name"));
		request.setAddress(getInput("Enter member address"));
		request.setPhoneNumber(getInput("Enter member phone number"));
		request.setFeePaid(100.00);
		// Pavel: is fee supposed to be a boolean? like, if they paid it or not? to me
		// that makes the most sense
		Result result = groceryStore.enrollMember(request);
		if (result.getResultCode() == result.OPERATION_COMPLETED) {
			System.out.println("Member added. The member ID is: " + result.getMemberID());
		} else {
			System.out.println("Could not add member");
		}
	}

	public void removeMember() {
		Request request = new Request();
		request.setMemberID(getNumberFromUser("Enter member ID: "));
		Result result = groceryStore.removeMember(request);
		if (result.getResultCode() == Result.OPERATION_COMPLETED) {
			System.out.println("Member Removed");
		} else if (result.getResultCode() == Result.MEMBER_NOT_FOUND) {
			System.out.println("Member not found");
		} else {
			//if the list fails to remove member for some reason
			System.out.println("Member could not be removed");
		}
	}

	public void addProduct() {
		Request request = new Request();
		request.setProductName(getInput("Enter product name :"));
		request.setCurrentStock(getNumberFromUser("Enter current stock :"));
		request.setPrice(getPriceFromUser("Enter current price: "));
		request.setReorderLevel(getNumberFromUser("Enter reorder point"));
		Result result = groceryStore.addProduct(request);
		if (result.getResultCode() == Result.OPERATION_COMPLETED) {
			System.out.println("Product added! Product ID is: " + result.getProductID());
		} else {
			System.out.println("Product could not be added");
		}
	}

	//I'm still working on this please don't delete -Pavel. It works except for the reordering which I need to troubleshoot
	public void checkOutItems1() {
		int command;
		Request request = new Request();
		request.setDate(LocalDate.now());
		request.setMemberID(getNumberFromUser("Enter member ID"));
		Member member = groceryStore.verifyMember(request.getMemberID());
		if (member == null) {
			System.out.println("Member ID not found in records");
			return;
		}
		Transaction transaction = new Transaction(0, request.getDate());
		do {
			request.setProductID(getNumberFromUser("Enter product ID: "));
			request.setQuantity(getNumberFromUser("Enter quantity: "));
			Result result = groceryStore.checkOutItems1(request);
			if (result.getResultCode() == Result.PRODUCT_NOT_FOUND) {
				System.out.println("Product not found");
			} else {
				transaction.addProduct(result.getProduct(), request.getQuantity());
				double itemsTotal = request.getQuantity() * result.getPrice();
				System.out.println(result.getProductName() + " " + request.getQuantity() + " $" + result.getPrice()
						+ " $" + itemsTotal);
			}
		} while ((command = getNumberFromUser("Check out more items? Type 1 for \"yes\", any number for \"no\"")) == 1);
		//print total
		System.out.println("Total is: $" + transaction.getTotalAmount());
		//take all itmes and order the ones that are needed
		ArrayList<Shipment> shipments = groceryStore.orderProducts(transaction);
		for (Shipment shipment : shipments) {
			System.out.println("Item reordered: " + shipment.getProduct().getName());
			System.out.println("Orered quantity: " + shipment.getOrderedQuantity());
			System.out.println("Shipment order number: " + shipment.getOrderNumber());
		}
	}

	//Original version
	public void checkOutItems() {
		Request request = new Request();
		request.setDate(LocalDate.now());
		request.setMemberID(getNumberFromUser("Enter member ID"));
		groceryStore.checkOutItems(request);
	}

	public void processShipment() {
		Request request = new Request();
		int command;
		do {
			request.setOrderNumber(getNumberFromUser("Enter order number: "));
			Result result = groceryStore.processShipment(request);
			if (result.getResultCode() == Result.OPERATION_COMPLETED) {
				System.out.println("Product ID: " + result.getProductID());
				System.out.println("Product name: " + result.getProductName());
				System.out.println("New stock: " + result.getCurrentStock());
			} else {
				System.out.println("Shipment not found");
			}
		} while ((command = getNumberFromUser(
				"Process more shipments? Type 1 for \"yes\", any number for \"no\"")) == 1);

	}

	public void changePrice() {
		Request request = new Request();
		request.setProductID(getNumberFromUser("Enter product ID: "));
		request.setPrice(getPriceFromUser("Enter new price: "));
		Result result = groceryStore.changePrice(request);
		if (result.getResultCode() == Result.OPERATION_COMPLETED) {
			System.out.println("New price has been set");
		} else {
			System.out.println("Product not found");
		}
	}

	public void getProductInfo() {
		Request request = new Request();
		request.setProductName(getInput("Enter the name of the product: "));
		System.out.println(request.getProductName());
		ArrayList<Product> results = groceryStore.getProductInfo(request);
		if (results.isEmpty()) {
			System.out.println("No products found with given criteria");
		} else {
			for (Product product : results) {
				System.out.println("Product name: " + product.getName());
				System.out.println("Product ID: " + product.getProductID());
				System.out.println("Product price: " + product.getPrice());
				System.out.println("Product stock: " + product.getCurrentStock());
				System.out.println("Product reodrder quantity: " + product.getReorderLevel());
				System.out.println();
			}
		}
	}

	public void getMemberInfo() {
		Request request = new Request();
		request.setMemberName(getInput("Enter the name you're looking for: "));
		ArrayList<Member> results = groceryStore.getMemberInfo(request);
		if (results.isEmpty()) {
			System.out.println("No members found with given criteria");
		} else {
			for (Member member : results) {
				System.out.println("Member name: " + member.getName());
				System.out.println("Address: " + member.getAddress());
				System.out.println("Member ID: " + member.getMemberID());
				System.out.println("Fee paid: " + member.getFeePaid());
				System.out.println();
			}
		}
	}

	//This should validate between the two dates
	public boolean validBetweenDates(LocalDate date1, LocalDate date2) {
		if (date1.isBefore(date2) || date1.isEqual(date2)) {
			return true;
		}
		return false;
	}

	//gets the transactions by member ID and 2 dates
	//Step 9
	public void printTransactions() {
		Request request = new Request();
		request.setMemberID(getNumberFromUser("Enter member ID"));
		LocalDate date1 = getDate("Please enter first date in format mm/dd/yyyy");
		LocalDate date2 = getDate("Please enter second date in format mm/dd/yyyy");
		request.setStartDate(date1);
		request.setEndDate(date2);

		if (validBetweenDates(date1, date2) != true) {
			System.out.println("You're first date must come before the second date");
		} else {
			ArrayList<Transaction> result = groceryStore.getTransactions(request);
			if (result == null) {
				//We still need this because null is returned if member is not found, but list of 0 is returned if member
				//is found but no transactions matched criteria
				System.out.println("Member does not exist: ");
			} else if (result.isEmpty()) {
				System.out.println("This member does not have any transactions");
			} else {
				for (Transaction transaction : result) {
					System.out.println(transaction.toString());
					System.out.println();
				}
			}
		}

	}

	//LocalDate date1 = LocalDate.parse(userDate1, format);
	//LocalDate date2 = LocalDate.parse(userDate2, format);

	/*
	if (!(date1.isBefore(date2) || date1.isEqual(date2))) {
		System.out.println("Improper sequence of dates, first date is after the second date.");
	} else {
		ArrayList<Transaction> transactions = groceryStore.getTransactions(memberID, date1, date2);
		if (transactions.isEmpty()) {
			System.out.println("This member does not have any transactions");
		} //I think we can get rid of this because if it is empty, it's empty
		if (transactions == null) {
			System.out.println("Member does not exist");
		} else {
			for (Transaction transaction : transactions) {
				transaction.toString();
			}
		}
	}*/

	public void printOrders() {
		ArrayList<Shipment> orders = groceryStore.getShipments();
		if (!orders.isEmpty()) {
			for (Shipment ship : orders) {
				System.out.println("Order ID: " + ship.getOrderNumber());
				System.out.println("Product name: " + ship.getProduct().getName());
				System.out.println("Date of order: " + ship.getDate());
				System.out.println("Quantity ordered: " + ship.getOrderedQuantity());
			}
		} else {
			System.out.println("There are currently no outstanding orders.");
		}

	}

	//This returns all the members
	//Step 11
	public void getAllMembersInfo() {
		ArrayList<Member> results = groceryStore.getAllMemberInfo();
		if (results.isEmpty()) {
			System.out.println("There are currently no members listed.\n");
		} else {
			for (Member member : results) {
				System.out.println("Member name: " + member.getName());
				System.out.println("Member ID: " + member.getMemberID());
				System.out.println("Address: " + member.getAddress());
				System.out.println("Member phone muber: " + member.getPhoneNumber());
				System.out.println("Fee paid: " + member.getFeePaid() + "\n");
			}
		}
	}

	//This returns all the product information
	//Step 12
	public void getAllProductInfo() {
		ArrayList<Product> results = groceryStore.getAllProducts();
		if (results.isEmpty()) {
			System.out.println("There are currently no products listed.\n");
		} else {
			for (Product product : results) {
				System.out.println("Product name: " + product.getName());
				System.out.println("Product ID: " + product.getProductID());
				System.out.println("Product price: " + product.getPrice());
				System.out.println("Product stock: " + product.getCurrentStock());
				System.out.println("Product reodrder quantity: " + product.getReorderLevel() + "\n");
			}
		}
	}

	public void process() {
		int userInput;
		help();
		while ((userInput = getNumberFromUser("Enter command. Enter " + HELP + " for help.")) != 0) {
			switch (userInput) {
			case ENROLL_MEMBER:
				enrollMember();
				break;
			case REMOVE_MEMBER:
				removeMember();
				break;
			case ADD_PRODUCT:
				addProduct();
				break;
			case CHECK_OUT:
				checkOutItems();
				break;
			case PROCESS_SHIPMENT:
				processShipment();
				break;
			case CHANGE_PRICE:
				changePrice();
				break;
			case GET_PRODUCT_INFO:
				getProductInfo();
				break;
			case GET_MEMBER_INFO:
				getMemberInfo();
				break;
			case PRINT_TRANSACTIONS:
				printTransactions();
				break;
			case LIST_OUTSTANDING_ORDERS:
				printOrders();
				break;
			case LIST_MEMBERS: //Step 11
				getAllMembersInfo();
				break;
			case LIST_PRODUCTS: //Step 12
				getAllProductInfo();
				break;
			case SAVE:
				save();
				break;
			case HELP:
				help();
				break;
			}
		}
	}

	public static void main(String[] args) {

		GroceryStore store = GroceryStore.getInstance();
		UserInterface.instance().process();

		/*
		 * store.enrollMember("Pavel Bondarenko", "123 Main St. Chaska, MN 55318",
		 * "952 123-4567", date, 100.00); System.out.println(store.removeMember(0));
		 * System.out.println(store.removeMember(1));
		 * 
		 * // Testing addProduct, checkout store.addProduct("a", 20, 10.5, 5);
		 * store.addProduct("b", 20, 10.02, 5); store.addProduct("c", 20, 10.11, 5);
		 * store.addProduct("d", 20, 10.9, 5); store.addProduct("e", 20, 10, 5);
		 * 
		 * store.checkOutItems(0, date); System.out.println("Member: " +
		 * store.getTransaction(0).getMemberID());
		 * 
		 * System.out.println("Product a stock: " +
		 * store.getProduct(0).getCurrentStock());
		 * System.out.println("Product b stock: " +
		 * store.getProduct(1).getCurrentStock());
		 * System.out.println("Product c stock: " +
		 * store.getProduct(2).getCurrentStock());
		 * System.out.println("Product d stock: " +
		 * store.getProduct(3).getCurrentStock());
		 * System.out.println("Product e stock: " +
		 * store.getProduct(4).getCurrentStock());
		 * 
		 * System.out.println("Transaction Total: " +
		 * store.getTransaction(0).getTotal());
		 * 
		 * // Testing for shipment System.out
		 * .println(store.getShipment(0).getProduct().getName() + " " +
		 * store.getShipment(0).getOrderedQuantity()); System.out
		 * .println(store.getShipment(1).getProduct().getName() + " " +
		 * store.getShipment(1).getOrderedQuantity());
		 * 
		 * store.getShipment(0).process();
		 * 
		 * System.out.println("Product a stock: " +
		 * store.getProduct(0).getCurrentStock()); System.out.println("Processed: " +
		 * store.getShipment(0).isProcessed());
		 */

	}

}