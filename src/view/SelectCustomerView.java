package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.CustomerController;
import model.Customer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SelectCustomerView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtSearch;
	private JTable table;
	private ArrayList<Customer> cusList = new ArrayList<Customer>();
	private JComboBox comboBoxSearchCond;
	private JComboBox comboBoxStrCond;
	private boolean selected = false;
	private Customer selectedCustomer = new Customer();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SelectCustomerView dialog = new SelectCustomerView();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SelectCustomerView() {
		setResizable(false);
		initComponent();
		reloadCustomerTable("");
	}
	
	public void initComponent()
	{
		setModal(true);
		setBounds(100, 100, 745, 395);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelNorth = new JPanel();
			contentPanel.add(panelNorth, BorderLayout.NORTH);
			panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			JLabel lblSelectCustomer = new JLabel("Select Customer");
			lblSelectCustomer.setFont(new Font("Tahoma", Font.BOLD, 15));
			panelNorth.add(lblSelectCustomer);
		}
		{
			JPanel panelSouth = new JPanel();
			contentPanel.add(panelSouth, BorderLayout.SOUTH);
			panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnCancel.addActionListener(new CancelButtonListener());
				panelSouth.add(btnCancel);
			}
			{
				JButton btnSelect = new JButton("Select");
				btnSelect.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnSelect.addActionListener(new SelectButtonListener());
				panelSouth.add(btnSelect);
			}
		}
		{
			JPanel panelCenter = new JPanel();
			contentPanel.add(panelCenter, BorderLayout.CENTER);
			panelCenter.setLayout(new BorderLayout(0, 0));
			{
				JPanel panelCenterNorth = new JPanel();
				panelCenter.add(panelCenterNorth, BorderLayout.NORTH);
				JLabel label = new JLabel("Search by:");
				label.setFont(new Font("Tahoma", Font.PLAIN, 12));
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
				
				JButton btnSearch = new JButton("Search");
				btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
				btnSearch.addActionListener(new SearchCustomerButtonListener());
				
				JButton btnReset = new JButton("Reset");
				btnReset.setFont(new Font("Tahoma", Font.PLAIN, 12));
				btnReset.addActionListener(new ResetCustomerButtonListener());
				
				GroupLayout gl_panelCenterNorth = new GroupLayout(panelCenterNorth);
				gl_panelCenterNorth.setHorizontalGroup(
					gl_panelCenterNorth.createParallelGroup(Alignment.LEADING)
						.addGap(0, 721, Short.MAX_VALUE)
						.addGroup(gl_panelCenterNorth.createSequentialGroup()
							.addContainerGap()
							.addComponent(label)
							.addGap(10)
							.addComponent(comboBoxSearchCond, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBoxStrCond, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSearch)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnReset)
							.addGap(12))
				);
				gl_panelCenterNorth.setVerticalGroup(
					gl_panelCenterNorth.createParallelGroup(Alignment.LEADING)
						.addGap(0, 28, Short.MAX_VALUE)
						.addGroup(gl_panelCenterNorth.createSequentialGroup()
							.addGap(5)
							.addGroup(gl_panelCenterNorth.createParallelGroup(Alignment.BASELINE)
								.addComponent(label)
								.addComponent(comboBoxStrCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(comboBoxSearchCond, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnSearch)
								.addComponent(btnReset)))
				);
				panelCenterNorth.setLayout(gl_panelCenterNorth);
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				panelCenter.add(scrollPane, BorderLayout.CENTER);
				{
					table = new JTable();
					scrollPane.setViewportView(table);
					table.setModel(new CustomerTableModel());
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				}
			}
		}
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public Customer getSelectedCustomer()
	{
		return selectedCustomer;
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
			table.repaint();
			table.revalidate();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	
	private class SelectButtonListener implements ActionListener
	{		
		public void actionPerformed(ActionEvent e)
		{
	    	int index = table.getSelectedRow();
	    	if(index < 0)
	    		return;
    		int id = (int) table.getValueAt(index, 0);	 
    		String condition = String.format("id = %d", id);
    		CustomerController cusController = new CustomerController();
    		try {
    			selectedCustomer = cusController.selectWhere(condition, 0, 1).get(0);
    			selected = true;
    			SelectCustomerView.this.dispose();
    		} catch (ClassNotFoundException | SQLException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}	    
		}
	}	
	
	private class CancelButtonListener implements ActionListener
	{		
		public void actionPerformed(ActionEvent e)
		{
			SelectCustomerView.this.dispose();
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

}
