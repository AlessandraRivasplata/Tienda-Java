package view;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import java.awt.event.WindowAdapter;


import main.*;
import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class CashView extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldMoney;
	private Shop shop;// instancia la clase shop

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CashView dialog = new CashView();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CashView() {
		
		shop=new Shop(); // instancia de la clase shop
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			textFieldMoney = new JTextField();
			textFieldMoney.setEditable(false);
			textFieldMoney.setBounds(174, 116, 100, 26);
			contentPanel.add(textFieldMoney);
			textFieldMoney.setColumns(10);
		}
		textFieldMoney.setText(String.valueOf(shop.showCash()));
		{
			JLabel lblNewLabel = new JLabel("Dinero en caja");
			lblNewLabel.setBounds(104, 88, 90, 16);
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(e ->{
					dispose();
			     });
	            }
	        }
		   addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                
	                System.out.println(" FUNCIONA cerrado");
	            }
	        });
	    }
}
	


