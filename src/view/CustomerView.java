package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import controller.CustomerController;
import exception.InputException;
import model.Customer;

public class CustomerView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextField txtIc;
	private JTextField txtContactNo;
	private JTextField txtEmail;
	private JButton btnSave;
	private boolean updated = false;
	private boolean viewOnly = false;
	private Customer data = new Customer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CustomerView dialog = new CustomerView(null, false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CustomerView(Customer data, boolean viewOnly) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 355, 243);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelSouth = new JPanel();
			contentPanel.add(panelSouth, BorderLayout.SOUTH);
			panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
				btnCancel.addActionListener(new CancelButtonListener());
				panelSouth.add(btnCancel);
			}
			{
				btnSave = new JButton("Save");
				btnSave.setFont(new Font("Tahoma", Font.PLAIN, 12));
				btnSave.addActionListener(new SaveButtonListener());
				panelSouth.add(btnSave);
			}
		}
		{
			JPanel panelNorth = new JPanel();
			contentPanel.add(panelNorth, BorderLayout.NORTH);
			JLabel lblTitle = new JLabel("Customer"); 
			if(data == null)
				lblTitle.setText("Add Customer");
			else
			{
				this.viewOnly = viewOnly;
				this.data = data;
				if(!viewOnly)
					lblTitle.setText("Edit Customer");
				else
					lblTitle.setText("View Customer Detail");
			}
			panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
			panelNorth.add(lblTitle);
		}
		{
			JPanel panelCenter = new JPanel();
			panelCenter.setLayout(null);
			contentPanel.add(panelCenter, BorderLayout.CENTER);
			{
				JLabel lblName = new JLabel("Name: ");
				lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));
				lblName.setBounds(10, 10, 46, 13);
				panelCenter.add(lblName);
			}
			{
				JLabel lblIc = new JLabel("IC Number: ");
				lblIc.setFont(new Font("Tahoma", Font.PLAIN, 12));
				lblIc.setBounds(10, 41, 66, 13);
				panelCenter.add(lblIc);
			}
			{
				JLabel lblContactNo = new JLabel("Contact Number: ");
				lblContactNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
				lblContactNo.setBounds(10, 72, 99, 13);
				panelCenter.add(lblContactNo);
			}
			{
				JLabel lblEmail = new JLabel("Email: ");
				lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
				lblEmail.setBounds(10, 103, 46, 13);
				panelCenter.add(lblEmail);
			}
			{
				txtName = new JTextField();
				txtName.setFont(new Font("Tahoma", Font.PLAIN, 12));
				txtName.setColumns(10);
				txtName.setBounds(108, 7, 215, 19);
				panelCenter.add(txtName);
			}
			{
				txtIc = new JTextField();
				txtIc.setFont(new Font("Tahoma", Font.PLAIN, 12));
				txtIc.setColumns(10);
				txtIc.setBounds(108, 38, 215, 19);
				panelCenter.add(txtIc);
			}
			{
				txtContactNo = new JTextField();
				txtContactNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
				txtContactNo.setColumns(10);
				txtContactNo.setBounds(108, 69, 215, 19);
				panelCenter.add(txtContactNo);
			}
			{
				txtEmail = new JTextField();
				txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
				txtEmail.setColumns(10);
				txtEmail.setBounds(108, 100, 215, 19);
				panelCenter.add(txtEmail);
			}
		}
		
		if(data != null)
			loadData();
		if(viewOnly)
			disableEdit();
	}

	private void loadData()
	{
		txtName.setText(data.getName());
		txtIc.setText(data.getIc());
		txtContactNo.setText(data.getContactNo());
		txtEmail.setText(data.getEmail());
	}
	
	private void disableEdit()
	{
		txtName.setEditable(false);
		txtIc.setEditable(false);
		txtContactNo.setEditable(false);
		txtEmail.setEditable(false);
		btnSave.setEnabled(false);;
	}
	
	private class SaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			data.setName(txtName.getText());
			data.setIc(txtIc.getText());
			data.setContactNo(txtContactNo.getText());
			data.setEmail(txtEmail.getText());
			CustomerController controller = new CustomerController();
			int result = -1;
			try {
				if(data.getId() == 0)
					result = controller.insert(data);
				else
					result = controller.update(data);
				if (result == 1)
				{	
					updated = true;
					CustomerView.this.dispose();
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
			CustomerView.this.dispose();
		}
	}
	
	public boolean confirmUpdate()
	{
		return updated;
	}

}
