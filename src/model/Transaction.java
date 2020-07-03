package model;

import java.time.LocalDateTime;

public class Transaction {
	private int id;
	private String code;
	private int employeeId;
	private int customerId;
	private double totalPrice;
	private LocalDateTime dateTime;
	
	//Getter
	public int getId() {
		return id;
	}
	public String getCode() {
		return code;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public int getCustomerId() {
		return customerId;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	//Setters
	public void setId(int id) {
		this.id = id;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public void setDateTime(LocalDateTime localDateTime) {
		this.dateTime = localDateTime;
	}
}
