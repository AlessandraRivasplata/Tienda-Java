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
    private Connection connection; // OBJETO CONNECTION PARA GESTIONAR LA CONEXIÓN A NUESTRA BASE DE DATOS

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
            // CONSULTA SQL PARA SELECCIONAR EMPLEADO POR SU ID Y CONTRASEÑA
            String query = "SELECT * FROM employee WHERE employeeId = ? AND password = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, employeeId); // ESTABLECEMOS PARAMETROS EN LA CONSULTA
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery(); // EJECUTAMOS LA CONSULTA PARA TENER EL RESULTADO

            if (rs.next()) {// SI LO HAY SE CREA UN OBJETO EMPLOYEE
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
    public ArrayList<Product> getInventory() {
        ArrayList<Product> products = new ArrayList<>();
        String query = "SELECT * FROM inventory";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product(
                    rs.getString("name"),
                    rs.getDouble("wholesalerPrice"),
                    rs.getBoolean("available"),
                    rs.getInt("stock")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error al leer el inventario: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }


    @Override
    public void addProduct(Product product) {
        String insertQuery = "INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, product.getName());
            ps.setBigDecimal(2, BigDecimal.valueOf(product.getWholesalerPrice().getValue()));  // Extraemos el valor numérico
            ps.setBoolean(3, product.isAvailable());
            ps.setInt(4, product.getStock());
            ps.executeUpdate();
            System.out.println("Producto añadido correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al añadir el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        String updateQuery = "UPDATE inventory SET stock = ? WHERE name = ?";
        try (PreparedStatement psUpdate = connection.prepareStatement(updateQuery)) {
            psUpdate.setInt(1, product.getStock());
            psUpdate.setString(2, product.getName());
            psUpdate.executeUpdate();
            System.out.println("Producto actualizado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(Product product) {
        String deleteQuery = "DELETE FROM inventory WHERE name = ?";
        try (PreparedStatement psDelete = connection.prepareStatement(deleteQuery)) {
            psDelete.setString(1, product.getName().trim());
            int rowsAffected = psDelete.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("El producto no existe en el inventario.");
            } else {
                System.out.println("Producto eliminado correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public boolean writeInventory(ArrayList<Product> product) {
        String query = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) "
                        + "VALUES (?, ?, ?, ?, ?, NOW())";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Product prod : product) {
                ps.setInt(1, prod.getId());
                ps.setString(2, prod.getName());
                ps.setBigDecimal(3, BigDecimal.valueOf(prod.getWholesalerPrice().getValue()));
                ps.setBoolean(4, prod.isAvailable());
                ps.setInt(5, prod.getStock());

                ps.addBatch(); // Añadir al batch
            }

            int[] affectedRows = ps.executeBatch();
            return affectedRows.length == product.size();  // Verificar si el número de filas afectadas es correcto
        } catch (SQLException e) {
            System.out.println("Error inserting data into historical_inventory: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}

