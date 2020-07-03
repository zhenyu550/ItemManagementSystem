package view;

import javax.swing.JOptionPane;

public class OptionPaneView {
	
	private boolean result = false;
	public OptionPaneView(String operation)
	{
		int response = -1;
		if(operation.equals("Delete Customer"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to delete this customer?\nThis action will delete the selected customer from the database.", "Confirm Delete Customer",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Delete Employee"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to delete this employee?\nThis action will delete the selected employee from the database.", "Confirm Delete Employee",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Delete Item"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to delete this item?\nThis action will delete the selected item from the database.", "Confirm Delete Item",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Delete Item Type"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to delete this item type?\nThis action will delete the selected item type from the database.", "Confirm Delete Item Type",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Remove Item"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to remove this item from the list?", "Confirm Remove Item",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Reset Transaction"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to reset the transaction?\nThis action will remove all items from the list and reset the selected customer", "Confirm Reset Transaction",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(operation.equals("Delete Transaction"))
			response = JOptionPane.showConfirmDialog(null, "Do you want to delete this transaction?\nThis action will delete the selected transaction from the database.", "Confirm Delete Transaction",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if (response == JOptionPane.YES_OPTION)
			result = true;
    }
	
	public boolean getResult()
	{
		return result;
	}
}
