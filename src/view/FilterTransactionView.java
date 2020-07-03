package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import controller.TransactionController;
import other.DateLabelFormatter;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Properties;

public class FilterTransactionView extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JDatePickerImpl datePickerStart;
	private JDatePickerImpl datePickerEnd;
	private String condition = "";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FilterTransactionView dialog = new FilterTransactionView();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FilterTransactionView() {
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 309, 194);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panelNorth = new JPanel();
			contentPanel.add(panelNorth, BorderLayout.NORTH);
			panelNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lblFilterTransaction = new JLabel("Filter Transaction");
				lblFilterTransaction.setFont(new Font("Tahoma", Font.BOLD, 18));
				panelNorth.add(lblFilterTransaction);
			}
		}
		{
			JPanel panelSouth = new JPanel();
			contentPanel.add(panelSouth, BorderLayout.SOUTH);
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnCancel.addActionListener(new CancelButtonListener());
				panelSouth.add(btnCancel);
			}
			{
				JButton btnConfirm = new JButton("Confirm");
				btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
				btnConfirm.addActionListener(new ConfirmButtonListener());
				panelSouth.add(btnConfirm);
			}
		}
		{
			JPanel panelCenter = new JPanel();
			contentPanel.add(panelCenter, BorderLayout.CENTER);
			panelCenter.setLayout(null);
			
	        Properties p = new Properties();
	        p.put("text.today", "Today");
	        p.put("text.month", "Month");
	        p.put("text.year", "Year");
	        
	        TransactionController controller = new TransactionController();
	        
	        UtilDateModel modelStart = new UtilDateModel();
	        modelStart.setDate(1990, 8, 24);
	        modelStart.setSelected(true);
	        LocalDateTime firstDate = controller.getFirstDate();
	        if(firstDate != null)
	        	modelStart.setDate(firstDate.getYear(), firstDate.getMonthValue()-1, firstDate.getDayOfMonth());

	        JDatePanelImpl datePanelStart = new JDatePanelImpl(modelStart, p);

	        datePickerStart = new JDatePickerImpl(datePanelStart, new DateLabelFormatter());
	        datePickerStart.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 12));
			datePickerStart.setBounds(76, 10, 202, 31);
			panelCenter.add(datePickerStart);
			
			LocalDateTime todayDate = LocalDateTime.now(); 
			int day = todayDate.getDayOfMonth();
			int month = todayDate.getMonthValue();
			int year = todayDate.getYear();
			
	        UtilDateModel modelEnd = new UtilDateModel();
	        modelEnd.setDate(year, month-1, day); //January at index 0, February at index 1,..., December at index 11
	        modelEnd.setSelected(true);

	        JDatePanelImpl datePanelEnd = new JDatePanelImpl(modelEnd, p);

	        datePickerEnd = new JDatePickerImpl(datePanelEnd, new DateLabelFormatter());
	        datePickerEnd.getJFormattedTextField().setFont(new Font("Tahoma", Font.PLAIN, 12));
			datePickerEnd.setBounds(76, 51, 202, 31);
			panelCenter.add(datePickerEnd);

			JLabel lblStartFrom = new JLabel("Start From:  ");
			lblStartFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblStartFrom.setBounds(10, 18, 73, 13);
			panelCenter.add(lblStartFrom);
			
			JLabel lblEndAt = new JLabel("End At: ");
			lblEndAt.setFont(new Font("Tahoma", Font.PLAIN, 12));
			lblEndAt.setBounds(10, 57, 46, 13);
			panelCenter.add(lblEndAt);
		}
	}
	
	public String getConditionString()
	{
		return condition;
	}
	
	private class ConfirmButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Date startDateValue = (Date) datePickerStart.getModel().getValue();
			Date endDateValue = (Date) datePickerEnd.getModel().getValue();
    		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    		String startDateStr = formatter.format(startDateValue);
    		String endDateStr = formatter.format(endDateValue);
    		
    		if(endDateValue.before(startDateValue))
    		{
    			MessageBoxView.infoBox("Input Error: Invalid Date Range.\n'End At' date must be equal or later than 'Start By' date.",
    					"Input Error: Invalid Date Range");
    			return;
    		}
    		else
    		{
    			condition = "date_time BETWEEN '%s 00:00:00' AND '%s 23:59:59'";
    			condition = String.format(condition, startDateStr, endDateStr);
    			FilterTransactionView.this.dispose();
    		}

		}
	}
	
	private class CancelButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			FilterTransactionView.this.dispose();
		}
	}

}
