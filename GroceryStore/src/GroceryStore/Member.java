package GroceryStore;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;

public class Member {
	private static int idCounter = 1;
	private int memberID;
	private String name;
	private String address;
	private String phoneNumber;
	private Date dateJoined;
	private double feePaid;

	public Member(String name, String address, String phoneNumber, Date dateJoined, double feePaid) {
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.dateJoined = dateJoined;
		this.feePaid = feePaid;
		this.memberID = idCounter++;
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

	public Date getDateJoined() {
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
}
