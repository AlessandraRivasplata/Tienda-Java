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
    private ShopView shopView;

    public ProductView(Shop shop, int option) {
        this.shop = shop;
        this.option = option;
        //this.shopView = shopView;

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
            int productStock = Integer.parseInt(textFieldProductStock.getText());
            double productPrice = Double.parseDouble(textFieldProductPrice.getText());

            switch (option) {
                case 2: // Añadir producto
                   
                    Product newProduct = new Product(productName, productPrice, true, productStock);

                    // Verificar si el producto ya existe en el inventario
                    if (shop.findProduct(productName) == null) {
                        // Si no existe, agregar el nuevo producto al inventario
                        shop.addProduct(newProduct);
                        JOptionPane.showMessageDialog(this, "Producto añadido exitosamente al inventario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        // Si el producto ya existe, mostrar un mensaje de error
                        JOptionPane.showMessageDialog(this, "El producto ya existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 3: // Añadir stock
                	// Obtener el nombre del producto y el stock desde los campos de texto
             productName = textFieldNameProduct.getText();
                productStock = Integer.parseInt(textFieldProductStock.getText());

                    // Llamar al método de la tienda para agregar stock al producto
                    shop.addStockToProduct(productName, productStock);
                    break;
                   
                case 9: 
                	
                default:
                    break;
            }
        }
    }
}

            /*switch (option) {
                case 2: // Añadir producto
                    if (shop.findProduct(productName) == null) {
                        shop.addProduct(productName, productStock, productPrice);
                        JOptionPane.showMessageDialog(this, "Producto añadido exitosamente al inventario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Cerrar la ventana actual
                    } else {
                        JOptionPane.showMessageDialog(this, "El producto ya existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 3: // Añadir stock
                    Product existingProduct = shop.findProduct(productName);
                    if (existingProduct != null) {
                        existingProduct.setStock(existingProduct.getStock() + productStock);
                        JOptionPane.showMessageDialog(this, "Stock actualizado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Cerrar la ventana actual
                    } else {
                        JOptionPane.showMessageDialog(this, "El producto no existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case 9: // Eliminar producto
                    Product productToRemove = shop.findProduct(productName);
                    if (productToRemove != null) {
                        shop.removeProduct(productToRemove); // Se debe pasar el producto a eliminar como argumento
                        JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente del inventario.", "Información", JOptionPane.INFORMATION_MESSAGE);
                        dispose(); // Cerrar la ventana actual
                        shopView.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "El producto no existe en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}*/


	