package model;

public class Customer {
	private int id;
	private String name;
	private String ic;
	private String contactNo;
	private String email;
	
	//Getters
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getIc() {
		return ic;
	}
	public String getContactNo() {
		return contactNo;
	}
	public String getEmail() {
		return email;
	}
	
	//Setters
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIc(String ic) {
		this.ic = ic;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
