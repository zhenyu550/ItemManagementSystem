package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 

import database.DBConnection;
import exception.InputException;
import model.Employee;
import other.CheckString;

public class EmployeeController {
	//Database Operations
		public ArrayList<Employee> selectAll(int count, int offset) throws ClassNotFoundException, SQLException //passed
		{
			ArrayList<Employee> list = new ArrayList<Employee>();
	        String sql = "SELECT * FROM Employee ORDER BY id ASC LIMIT ?,?;";
	        
	        Connection con = DBConnection.doConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, count);
	        ps.setInt(2, offset);
	        
	        ResultSet rs = ps.executeQuery();
	        while(rs.next())
	        {
	        	Employee emp = new Employee();
	        	emp.setId(rs.getInt("id"));
	        	emp.setName(rs.getString("name"));
	        	emp.setIc(rs.getString("ic"));
	        	emp.setContactNo(rs.getString("contact_no"));
	        	emp.setEmail(rs.getString("email"));
	        	emp.setAddress(rs.getString("address"));
	        	emp.setUsername(rs.getString("username"));
	        	emp.setPassword(rs.getString("password"));
	        	emp.setLevel(rs.getString("level"));
	        	list.add(emp);
	        }
	        con.close();
			return list;
		}
		
		public ArrayList<Employee> selectWhere(String condition, int count, int offset) throws ClassNotFoundException, SQLException //passed
		{
			ArrayList<Employee> list = new ArrayList<Employee>();
			String sql = "SELECT * FROM Employee WHERE %s ORDER BY id ASC LIMIT %d,%d;";
			sql = String.format(sql, condition, count, offset);

	        Connection con = DBConnection.doConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        
	        ResultSet rs = ps.executeQuery();
	        while(rs.next())
	        {
	        	Employee emp = new Employee();
	        	emp.setId(rs.getInt("id"));
	        	emp.setName(rs.getString("name"));
	        	emp.setIc(rs.getString("ic"));
	        	emp.setContactNo(rs.getString("contact_no"));
	        	emp.setEmail(rs.getString("email"));
	        	emp.setAddress(rs.getString("address"));
	        	emp.setUsername(rs.getString("username"));
	        	emp.setPassword(rs.getString("password"));
	        	emp.setLevel(rs.getString("level"));
	        	list.add(emp);
	        }
	        con.close();
			return list;
		}
		
		public int insert(Employee data) throws ClassNotFoundException, SQLException, InputException //passed
		{
			int success = -1;
			String sql = "INSERT INTO Employee (name, ic, contact_no, email, username, password, level, address) values (?,?,?,?,?,?,?,?);";
			//hash the password before inserting
			
			String name = data.getName();
			String ic = data.getIc();
			String contactNo = data.getContactNo();
			String email = data.getEmail();
			String address = data.getAddress();
			String username = data.getUsername();
			String password = data.getPassword();
			
			CheckString chkStr = new CheckString();	
			if(chkStr.isNullOrWhiteSpace(name) || chkStr.isNullOrWhiteSpace(ic) || chkStr.isNullOrWhiteSpace(email) || chkStr.isNullOrWhiteSpace(contactNo) ||
					chkStr.isNullOrWhiteSpace(address) || chkStr.isNullOrWhiteSpace(username) || chkStr.isNullOrEmpty(password))
				throw new InputException("Empty Field");
			if(chkStr.isOverLimit(name, 100) || chkStr.isOverLimit(ic, 12) || chkStr.isOverLimit(contactNo, 20) || chkStr.isOverLimit(email, 100) ||
					chkStr.isOverLimit(username, 50) || chkStr.isOverLimit(address, 200))
				throw new InputException("Over Limit");
			if(!chkStr.isValidIc(ic))
				throw new InputException("Invalid IC");
			if(!chkStr.isValidContactNo(contactNo))
				throw new InputException("Invalid Contact No");
			if(nameExist(name))
				throw new InputException("Duplicate Name");
			if(icExist(ic))
				throw new InputException("Duplicate IC");
			if(usernameExist(username))
				throw new InputException("Duplicate Username");
			
			data.setPassword(getSHA(data.getPassword()));
			
			Connection con = DBConnection.doConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, data.getName()); 
			ps.setString(2, data.getIc()); 
			ps.setString(3, data.getContactNo()); 
			ps.setString(4, data.getEmail()); 
			ps.setString(5, data.getUsername());
			ps.setString(6, data.getPassword());
			ps.setString(7, data.getLevel());
			ps.setString(8, data.getAddress());
	        
			success = ps.executeUpdate();
			con.close();
		
			return success;
		}
		
		public int update(Employee data) throws ClassNotFoundException, SQLException, InputException
		{
			int success = -1;
			CheckString chkStr = new CheckString();	
			int id = data.getId();
			
			if(!chkStr.isNullOrWhiteSpace(data.getPassword()))
				data.setPassword(getSHA(data.getPassword()));
			else
				data.setPassword(getOldPassword(id));
				
			String sql = "UPDATE Employee SET name=?, ic=?, contact_no=?, email=?, username=?, password=?, level=?, address=? WHERE id=?;";
			
			String name = data.getName();
			String ic = data.getIc();
			String contactNo = data.getContactNo();
			String email = data.getEmail();
			String address = data.getAddress();
			String username = data.getUsername();


			if(chkStr.isNullOrWhiteSpace(name) || chkStr.isNullOrWhiteSpace(ic) || chkStr.isNullOrWhiteSpace(email) || chkStr.isNullOrWhiteSpace(contactNo) ||
					chkStr.isNullOrWhiteSpace(address) || chkStr.isNullOrWhiteSpace(username))
				throw new InputException("Empty Field");
			if(chkStr.isOverLimit(name, 100) || chkStr.isOverLimit(ic, 12) || chkStr.isOverLimit(contactNo, 20) || chkStr.isOverLimit(email, 100) ||
					chkStr.isOverLimit(username, 50) || chkStr.isOverLimit(address, 200))
				throw new InputException("Over Limit");
			if(!chkStr.isValidIc(ic))
				throw new InputException("Invalid IC");
			if(!chkStr.isValidContactNo(contactNo))
				throw new InputException("Invalid Contact No");
			if(!nameUnique(name, id))
				throw new InputException("Duplicate Name");
			if(!icUnique(ic, id))
				throw new InputException("Duplicate IC");
			if(!usernameUnique(username, id))
				throw new InputException("Duplicate Username");
			
			Connection con = DBConnection.doConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, data.getName());
			ps.setString(2, data.getIc());
			ps.setString(3, data.getContactNo());
			ps.setString(4, data.getEmail());
			ps.setString(5, data.getUsername());
			ps.setString(6, data.getPassword());
			ps.setString(7, data.getLevel());
			ps.setString(8, data.getAddress());
			ps.setInt(9, data.getId());

			success = ps.executeUpdate();
			con.close();
	
			return success;
		}
		
		public int delete(Employee data) throws ClassNotFoundException, SQLException
		{
			int success = -1;
			String sql = "DELETE FROM Employee WHERE id=?;";
	        Connection con = DBConnection.doConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, data.getId());
	        
	        success = ps.executeUpdate();
	        con.close();
			return success;
		}
		
		public boolean nameExist(String name) throws ClassNotFoundException, SQLException
		{
			boolean found = false;
			String sql = "SELECT EXISTS(SELECT * FROM Employee WHERE name=?) AS result;";
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
		
		public boolean icExist(String ic) throws ClassNotFoundException, SQLException
		{
			boolean found = false;
			String sql = "SELECT EXISTS(SELECT * FROM Employee WHERE ic=?) AS result;";
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
		
		public boolean usernameExist(String username) throws ClassNotFoundException, SQLException
		{
			boolean found = false;
			String sql = "SELECT EXISTS(SELECT * FROM Employee WHERE username=?) AS result;";
			Connection con = DBConnection.doConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);

			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==1)
				found = true;
			con.close();
			return found;
		}

		public boolean nameUnique(String name, int id) throws ClassNotFoundException, SQLException
		{
			boolean unique = true;
			String sql = "SELECT EXISTS(SELECT id FROM Employee WHERE name=? AND id<>?) AS result;";
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
		
		public boolean icUnique(String ic, int id) throws ClassNotFoundException, SQLException
		{
			boolean unique = true;
			String sql = "SELECT EXISTS(SELECT id FROM Employee WHERE ic=? AND id<>?) AS result;";
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
		
		public boolean usernameUnique(String username, int id) throws ClassNotFoundException, SQLException
		{
			boolean unique = true;
			String sql = "SELECT EXISTS(SELECT id FROM Employee WHERE username=? AND id<>?) AS result;";
			Connection con = DBConnection.doConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setInt(2, id);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==1)
				unique = false;
			
			con.close();
			return unique;
		}
		
		public static String getSHA(String input)
		{
	        try { 
	        	  
	            // Static getInstance method is called with hashing SHA 
	            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
	  
	            // digest() method called 
	            // to calculate message digest of an input 
	            // and return array of byte 
	            byte[] messageDigest = md.digest(input.getBytes()); 
	  
	            // Convert byte array into signum representation 
	            BigInteger no = new BigInteger(1, messageDigest); 
	  
	            // Convert message digest into hex value 
	            String hashtext = no.toString(16); 
	  
	            while (hashtext.length() < 32) { 
	                hashtext = "0" + hashtext; 
	            } 
	  
	            return hashtext; 
	        } 
	  
	        // For specifying wrong message digest algorithms 
	        catch (NoSuchAlgorithmException e) { 
	            System.out.println("Exception thrown"
	                               + " for incorrect algorithm: " + e); 
	  
	            return null; 
	        } 
		}
		
		public boolean validateLogin(String username, String password) throws ClassNotFoundException, SQLException
		{
			boolean validate = false;
			String hashedPassword = getSHA(password);
			
			String sql = "SELECT EXISTS(SELECT id FROM Employee WHERE username=? AND password=?) AS result;";
			Connection con = DBConnection.doConnection();
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, hashedPassword);
			
			ResultSet rs = ps.executeQuery();
			rs.next();
			if(rs.getInt(1)==1)
				validate = true;
			
			con.close();			
			return validate;
		}
		
		private String getOldPassword(int id) throws ClassNotFoundException, SQLException
		{
			String hashedPassword = "";
			String sql = "SELECT password FROM Employee WHERE id=?;";
			
	        Connection con = DBConnection.doConnection();
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, id);
	        
	        ResultSet rs = ps.executeQuery();
	        while(rs.next())
	        {
	        	 hashedPassword = rs.getString("password");
	        }
	        con.close();
			return hashedPassword;
		}
		
		//for testing purposes
		public static void main(String[] args) {
			EmployeeController empController = new EmployeeController();
			try {
				//test add customer
				Employee data1 = new Employee();
				data1.setId(3);
				data1.setName("Test Data 2");
				data1.setIc("998877665543");
				data1.setContactNo("00001111222");
				data1.setEmail("testemail@email.com");
				data1.setAddress("Jalan Test, Taman Test, 00000 Melaka");
				data1.setUsername("test123");
				data1.setPassword("123");
				data1.setLevel("2");
				int number = empController.update(data1);
				System.out.println(number);
				
				//test view all customer
				for(Employee emp : empController.selectAll(0,100))
				{
					System.out.println("ID: "+emp.getId());
					System.out.println("Name: "+emp.getName());
					System.out.println("IC: "+emp.getIc());
					System.out.println("Contact No: "+emp.getContactNo());
					System.out.println("Email: "+emp.getEmail());
					System.out.println("Username: "+emp.getUsername());
					System.out.println("Password: "+emp.getPassword());
					System.out.println("Level: "+emp.getLevel());
					System.out.println("Address: "+emp.getAddress());
					System.out.println("");
				}
				
				System.out.println(empController.validateLogin("",""));
				
			} catch (InputException e) {
				e.displayMessage();
			}
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
