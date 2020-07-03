package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.DBConnection;
import exception.InputException;
import model.Customer;
import other.CheckString;

public class CustomerController {
		
	//Database Operations
	public ArrayList<Customer> selectAll(int count, int offset) throws ClassNotFoundException, SQLException //passed
	{
		ArrayList<Customer> list = new ArrayList<Customer>();
        String sql = "SELECT * FROM Customer ORDER BY id ASC LIMIT ?,?;";
        
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, count);
        ps.setInt(2, offset);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	Customer cus = new Customer();
        	cus.setId(rs.getInt("id"));
        	cus.setName(rs.getString("name"));
        	cus.setIc(rs.getString("ic"));
        	cus.setContactNo(rs.getString("contact_no"));
        	cus.setEmail(rs.getString("email"));
        	list.add(cus);
        }
        con.close();
		return list;
	}
	
	public ArrayList<Customer> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException
	{
		ArrayList<Customer> list = new ArrayList<Customer>();
		String sql = "SELECT * FROM Customer WHERE %s ORDER BY id ASC LIMIT %d,%d;";
		sql = String.format(sql, condition, count, offset);
        Connection con = DBConnection.doConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        while(rs.next())
        {
        	Customer cus = new Customer();
        	cus.setId(rs.getInt("id"));
        	cus.setName(rs.getString("name"));
        	cus.setIc(rs.getString("ic"));
        	cus.setContactNo(rs.getString("contact_no"));
        	cus.setEmail(rs.getString("email"));
        	list.add(cus);
        }
        con.close();
		return list;
	}
	
	public int insert(Customer data) throws ClassNotFoundException, SQLException, InputException //passed
	{
		int success = -1;
		String sql = "INSERT INTO Customer (name, ic, contact_no, email) values (?,?,?,?);";
		String name = data.getName();
		String ic = data.getIc();
		String contactNo = data.getContactNo();
		String email = data.getEmail();
		
		CheckString chkStr = new CheckString();	
		if(chkStr.isNullOrWhiteSpace(name) || chkStr.isNullOrWhiteSpace(ic) || chkStr.isNullOrWhiteSpace(email) || chkStr.isNullOrWhiteSpace(contactNo))
			throw new InputException("Empty Field");
		if(chkStr.isOverLimit(name, 100) || chkStr.isOverLimit(ic, 12) || chkStr.isOverLimit(contactNo, 20) || chkStr.isOverLimit(email, 100))
			throw new InputException("Over Limit");
		if(!chkStr.isValidIc(ic))
			throw new InputException("Invalid IC");
		if(!chkStr.isValidContactNo(contactNo))
			throw new InputException("Invalid Contact No");
		if(nameExist(name))
			throw new InputException("Duplicate Name");
		if(icExist(ic))
			throw new InputException("Duplicate IC");

		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName()); 
		ps.setString(2, data.getIc()); 
		ps.setString(3, data.getContactNo()); 
		ps.setString(4, data.getEmail()); 
        
		success = ps.executeUpdate();
		con.close();
		return success;
	}
	
	public int update(Customer data) throws ClassNotFoundException, SQLException, InputException //passed
	{
		int success = -1;
		String sql = "UPDATE Customer SET name=?, ic=?, contact_no=?, email=? WHERE id=?;";
		
		int id = data.getId();
		String name = data.getName(), ic = data.getIc(), contactNo = data.getContactNo(), email = data.getEmail();
		
		CheckString chkStr = new CheckString();	
		if(chkStr.isNullOrWhiteSpace(name) || chkStr.isNullOrWhiteSpace(ic) || chkStr.isNullOrWhiteSpace(email) || chkStr.isNullOrWhiteSpace(contactNo))
			throw new InputException("Empty Field");
		if(chkStr.isOverLimit(name, 100) || chkStr.isOverLimit(ic, 12) || chkStr.isOverLimit(contactNo, 20) || chkStr.isOverLimit(email, 100))
			throw new InputException("Over Limit");
		if(!chkStr.isValidIc(ic))
			throw new InputException("Invalid IC");
		if(!chkStr.isValidContactNo(contactNo))
			throw new InputException("Invalid Contact No");
		if(!nameUnique(name, id))
			throw new InputException("Duplicate Name");
		if(!icUnique(ic, id))
			throw new InputException("Duplicate IC");

		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, data.getName());
		ps.setString(2, data.getIc());
		ps.setString(3, data.getContactNo());
		ps.setString(4, data.getEmail());
		ps.setInt(5, data.getId());

		success = ps.executeUpdate();
		con.close();	
		return success;
	}
	
	public int delete(Customer data) throws ClassNotFoundException, SQLException //passed
	{
		int success = -1;
		String sql = "DELETE FROM Customer WHERE id=?;";
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
		String sql = "SELECT EXISTS(SELECT * FROM customer WHERE name=?) AS result;";
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
	
	public boolean icExist(String ic) throws ClassNotFoundException, SQLException //passed
	{
		boolean found = false;
		String sql = "SELECT EXISTS(SELECT * FROM customer WHERE ic=?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, ic);

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
		String sql = "SELECT EXISTS(SELECT id FROM customer WHERE name=? AND id<>?) AS result;";
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
	
	public boolean icUnique(String ic, int id) throws ClassNotFoundException, SQLException //passed
	{
		boolean unique = true;
		String sql = "SELECT EXISTS(SELECT id FROM customer WHERE ic=? AND id<>?) AS result;";
		Connection con = DBConnection.doConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.setString(1, ic);
		ps.setInt(2, id);
		
		ResultSet rs = ps.executeQuery();
		rs.next();
		if(rs.getInt(1)==1)
			unique = false;
		
		con.close();
		return unique;
	}
	
	//for testing purposes
	public static void main(String[] args) {
		CustomerController cusController = new CustomerController();
		try {
			//test add customer
			Customer data1 = new Customer();
			data1.setId(-1);
			data1.setName("Test Data 10");
			data1.setIc("123456");
			data1.setContactNo("1234abc");
			data1.setEmail("testemail@email.com");
			//int number = cusController.insert(data1);
			System.out.println("working");
			
			//test view all customer
			for(Customer cus : cusController.selectWhere("id LIKE '7%'", 0,100))
			{
				System.out.println("ID: "+cus.getId());
				System.out.println("Name: "+cus.getName());
				System.out.println("IC: "+cus.getIc());
				System.out.println("Contact No:"+cus.getContactNo());
				System.out.println("Email: "+cus.getEmail()+"\n");
			}
			System.out.println("missing");
			
			
		} //catch (InputException e) {
			//e.displayMessage();
		//}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
