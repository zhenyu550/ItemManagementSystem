package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import exception.InputException;
import model.Item;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

public class AddTransactionItemView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtItemId;
	private JTextField txtName;
	private JTextField txtCode;
	private JTextField txtPrice;
	private	JSpinner spinner;
	private boolean selected = false;
	private boolean valid = false;
	private Item selectedItem = new Item();
	private int itemAmount = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddTransactionItemView dialog = new AddTransactionItemView(null, 0);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddTransactionItemView(Item data, int amount) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 442, 271);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPanel.add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblTitle = new JLabel("Item Transaction");
		if(data == null)
			lblTitle.setText("Add Item");
		else
			lblTitle.setText("Edit Amount");
		
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 15));
		panelNorth.add(lblTitle);
		
		JPanel panelSouth = new JPanel();
		contentPanel.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnCancel.addActionListener(new CancelButtonListener());
		panelSouth.add(btnCancel);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnConfirm.addActionListener(new ConfirmButtonListener());
		panelSouth.add(btnConfirm);
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblItemId = new JLabel("Item Id: ");
		lblItemId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblItemId.setBounds(10, 10, 56, 13);
		panel.add(lblItemId);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setBounds(10, 39, 46, 13);
		panel.add(lblName);
		
		JLabel lblCode = new JLabel("Code");
		lblCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCode.setBounds(10, 68, 46, 13);
		panel.add(lblCode);
		
		JLabel lblUnitPrice = new JLabel("Unit Price");
		lblUnitPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnitPrice.setBounds(10, 103, 65, 13);
		panel.add(lblUnitPrice);
		
		JLabel lblAmount = new JLabel("Amount: ");
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAmount.setBounds(10, 136, 56, 13);
		panel.add(lblAmount);
		
		txtItemId = new JTextField();
		txtItemId.setEditable(false);
		txtItemId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtItemId.setBounds(85, 8, 210, 19);
		panel.add(txtItemId);
		txtItemId.setColumns(10);
		
		txtName = new JTextField();
		txtName.setEditable(false);
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtName.setEnabled(true);
		txtName.setText("");
		txtName.setBounds(85, 37, 210, 19);
		panel.add(txtName);
		txtName.setColumns(10);
		
		txtCode = new JTextField();
		txtCode.setEditable(false);
		txtCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtCode.setBounds(85, 66, 210, 19);
		panel.add(txtCode);
		txtCode.setColumns(10);
		
		txtPrice = new JTextField();
		txtPrice.setEditable(false);
		txtPrice.setEnabled(true);
		txtPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtPrice.setText("");
		txtPrice.setBounds(85, 100, 210, 19);
		panel.add(txtPrice);
		txtPrice.setColumns(10);
		
		spinner = new JSpinner();
		spinner.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinner.setBounds(85, 134, 210, 20);
		spinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
		JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		NumberFormatter formatter = (NumberFormatter) txt.getFormatter(); 
		DecimalFormat decimalFormat = new DecimalFormat("0"); 
		formatter.setFormat(decimalFormat); 
		formatter.setAllowsInvalid(false);
		panel.add(spinner);
		
		JButton btnSelectItem = new JButton("Select Item");
		btnSelectItem.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSelectItem.setBounds(308, 10, 102, 144);
		btnSelectItem.addActionListener(new SelectItemButtonListener());
		if(data != null)
		{	
			selectedItem = data;
			itemAmount = amount;
			loadItemData();
			btnSelectItem.setEnabled(false); 
		}
		panel.add(btnSelectItem);
	}
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public boolean isValid()
	{
		return valid;
	}
	
	public Item getSelectedItem()
	{
		return selectedItem;
	}
	
	public int getItemAmount()
	{
		return itemAmount;
	}
	
	private void loadItemData()
	{
		txtItemId.setText(Integer.toString(selectedItem.getId()));
		txtName.setText(selectedItem.getName());
		txtCode.setText(selectedItem.getCode());
		txtPrice.setText(String.format("%.2f", selectedItem.getPrice()));
		if(itemAmount > 0)
			spinner.setValue(itemAmount);
	}
	
	private class CancelButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			AddTransactionItemView.this.dispose();
		}
	}

	private class ConfirmButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(selectedItem.getId() > 0)
			{
				selected = true;
				itemAmount = (int)spinner.getValue(); 
				ItemController controller = new ItemController();
				try {
					valid = controller.checkItemQuantity(selectedItem, itemAmount);
					AddTransactionItemView.this.dispose();
				} catch (InputException e1) {
					e1.displayMessage();
				}
			}
			else
				MessageBoxView.infoBox("Input Error: No Item Selected.\nYou must select an item to proceed.", "Input Error: No Item Selected.");
		}
	}
	
	private class SelectItemButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			selectedItem = new Item();
			SelectItemView dialog = new SelectItemView();
			dialog.setVisible(true);
			if(dialog.isSelected())
			{
				selected = true;
				selectedItem = dialog.getSelectedItem();
				loadItemData();
			}
		}
	}
}
