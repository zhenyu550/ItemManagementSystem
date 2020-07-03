package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.*;
import model.*;
import exception.InputException;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

public class MainPanelView extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearch;
	private JTable tableCustomer;
	private JScrollPane scollPaneCustomer;
	private JComboBox comboBoxSearchCond;
	private JComboBox comboBoxStrCond;
	private ArrayList<Customer> cusList = new ArrayList<Customer>();
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Integer> amountList = new ArrayList<Integer>();
	private ArrayList<TransactionItem> transactionItemList = new ArrayList<TransactionItem>();
	private JTable tableTransactionItem;
	private Customer selectedCustomer = new Customer();
	private Employee loginEmployee = new Employee();
	private JLabel lblSelectedCustomer;
	private JLabel lblTotalPriceDisplay;
	private double totalPrice;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainPanelView frame = new MainPanelView(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainPanelView(Employee loginEmployee) {
		initComponents();
		reloadCustomerTable("");
		this.loginEmployee = loginEmployee;
	}
	
	public void initComponents()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPane.add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblMainPanel = new JLabel("Main Panel");
		lblMainPanel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnLogOut = new JButton("Log \r\nOut");
		btnLogOut.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnLogOut.addActionListener(new LogOutButtonListener());
		
		JButton btnAdminPanel = new JButton("Admin Panel");
		btnAdminPanel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAdminPanel.addActionListener(new AdminPanelButtonListener());
		
		GroupLayout gl_panelNorth = new GroupLayout(panelNorth);
		gl_panelNorth.setHorizontalGroup(
			gl_panelNorth.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelNorth.createSequentialGroup()
					.addContainerGap(304, Short.MAX_VALUE)
					.addComponent(lblMainPanel)
					.addGap(118)
					.addComponent(btnAdminPanel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnLogOut, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelNorth.setVerticalGroup(
			gl_panelNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNorth.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelNorth.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelNorth.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnLogOut)
							.addComponent(btnAdminPanel))
						.addComponent(lblMainPanel))
					.addContainerGap())
		);
		panelNorth.setLayout(gl_panelNorth);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelTransaction = new JPanel();
		tabbedPane.addTab("Transaction", null, panelTransaction, null);
		panelTransaction.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTransactionNorth = new JPanel();
		panelTransaction.add(panelTransactionNorth, BorderLayout.NORTH);
		
		lblSelectedCustomer = new JLabel("Customer: ");
		lblSelectedCustomer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnSelectCustomer = new JButton("Select Customer");
		btnSelectCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSelectCustomer.addActionListener(new SelectCustomerButtonListener());
		
		GroupLayout gl_panelTransactionNorth = new GroupLayout(panelTransactionNorth);
		gl_panelTransactionNorth.setHorizontalGroup(
			gl_panelTransactionNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelTransactionNorth.createSequentialGroup()
					.addComponent(lblSelectedCustomer, GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSelectCustomer))
		);
		gl_panelTransactionNorth.setVerticalGroup(
			gl_panelTransactionNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTransactionNorth.createSequentialGroup()
					.addGroup(gl_panelTransactionNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSelectedCustomer)
						.addComponent(btnSelectCustomer))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelTransactionNorth.setLayout(gl_panelTransactionNorth);
		
		JPanel panelTransactionWest = new JPanel();
		panelTransaction.add(panelTransactionWest, BorderLayout.WEST);
		
		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddItem.addActionListener(new AddItemButtonListener());
		
		JButton btnEditAmount = new JButton("Edit Amount");
		btnEditAmount.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEditAmount.addActionListener(new EditItemButtonListener());
		
		JButton btnRemoveItem = new JButton("Remove Item");
		btnRemoveItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRemoveItem.addActionListener(new RemoveItemButtonListener());
		
		GroupLayout gl_panelTransactionWest = new GroupLayout(panelTransactionWest);
		gl_panelTransactionWest.setHorizontalGroup(
			gl_panelTransactionWest.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTransactionWest.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelTransactionWest.createParallelGroup(Alignment.LEADING)
						.addComponent(btnRemoveItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnAddItem, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
						.addComponent(btnEditAmount, GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelTransactionWest.setVerticalGroup(
			gl_panelTransactionWest.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTransactionWest.createSequentialGroup()
					.addComponent(btnAddItem, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEditAmount, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnRemoveItem, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(235, Short.MAX_VALUE))
		);
		panelTransactionWest.setLayout(gl_panelTransactionWest);
		
		JPanel panelTransactionSouth = new JPanel();
		panelTransaction.add(panelTransactionSouth, BorderLayout.SOUTH);
		
		JButton btnResetList = new JButton("Reset");
		btnResetList.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnResetList.addActionListener(new ResetListButtonListener());
		
		JButton btnPayment = new JButton("Payment");
		btnPayment.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnPayment.addActionListener(new PaymentButtonListener());
		
		GroupLayout gl_panelTransactionSouth = new GroupLayout(panelTransactionSouth);
		gl_panelTransactionSouth.setHorizontalGroup(
			gl_panelTransactionSouth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTransactionSouth.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnResetList, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 541, Short.MAX_VALUE)
					.addComponent(btnPayment, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
		);
		gl_panelTransactionSouth.setVerticalGroup(
			gl_panelTransactionSouth.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelTransactionSouth.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelTransactionSouth.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnResetList)
						.addComponent(btnPayment)))
		);
		panelTransactionSouth.setLayout(gl_panelTransactionSouth);
		
		JPanel panelTransactionCenter = new JPanel();
		panelTransaction.add(panelTransactionCenter, BorderLayout.CENTER);
		panelTransactionCenter.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTransactionCenter_South = new JPanel();
		panelTransactionCenter.add(panelTransactionCenter_South, BorderLayout.SOUTH);
		
		JLabel lblTotalPrice = new JLabel("Total Price: ");
		lblTotalPrice.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		lblTotalPriceDisplay = new JLabel("RM 0.00");
		lblTotalPriceDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalPriceDisplay.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		GroupLayout gl_panelTransactionCenter_South = new GroupLayout(panelTransactionCenter_South);
		gl_panelTransactionCenter_South.setHorizontalGroup(
			gl_panelTransactionCenter_South.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelTransactionCenter_South.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTotalPrice)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblTotalPriceDisplay, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panelTransactionCenter_South.setVerticalGroup(
			gl_panelTransactionCenter_South.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTransactionCenter_South.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panelTransactionCenter_South.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalPrice)
						.addComponent(lblTotalPriceDisplay))
					.addGap(13))
		);
		panelTransactionCenter_South.setLayout(gl_panelTransactionCenter_South);
		
		JScrollPane scrollPane = new JScrollPane();
		panelTransactionCenter.add(scrollPane, BorderLayout.CENTER);
		
		tableTransactionItem = new JTable();
		tableTransactionItem.setModel(new TransactionItemTableModel());
		tableTransactionItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tableTransactionItem);
		
		JPanel panelCustomer = new JPanel();
		tabbedPane.addTab("Customer", null, panelCustomer, null);
		panelCustomer.setLayout(new BorderLayout(0, 0));
		
		JPanel panelCustSouth = new JPanel();
		panelCustomer.add(panelCustSouth, BorderLayout.SOUTH);
		panelCustSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnAddCustomer = new JButton("Add Customer");
		btnAddCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddCustomer.addActionListener(new AddCustomerButtonListener());
		panelCustSouth.add(btnAddCustomer);
		
		JButton btnEditCustomer = new JButton("Edit Customer");
		btnEditCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEditCustomer.addActionListener(new EditCustomerButtonListener());
		panelCustSouth.add(btnEditCustomer);
		
		JButton btnDeleteCustomer = new JButton("Delete Customer");
		btnDeleteCustomer.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDeleteCustomer.addActionListener(new DeleteCustomerButtonListener());
		panelCustSouth.add(btnDeleteCustomer);
		
		JPanel panelCustNorth = new JPanel();
		panelCustomer.add(panelCustNorth, BorderLayout.NORTH);
		
		JLabel lblSearchBy = new JLabel("Search by:");
		lblSearchBy.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		comboBoxSearchCond = new JComboBox();
		comboBoxSearchCond.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxSearchCond.addItem("IC Number");
		comboBoxSearchCond.addItem("Name");
		
		comboBoxStrCond = new JComboBox();
		comboBoxStrCond.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxStrCond.addItem("Start with");
		comboBoxStrCond.addItem("End with");
		comboBoxStrCond.addItem("Contains");
		
		txtSearch = new JTextField();
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtSearch.setColumns(10);
		
		JButton btnSearchCustomer = new JButton("Search");
		btnSearchCustomer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchCustomer.addActionListener(new SearchCustomerButtonListener());
		
		JButton btnResetCustomer = new JButton("Reset");
		btnResetCustomer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetCustomer.addActionListener(new ResetCustomerButtonListener());
		
		GroupLayout gl_panelCustNorth = new GroupLayout(panelCustNorth);
		gl_panelCustNorth.setHorizontalGroup(
			gl_panelCustNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCustNorth.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearchBy)
					.addGap(10)
					.addComponent(comboBoxSearchCond, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxStrCond, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearchCustomer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetCustomer)
					.addGap(12))
		);
		gl_panelCustNorth.setVerticalGroup(
			gl_panelCustNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelCustNorth.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelCustNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSearchBy)
						.addComponent(comboBoxStrCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSearchCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchCustomer)
						.addComponent(btnResetCustomer)))
		);
		panelCustNorth.setLayout(gl_panelCustNorth);
		
		scollPaneCustomer = new JScrollPane();
		panelCustomer.add(scollPaneCustomer, BorderLayout.CENTER);
		
		tableCustomer = new JTable();
		scollPaneCustomer.setViewportView(tableCustomer);
		tableCustomer.setModel(new CustomerTableModel());
		tableCustomer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	private void reloadCustomerTable(String condition)
	{
		CustomerController cusController = new CustomerController();
		cusList.clear();
		try {
			if(condition == "")
				cusList = cusController.selectAll(0, 2147483467);
			else
				cusList = cusController.selectWhere(condition, 0, 2147483467);
			tableCustomer.repaint();
			tableCustomer.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void reloadTransactionItemList()
	{
		tableTransactionItem.repaint();
		tableTransactionItem.revalidate();
		TransactionController controller = new TransactionController();
		totalPrice = controller.calculateTotal(itemList, amountList);
		lblTotalPriceDisplay.setText("RM " + String.format("%.2f", totalPrice));
	}
	
	//Button Events
	private class LogOutButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			LoginView loginFrame = new LoginView();
			loginFrame.setVisible(true);
			MainPanelView.this.dispose();
		}
	}
	
	private class AdminPanelButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(loginEmployee.getLevel().equals("Admin"))
			{
				AdminPanelView adminFrame = new AdminPanelView(loginEmployee);
				adminFrame.setVisible(true);
				MainPanelView.this.dispose();
			}
			else
			{
				MessageBoxView.infoBox("Access Error: Invalid Permission\nYou do not have the permission to access the admin panel.\nOnly admin can access the admin panel.", 
						"Access Error: Invalid Permission");
			}
		}
	}
	
	private class AddCustomerButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	      CustomerView dialog = new CustomerView(null, false);
	      dialog.setVisible(true);
	      if (dialog.confirmUpdate())
	      {
	    	  reloadCustomerTable("");
	      }
	    }
	}
	
	private class EditCustomerButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableCustomer.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	int id = (int) tableCustomer.getValueAt(index, 0);	 
	        String condition = String.format("id = %d", id);
	    	CustomerController cusController = new CustomerController();
	        Customer customer = null;
	        
	        try {
				customer = cusController.selectWhere(condition, 0, 1).get(0);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        CustomerView dialog = new CustomerView(customer, false);
	        dialog.setVisible(true);
	        if (dialog.confirmUpdate())
	        {
	        	reloadCustomerTable("");
	        }
	    }
	}
	
	private class DeleteCustomerButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableCustomer.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	OptionPaneView opView = new OptionPaneView("Delete Customer");
	    	if(opView.getResult()) 
	    	{
	    		int id = (int) tableCustomer.getValueAt(index, 0);	 
	    		CustomerController cusController = new CustomerController();
	    		Customer customer = new Customer();
	    		customer.setId(id);
	    		try {
	    			int result = cusController.delete(customer);
	    			if(result == 1)
	    				reloadCustomerTable("");
	    		} catch (ClassNotFoundException | SQLException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}	    
	    	}    
	    }
	}
	
	private class SearchCustomerButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String condition = "%s LIKE '%s'", searchCond = "", strCond = "";
			String searchText = txtSearch.getText();
			if((String)comboBoxStrCond.getSelectedItem() == "End with")
				strCond = "%" + searchText;
			else if((String)comboBoxStrCond.getSelectedItem() == "Start with")
				strCond = searchText + "%";
			else
				strCond = "%" + searchText + "%";
			
			if((String)comboBoxSearchCond.getSelectedItem() == "Name")
				searchCond = "name";
			else
				searchCond = "ic";
			
			condition = String.format(condition, searchCond, strCond);
			reloadCustomerTable(condition);
		}
	}
	
	private class ResetCustomerButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			txtSearch.setText("");
			comboBoxSearchCond.setSelectedIndex(0);
			comboBoxStrCond.setSelectedIndex(0);
			reloadCustomerTable("");
		}
	}
 
	private class SelectCustomerButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			SelectCustomerView dialog = new SelectCustomerView();
			dialog.setVisible(true);
			selectedCustomer = new Customer();
			if(dialog.isSelected())
			{	
				selectedCustomer = dialog.getSelectedCustomer();
				lblSelectedCustomer.setText("Customer: "+ selectedCustomer.getName() + " (" + selectedCustomer.getIc() +")");
			}
		}
	}
	
	private class AddItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			AddTransactionItemView dialog = new AddTransactionItemView(null, 0);
			dialog.setVisible(true);
			if(dialog.isSelected() && dialog.isValid())
			{
				boolean duplicateExist = itemList.stream().filter(x -> x.getCode().equals(dialog.getSelectedItem().getCode())).findFirst().isPresent();
				totalPrice = 0;
				if(!duplicateExist)
				{
					itemList.add(dialog.getSelectedItem());
					amountList.add(dialog.getItemAmount());
					reloadTransactionItemList();
				}
				else
					MessageBoxView.infoBox("Input Error: Duplicate Item detected.\nThis item already exists in the list", "Input Error: Duplicate Item detected");
			}
		}
	}
	
	private class EditItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
	    	int index = tableTransactionItem.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	Item item = itemList.get(index);
	    	int amount = amountList.get(index);
	    	    	
			AddTransactionItemView dialog = new AddTransactionItemView(item, amount);
			dialog.setVisible(true);
			if(dialog.isSelected())
			{
				totalPrice = 0;

				amountList.add(index, dialog.getItemAmount());
				reloadTransactionItemList();
			}
		}
	}
	
	private class RemoveItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int index = tableTransactionItem.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	OptionPaneView opView = new OptionPaneView("Remove Item");
	    	if(opView.getResult()) 
	    	{
	    		itemList.remove(index);
	    		amountList.remove(index);
	    		reloadTransactionItemList();
	    	}	    	
		}
	}
	
	private class ResetListButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
	    	OptionPaneView opView = new OptionPaneView("Reset Transaction");
	    	if(opView.getResult()) 
	    	{
	    		selectedCustomer = new Customer();
	    		lblSelectedCustomer.setText("Customer: ");
	    		itemList.clear();
	    		amountList.clear();
	    		reloadTransactionItemList();
	    	}	    	
		}
	}
	
	private class PaymentButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			TransactionController controller = new TransactionController();
			try {
				controller.validateTransaction(selectedCustomer, itemList, amountList);
				PaymentView dialog = new PaymentView(selectedCustomer, loginEmployee, itemList, amountList);
				dialog.setVisible(true);
				if(dialog.isAdded())
				{
		    		selectedCustomer = new Customer();
		    		lblSelectedCustomer.setText("Customer: ");
		    		itemList.clear();
		    		amountList.clear();
		    		reloadTransactionItemList();
				}
			}catch (InputException e1){
				e1.displayMessage();
			}
				
		}
	}
	
	//Table Models
	private class CustomerTableModel implements TableModel 
	{
	   
	   String header[] = new String[] { "ID", "Name", "IC No.", "Phone", "Email"};
	    
	   public int getColumnCount() 
	   {
	      return header.length;
	   }
	   
	   public int getRowCount() 
	   {
	      return cusList.size();
	   }    
	    
	   public String getColumnName(int column) 
	   {
	     return header[column];
	   }
	        
	   public Class getColumnClass(int column) 
	   {
	       return String.class;
	   }
	   
	   public Object getValueAt(int rowIndex, int columnIndex) 
	   {
		   Customer customer = cusList.get(rowIndex);
		   switch(columnIndex)
		   {
		   	case 0: return customer.getId();
		   	case 1: return customer.getName();
		   	case 2: return customer.getIc();
		   	case 3: return customer.getContactNo();
		   	case 4: return customer.getEmail();
		   }
	        return "";
	    }    
	    
	    public boolean isCellEditable(int rowIndex, int columnIndex) 
	    {
	        return false;
	    }
	    
	    public void setValueAt(Object value, int row, int column) {}

	    public void addTableModelListener(TableModelListener l) {}

	    public void removeTableModelListener(TableModelListener l){}  
	  }

	private class TransactionItemTableModel implements TableModel 
	{
	   
	   String header[] = new String[] { "Item ID", "Item Name", "Item Code", "Unit Price", "Amount", "Sub Total"};
	    
	   public int getColumnCount() 
	   {
	      return header.length;
	   }
	   
	   public int getRowCount() 
	   {
	      return itemList.size();
	   }    
	    
	   public String getColumnName(int column) 
	   {
	     return header[column];
	   }
	        
	   public Class getColumnClass(int column) 
	   {
	       return String.class;
	   }
	   
	   public Object getValueAt(int rowIndex, int columnIndex) 
	   {
		   Item item = itemList.get(rowIndex);
		   switch(columnIndex)
		   {
		   	case 0: return item.getId();
		   	case 1: return item.getName();
		   	case 2: return item.getCode();
		   	case 3: return String.format("%.2f", item.getPrice());
		   	case 4: return amountList.get(rowIndex);
		   	case 5: return String.format("%.2f", amountList.get(rowIndex) * item.getPrice());
		   }
	        return "";
	    }    
	    
	    public boolean isCellEditable(int rowIndex, int columnIndex) 
	    {
	        return false;
	    }
	    
	    public void setValueAt(Object value, int row, int column) {}

	    public void addTableModelListener(TableModelListener l) {}

	    public void removeTableModelListener(TableModelListener l){}  
	  }
}
