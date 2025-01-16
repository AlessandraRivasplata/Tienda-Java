package view;

import java.awt.BorderLayout;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.Shop;
import model.Product;

public class ProductView extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textFieldNameProduct;
    private JTextField textFieldProductStock;
    private JTextField textFieldProductPrice;

    private int option;
    private Shop shop;
    private JButton okButton;
   

    public ProductView(Shop shop, int option) {
        this.shop = shop;
        this.option = option;
       

        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblNewLabel = new JLabel("Nombre producto:");
        lblNewLabel.setBounds(38, 60, 177, 16);
        contentPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Stock producto:");
        lblNewLabel_1.setBounds(38, 110, 100, 16);
        contentPanel.add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel("Precio producto:");
        lblNewLabel_2.setBounds(38, 164, 153, 16);
        contentPanel.add(lblNewLabel_2);

        textFieldNameProduct = new JTextField();
        textFieldNameProduct.setBounds(170, 55, 130, 26);
        contentPanel.add(textFieldNameProduct);
        textFieldNameProduct.setColumns(10);

        textFieldProductStock = new JTextField();
        textFieldProductStock.setBounds(170, 105, 130, 26);
        contentPanel.add(textFieldProductStock);
        textFieldProductStock.setColumns(10);

        textFieldProductPrice = new JTextField();
        textFieldProductPrice.setBounds(170, 159, 130, 26);
        contentPanel.add(textFieldProductPrice);
        textFieldProductPrice.setColumns(10);

    

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);
        okButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
        	
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                // Muestra el menú principal
                ShopView shopView = new ShopView();
                shopView.setVisible(true);
            }
        });

    
   
    switch (option) {
        case 2: // Añadir producto
        	setTitle("Añadir Producto");
            textFieldNameProduct.setVisible(true);
            textFieldProductStock.setVisible(true);
            textFieldProductPrice.setVisible(true);
            
            break;
        case 3: // Añadir stock
        	setTitle("Añadir Stock");
        	 textFieldNameProduct.setVisible(true);
        	 lblNewLabel.setVisible(true);
             textFieldProductStock.setVisible(true);
             lblNewLabel_1.setVisible(true);
             textFieldProductPrice.setVisible(false);
             lblNewLabel_2.setVisible(false);
            break;
        case 9: // Eliminar producto
        	setTitle("Eliminar producto");
            textFieldNameProduct.setVisible(true);
            textFieldProductStock.setVisible(false);
            lblNewLabel_1.setVisible(false);
            textFieldProductPrice.setVisible(false);
            lblNewLabel_2.setVisible(false);
            break;
        default:
            break;
    }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            String productName = textFieldNameProduct.getText();
            String productStockText = textFieldProductStock.getText();
            String productPriceText = textFieldProductPrice.getText();

            switch (option) {
                case 2: // Añadir producto
                    if (!productName.isEmpty() && !productStockText.isEmpty() && !productPriceText.isEmpty()) {
                        try {
                            int productStock = Integer.parseInt(productStockText);
                            double productPrice = Double.parseDouble(productPriceText);
                            if (shop.findProduct(productName) == null) {
                                Product newProduct = new Product(productName, productPrice, true, productStock);
                                shop.addProduct(newProduct);
                                JOptionPane.showMessageDialog(this, "Producto añadido al inventario", "Información", JOptionPane.INFORMATION_MESSAGE);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "El producto ya existe en el inventario", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "El stock y el precio tienen que ser números válido", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "No puede dejar los campos vacios", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 3: // Añadir stock
                    if (!productName.isEmpty() && !productStockText.isEmpty()) {
                        try {
                            int productStock = Integer.parseInt(productStockText);
                            Product product = shop.findProduct(productName);
                            if (product != null) {
                                product.setStock(product.getStock() + productStock);
                                shop.updateProduct(product);
                                JOptionPane.showMessageDialog(this, "Stock actualizado", "Información", JOptionPane.INFORMATION_MESSAGE);
                                dispose();
                            } else {
                                JOptionPane.showMessageDialog(this, "El producto no existe en el inventario", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "El stock debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El nombre del producto y el stock no pueden estar vacios", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case 9: // Eliminar producto
                    if (!productName.isEmpty()) {
                        Product product = shop.findProduct(productName);
                        if (product != null) {
                            shop.removeProduct(productName);
                            JOptionPane.showMessageDialog(this, "Producto eliminado ", "Información", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(this, "El producto no existe en el inventario", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "El nombre del producto debe estar completo", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
  