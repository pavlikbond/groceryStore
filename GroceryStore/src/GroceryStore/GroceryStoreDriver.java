package GroceryStore;

import java.util.Date;

public class GroceryStoreDriver {

	public static void main(String[] args) {
		GroceryStore store = GroceryStore.getInstance();

		Date date = new Date();

		store.enrollMember("Pavel Bondarenko", "123 Main St. Chaska, MN 55318", "952 123-4567", date, 100.00);
		System.out.println(store.removeMember(0));
		System.out.println(store.removeMember(1));
	}

}
