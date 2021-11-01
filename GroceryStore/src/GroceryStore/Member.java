package GroceryStore;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Member implements Serializable {
	private static int idCounter = 1;
	private int memberID;
	private String name;
	private String address;
	private String phoneNumber;
	private LocalDate dateJoined;
	private double feePaid;
	private ArrayList<Transaction> transactionList;

	public Member(String name, String address, String phoneNumber, LocalDate dateJoined, double feePaid) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.dateJoined = dateJoined;
		this.feePaid = feePaid;
		this.memberID = idCounter++;
		transactionList = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public LocalDate getDateJoined() {
		return dateJoined;
	}

	public double getFeePaid() {
		return feePaid;
	}

	public int getMemberID() {
		return memberID;
	}

	//So we can save to the disk
	public static void save(ObjectOutputStream output) throws IOException {
		output.writeObject(idCounter);
	}

	public static void retrieve(ObjectInputStream input) throws IOException, ClassNotFoundException {
		idCounter = (int) input.readObject();
	}

	public boolean addTransaction(Transaction transaction) {
		return transactionList.add(transaction);
	}

	public Transaction getTransaction(int transactionId) {
		for (Transaction transaction : transactionList) {
			if (transaction.getTransactionID() == transactionId) {
				return transaction;
			}
		}
		return null;
	}

	//list of transactions based on certain dates
	public ArrayList<Transaction> getTransactionList(LocalDate date1, LocalDate date2) {
		ArrayList<Transaction> list = new ArrayList<Transaction>();
		for (Transaction t : transactionList) {
			if (validDate(date1, date2, t.getDate())) {
				list.add(t);
			}
		}
		return list;
	}

	//helper method makes sure that compare date is in between date1 and date2 or equal to one of them
	private boolean validDate(LocalDate date1, LocalDate date2, LocalDate transactionDate) {
		if ((transactionDate.isAfter(date1) || transactionDate.isEqual(date1))
				&& (transactionDate.isBefore(date2) || transactionDate.isEqual(date2))) {
			return true;
		}
		return false;
	}
}
