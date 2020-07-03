package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemTypeController;
import exception.InputException;
import model.Item;
import model.ItemType;

public class ItemView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextField txtCode;
	private JSpinner spinnerPrice;
	private JSpinner spinnerQuantity;
	private JComboBox comboBoxItemType;
	private JTextArea taDescription;
	private ArrayList<ItemType> iTypeList;
	private Item data = new Item();
	private boolean updated = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ItemView dialog = new ItemView(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemView(Item data) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 330, 410);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelSouth = new JPanel();
		contentPanel.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton btnCancelButton = new JButton("Cancel");
		btnCancelButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelButton.addActionListener(new CancelButtonListener());
		panelSouth.add(btnCancelButton);
		
		JButton btnSaveButton = new JButton("Save");
		btnSaveButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSaveButton.addActionListener(new SaveButtonListener());
		panelSouth.add(btnSaveButton);
		
		JPanel panelNorth = new JPanel();
		contentPanel.add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblTitle = new JLabel("Item");
		if (data == null)
			lblTitle.setText("Add Item");
		else
		{
			lblTitle.setText("Edit item");
			this.data = data;
		}
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelNorth.add(lblTitle);
		
		JPanel panelCenter = new JPanel();
		contentPanel.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setBounds(10, 10, 46, 13);
		panelCenter.add(lblName);
		
		JLabel lblCode = new JLabel("Code:");
		lblCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCode.setBounds(10, 41, 46, 13);
		panelCenter.add(lblCode);
		
		JLabel lblItemType = new JLabel("Item Type: ");
		lblItemType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblItemType.setBounds(10, 71, 71, 13);
		panelCenter.add(lblItemType);
		
		JLabel lblPrice = new JLabel("Price: ");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(10, 102, 46, 13);
		panelCenter.add(lblPrice);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQuantity.setBounds(10, 132, 64, 13);
		panelCenter.add(lblQuantity);
		
		JLabel lblDescription = new JLabel("Description: ");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescription.setBounds(10, 161, 71, 13);
		panelCenter.add(lblDescription);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtName.setBounds(91, 7, 200, 19);
		panelCenter.add(txtName);
		txtName.setColumns(10);
		
		txtCode = new JTextField();
		txtCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtCode.setBounds(91, 38, 200, 19);
		panelCenter.add(txtCode);
		txtCode.setColumns(10);
		
		spinnerPrice = new JSpinner();
		spinnerPrice.setModel(new SpinnerNumberModel(0.00, 0.00, null, 1.00));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinnerPrice,"0.00");
		spinnerPrice.setEditor(editor);
		JFormattedTextField txt = ((JSpinner.NumberEditor) spinnerPrice.getEditor()).getTextField();
		NumberFormatter formatter = (NumberFormatter) txt.getFormatter(); 
		DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
		formatter.setFormat(decimalFormat); 
		formatter.setAllowsInvalid(false);
		spinnerPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerPrice.setBounds(91, 98, 200, 20);
		panelCenter.add(spinnerPrice);
		
		spinnerQuantity = new JSpinner();
		spinnerQuantity.setModel(new SpinnerNumberModel(0, 0, null, 1));
		txt = ((JSpinner.NumberEditor) spinnerQuantity.getEditor()).getTextField();
		formatter = (NumberFormatter) txt.getFormatter(); 
		decimalFormat = new DecimalFormat("0"); 
		formatter.setFormat(decimalFormat); 
		formatter.setAllowsInvalid(false);
		spinnerQuantity.setFont(new Font("Tahoma", Font.PLAIN, 12));
		spinnerQuantity.setBounds(91, 128, 200, 20);
		panelCenter.add(spinnerQuantity);
		
		comboBoxItemType = new JComboBox();
			iTypeList = new ArrayList<ItemType>();
			ItemTypeController iTypeController = new ItemTypeController();
			try {
				iTypeList = iTypeController.selectAll(0, 2147483467);
				for(ItemType iType : iTypeList)
				{	
		comboBoxItemType.addItem(iType.getName());
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		comboBoxItemType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBoxItemType.setBounds(91, 67, 200, 21);
		panelCenter.add(comboBoxItemType);
		
		taDescription = new JTextArea();
		taDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		taDescription.setBounds(91, 158, 200, 120);
		taDescription.setLineWrap(true);
		panelCenter.add(taDescription);
		
		if(data != null)
			loadData();
	}
	
	public boolean confirmUpdate()
	{
		return updated;
	}
	
	public void loadData()
	{
		txtName.setText(data.getName());
		txtCode.setText(data.getCode());
		spinnerPrice.setValue(data.getPrice());
		spinnerQuantity.setValue(data.getQuantity());
		ItemTypeController itemTypeController = new ItemTypeController();
		ItemType itemType = new ItemType();
		try {
			itemType = itemTypeController.selectWhere(String.format("id = %d", data.getTypeId()), 0, 1).get(0);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comboBoxItemType.setSelectedItem(itemType.getName());
		taDescription.setText(data.getDescription());
	}
	
	private class SaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			int typeId = -1, selectedIndex = comboBoxItemType.getSelectedIndex();
			if(selectedIndex >= 0)
				typeId = iTypeList.get(selectedIndex).getId();

			data.setName(txtName.getText());
			data.setCode(txtCode.getText());
			data.setPrice((double)spinnerPrice.getValue());
			data.setQuantity((int)spinnerQuantity.getValue());
			data.setDescription(taDescription.getText());
			data.setTypeId(typeId);
			
			ItemController controller = new ItemController();
			int result;
			try {
				if(data.getId() == 0)
					result = controller.insert(data);
				else
					result = controller.update(data);
				if (result == 1)
				{
					updated = true;
					ItemView.this.dispose();
				}
			} catch (InputException e1) {
				e1.displayMessage();
			}
			catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	private class CancelButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			ItemView.this.dispose();
		}
	}
}
