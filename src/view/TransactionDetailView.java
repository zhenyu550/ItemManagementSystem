package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import model.Customer;
import model.Employee;
import model.Item;
import model.Transaction;
import model.TransactionItem;
import controller.CustomerController;
import controller.EmployeeController;
import controller.ItemController;
import controller.TransactionController;
import controller.TransactionItemController;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TransactionDetailView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtId;
	private JTextField txtCode;
	private JTextField txtDate;
	private JTextField txtTime;
	private JTextField txtEmployeeId;
	private JTextField txtCustomerId;
	private JTextField txtTotalPrice;
	private JTable tableTransactionItem;
	
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private ArrayList<Integer> amountList = new ArrayList<Integer>();
	private ArrayList<TransactionItem> tItemList = new ArrayList<TransactionItem>();
	private Transaction transaction = new Transaction();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TransactionController controller = new TransactionController();
			Transaction data = new Transaction();
			data = controller.selectWhere("id = 4", 0, 1).get(0);
			TransactionDetailView dialog = new TransactionDetailView(data);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TransactionDetailView(Transaction data) {
		initCompoments();
		transaction = data;
		loadTextField();
		reloadTransactionItemList();
	}
	
	public void initCompoments()
	{
		setModal(true);
		setBounds(100, 100, 800, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPanel.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblTransactionDetail = new JLabel("Transaction Detail");
		lblTransactionDetail.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelNorth.add(lblTransactionDetail);
		
		JPanel panelSouth = new JPanel();
		contentPanel.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnClose = new JButton("Close");
		btnClose.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnClose.addActionListener(new CloseButtonListener());
		panelSouth.add(btnClose);
		
		JButton btnViewEmployeeDetail = new JButton("View Employee Detail");
		btnViewEmployeeDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnViewEmployeeDetail.addActionListener(new ViewEmployeeDetailButtonListener());
		panelSouth.add(btnViewEmployeeDetail);
		
		JButton btnViewCustomerDetail = new JButton("View Customer Detail");
		btnViewCustomerDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnViewCustomerDetail.addActionListener(new ViewCustomerDetailButtonListener());
		panelSouth.add(btnViewCustomerDetail);
		
		JSplitPane splitPane = new JSplitPane();
		contentPanel.add(splitPane, BorderLayout.CENTER);
		
		JPanel panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);
		panelRight.setLayout(new BorderLayout(0, 0));
		
		JPanel panelRightNorth = new JPanel();
		panelRight.add(panelRightNorth, BorderLayout.NORTH);
		panelRightNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblItemList = new JLabel("Item List");
		lblItemList.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panelRightNorth.add(lblItemList);
		
		JScrollPane scrollPane = new JScrollPane();
		panelRight.add(scrollPane, BorderLayout.CENTER);
		
		tableTransactionItem = new JTable();
		tableTransactionItem.setModel(new OrderItemTableModel());
		tableTransactionItem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tableTransactionItem);
		
		JPanel panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		
		JLabel lblId = new JLabel("ID: ");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtId = new JTextField();
		txtId.setEditable(false);
		txtId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtId.setColumns(10);
		
		JLabel lblCode = new JLabel("Code: ");
		lblCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtCode = new JTextField();
		txtCode.setEditable(false);
		txtCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtCode.setColumns(10);
		
		JLabel lblDate = new JLabel("Date: ");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtDate = new JTextField();
		txtDate.setEditable(false);
		txtDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtDate.setColumns(10);
		
		txtTime = new JTextField();
		txtTime.setEditable(false);
		txtTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtTime.setColumns(10);
		
		JLabel lblTime = new JLabel("Time: ");
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblEmployeeId = new JLabel("Employee ID: ");
		lblEmployeeId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtEmployeeId = new JTextField();
		txtEmployeeId.setEditable(false);
		txtEmployeeId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtEmployeeId.setColumns(10);
		
		JLabel lblCustomerId = new JLabel("Customer ID: ");
		lblCustomerId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtCustomerId = new JTextField();
		txtCustomerId.setEditable(false);
		txtCustomerId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtCustomerId.setColumns(10);
		
		JLabel lblTotalPrice = new JLabel("Total Price: ");
		lblTotalPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		txtTotalPrice = new JTextField();
		txtTotalPrice.setEditable(false);
		txtTotalPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtTotalPrice.setColumns(10);
		
		GroupLayout gl_panelLeft = new GroupLayout(panelLeft);
		gl_panelLeft.setHorizontalGroup(
			gl_panelLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLeft.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelLeft.createParallelGroup(Alignment.LEADING, false)
							.addComponent(lblCustomerId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblEmployeeId, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblId)
							.addComponent(lblCode, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panelLeft.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDate, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTime, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblTotalPrice))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelLeft.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(txtDate, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
							.addComponent(txtCode, Alignment.LEADING)
							.addComponent(txtId, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
						.addComponent(txtTime, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addComponent(txtEmployeeId, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addComponent(txtCustomerId, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
						.addComponent(txtTotalPrice, GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelLeft.setVerticalGroup(
			gl_panelLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLeft.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblId)
						.addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtCode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCode))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDate))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTime)
						.addComponent(txtTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEmployeeId)
						.addComponent(txtEmployeeId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCustomerId)
						.addComponent(txtCustomerId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTotalPrice)
						.addComponent(txtTotalPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(89, Short.MAX_VALUE))
		);
		panelLeft.setLayout(gl_panelLeft);
	}
	
	public void loadTextField()
	{
		DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		txtId.setText(Integer.toString(transaction.getId()));
		txtCode.setText(transaction.getCode());
		txtDate.setText(transaction.getDateTime().format(dateFormat));
		txtTime.setText(transaction.getDateTime().format(timeFormat));
		txtEmployeeId.setText(Integer.toString(transaction.getEmployeeId()));
		txtCustomerId.setText(Integer.toString(transaction.getCustomerId()));
		txtTotalPrice.setText(String.format("RM %.2f", transaction.getTotalPrice()));
	}
	
	public void reloadTransactionItemList()
	{
		TransactionItemController tItemController = new TransactionItemController();
		ItemController itemController = new ItemController();
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			tItemList = tItemController.selectWhere(String.format("transaction_id = %d", transaction.getId()), 0, 2147483467);
			for(int i = 0; i < tItemList.size(); i++)
			{
				Item item = new Item();
				items = itemController.selectWhere(String.format("id = %d", tItemList.get(i).getItemId()), 0, 1);
				if(items.isEmpty())
				{
					item.setId(tItemList.get(i).getItemId());
				}
				else
					item = items.get(0);
				itemList.add(item);
				amountList.add(tItemList.get(i).getAmount());
			}
			tableTransactionItem.repaint();
			tableTransactionItem.revalidate();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private class CloseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			TransactionDetailView.this.dispose();
		}
	}
	
	private class ViewCustomerDetailButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int customerId = transaction.getCustomerId();
			ArrayList<Customer> cusList = new ArrayList<Customer>();
			Customer customer = new Customer();
			CustomerController controller = new CustomerController();
			try {
				cusList = controller.selectWhere(String.format("id = %d", customerId), 0, 1);
				if(cusList.isEmpty())
				{
					MessageBoxView.infoBox("Display Error: Customer Does Not Exist.\nThe customer for this transaction may have been deleted from the database.",
							"Display Error: Customer Does Not Exist");
					return;
				}
				customer = cusList.get(0);
				CustomerView dialog = new CustomerView(customer, true);
				dialog.setVisible(true);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private class ViewEmployeeDetailButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			int employeeId = transaction.getEmployeeId();
			ArrayList<Employee> empList = new ArrayList<Employee>();
			Employee employee = new Employee();
			EmployeeController controller = new EmployeeController();
			try {
				empList = controller.selectWhere(String.format("id = %d", employeeId), 0, 1);
				if(empList.isEmpty())
				{
					MessageBoxView.infoBox("Display Error: Employee Does Not Exist.\nThe employee for this transaction may have been deleted from the database.",
							"Display Error: Employee Does Not Exist");
					return;
				}
				employee = empList.get(0);
				EmployeeView dialog = new EmployeeView(employee, true);
				dialog.setVisible(true);
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}

		}
	}
	
	private class OrderItemTableModel implements TableModel 
	{
	   
	   String header[] = new String[] { "Item ID", "Item Name", "Item Code", "Amount"};
	    
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
		   	case 3: return amountList.get(rowIndex);
		   }
	        return "";
	    }    
	    
	    public boolean isCellEditable(int rowIndex, int columnIndex) 
	    {
	        return false;
	    }
	    
	    public void setValueAt(Object value, int row, int column) {}

	    public void addTableModelListener(TableModelListener l) {}

	    public void removeTableModelListener(TableModelListener l) {}  
	}



	


}
