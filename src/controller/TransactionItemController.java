package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBConnection;
import model.TransactionItem;

public class TransactionItemController {
	public ArrayList<TransactionItem> selectAll(int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<TransactionItem> list = new ArrayList<TransactionItem>();
        String sql = "SELECT * FROM TransactionItem ORDER BY transaction_id ASC LIMIT ?,?;";
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, count);
        ps.setInt(2, offset);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	TransactionItem tItem = new TransactionItem();
        	tItem.setTransactionId(rs.getInt("transaction_id"));
        	tItem.setItemId(rs.getInt("item_id"));
        	tItem.setAmount(rs.getInt("amount"));
        	list.add(tItem);
        }
        con.close();
		return list;
	}
	
	public ArrayList<TransactionItem> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<TransactionItem> list = new ArrayList<TransactionItem>();
        String sql = "SELECT * FROM TransactionItem WHERE %s ORDER BY item_id ASC LIMIT %d,%d;";
        sql = String.format(sql, condition, count, offset);
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	TransactionItem tItem = new TransactionItem();
        	tItem.setTransactionId(rs.getInt("transaction_id"));
        	tItem.setItemId(rs.getInt("item_id"));
        	tItem.setAmount(rs.getInt("amount"));
        	list.add(tItem);
        }
        con.close();
		return list;
	}
	
	public int insert(TransactionItem data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "INSERT INTO TransactionItem (transaction_id, item_id, amount) values (?,?,?);";
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, data.getTransactionId()); 
		ps.setInt(2, data.getItemId()); 
		ps.setInt(3, data.getAmount()); 
        
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int update(TransactionItem data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "UPDATE TransactionItem SET amount=? WHERE transaction_id=? AND item_id=?;";
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setInt(1, data.getAmount()); 
		ps.setInt(2, data.getTransactionId()); 
		ps.setInt(3, data.getItemId()); 
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int delete(TransactionItem data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "DELETE FROM TransactionItem WHERE transaction_id=?;";
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, data.getTransactionId());
        
        success = ps.executeUpdate();
        con.close();
		return success;
	}
}
