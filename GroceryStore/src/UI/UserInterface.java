package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
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
		if (getNumberFromUser("Retreive data? Enter \"1\" for yes, any other number for no") == 1) {
			retrieve();
		} else {
			if (getNumberFromUser("Run test bed? Enter \"1\" for yes, any other number for no") == 1) {
				groceryStore = GroceryStore.getInstance();
				test();
			} else {
				groceryStore = GroceryStore.getInstance();
			}
		}
	}

	public static UserInterface instance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		} else {
			return userInterface;
		}
	}

	//prints the menu of options
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

	//gets input from user and returns as string
	public int getNumberFromUser(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String userInput = scanner.nextLine();
				int toInt = Integer.parseInt(userInput); // This changes the String to an int
				return toInt;
			} catch (NumberFormatException e) {
				System.out.println("Please, only enter digit(s)");
			}
		} while (true);
	}

	//gets input from user and returns as double
	public double getPriceFromUser(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String userInput = scanner.nextLine();
				double toDouble = Double.parseDouble(userInput); // This changes the String to an int
				return toDouble;
			} catch (NumberFormatException e) {
				System.out.println("Input has to be a number");
			}
		} while (true);

	}

	//gets any input and returns as string
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

	//gets input from user and returns it as LocalDate
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

	//adds a member to the memberList
	public void enrollMember() {
		Request request = new Request();
		request.setMemberName(getInput("Enter member name"));
		request.setAddress(getInput("Enter member address"));
		request.setPhoneNumber(getInput("Enter member phone number"));
		request.setFeePaid(100.00);
		Result result = groceryStore.enrollMember(request);
		if (result.getResultCode() == result.OPERATION_COMPLETED) {
			System.out.println("Member added. The member ID is: " + result.getMemberID());
		} else {
			System.out.println("Could not add member");
		}
	}

	//method to remove member using member id
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

	//add a new product to the list of products. Names can't be duplicated
	public void addProduct() {
		Request request = new Request();
		request.setProductName(getInput("Enter product name :"));
		request.setCurrentStock(getNumberFromUser("Enter current stock :"));
		request.setPrice(getPriceFromUser("Enter current price: "));
		request.setReorderLevel(getNumberFromUser("Enter reorder point"));
		Result result = groceryStore.addProduct(request);
		if (result.getResultCode() == Result.OPERATION_COMPLETED) {
			System.out.println("Product added! Product ID is: " + result.getProductID());
		} else if (result.getResultCode() == Result.SAME_NAME_EXISTS) {
			System.out.println("Product name already exists, can't add duplicates");
		} else {
			System.out.println("Product could not be added");
		}
	}

	//Checkout. verifies member ID, then takes product id and quantity. generates transaction and passes
	//it along to member to be stored. Then reorders and prints all items that need to be reordered
	public void checkOutItems() {
		int command;
		Request request = new Request();
		request.setMemberID(getNumberFromUser("Enter member ID"));
		Result result = groceryStore.verifyMember(request);
		if (result.getResultCode() == Result.MEMBER_NOT_FOUND) {
			System.out.println("Member ID not found in records");
			return;
		}
		Transaction transaction = new Transaction(0, LocalDate.now());
		do {
			request.setProductID(getNumberFromUser("Enter product ID: "));
			request.setQuantity(getNumberFromUser("Enter quantity: "));
			result = groceryStore.checkOutItems(request);
			if (result.getResultCode() == Result.PRODUCT_NOT_FOUND) {
				System.out.println("Product not found");
			} else if (result.getResultCode() == Result.QUANTITY_EXCEEDS_STOCK) {
				System.out.println("Quantity exceeds current stock");
			} else {
				transaction.addProduct(result.getProduct(), request.getQuantity());
				double itemsTotal = request.getQuantity() * result.getPrice();
				System.out.println(result.getProductName() + " " + request.getQuantity() + " $" + result.getPrice()
						+ " $" + itemsTotal);
			}
		} while ((command = getNumberFromUser("Check out more items? Type 1 for \"yes\", any number for \"no\"")) == 1);
		//print total
		System.out.println("Total is: $" + transaction.getTotal());
		request.setTransaction(transaction);
		ArrayList<Shipment> shipments = groceryStore.getShipments(request);
		for (Shipment shipment : shipments) {
			System.out.println("Item reordered: " + shipment.getProduct().getName());
			System.out.println("Orered quantity: " + shipment.getOrderedQuantity());
			System.out.println("Shipment order number: " + shipment.getOrderNumber());
		}
	}

	//process shipments. take shipment number and adds the ordered qty to stock of product
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

	//changes the price for a given product id, if product can be found
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

	//given a search input, looks for all products that begin with that input
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

	//given a part of a name, searches for and prints all members that begin with that name
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
				System.out.println("Fee paid: $" + member.getFeePaid());
				System.out.println("Date joined: " + member.getDateJoined());
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

	//prints all current orders (shipments) that are waiting to be processed
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

	//This returns all the members from the member list
	//Step 11
	public void getAllMembersInfo() {
		ArrayList<Member> results = groceryStore.getAllMemberInfo();
		if (results.isEmpty()) {
			System.out.println("There are currently no members listed.\n");
		} else {
			for (Member member : results) {
				System.out.println("Member name: " + member.getName());
				System.out.println("Address: " + member.getAddress());
				System.out.println("Member phone number: " + member.getPhoneNumber());
				System.out.println("Member ID: " + member.getMemberID());
				System.out.println("Date joined: " + member.getDateJoined() + "\n");
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

	//saves the grocery store object and all the rest of the objects that it stores
	private void save() {
		if (groceryStore.save()) {
			System.out.println(" The grocery store has been successfully saved in the file GroceryStoreData \n");
		} else {
			System.out.println(" There has been an error in saving \n");
		}
	}

	//retrieves grocery store object if one is saved
	private void retrieve() {
		try {
			if (groceryStore == null) {
				groceryStore = GroceryStore.retrieve();
				if (groceryStore != null) {
					System.out.println(
							" The grocery store has been successfully retrieved from the file GroceryStoreData \n");
				} else {
					System.out.println("File doesnt exist; creating new Grocery Store");
					groceryStore = GroceryStore.getInstance();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//the menu method to run all other methods
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

	//test bed to test business processes 1-6
	public static void test() {

		GroceryStore gs = GroceryStore.getInstance();

		String[] names = { "Jeff Bridges", "Mickey Rourke", "Chris Brown", "Johnny Depp", "Lady Gaga" };
		String[] products = { "Milk", "Milk Duds", "Milk Choclate", "Milky Way", "Cinnamon", "heavy cream", "eel",
				"raspberries", "peanuts", "bass", "swiss cheese", "quail eggs", "lima beans", "tomato puree",
				"truffles", "parmesan cheese", "olives", "brown sugar", "pine nunts", "Bud light beer" };
		int[] stock = { 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105 };
		int[] reorder = { 50, 100, 5, 40, 12, 33, 40, 80, 8, 5, 12, 150, 20, 22, 44, 19, 150, 250, 305, 100 };
		double[] prices = { 4.50, 5, 6.99, 7.99, 8.50, 3.50, 3.99, 3.75, 19.99, 19.50, 14.99, 12.00, 40.0, 18.0, 0.99,
				1.29, 1.50, 1.75, 1.80, 1.99 };
		Random random = new Random();
		//generate 5 members
		for (int i = 0; i < 5; i++) {
			Request request = new Request();
			request.setMemberName(names[i]);
			request.setAddress("123 Spooner Street");
			request.setPhoneNumber(String.valueOf(random.nextInt(999999999)));
			request.setFeePaid(100);
			gs.enrollMember(request);
		}

		//generate 20 products
		for (int i = 0; i < products.length; i++) {
			Request request = new Request();
			request.setProductName(products[i]);
			request.setCurrentStock(stock[i]);
			request.setReorderLevel(reorder[i]);
			request.setPrice(prices[i]);
			gs.addProduct(request);
		}

		//test process 1, enrollMember
		Request request1 = new Request();
		request1.setMemberName("Tom Cruise");
		request1.setAddress("123 Yorkshire Ln");
		request1.setPhoneNumber("1234567890");
		request1.setFeePaid(100);

		assert request1.getMemberName().equals("Tom Cruise");
		assert request1.getAddress().equals("123 Yorkshire Ln");
		assert request1.getPhoneNumber().equals("1234567890");
		assert request1.getFeePaid() == 100;

		Result result1 = gs.enrollMember(request1);

		assert result1.getMemberID() == 6;
		assert result1.getMemberName().equals("Tom Cruise");
		assert result1.getResultCode() == Result.OPERATION_COMPLETED;
		//test process 2, remove member
		Request request2 = new Request();
		request2.setMemberID(6);

		Result result2 = gs.removeMember(request2);
		assert result2.getResultCode() == Result.OPERATION_COMPLETED;
		request2.setMemberID(-2);
		result2 = gs.removeMember(request2);
		assert result2.getResultCode() == Result.MEMBER_NOT_FOUND;

		//test process 3, add product
		Request request3 = new Request();
		request3.setProductName("Spinach");
		request3.setCurrentStock(50);
		request3.setReorderLevel(100);
		request3.setPrice(4.50);
		Result result3 = gs.addProduct(request3);

		assert request3.getProductName().equals("Spinach");
		assert request3.getCurrentStock() == 50;
		assert request3.getReorderLevel() == 100;
		assert request3.getPrice() == 4.50;

		assert result3.getResultCode() == Result.OPERATION_COMPLETED;
		assert result3.getProductName().equals("Spinach");
		assert result3.getReorderLevel() == 100;
		assert result3.getCurrentStock() == 50;
		assert result3.getProductID() == 21;
		assert result3.getPrice() == 4.50;

		request3.setProductName("Spinach");
		result3 = gs.addProduct(request3);
		assert result3.getResultCode() == Result.SAME_NAME_EXISTS;

		//test process 4, checkout
		Request request4 = new Request();
		request4.setProductID(21);
		request4.setQuantity(10);
		assert request4.getProductID() == 21;
		assert request4.getQuantity() == 10;

		Result result4 = gs.checkOutItems(request4);

		assert result4.getResultCode() == Result.OPERATION_COMPLETED;
		assert result4.getProductID() == 21;
		assert result4.getProductName() == "Spinach";

		request4.setQuantity(500);
		result4 = gs.checkOutItems(request4);
		assert result4.getResultCode() == Result.QUANTITY_EXCEEDS_STOCK;

		request4.setQuantity(1);
		request4.setProductID(500);
		result4 = gs.checkOutItems(request4);

		assert result4.getResultCode() == Result.PRODUCT_NOT_FOUND;

		//test process 5, process shipments
		Request request5 = new Request();
		request5.setOrderNumber(21);
		assert request5.getOrderNumber() == 21;

		Result result5 = gs.processShipment(request5);
		assert result5.getResultCode() == Result.OPERATION_COMPLETED;
		assert result5.getProductName().equals("Spinach");
		assert result5.getReorderLevel() == 100;
		assert result5.getCurrentStock() == 240;

		request5.setOrderNumber(250);
		result5 = gs.processShipment(request5);
		assert result5.getResultCode() == Result.SHIPMENT_NOT_FOUND;

		//test process 6, change price
		Request request6 = new Request();
		request6.setProductID(21);
		request6.setPrice(8.00);

		Result result6 = gs.changePrice(request6);
		assert result6.getPrice() == 8.00;
		assert result6.getResultCode() == Result.OPERATION_COMPLETED;

		request6.setProductID(250);
		result6 = gs.changePrice(request6);
		assert result6.getResultCode() == Result.PRODUCT_NOT_FOUND;

	}

	public static void main(String[] args) {
		UserInterface.instance().process();

	}

}