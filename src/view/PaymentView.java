package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;

import controller.TransactionController;
import exception.InputException;
import model.Customer;
import model.Employee;
import model.Item;
import model.Transaction;
import model.TransactionItem;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;

public class PaymentView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtTotal;
	private JTextField txtChange;
	private JPanel panelSouth;
	private JButton btnClose;
	private JButton btnConfirm;
	private JSpinner spinner;
	
	private Customer customer = new Customer();
	private Employee employee = new Employee();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Integer> amounts = new ArrayList<Integer>();
	private double totalPrice = 0;
	private boolean added = false;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PaymentView dialog = new PaymentView(null, null, null, null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PaymentView(Customer customer, Employee employee, ArrayList<Item> items, ArrayList<Integer> amounts) {
		setResizable(false);
		initComponents();
		this.customer = customer;
		this.employee = employee;
		this.items = items;
		this.amounts = amounts;
		this.totalPrice = new TransactionController().calculateTotal(items, amounts);
		txtTotal.setText(String.format("%.2f", totalPrice));
	}
	
	public void initComponents()
	{
		setModal(true);
		setBounds(100, 100, 312, 247);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelNorth = new JPanel();
			contentPanel.add(panelNorth, BorderLayout.NORTH);
			panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lblTitle = new JLabel("Payment");
				lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
				panelNorth.add(lblTitle);
			}
		}
		{
			panelSouth = new JPanel();
			contentPanel.add(panelSouth, BorderLayout.SOUTH);
			{
				btnClose = new JButton("Close");
				btnClose.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnClose.addActionListener(new CloseButtonListener());
				panelSouth.add(btnClose);
				
				btnConfirm = new JButton("Confirm");
				btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnConfirm.addActionListener(new ConfirmButtonListener());								
				panelSouth.add(btnConfirm);
			}
		}
		{
			JPanel panelCenter = new JPanel();
			contentPanel.add(panelCenter, BorderLayout.CENTER);
			panelCenter.setLayout(null);
			
			JLabel lblTotal = new JLabel("Total: ");
			lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblTotal.setBounds(10, 10, 46, 19);
			panelCenter.add(lblTotal);
			
			JLabel lblPayment = new JLabel("Payment: ");
			lblPayment.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblPayment.setBounds(10, 50, 74, 19);
			panelCenter.add(lblPayment);
			
			JLabel lblChange = new JLabel("Change: ");
			lblChange.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblChange.setBounds(10, 94, 74, 19);
			panelCenter.add(lblChange);
			
			txtTotal = new JTextField();
			txtTotal.setHorizontalAlignment(SwingConstants.TRAILING);
			txtTotal.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtTotal.setEditable(false);
			txtTotal.setBounds(92, 7, 180, 26);
			panelCenter.add(txtTotal);
			txtTotal.setColumns(10);
			
			txtChange = new JTextField();
			txtChange.setText("0.00");
			txtChange.setHorizontalAlignment(SwingConstants.TRAILING);
			txtChange.setFont(new Font("Tahoma", Font.PLAIN, 15));
			txtChange.setEditable(false);
			txtChange.setBounds(92, 91, 180, 26);
			panelCenter.add(txtChange);
			txtChange.setColumns(10);
			
			spinner = new JSpinner();
			spinner.setModel(new SpinnerNumberModel(0.00, 0.00, null, 1.00));
			JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner,"0.00");
			spinner.setEditor(editor);
			JFormattedTextField txt = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
			NumberFormatter formatter = (NumberFormatter) txt.getFormatter(); 
			DecimalFormat decimalFormat = new DecimalFormat("0.00"); 
			formatter.setFormat(decimalFormat); 
			formatter.setAllowsInvalid(false);
			spinner.setFont(new Font("Tahoma", Font.PLAIN, 15));
			spinner.setBounds(94, 47, 180, 26);
			panelCenter.add(spinner);
		}
	}
	
	public boolean isAdded()
	{
		return added;
	}
	
	private class ConfirmButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			TransactionController controller = new TransactionController();
			try {
				double change = controller.calculateChange(totalPrice, (double)spinner.getValue());
				txtChange.setText(Double.toString(change));
				btnConfirm.setEnabled(false);
				added = controller.addTransaction(customer, employee, items, amounts, totalPrice);
			}catch (InputException e1) {
				e1.displayMessage();
			}
		}
	}
	
	private class CloseButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			PaymentView.this.dispose();
		}
	}
}
