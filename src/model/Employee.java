package model;

public class Employee {
	private int id;
	private String name;
	private String ic;
	private String contactNo;
	private String email;
	private String address;
	private String username;
	private String password;
	private String level;
	
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
	public String getAddress() {
		return address;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getLevel() {
		return level;
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
	public void setAddress(String address) {
		this.address = address;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
