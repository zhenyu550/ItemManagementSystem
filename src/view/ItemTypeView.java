package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import controller.ItemTypeController;
import exception.InputException;
import model.ItemType;

public class ItemTypeView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextField txtCode;
	private boolean updated = false;
	private ItemType data = new ItemType();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ItemTypeView dialog = new ItemTypeView(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemTypeView(ItemType data) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 300, 185);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panelNorth = new JPanel();
		contentPanel.add(panelNorth, BorderLayout.NORTH);
		
		JLabel lblTitle = new JLabel("Item Type");
		if(data == null)
			lblTitle.setText("Add Item Type");
		else
		{
			lblTitle.setText("Edit Item Type");
			this.data = data;
		}
		panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelNorth.add(lblTitle);
		
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
		
		JPanel panelCenter = new JPanel();
		contentPanel.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(null);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblName.setBounds(10, 10, 46, 13);
		panelCenter.add(lblName);
		
		JLabel lblCode = new JLabel("Code: ");
		lblCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCode.setBounds(10, 41, 66, 13);
		panelCenter.add(lblCode);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtName.setBounds(55, 7, 215, 19);
		panelCenter.add(txtName);
		txtName.setColumns(10);
		
		txtCode = new JTextField();
		txtCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtCode.setBounds(55, 38, 215, 19);
		panelCenter.add(txtCode);
		txtCode.setColumns(10);
		
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
	}
	
	private class SaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			data.setName(txtName.getText());
			data.setCode(txtCode.getText());
			
			ItemTypeController controller = new ItemTypeController();
			int result;
			try {
				if(data.getId() == 0)
					result = controller.insert(data);
				else
					result = controller.update(data);
				if (result == 1)
				{	
					updated = true;
					ItemTypeView.this.dispose();
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
			ItemTypeView.this.dispose();
		}
	}

}
