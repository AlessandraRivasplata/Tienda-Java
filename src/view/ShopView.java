package view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import main.Shop;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import model.*;

public class ShopView extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btnNewButtonCount;
    private JButton btnNewButtonAdd;
    private JButton btnNewButtonStock;
    private JButton btnNewButtonDelete;
    private Shop shop;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ShopView frame = new ShopView();
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
    public ShopView() {
        super("MiTienda.com - Menu principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Seleccione o pulse una opción");
        lblNewLabel.setBounds(25, 24, 342, 16);
        contentPane.add(lblNewLabel);

        btnNewButtonCount = new JButton("1. Contar caja");
        btnNewButtonCount.setBackground(new Color(255, 255, 255));
        btnNewButtonCount.addActionListener(this);
        btnNewButtonCount.setBounds(71, 52, 308, 29);
        contentPane.add(btnNewButtonCount);

        btnNewButtonAdd = new JButton("2. Añadir producto");
        btnNewButtonAdd.setBounds(71, 93, 308, 29);
        contentPane.add(btnNewButtonAdd);
        btnNewButtonAdd.addActionListener(this);

        btnNewButtonStock = new JButton("3. Añadir stock");
        btnNewButtonStock.setBounds(71, 133, 308, 29);
        contentPane.add(btnNewButtonStock);
        btnNewButtonStock.addActionListener(this);

        btnNewButtonDelete = new JButton("9. Eliminar producto");
        btnNewButtonDelete.setBounds(71, 174, 308, 29);
        contentPane.add(btnNewButtonDelete);
        btnNewButtonDelete.addActionListener(this);

        contentPane.setFocusable(true);
        contentPane.requestFocusInWindow();
        contentPane.addKeyListener(this);

        // poner KeyListener a los botones
        btnNewButtonCount.addKeyListener(this);
        btnNewButtonAdd.addKeyListener(this);
        btnNewButtonStock.addKeyListener(this);
        btnNewButtonDelete.addKeyListener(this);

        // Crear objeto Shop y cargar inventario
        shop = new Shop();
        shop.loadInventory();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNewButtonCount) {
            openCashView();
        } else if (e.getSource() == btnNewButtonAdd) {
            openProductView(2);
        } else if (e.getSource() == btnNewButtonStock) {
            openProductView(3);
        } else if (e.getSource() == btnNewButtonDelete) {
            openProductView(9);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_1) {
            openCashView();
        } else if (key == KeyEvent.VK_2) {
            openProductView(2);
        } else if (key == KeyEvent.VK_3) {
            openProductView(3);
        } else if (key == KeyEvent.VK_9) {
            openProductView(9);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
       
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    public void openCashView() {
        //  método para abrir la vista de contar caja
        CashView cashView = new CashView();
        cashView.setVisible(true);
    }

    public void openProductView(int option) {
        //  método para abrir la vista de añadir producto,stock o eliminar producto
        ProductView productView = new ProductView(shop, option);
        productView.setVisible(true);
    }
}

