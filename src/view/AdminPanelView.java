package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.EmployeeController;
import controller.ItemController;
import controller.ItemTypeController;
import controller.TransactionController;
import controller.TransactionItemController;
import model.Employee;
import model.Item;
import model.ItemType;
import model.Transaction;
import model.TransactionItem;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.FlowLayout;

public class AdminPanelView extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearchEmp;
	private JComboBox comboBoxSearchCondEmp;
	private JComboBox comboBoxStrCondEmp;
	private JComboBox comboBoxSearchCondIType;
	private JComboBox comboBoxStrCondIType;
	private JComboBox comboBoxSearchCondItem;
	private JComboBox comboBoxStrCondItem;
	private JTable tableEmployee;
	private JTable tableItemType;
	private JTable tableItem;

	private ArrayList<Employee> empList = new ArrayList<Employee>();
	private ArrayList<ItemType> iTypeList = new ArrayList<ItemType>();
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	private JTextField txtSearchIType;
	private JTextField txtSearchItem;
	private Employee loginEmployee = new Employee();
	private JTable tableTransaction;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminPanelView frame = new AdminPanelView(null);
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
	public AdminPanelView(Employee loginEmployee) {
		initComponents();
		reloadEmployeeTable("");
		reloadItemTypeTable("");
		reloadItemTable("");
		reloadTransactionTable("");
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
		
		JLabel label = new JLabel("Admin Panel");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnMainPanel = new JButton("Main Panel");
		btnMainPanel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnMainPanel.addActionListener(new MainPanelButtonListener());
		
		GroupLayout gl_panelNorth = new GroupLayout(panelNorth);
		gl_panelNorth.setHorizontalGroup(
			gl_panelNorth.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelNorth.createSequentialGroup()
					.addContainerGap(296, Short.MAX_VALUE)
					.addComponent(label)
					.addGap(202)
					.addComponent(btnMainPanel, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panelNorth.setVerticalGroup(
			gl_panelNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNorth.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnMainPanel)
						.addComponent(label))
					.addContainerGap())
		);
		panelNorth.setLayout(gl_panelNorth);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelEmployee = new JPanel();
		tabbedPane.addTab("Employee", null, panelEmployee, null);
		panelEmployee.setLayout(new BorderLayout(0, 0));
		
		JPanel panelEmpSouth = new JPanel();
		panelEmployee.add(panelEmpSouth, BorderLayout.SOUTH);
		
		JButton btnAddEmployee = new JButton("Add Employee");
		btnAddEmployee.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddEmployee.addActionListener(new AddEmployeeButtonListener());
		panelEmpSouth.add(btnAddEmployee);
		
		JButton btnEditEmployee = new JButton("Edit Employee");
		btnEditEmployee.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEditEmployee.addActionListener(new EditEmployeeButtonListener());
		panelEmpSouth.add(btnEditEmployee);
		
		JButton btnDeleteEmployee = new JButton("Delete Employee");
		btnDeleteEmployee.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDeleteEmployee.addActionListener(new DeleteEmployeeButtonListener());
		panelEmpSouth.add(btnDeleteEmployee);
		
		JPanel panelEmpNorth = new JPanel();
		panelEmployee.add(panelEmpNorth, BorderLayout.NORTH);
		
		JLabel lblSearchBy = new JLabel("Search by: ");
		lblSearchBy.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		comboBoxSearchCondEmp = new JComboBox();
		comboBoxSearchCondEmp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxSearchCondEmp.addItem("IC Number");
		comboBoxSearchCondEmp.addItem("Name");
		
		comboBoxStrCondEmp = new JComboBox();
		comboBoxStrCondEmp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxStrCondEmp.addItem("Start with");
		comboBoxStrCondEmp.addItem("End with");
		comboBoxStrCondEmp.addItem("Contains");
		
		txtSearchEmp = new JTextField();
		txtSearchEmp.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtSearchEmp.setColumns(10);
		
		JButton btnSearchEmployee = new JButton("Search");
		btnSearchEmployee.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchEmployee.addActionListener(new SearchEmployeeButtonListener());
		
		JButton btnResetEmployee = new JButton("Reset");
		btnResetEmployee.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetEmployee.addActionListener(new ResetEmployeeButtonListener());

		GroupLayout gl_panelEmpNorth = new GroupLayout(panelEmpNorth);
		gl_panelEmpNorth.setHorizontalGroup(
				gl_panelEmpNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelEmpNorth.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSearchBy)
					.addGap(10)
					.addComponent(comboBoxSearchCondEmp, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxStrCondEmp, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSearchEmp, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearchEmployee)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetEmployee)
					.addGap(12))
		);
		gl_panelEmpNorth.setVerticalGroup(
				gl_panelEmpNorth.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelEmpNorth.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelEmpNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSearchBy)
						.addComponent(comboBoxStrCondEmp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSearchCondEmp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtSearchEmp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchEmployee)
						.addComponent(btnResetEmployee)))
		);
		panelEmpNorth.setLayout(gl_panelEmpNorth);
		
		JScrollPane scrollPaneEmp = new JScrollPane();
		panelEmployee.add(scrollPaneEmp, BorderLayout.CENTER);
		
		tableEmployee = new JTable();
		tableEmployee.setModel(new EmployeeTableModel());
		tableEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneEmp.setViewportView(tableEmployee);
		
		JPanel panelItemType = new JPanel();
		tabbedPane.addTab("Item Type", null, panelItemType, null);
		panelItemType.setLayout(new BorderLayout(0, 0));
		
		JPanel panelITypeSouth = new JPanel();
		panelItemType.add(panelITypeSouth, BorderLayout.SOUTH);
		panelITypeSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnAddItemType = new JButton("Add Item Type");
		btnAddItemType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddItemType.addActionListener(new AddItemTypeButtonListener());
		panelITypeSouth.add(btnAddItemType);
		
		JButton btnEditItemType = new JButton("Edit Item Type");
		btnEditItemType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEditItemType.addActionListener(new EditItemTypeButtonListener());
		panelITypeSouth.add(btnEditItemType);
		
		JButton btnDeleteItemType = new JButton("Delete Item Type");
		btnDeleteItemType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDeleteItemType.addActionListener(new DeleteItemTypeButtonListener());
		panelITypeSouth.add(btnDeleteItemType);
		
		JPanel panelITypeNorth = new JPanel();
		panelItemType.add(panelITypeNorth, BorderLayout.NORTH);
		
		JLabel label_1 = new JLabel("Search by: ");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		comboBoxSearchCondIType = new JComboBox();
		comboBoxSearchCondIType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxSearchCondIType.addItem("Code");
		comboBoxSearchCondIType.addItem("Name");
		
		comboBoxStrCondIType = new JComboBox();
		comboBoxStrCondIType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxStrCondIType.addItem("Start with");
		comboBoxStrCondIType.addItem("End with");
		comboBoxStrCondIType.addItem("Contains");
		
		txtSearchIType = new JTextField();
		txtSearchIType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtSearchIType.setColumns(10);
		
		JButton btnSearchItemType = new JButton("Search");
		btnSearchItemType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchItemType.addActionListener(new SearchItemTypeButtonListener());
		
		JButton btnResetItemType = new JButton("Reset");
		btnResetItemType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetItemType.addActionListener(new ResetItemTypeButtonListener());

		GroupLayout gl_panelITypeNorth = new GroupLayout(panelITypeNorth);
		gl_panelITypeNorth.setHorizontalGroup(
			gl_panelITypeNorth.createParallelGroup(Alignment.LEADING)
				.addGap(0, 721, Short.MAX_VALUE)
				.addGroup(gl_panelITypeNorth.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_1)
					.addGap(10)
					.addComponent(comboBoxSearchCondIType, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxStrCondIType, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSearchIType, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearchItemType)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetItemType)
					.addGap(12))
		);
		gl_panelITypeNorth.setVerticalGroup(
			gl_panelITypeNorth.createParallelGroup(Alignment.LEADING)
				.addGap(0, 28, Short.MAX_VALUE)
				.addGroup(gl_panelITypeNorth.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelITypeNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(comboBoxStrCondIType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSearchCondIType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtSearchIType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchItemType)
						.addComponent(btnResetItemType)))
		);
		panelITypeNorth.setLayout(gl_panelITypeNorth);
		
		JScrollPane scrollPaneItemType = new JScrollPane();
		panelItemType.add(scrollPaneItemType, BorderLayout.CENTER);
		
		tableItemType = new JTable();
		tableItemType.setModel(new ItemTypeTableModel());
		tableItemType.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneItemType.setViewportView(tableItemType);
		
		JPanel panelItem = new JPanel();
		tabbedPane.addTab("Item", null, panelItem, null);
		panelItem.setLayout(new BorderLayout(0, 0));
		
		JPanel panelItemSouth = new JPanel();
		panelItem.add(panelItemSouth, BorderLayout.SOUTH);
		panelItemSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddItem.addActionListener(new AddItemButtonListener());
		panelItemSouth.add(btnAddItem);
		
		JButton btnEditItem = new JButton("Edit Item");
		btnEditItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnEditItem.addActionListener(new EditItemButtonListener());
		panelItemSouth.add(btnEditItem);
		
		JButton btnDeleteItem = new JButton("Delete Item");
		btnDeleteItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDeleteItem.addActionListener(new DeleteItemButtonListener());
		panelItemSouth.add(btnDeleteItem);
		
		JPanel panelItemNorth = new JPanel();
		panelItem.add(panelItemNorth, BorderLayout.NORTH);
		
		JLabel label_2 = new JLabel("Search by: ");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		comboBoxSearchCondItem = new JComboBox();
		comboBoxSearchCondItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxSearchCondItem.addItem("Code");
		comboBoxSearchCondItem.addItem("Name");
		
		comboBoxStrCondItem = new JComboBox();
		comboBoxStrCondItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxStrCondItem.addItem("Start with");
		comboBoxStrCondItem.addItem("End with");
		comboBoxStrCondItem.addItem("Contains");
		
		txtSearchItem = new JTextField();
		txtSearchItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtSearchItem.setColumns(10);
		
		JButton btnSearchItem = new JButton("Search");
		btnSearchItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSearchItem.addActionListener(new SearchItemButtonListener());
		
		JButton btnResetItem = new JButton("Reset");
		btnResetItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnResetItem.addActionListener(new ResetItemButtonListener());
		
		GroupLayout gl_panelItemNorth = new GroupLayout(panelItemNorth);
		gl_panelItemNorth.setHorizontalGroup(
			gl_panelItemNorth.createParallelGroup(Alignment.LEADING)
				.addGap(0, 721, Short.MAX_VALUE)
				.addGroup(gl_panelItemNorth.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_2)
					.addGap(10)
					.addComponent(comboBoxSearchCondItem, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBoxStrCondItem, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtSearchItem, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSearchItem)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetItem)
					.addGap(12))
		);
		gl_panelItemNorth.setVerticalGroup(
			gl_panelItemNorth.createParallelGroup(Alignment.LEADING)
				.addGap(0, 28, Short.MAX_VALUE)
				.addGroup(gl_panelItemNorth.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelItemNorth.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_2)
						.addComponent(comboBoxStrCondItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxSearchCondItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtSearchItem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSearchItem)
						.addComponent(btnResetItem)))
		);
		panelItemNorth.setLayout(gl_panelItemNorth);
		
		JScrollPane scrollPaneItem = new JScrollPane();
		panelItem.add(scrollPaneItem, BorderLayout.CENTER);
		
		tableItem = new JTable();
		tableItem.setModel(new ItemTableModel());
		tableItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneItem.setViewportView(tableItem);
		
		JPanel panelTRecords = new JPanel();
		tabbedPane.addTab("Transaction Records", null, panelTRecords, null);
		panelTRecords.setLayout(new BorderLayout(0, 0));
		
		JPanel panelTRecordsSouth = new JPanel();
		panelTRecords.add(panelTRecordsSouth, BorderLayout.SOUTH);
		panelTRecordsSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnViewTransactionDetail = new JButton("View Transaction Detail");
		btnViewTransactionDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnViewTransactionDetail.addActionListener(new ViewTransactionDetailButtonListener());
		panelTRecordsSouth.add(btnViewTransactionDetail);
		
		JButton btnDeleteTransaction = new JButton("Delete Transaction");
		btnDeleteTransaction.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDeleteTransaction.addActionListener(new DeleteTransactionButtonListener());
		panelTRecordsSouth.add(btnDeleteTransaction);
		
		JButton btnFilterTransaction = new JButton("Filter Transaction");
		btnFilterTransaction.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnFilterTransaction.addActionListener(new FilterTransactionButtonListener());
		panelTRecordsSouth.add(btnFilterTransaction);
		
		JButton btnResetTransactionList = new JButton("Reset Transaction List");
		btnResetTransactionList.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnResetTransactionList.addActionListener(new ResetTransactionListButtonListener());
		panelTRecordsSouth.add(btnResetTransactionList);
		
		JScrollPane scrollPaneTransaction = new JScrollPane();
		panelTRecords.add(scrollPaneTransaction, BorderLayout.CENTER);
		
		tableTransaction = new JTable();
		tableTransaction.setModel(new TransactionTableModel());
		tableTransaction.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneTransaction.setViewportView(tableTransaction);

	}
	
	private void reloadEmployeeTable(String condition)
	{
		EmployeeController controller = new EmployeeController();
		empList.clear();
		try {
			if(condition == "")
				empList = controller.selectAll(0, 2147483467);
			else
				empList = controller.selectWhere(condition, 0, 2147483467);
			tableEmployee.repaint();
			tableEmployee.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void reloadItemTypeTable(String condition)
	{
		ItemTypeController controller = new ItemTypeController();
		iTypeList.clear();
		try {
			if(condition == "")
				iTypeList = controller.selectAll(0, 2147483467);
			else
				iTypeList = controller.selectWhere(condition, 0, 2147483467);
			tableItemType.repaint();
			tableItemType.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}

	
	private void reloadItemTable(String condition)
	{
		ItemController controller = new ItemController();
		itemList.clear();
		try {
			if(condition == "")
				itemList = controller.selectAll(0, 2147483467);
			else
				itemList = controller.selectWhere(condition, 0, 2147483467);
			tableItem.repaint();
			tableItem.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private void reloadTransactionTable(String condition)
	{
		TransactionController controller = new TransactionController();
		transactionList.clear();
		try {
			if(condition == "")
				transactionList = controller.selectAll(0, 2147483467);
			else
				transactionList = controller.selectWhere(condition, 0, 2147483467);
			tableTransaction.repaint();
			tableTransaction.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}


	
	//Button Events
	private class MainPanelButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			MainPanelView mainFrame = new MainPanelView(loginEmployee);
			mainFrame.setVisible(true);
			AdminPanelView.this.dispose();
		}
	}
	
	private class AddEmployeeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	      EmployeeView dialog = new EmployeeView(null, false);
	      dialog.setVisible(true);
	      if (dialog.confirmUpdate())
	      {
	    	  reloadEmployeeTable("");
	      }
	    }
	}
	
	private class EditEmployeeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableEmployee.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	int id = (int) tableEmployee.getValueAt(index, 0);	 
	        String condition = String.format("id = %d", id);
	    	EmployeeController controller = new EmployeeController();
	        Employee employee = null;
	        
	        try {
				employee = controller.selectWhere(condition, 0, 1).get(0);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        EmployeeView dialog = new EmployeeView(employee, false);
	        dialog.setVisible(true);
	        if (dialog.confirmUpdate())
	        {
	        	reloadEmployeeTable("");
	        }
	    }
	}

	private class DeleteEmployeeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableEmployee.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	OptionPaneView opView = new OptionPaneView("Delete Employee");
	    	if(opView.getResult()) 
	    	{
	    		int id = (int) tableEmployee.getValueAt(index, 0);	
	    		if(id == loginEmployee.getId())
	    		{	
	    			MessageBoxView.infoBox("Deletion Error: Employee Logged In.\nThe selected employee have logged into the system.", "Deleted Error: Employee Logged In");
	    			return;
	    		}
	    		EmployeeController controller = new EmployeeController();
	    		Employee employee = new Employee();
	    		employee.setId(id);
	    		try {
	    			int result = controller.delete(employee);
	    			if(result == 1)
	    				reloadEmployeeTable("");
	    		} catch (ClassNotFoundException | SQLException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}	    
	    	}    
	    }
	}

	private class SearchEmployeeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String condition = "%s LIKE '%s'", searchCond = "", strCond = "";
			String searchText = txtSearchEmp.getText();
			if((String)comboBoxStrCondEmp.getSelectedItem() == "End with")
				strCond = "%" + searchText;
			else if((String)comboBoxStrCondEmp.getSelectedItem() == "Start with")
				strCond = searchText + "%";
			else
				strCond = "%" + searchText + "%";
			
			if((String)comboBoxSearchCondEmp.getSelectedItem() == "Name")
				searchCond = "name";
			else
				searchCond = "ic";
			
			condition = String.format(condition, searchCond, strCond);
			reloadEmployeeTable(condition);
		}
	}
	
	private class ResetEmployeeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			txtSearchEmp.setText("");
			comboBoxSearchCondEmp.setSelectedIndex(0);
			comboBoxStrCondEmp.setSelectedIndex(0);
			reloadEmployeeTable("");
		}
	}

	private class AddItemTypeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	      ItemTypeView dialog = new ItemTypeView(null);
	      dialog.setVisible(true);
	      if (dialog.confirmUpdate())
	      {
	    	  reloadItemTypeTable("");
	      }
	    }
	}
	
	private class EditItemTypeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableItemType.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	int id = (int) tableItemType.getValueAt(index, 0);	 
	        String condition = String.format("id = %d", id);
	    	ItemTypeController controller = new ItemTypeController();
	        ItemType itemType = null;
	        
	        try {
	        	itemType = controller.selectWhere(condition, 0, 1).get(0);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        ItemTypeView dialog = new ItemTypeView(itemType);
	        dialog.setVisible(true);
	        if (dialog.confirmUpdate())
	        {
	        	reloadItemTypeTable("");
	        }
	    }
	}

	private class DeleteItemTypeButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableItemType.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	OptionPaneView opView = new OptionPaneView("Delete Item Type");
	    	if(opView.getResult()) 
	    	{
	    		int id = (int) tableItemType.getValueAt(index, 0);	 
	    		ItemTypeController controller = new ItemTypeController();
	    		ItemType itemType = new ItemType();
	    		itemType.setId(id);
	    		
	    		ItemController iController = new ItemController();
	    		ArrayList<Item> items = new ArrayList();
	    		
	    		try {
	    			items = iController.selectWhere(String.format("type_id = %d", id), 0, 1);
	    			if(!items.isEmpty())
	    			{
	    				MessageBoxView.infoBox("Deletion Error: Item Detected.\nFor this item type, there are one or more items detected in the database."
	    						+ "\nPlease delete all items related to this item type if you want to delete this item type.", "Deletion Error: Item Detected");
	    				return;
	    			}
	    			int result = controller.delete(itemType);
	    			if(result == 1)
	    				reloadItemTypeTable("");
	    		} catch (ClassNotFoundException | SQLException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}	    
	    	}    
	    }
	}

	private class SearchItemTypeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String condition = "%s LIKE '%s'", searchCond = "", strCond = "";
			String searchText = txtSearchIType.getText();
			if((String)comboBoxStrCondIType.getSelectedItem() == "End with")
				strCond = "%" + searchText;
			else if((String)comboBoxStrCondIType.getSelectedItem() == "Start with")
				strCond = searchText + "%";
			else
				strCond = "%" + searchText + "%";
			
			if((String)comboBoxSearchCondIType.getSelectedItem() == "Code")
				searchCond = "code";
			else
				searchCond = "name";
			
			condition = String.format(condition, searchCond, strCond);
			reloadItemTypeTable(condition);
		}
	}
	
	private class ResetItemTypeButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			txtSearchIType.setText("");
			comboBoxSearchCondIType.setSelectedIndex(0);
			comboBoxStrCondIType.setSelectedIndex(0);
			reloadItemTypeTable("");
		}
	}

	private class AddItemButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	      ItemView dialog = new ItemView(null);
	      dialog.setVisible(true);
	      if (dialog.confirmUpdate())
	      {
	    	  reloadItemTable("");
	      }
	    }
	}
	
	private class EditItemButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableItem.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	int id = (int) tableItem.getValueAt(index, 0);	 
	        String condition = String.format("id = %d", id);
	    	ItemController controller = new ItemController();
	        Item item = null;
	        
	        try {
	        	item = controller.selectWhere(condition, 0, 1).get(0);
			} catch (ClassNotFoundException | SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        ItemView dialog = new ItemView(item);
	        dialog.setVisible(true);
	        if (dialog.confirmUpdate())
	        {
	        	reloadItemTable("");
	        }
	    }
	}

	private class DeleteItemButtonListener implements ActionListener
	{
	    public void actionPerformed(ActionEvent e)
	    {
	    	int index = tableItem.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	OptionPaneView opView = new OptionPaneView("Delete Item");
	    	if(opView.getResult()) 
	    	{
	    		int id = (int) tableItem.getValueAt(index, 0);	 
	    		ItemController controller = new ItemController();
	    		Item item = new Item();
	    		item.setId(id);
	    		try {
	    			int result = controller.delete(item);
	    			if(result == 1)
	    				reloadItemTable("");
	    		} catch (ClassNotFoundException | SQLException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}	    
	    	}    
	    }
	}

	private class SearchItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String condition = "%s LIKE '%s'", searchCond = "", strCond = "";
			String searchText = txtSearchItem.getText();
			if((String)comboBoxStrCondItem.getSelectedItem() == "End with")
				strCond = "%" + searchText;
			else if((String)comboBoxStrCondItem.getSelectedItem() == "Start with")
				strCond = searchText + "%";
			else
				strCond = "%" + searchText + "%";
			
			if((String)comboBoxSearchCondItem.getSelectedItem() == "Code")
				searchCond = "code";
			else
				searchCond = "name";
			
			condition = String.format(condition, searchCond, strCond);
			reloadItemTable(condition);
		}
	}
	
	private class ResetItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			txtSearchItem.setText("");
			comboBoxSearchCondItem.setSelectedIndex(0);
			comboBoxStrCondItem.setSelectedIndex(0);
			reloadItemTable("");
		}
	}

	private class ViewTransactionDetailButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
	    	int index = tableTransaction.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	
	    	int id = (int) tableTransaction.getValueAt(index, 0);	 
	        String condition = String.format("id = %d", id);
	    	TransactionController controller = new TransactionController();
	        Transaction transaction = null;
	        try {
	        	transaction = controller.selectWhere(condition, 0, 1).get(0);
				TransactionDetailView dialog = new TransactionDetailView(transaction);
				dialog.setVisible(true);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	private class DeleteTransactionButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
	    	int index = tableTransaction.getSelectedRow();
	    	if(index < 0)
	    		return;
	    	OptionPaneView opView = new OptionPaneView("Delete Transaction");
	    	if(opView.getResult()) 
	    	{
	    		int id = (int) tableTransaction.getValueAt(index, 0);	 
	    		TransactionController controller = new TransactionController();
		        Transaction transaction = new Transaction();
    			transaction.setId(id);
    			TransactionItemController tItemController = new TransactionItemController();
    			TransactionItem tItem = new TransactionItem();
    			tItem.setTransactionId(id);
	    		try {
	    			int result = controller.delete(transaction);
	    			tItemController.delete(tItem);
	    			if(result == 1)
	    				reloadTransactionTable("");
	    		} catch (ClassNotFoundException | SQLException e1) {
	    			// TODO Auto-generated catch block
	    			e1.printStackTrace();
	    		}	    
	    	}    
		}
	}
	
	private class FilterTransactionButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			FilterTransactionView dialog = new FilterTransactionView();
			dialog.setVisible(true);
			String condition = dialog.getConditionString();
			if(!condition.equals(""))
			{
				reloadTransactionTable(condition);
			}
		}
	}
	
	private class ResetTransactionListButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			reloadTransactionTable("");
		}
	}
	//Table Models
	private class EmployeeTableModel implements TableModel
	{
		String header[] = new String[] { "ID", "Username", "Name", "IC No.", "Phone", "Email", "Level", "Address"};
		
		public int getColumnCount() 
		{
		   return header.length;
		}
		
		public int getRowCount() 
		{
		   return empList.size();
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
			Employee employee = empList.get(rowIndex);
		 	switch(columnIndex)
			{
			 	case 0: return employee.getId();
			 	case 1: return employee.getUsername();
			   	case 2: return employee.getName();
			   	case 3: return employee.getIc();
			   	case 4: return employee.getContactNo();
			   	case 5: return employee.getEmail();
			   	case 6: return employee.getLevel();
			   	case 7: return employee.getAddress();
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
	
	private class ItemTypeTableModel implements TableModel
	{
		String header[] = new String[] { "ID", "Name", "Code"};
		
		public int getColumnCount() 
		{
		   return header.length;
		}
		
		public int getRowCount() 
		{
		   return iTypeList.size();
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
			ItemType iType = iTypeList.get(rowIndex);
		 	switch(columnIndex)
			{
			 	case 0: return iType.getId();
			   	case 1: return iType.getName();
			   	case 2: return iType.getCode();
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

	private class ItemTableModel implements TableModel
	{
		String header[] = new String[] { "ID", "Name", "Code","Item Type", "Unit Price", "Quantity", "Description"};
		
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
			ItemType iType = new ItemType();
			try {
				iType = new ItemTypeController().selectWhere(String.format("id = %d", item.getTypeId()), 0, 1).get(0);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 	switch(columnIndex)
			{
			 	case 0: return item.getId();
			 	case 1: return item.getName();
			   	case 2: return item.getCode();
			   	case 3: return iType.getName();
			   	case 4: return item.getPrice();
			   	case 5: return item.getQuantity();
			   	case 6: return item.getDescription();
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

	private class TransactionTableModel implements TableModel
	{
		String header[] = new String[] { "ID", "Code", "Date", "Time","Employee ID", "Customer ID", "Total Price"};
		
		public int getColumnCount() 
		{
		   return header.length;
		}
		
		public int getRowCount() 
		{
		   return transactionList.size();
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
			Transaction transaction = transactionList.get(rowIndex);
			LocalDateTime transactionDateTime = transaction.getDateTime();
			DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
			DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		 	switch(columnIndex)
			{
			 	case 0: return transaction.getId();
			 	case 1: return transaction.getCode();
			 	case 2: return transactionDateTime.format(dateFormat);
			 	case 3: return transactionDateTime.format(timeFormat);
			   	case 4: return transaction.getEmployeeId();
			   	case 5: return transaction.getCustomerId();
			   	case 6: return String.format("%.2f", transaction.getTotalPrice());
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
