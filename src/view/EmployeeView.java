package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controller.EmployeeController;
import exception.InputException;
import model.Employee;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.SwingConstants;

public class EmployeeView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtName;
	private JTextField txtIc;
	private JTextField txtContactNo;
	private JTextField txtEmail;
	private JTextField txtUsername;
	private JTextArea taAddress;
	private JPasswordField pfPassword1;
	private JPasswordField pfPassword2;
	private JComboBox comboBox;
	private JButton btnSaveButton;
	private Employee data = new Employee();
	private boolean updated = false;
	private boolean viewOnly = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EmployeeView dialog = new EmployeeView(null, false);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EmployeeView(Employee data, boolean viewOnly) {
		setResizable(false);
		setModal(true);
		setBounds(100, 100, 625, 320);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelNorth = new JPanel();
			contentPanel.add(panelNorth, BorderLayout.NORTH);
			
			JLabel lblTitle = new JLabel("Employee");
			if(data == null)
				lblTitle.setText("Add Employee");
			else
			{
				this.viewOnly = viewOnly;
				this.data = data;
				if(!viewOnly)
					lblTitle.setText("Edit Employee");
				else
					lblTitle.setText("View Employee Detail");
			}
			panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
			panelNorth.add(lblTitle);
		}
						
		JPanel panelSouth = new JPanel();
		contentPanel.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnCancelButton = new JButton("Cancel");
		btnCancelButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancelButton.addActionListener(new CancelButtonListener());
		panelSouth.add(btnCancelButton);
		
		btnSaveButton = new JButton("Save");
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
		
		JLabel lblIcNumber = new JLabel("IC Number: ");
		lblIcNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIcNumber.setBounds(10, 41, 66, 13);
		panelCenter.add(lblIcNumber);
		
		JLabel lblContactNumber = new JLabel("Contact Number: ");
		lblContactNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblContactNumber.setBounds(10, 72, 99, 13);
		panelCenter.add(lblContactNumber);
		
		JLabel lblEmail = new JLabel("Email: ");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEmail.setBounds(10, 103, 46, 13);
		panelCenter.add(lblEmail);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtName.setBounds(108, 7, 180, 19);
		panelCenter.add(txtName);
		txtName.setColumns(10);
		
		txtIc = new JTextField();
		txtIc.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtIc.setBounds(108, 38, 180, 19);
		panelCenter.add(txtIc);
		txtIc.setColumns(10);
		
		txtContactNo = new JTextField();
		txtContactNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtContactNo.setBounds(108, 69, 180, 19);
		panelCenter.add(txtContactNo);
		txtContactNo.setColumns(10);
		
		txtEmail = new JTextField();
		txtEmail.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtEmail.setBounds(108, 100, 180, 19);
		panelCenter.add(txtEmail);
		txtEmail.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsername.setBounds(316, 11, 66, 13);
		panelCenter.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password (PW):");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(316, 42, 85, 13);
		panelCenter.add(lblPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm PW:");
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblConfirmPassword.setBounds(316, 73, 99, 13);
		panelCenter.add(lblConfirmPassword);
		
		JLabel lblLevel = new JLabel("Level: ");
		lblLevel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLevel.setBounds(316, 103, 46, 13);
		panelCenter.add(lblLevel);
		
		JLabel lblHomeAddress = new JLabel("Home Address:");
		lblHomeAddress.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblHomeAddress.setBounds(10, 138, 85, 13);
		panelCenter.add(lblHomeAddress);
		
		taAddress = new JTextArea();
		taAddress.setFont(new Font("Tahoma", Font.PLAIN, 12));
		taAddress.setBounds(108, 132, 486, 66);
		taAddress.setLineWrap(true);
		panelCenter.add(taAddress);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(414, 8, 180, 19);
		panelCenter.add(txtUsername);
		txtUsername.setColumns(10);
		
		pfPassword1 = new JPasswordField();
		pfPassword1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pfPassword1.setBounds(414, 38, 180, 19);
		panelCenter.add(pfPassword1);
		
		pfPassword2 = new JPasswordField();
		pfPassword2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pfPassword2.setBounds(414, 69, 180, 19);
		panelCenter.add(pfPassword2);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(414, 99, 180, 21);
		comboBox.addItem("Staff");
		comboBox.addItem("Admin");
		panelCenter.add(comboBox);
		
		if(data != null)
			loadData();
		
		if(viewOnly)
			disableEdit();
	}
	
	public boolean confirmUpdate()
	{
		return updated;
	}
	
	private void loadData()
	{
		txtName.setText(data.getName());
		txtIc.setText(data.getIc());
		txtContactNo.setText(data.getContactNo());
		txtEmail.setText(data.getEmail());
		txtUsername.setText(data.getUsername());
		taAddress.setText(data.getAddress());
		comboBox.setSelectedItem(data.getLevel());
	}
	
	private void disableEdit()
	{
		txtName.setEditable(false);
		txtIc.setEditable(false);
		txtContactNo.setEditable(false);
		txtEmail.setEditable(false);
		txtUsername.setEditable(false);
		taAddress.setEditable(false);
		pfPassword1.setEditable(false);
		pfPassword2.setEditable(false);
		comboBox.setEnabled(false);
		btnSaveButton.setEnabled(false);
	}
	
	private class SaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String password1 = new String(pfPassword1.getPassword());
			String password2 = new String(pfPassword2.getPassword());
			
			if(!password1.equals(password2))
			{	
				MessageBoxView.infoBox("Input Error: Password Not Same.\nPlease confirm that the password in 'Password (PW)' and 'Confirm PW' are the same.", 
						"Input Error: Password Not Same");
				return;
			}
			data.setName(txtName.getText());
			data.setIc(txtIc.getText());
			data.setContactNo(txtContactNo.getText());
			data.setEmail(txtEmail.getText());
			data.setAddress(taAddress.getText());
			data.setUsername(txtUsername.getText());
			data.setPassword(new String(pfPassword1.getPassword()));
			data.setLevel((String) comboBox.getSelectedItem());
					
			EmployeeController controller = new EmployeeController();
			int result;
			try {
				if(data.getId() == 0)
					result = controller.insert(data);
				else
					result = controller.update(data);
				if (result == 1)
				{
					updated = true;
					EmployeeView.this.dispose();
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
			EmployeeView.this.dispose();
		}
	}

}

