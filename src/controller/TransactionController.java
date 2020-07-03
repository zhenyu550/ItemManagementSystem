package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import database.DBConnection;
import exception.InputException;
import model.Customer;
import model.Employee;
import model.Item;
import model.Transaction;
import model.TransactionItem;

public class TransactionController {
	public ArrayList<Transaction> selectAll(int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<Transaction> list = new ArrayList<Transaction>();
        String sql = "SELECT * FROM Transaction ORDER BY id ASC LIMIT ?,?;";
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, count);
        ps.setInt(2, offset);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	Transaction trans = new Transaction();
        	trans.setId(rs.getInt("id"));
        	trans.setCode(rs.getString("code"));
        	trans.setEmployeeId(rs.getInt("employee_id"));
        	trans.setCustomerId(rs.getInt("customer_id"));
        	trans.setTotalPrice(rs.getDouble("total_price"));
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        	trans.setDateTime(LocalDateTime.parse(rs.getString("date_time"), formatter));
        	list.add(trans);
        }
        con.close();
		return list;
	}

	public ArrayList<Transaction> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<Transaction> list = new ArrayList<Transaction>();
        String sql = "SELECT * FROM Transaction WHERE %s ORDER BY id ASC LIMIT %d,%d;";
        sql = String.format(sql, condition, count, offset);
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	Transaction trans = new Transaction();
        	trans.setId(rs.getInt("id"));
        	trans.setCode(rs.getString("code"));
        	trans.setEmployeeId(rs.getInt("employee_id"));
        	trans.setCustomerId(rs.getInt("customer_id"));
        	trans.setTotalPrice(rs.getDouble("total_price"));
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        	trans.setDateTime(LocalDateTime.parse(rs.getString("date_time"), formatter));
        	list.add(trans);
        }
        con.close();
		return list;
	}
	
	public int insert(Transaction data) throws ClassNotFoundException, SQLException
	{
		int success = -1;
		String sql = "INSERT INTO Transaction (code, employee_id, customer_id, total_price, date_time) values (?,?,?,?,?);";
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getCode()); 
		ps.setInt(2, data.getEmployeeId());
		ps.setInt(3, data.getCustomerId());
		ps.setDouble(4, data.getTotalPrice());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ps.setString(5, data.getDateTime().format(formatter));
        
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int update(Transaction data) throws ClassNotFoundException, SQLException
	{
		int success = -1;
		String sql = "UPDATE Transaction SET code=?, employee_id=?, customer_id=?, total_price=?, date_time=? WHERE id=?;";
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getCode()); 
		ps.setInt(2, data.getEmployeeId());
		ps.setInt(3, data.getCustomerId());
		ps.setDouble(4, data.getTotalPrice());
		ps.setInt(6, data.getId());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ps.setString(5, data.getDateTime().format(formatter));
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int delete(Transaction data) throws ClassNotFoundException, SQLException
	{
		int success = -1;
		String sql = "DELETE FROM Transaction WHERE id=?;";
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, data.getId());
        
        success = ps.executeUpdate();
        con.close();
		return success;
	}
	
	//Other methods
	public boolean validateTransaction(Customer cus, ArrayList<Item> items, ArrayList<Integer> amounts) throws InputException
	{
		boolean validated = false;
		if(cus.getId() == 0 || items.isEmpty() || amounts.isEmpty())
			throw new InputException("Invalid Transaction Data");
		else
			validated = true;

		return validated;
	}

	public double calculateTotal(ArrayList<Item> itemList, ArrayList<Integer> amountList) 
	{
		double totalPrice = 0;
		for(int i=0; i < itemList.size(); i++)
		{
			totalPrice += itemList.get(i).getPrice() * amountList.get(i);
		}
		return totalPrice;
	}

	public double calculateChange(double total, double payment) throws InputException
	{
		double change = payment - total;
		if(change < 0)
			throw new InputException("Insufficient Payment");
		else
			return change;
	}
		
	public boolean addTransaction(Customer cus, Employee emp, ArrayList<Item> items, ArrayList<Integer> amounts, double price) throws InputException 
	{
		TransactionItemController tItemController = new TransactionItemController();
		Transaction data = new Transaction();
		LocalDateTime currentDateTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssS");
		String code = currentDateTime.format(formatter);
		
		data.setCustomerId(cus.getId());
		data.setEmployeeId(emp.getId());
		data.setCode(code);
		data.setTotalPrice(price);
		data.setDateTime(currentDateTime); 
		
		try {
			insert(data);
			int transactionId = selectWhere(String.format("code = '%s'", data.getCode()),0,1).get(0).getId();
			for(int i=0; i<items.size(); i++)
			{
				TransactionItem tItemData = new TransactionItem();
				tItemData.setTransactionId(transactionId);
				tItemData.setItemId(items.get(i).getId());
				tItemData.setAmount(amounts.get(i));
				tItemController.insert(tItemData);			
				
				Item item = items.get(i);
				int newQuantity = item.getQuantity() - amounts.get(i);
				item.setQuantity(newQuantity);
				ItemController itemController = new ItemController();
				itemController.update(item);
			}
			return true;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
	
	public LocalDateTime getFirstDate()
	{
		LocalDateTime firstDate = null;
		try {
			firstDate = selectAll(0,1).get(0).getDateTime();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return firstDate;
	}
	public static void main(String[] args)
	{
		try {
			TransactionController controller = new TransactionController();
			Transaction data = controller.selectWhere("id = 1", 0, 1).get(0);
			System.out.println(data.getId());
			System.out.println(data.getCustomerId());
			System.out.println(data.getEmployeeId());
			System.out.println(data.getCode());
			System.out.println(data.getTotalPrice());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			System.out.println(data.getDateTime().format(formatter));

		}catch(ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
