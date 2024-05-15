package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.*;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class LoginView extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	private JButton btnNewButton;
	private int intentosLogin;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView frame = new LoginView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginView() {
		super("LOGIN USUARIO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Número de empleado");
		lblNewLabel.setBounds(22, 35, 143, 16);
		contentPane.add(lblNewLabel);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(22, 63, 198, 26);
		contentPane.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(22, 101, 72, 16);
		contentPane.add(lblPassword);
		
		textFieldPassword = new JTextField();
		textFieldPassword.setColumns(10);
		textFieldPassword.setBounds(22, 127, 198, 26);
		contentPane.add(textFieldPassword);
		
		btnNewButton = new JButton("Acceder");
		btnNewButton.setBounds(316, 220, 117, 29);
		contentPane.add(btnNewButton);
		btnNewButton.addActionListener(this);
		
	   intentosLogin = 0;
	    

	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		  if (e.getSource() == btnNewButton) {
	            Employee employee = new Employee();
	            String username = textFieldUsername.getText();
	            String password = textFieldPassword.getText();
	            
	            if (employee.login(Integer.valueOf(username), password)) {
	                // Si las credenciales son correctas, abrir la ventana ShopView
	                ShopView shopView = new ShopView();
	                shopView.setVisible(true);
	                dispose();
	            } else {
	                
	                intentosLogin++;
	                
	                // Si se han realizado 3 intentos malos, mostrar un mensaje de error y cerrar la aplicación
	                if (intentosLogin >= 3) {
	                    JOptionPane.showMessageDialog(this, "Haz alcanzado el número máximo de intentos. Cerrando la aplicación.", "Error", JOptionPane.ERROR_MESSAGE);
	                    System.exit(0); 	                } else {
	                    // Si las credenciales son incorrectas pero no se han alcanzado los 3 intentos, mostrar un mensaje de error
	                    JOptionPane.showMessageDialog(this, "Login incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            }
	        }
	    }
	}
