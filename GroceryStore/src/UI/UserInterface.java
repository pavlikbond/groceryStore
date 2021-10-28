package UI;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Scanner;

import GroceryStore.GroceryStore;
import GroceryStore.Member;

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

	public void help() {
		System.out.println("Enter a number between 0 and 12 as explained below:");
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

	// We can use this as the switch case for the switch statement
	// Pavel: this will create a new Scanner object each time the user enters a
	// number which I heard is not good, but I'm not sure
	// I'll put it up top that way it can be used in multiple methods
	public int getNumberFromUser(String prompt) {
		System.out.println(prompt);
		String userInput = scanner.nextLine();
		int toInt = Integer.parseInt(userInput); // This changes the String to an int
		return toInt;
	}

	public void enrollMember() {
		String name = getInput("Enter your name");
		String address = getInput("Enter address");
		String phoneNumber = getInput("Enter phone number");

		LocalDate date = LocalDate.now();
		// Pavel: is fee supposed to be a boolean? like, if they paid it or not? to me
		// that makes the most sense
		double fee = 100.00;

		boolean result = groceryStore.enrollMember(name, address, phoneNumber, date, fee);
		if (!result) {
			System.out.println("Could not add member");
		} else {
			System.out.println("Member added");
		}
	}

	public void removeMember() {
		int id = getNumberFromUser("Enter member ID: ");
		String result = groceryStore.removeMember(id);
		System.out.println(result);
	}

	// to implement

	public void process() {
		int userInput;
		help();
		while ((userInput = getNumberFromUser("Enter command ")) != 0) {
			switch (userInput) {
			case ENROLL_MEMBER:
				enrollMember();
				break;
			case REMOVE_MEMBER:
				removeMember();
				break;
			/*
			 * case ADD_PRODUCT: addProduct(); break; case CHECK_OUT: checkOutItems();
			 * break; case PROCESS_SHIPMENT: processShipment(); break; case CHANGE_PRICE:
			 * changePrice(); break; case GET_PRODUCT_INFO: getProductInfo(); break; case
			 * GET_MEMBER_INFO: getMemberInfo(); break; case PRINT_TRANSACTIONS:
			 * printTransactions(); break; case LIST_OUTSTANDING_ORDERS: printOrders();
			 * break; case LIST_MEMBERS: listMembers(); break; case LIST_PRODUCTS:
			 * listProducts(); break;
			 */
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
		LocalDate date = LocalDate.now();

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