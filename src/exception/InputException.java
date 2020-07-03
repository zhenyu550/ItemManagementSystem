package exception;

import view.MessageBoxView;

public class InputException extends Exception{
	private String error;
	
	public InputException(String error)
	{
		this.error = error;
	}
	
	public void displayMessage()
	{
		if(error == "Duplicate IC")
			MessageBoxView.infoBox("Input Error: Duplicate IC detected.\nPlease enter a new IC Number.", "Input Error: Duplicate IC");
		if(error == "Duplicate Name")
			MessageBoxView.infoBox("Input Error: Duplicate Name detected.\nPlease enter a new name.", "Input Error: Duplicate Name");
		if(error == "Empty Field")
			MessageBoxView.infoBox("Input Error: Empty Field detected.\nPlease fill in all input fields.", "Input Error: Empty Field");
		if(error == "Invalid IC")
			MessageBoxView.infoBox("Input Error: Invalid IC detected.\nPlease enter a valid IC Number without '-'.","Input Error: Invalid IC");
		if(error == "Invalid Contact No")
			MessageBoxView.infoBox("Input Error: Invalid Contact Number.\nPlease enter a valid Contact Number without '-'.","Input Error: Invalid Contact No");
		if(error == "Over Limit")
		{	
			MessageBoxView.infoBox("Input Error: Over Character Limit.\nOne or more text field(s) exceed its character limit.\n"
					+ "Please try to reduce the character length.", "Input Error: Over Character Limit");
		}
		if(error == "Duplicate Username")
			MessageBoxView.infoBox("Input Error: Duplicate Username detected.\nPlease enter a new username.", "Input Error: Duplicate Name");
		if(error == "Duplicate Code")
			MessageBoxView.infoBox("Input Error: Duplicate Code detected.\nPlease enter a new code.", "Input Error: Duplicate Code");
		if(error == "Invalid Transaction Data")
			MessageBoxView.infoBox("Input Error: Invalid Transaction Data.\nPlease choose a customer or ensure the list is not empty.", 
					"Input Error: Invalid Transaction Data");
		if(error == "Insufficient Payment")
			MessageBoxView.infoBox("Input Error: Insufficient Payment.\nPlease ensure that the payment amount is more or equal to the total price.", 
					"Input Error: Insufficient Payment");
		if(error == "Invalid Item Amount")
			MessageBoxView.infoBox("Input Error: Invalid Item Amount.\nThe stock quantity for that item in the storage is insufficient to support your purchase.", 
					"Input Error: Invalid Item Amount");

	}
}
