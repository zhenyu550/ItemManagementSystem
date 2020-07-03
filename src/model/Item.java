package model;

public class Item {
	private int id;
	private String name;
	private String code;
	private double price;
	private int quantity;
	private String description;
	private int typeId;
	
	//Getters
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public double getPrice() {
		return price;
	}
	public int getQuantity() {
		return quantity;
	}
	public String getDescription() {
		return description;
	}
	public int getTypeId() {
		return typeId;
	}
	
	//Setters
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	
}
