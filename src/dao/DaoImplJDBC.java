package dao;

import model.Employee;
import model.Product;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DaoImplJDBC implements Dao {
    private Connection connection; // OBJETO CONNECTION PARA GESTIONAR LA CONEXIÓNA A NUESTRA BASE DE DATOS

    @Override
    public void connect()  {
        try {
            // Define connection parameters
            String url = "jdbc:mysql://localhost:8889/shop";
            String user = "root";
            String pass = "root";
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
           
        }
    }


    @Override // MÉTODO PARA OBTENER UN EMPLEADO POR SU ID Y CONTRASEÑA
    public Employee getEmployee(int employeeId, String password) {
        Employee employee = null;
        try {
        	// CONSULTA SQL PARA SELECIONAR EMPPELADO POR SU ID Y CONTRASEÑA
            String query = "SELECT * FROM employee WHERE employeeId = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, employeeId); // ESTABLECEMOS PARAMETROS EN LA CONSULTA
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery(); // EJECUTAMOS LA CONSULTA PARA TENER EL RESULTADO

            if (rs.next()) {// SI LO HAY SE CREAR UN OBJETO EMPLOYEE
                employee = new Employee();
                employee.setEmployeeId(rs.getInt("employeeId"));
                employee.setName(rs.getString("name"));
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public void disconnect() { // METODO PARA DESCONECTAR LA BASE DE DATOS 
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public List<Product> getInventory() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM Inventory";

        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(rs.getString("Name"), rs.getBigDecimal("wholesalerPrice").doubleValue(),
                                              rs.getBoolean("Available"), rs.getInt("Stock"));
                product.setId(rs.getInt("Id"));
                productList.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error connecting with the database: " + e.getMessage());
            e.printStackTrace();
        }

        return productList;
    }
    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, BigDecimal.valueOf(product.getWholesalerPrice().getValue()));
            ps.setBoolean(3, product.isAvailable());
            ps.setInt(4, product.getStock());

            ps.executeUpdate();  // Ejecutar la inserción
        } catch (SQLException SqlError) {
            System.out.println("Error adding product to the database: " + SqlError.getMessage());
            SqlError.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE inventory SET stock = ? WHERE name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, product.getStock());  // Establecer el stock
            ps.setString(2, product.getName());  // Establecer el nombre del producto

            ps.executeUpdate();  // Ejecutar la actualización
        } catch (SQLException SqlError) {
            System.out.println("Error updating product stock in the database: " + SqlError.getMessage());
            SqlError.printStackTrace();
        }
    }
    @Override
    public void deleteProduct(Product product) {
        String query = "DELETE FROM inventory WHERE name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());  // Establecer el nombre del producto

            ps.executeUpdate();  // Ejecutar la eliminación
        } catch (SQLException SqlError) {
            System.out.println("Error deleting product from the database: " + SqlError.getMessage());
            SqlError.printStackTrace();
        }
    }


    @Override
    public boolean writeInventory(List<Product> inventory) {
        String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Product product : inventory) {
                ps.setInt(1, product.getId());
                ps.setString(2, product.getName());
                ps.setBigDecimal(3, BigDecimal.valueOf(product.getWholesalerPrice().getValue()));
                ps.setBoolean(4, product.isAvailable());
                ps.setInt(5, product.getStock());

                ps.addBatch(); // Añadir al batch
            }

            int[] affectedRows = ps.executeBatch();
            return affectedRows.length == inventory.size();  // Verificar si el número de filas afectadas es correcto
        } catch (SQLException e) {
            System.out.println("Error inserting data into historical_inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
