package UI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import GroceryStore.GroceryStore;
import GroceryStore.Product;
import UI.UserInterface;

public class UserInterface {
	private static UserInterface userInterface;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static GroceryStore GroceryStore;
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
	}
	
	public static UserInterface instance() {
		if (userInterface == null) {
			return userInterface = new UserInterface();
		} else {
			return userInterface;
		}
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
	
	//to implement
	/*
	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case ADD_MEMBER:
				addMember();
				break;
			case ADD_BOOKS:
				addBooks();
				break;
			case ISSUE_BOOKS:
				issueBooks();
				break;
			case RETURN_BOOKS:
				returnBooks();
				break;
			case REMOVE_BOOKS:
				removeBooks();
				break;
			case RENEW_BOOKS:
				renewBooks();
				break;
			case PLACE_HOLD:
				placeHold();
				break;
			case REMOVE_HOLD:
				removeHold();
				break;
			case PROCESS_HOLD:
				processHolds();
				break;
			case GET_TRANSACTIONS:
				getTransactions();
				break;
			case GET_MEMBERS:
				getMembers();
				break;
			case GET_BOOKS:
				getBooks();
				break;
			case GET_BOOK_TITLE:
				getBookTitle();
				break;
			case SAVE:
				save();
				break;
			case HELP:
				help();
				break;
			}
		}
	} */

	public static void main(String[] args) {

		
		
		
		GroceryStore store = GroceryStore.getInstance();

		Date date = new Date();

		store.enrollMember("Pavel Bondarenko", "123 Main St. Chaska, MN 55318", "952 123-4567", date, 100.00);
		System.out.println(store.removeMember(0));
		System.out.println(store.removeMember(1));

		// Testing addProduct, checkout
		store.addProduct("a", 20, 10.5, 5);
		store.addProduct("b", 20, 10.02, 5);
		store.addProduct("c", 20, 10.11, 5);
		store.addProduct("d", 20, 10.9, 5);
		store.addProduct("e", 20, 10, 5);

		store.checkOutItems(0, date);
		System.out.println("Member: " + store.getTransaction(0).getMemberID());

		for (Product product : store.getTransaction(0).getProductList()) {
			System.out.println(product.getName());
		}

		System.out.println("Product a stock: " + store.getProduct(0).getCurrentStock());
		System.out.println("Product b stock: " + store.getProduct(1).getCurrentStock());
		System.out.println("Product c stock: " + store.getProduct(2).getCurrentStock());
		System.out.println("Product d stock: " + store.getProduct(3).getCurrentStock());
		System.out.println("Product e stock: " + store.getProduct(4).getCurrentStock());

		System.out.println("Transaction Total: " + store.getTransaction(0).getTotal());

	}

}