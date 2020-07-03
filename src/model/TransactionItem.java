package model;

public class TransactionItem {
	private int itemId;
	private int transactionId;
	private int amount;
	
	//Getters
	public int getItemId() {
		return itemId;
	}
	public int getTransactionId() {
		return transactionId;
	}
	public int getAmount() {
		return amount;
	}
	
	//Setters
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
}
