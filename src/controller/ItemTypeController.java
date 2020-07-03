package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBConnection;
import exception.InputException;
import model.ItemType;
import other.CheckString;

public class ItemTypeController {
	//Database Operations
	public ArrayList<ItemType> selectAll(int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<ItemType> list = new ArrayList<ItemType>();
        String sql = "SELECT * FROM ItemType ORDER BY id ASC LIMIT ?,?;";
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, count);
        ps.setInt(2, offset);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	ItemType itype = new ItemType();
        	itype.setId(rs.getInt("id"));
        	itype.setName(rs.getString("name"));
        	itype.setCode(rs.getString("code"));
        	list.add(itype);
        }
        con.close();
		return list;
	}
	
	public ArrayList<ItemType> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<ItemType> list = new ArrayList<ItemType>();
        String sql = "SELECT * FROM ItemType WHERE %s ORDER BY id ASC LIMIT %d,%d;";
        sql = String.format(sql, condition, count, offset);
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	ItemType itype = new ItemType();
        	itype.setId(rs.getInt("id"));
        	itype.setName(rs.getString("name"));
        	itype.setCode(rs.getString("code"));
        	list.add(itype);
        }
        con.close();
		return list;
	}
	
	public int insert(ItemType data) throws ClassNotFoundException, SQLException, InputException //passed
	{
		int success = -1;
		String sql = "INSERT INTO ItemType (name, code) values (?,?);";
		
		CheckString chkStr = new CheckString();
		if(chkStr.isNullOrWhiteSpace(data.getName()) || chkStr.isNullOrWhiteSpace(data.getCode()))
			throw new InputException("Empty Field");
		if(chkStr.isOverLimit(data.getName(), 100) || chkStr.isOverLimit(data.getCode(), 50))
			throw new InputException("Over Limit");
		if(nameExist(data.getName()))
			throw new InputException("Duplicate Name");
		if(codeExist(data.getCode()))
			throw new InputException("Duplicate Code");
        
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName()); 
		ps.setString(2, data.getCode()); 
        
		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int update(ItemType data) throws ClassNotFoundException, SQLException, InputException//passed
	{
		int success = -1;
		String sql = "UPDATE ItemType SET name=?, code=? WHERE id=?;";
		
		if(!nameUnique(data.getName(), data.getId()))
				throw new InputException("Duplicate Name");
		if(!codeUnique(data.getCode(), data.getId()))
			throw new InputException("Duplicate Code");
		
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName());
		ps.setString(2, data.getCode());
		ps.setInt(3, data.getId());

		success = ps.executeUpdate();
		con.close();
		
		return success;
	}
	
	public int delete(ItemType data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "DELETE FROM ItemType WHERE id=?;";
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
		String sql = "SELECT EXISTS(SELECT * FROM ItemType WHERE name=?) AS result;";
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
		String sql = "SELECT EXISTS(SELECT * FROM ItemType WHERE code=?) AS result;";
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
		String sql = "SELECT EXISTS(SELECT id FROM ItemType WHERE name=? AND id<>?) AS result;";
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
		String sql = "SELECT EXISTS(SELECT id FROM ItemType WHERE code=? AND id<>?) AS result;";
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

}
