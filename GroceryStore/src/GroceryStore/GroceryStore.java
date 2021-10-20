package GroceryStore;

import java.util.ArrayList;
import java.util.Date;

public class GroceryStore {
	private ArrayList<Member> memberList;
	private ArrayList<Product> productList;
	private ArrayList<Transaction> transactionList;

	// singleton design pattern
	private static GroceryStore instance = null;

	private GroceryStore() {
		this.memberList = new ArrayList<Member>();
		this.productList = new ArrayList<Product>();
		this.transactionList = new ArrayList<Transaction>();
	};

	public static GroceryStore getInstance() {
		if (instance == null) {
			instance = new GroceryStore();
		}
		return instance;
	}

	// create date object, create member object, add member to list
	public void enrollMember(String name, String address, String phoneNumber, Date dateJoined, double feePaid) {
		Date date = new Date();
		Member newMember = new Member(name, address, phoneNumber, date, feePaid);
		memberList.add(newMember);
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
}
