package GroceryStore;

import java.util.Date;

public class GroceryStoreDriver {

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
		store.addProduct("d", 20, 10, 5);
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
