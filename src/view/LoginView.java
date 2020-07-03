package view;

import controller.EmployeeController;
import model.Employee;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class LoginView extends JDialog {

	/**
	 * 
	 */
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUsername;
	private JPasswordField pfPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoginView dialog = new LoginView();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoginView() {
		setResizable(false);
		setBounds(100, 100, 269, 188);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblLoginScreen = new JLabel("Login Screen");
		lblLoginScreen.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblLoginScreen.setBounds(77, 10, 104, 22);
		contentPanel.add(lblLoginScreen);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUsername.setBounds(10, 51, 68, 13);
		contentPanel.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPassword.setBounds(10, 86, 58, 13);
		contentPanel.add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
		txtUsername.setBounds(77, 49, 170, 19);
		contentPanel.add(txtUsername);
		txtUsername.setColumns(10);
		
		pfPassword = new JPasswordField();
		pfPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pfPassword.setBounds(77, 84, 170, 19);
		contentPanel.add(pfPassword);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				{
					JButton okButton = new JButton("OK");
					okButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
					okButton.setActionCommand("OK");
					okButton.addActionListener(new OKButtonListener());
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
					cancelButton.setActionCommand("Cancel");
					cancelButton.addActionListener(new CancelButtonListener());
					buttonPane.add(cancelButton);
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);
				}
			}
		}
	}
	
	private class OKButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			EmployeeController empController = new EmployeeController();
			try {
				boolean result = empController.validateLogin(txtUsername.getText(),  new String(pfPassword.getPassword()));
				if (result)
				{
					Employee emp = empController.selectWhere(String.format("username = '%s'", txtUsername.getText()), 0, 1).get(0);
					MainPanelView frame = new MainPanelView(emp);
					frame.setVisible(true);
					LoginView.this.dispose();
				}
				else
					MessageBoxView.infoBox("Login Error: Incorrect Username or Password!\nPlease check your username and password and try again!",
							"Login Error: Incorrect Username or Password");
			} catch (ClassNotFoundException e1) {
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
			LoginView.this.dispose();
		}
	}
}
