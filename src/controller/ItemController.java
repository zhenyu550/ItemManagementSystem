package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import database.DBConnection;
import exception.InputException;
import model.Item;
import other.CheckString;

public class ItemController {
	public ArrayList<Item> selectAll(int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<Item> list = new ArrayList<Item>();
        String sql = "SELECT * FROM Item ORDER BY id ASC LIMIT ?,?;";
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, count);
        ps.setInt(2, offset);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {        	
        	Item item = new Item();
        	item.setId(rs.getInt("id"));
        	item.setName(rs.getString("name"));
        	item.setCode(rs.getString("code"));
        	item.setPrice(rs.getDouble("price"));
        	item.setQuantity(rs.getInt("quantity"));
        	item.setDescription(rs.getString("description"));
        	item.setTypeId(rs.getInt("type_id"));
        	list.add(item);
        }
        con.close();
		return list;
	}
	
	public ArrayList<Item> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<Item> list = new ArrayList<Item>();
        String sql = "SELECT * FROM Item WHERE %s ORDER BY id ASC LIMIT %d,%d;";
		sql = String.format(sql, condition, count, offset);

        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	Item item = new Item();
        	item.setId(rs.getInt("id"));
        	item.setName(rs.getString("name"));
        	item.setCode(rs.getString("code"));
        	item.setPrice(rs.getDouble("price"));
        	item.setQuantity(rs.getInt("quantity"));
        	item.setDescription(rs.getString("description"));
        	item.setTypeId(rs.getInt("type_id"));
        	list.add(item);
        }
        con.close();
		return list;
	}
	
	public int insert(Item data) throws ClassNotFoundException, SQLException, InputException //passed
	{
		int success = -1;
		String sql = "INSERT INTO Item (name, code, price, quantity, description, type_id) values (?,?,?,?,?,?);";
		
		CheckString chkStr = new CheckString();
		
		if(data.getTypeId() == -1 || chkStr.isNullOrWhiteSpace(data.getName()) || chkStr.isNullOrWhiteSpace(data.getCode()))
			throw new InputException("Empty Field");
		if(chkStr.isOverLimit(data.getName(),100) || chkStr.isOverLimit(data.getCode(),50) || chkStr.isOverLimit(data.getDescription(),1000))
			throw new InputException("Over Limit");
		if(nameExist(data.getName()))
			throw new InputException("Duplicate Name");
		if(codeExist(data.getCode()))
			throw new InputException("Duplicate Code");
	
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName()); 
		ps.setString(2, data.getCode()); 
		ps.setDouble(3, data.getPrice()); 
		ps.setInt(4, data.getQuantity()); 
		ps.setString(5, data.getDescription()); 
		ps.setInt(6, data.getTypeId()); 
        
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int update(Item data) throws ClassNotFoundException, SQLException, InputException
	{
		int success = -1;
		String sql = "UPDATE Item SET name=?, code=?, price=?, quantity=?, description=?, type_id=? WHERE id=?;";
		
		CheckString chkStr = new CheckString();
		if(data.getTypeId() == -1 || chkStr.isNullOrWhiteSpace(data.getName()) || chkStr.isNullOrWhiteSpace(data.getCode()))
			throw new InputException("Empty Field");
		if(chkStr.isOverLimit(data.getName(),100) || chkStr.isOverLimit(data.getCode(),50) || chkStr.isOverLimit(data.getDescription(),1000))
			throw new InputException("Over Limit");
		if(!nameUnique(data.getName(), data.getId()))
			throw new InputException("Duplicate Name");
		if(!codeUnique(data.getCode(), data.getId()))
			throw new InputException("Duplicate Code");
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName()); 
		ps.setString(2, data.getCode()); 
		ps.setDouble(3, data.getPrice()); 
		ps.setInt(4, data.getQuantity()); 
		ps.setString(5, data.getDescription()); 
		ps.setInt(6, data.getTypeId()); 
		ps.setInt(7, data.getId());

		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int delete(Item data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "DELETE FROM Item WHERE id=?;";
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, data.getId());
        
        success = ps.executeUpdate();
        con.close();
		return success;
	}
	
	public boolean nameExist(String name) throws ClassNotFoundException, SQLException //passed
	{
		boolean found = false;
		String sql = "SELECT EXISTS(SELECT * FROM Item WHERE name=?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, name);

		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)==1)
			found = true;
		con.close();
		return found;
	}
	
	public boolean codeExist(String code) throws ClassNotFoundException, SQLException //passed
	{
		boolean found = false;
		String sql = "SELECT EXISTS(SELECT * FROM Item WHERE code=?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, code);

		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)==1)
			found = true;
		
		con.close();
		return found;
	}
	
	public boolean nameUnique(String name, int id) throws ClassNotFoundException, SQLException //passed
	{
		boolean unique = true;
		String sql = "SELECT EXISTS(SELECT id FROM Item WHERE name=? AND id<>?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, name);
		ps.setInt(2, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)==1)
			unique = false;
		
		con.close();
		return unique;
	}
	
	public boolean codeUnique(String code, int id) throws ClassNotFoundException, SQLException //passed
	{
		boolean unique = true;
		String sql = "SELECT EXISTS(SELECT id FROM Item WHERE code=? AND id<>?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, code);
		ps.setInt(2, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)==1)
			unique = false;
		
		con.close();
		return unique;
	}
	
	public boolean checkItemQuantity(Item item, int purchaseAmount) throws InputException
	{
		int newQuantity = item.getQuantity() - purchaseAmount;
		if(newQuantity < 0)
			throw new InputException("Invalid Item Amount");
		
		return true;
	}


}
