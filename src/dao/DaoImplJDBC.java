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
    	System.out.println("Conexión establecida.");
        try {
            String url = "jdbc:mysql://localhost:8889/SHOP";
            String user = "root";
            String pass = "root";
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al conectar con la base de datos.");
        
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
            System.out.println("Producto añadido correctamente."+ product.getName());
        } catch (SQLException e) {
            System.out.println("Error al añadir el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE inventory SET stock = ? WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, product.getStock());
            ps.setString(2, product.getName());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Producto actualizado correctamente.");
            } else {
                System.out.println("No se encontró el producto para actualizar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el producto: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void deleteProduct(int productId) {
    	try {
			if (connection == null || connection.isClosed()) {
			    System.out.println("La conexión está cerrada, reintentando...");
			    connect();  // Volver a conectar si es necesario
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        String deleteQuery = "DELETE FROM inventory WHERE id = ?";
        try (PreparedStatement psDelete = connection.prepareStatement(deleteQuery)) {
            psDelete.setInt(1, productId);
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
        String queryCheck = "SELECT COUNT(*) FROM historical_inventory WHERE name = ?";
        String queryInsert = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) "
                             + "VALUES (?, ?, ?, ?, ?, NOW())";

        try {
            // Verificar si la conexión está cerrada o no existe
            if (connection == null || connection.isClosed()) {
                connect();  // Si la conexión está cerrada o no existe, la establecemos
            }

            // Preparar las consultas
            try (PreparedStatement psCheck = connection.prepareStatement(queryCheck);
                 PreparedStatement psInsert = connection.prepareStatement(queryInsert)) {

                for (Product prod : product) {
                    // Verificar si ya existe un producto con el mismo nombre
                    psCheck.setString(1, prod.getName());
                    try (ResultSet rs = psCheck.executeQuery()) {
                        if (rs.next() && rs.getInt(1) > 0) {
                            System.out.println("El producto con el nombre " + prod.getName() + " ya existe en la tabla historical_inventory. No se insertará.");
                            continue; // Si existe, no lo insertamos y seguimos con el siguiente producto
                        }
                    }

                    // Si no existe, insertar el producto
                    psInsert.setInt(1, prod.getId());
                    psInsert.setString(2, prod.getName());
                    psInsert.setBigDecimal(3, BigDecimal.valueOf(prod.getWholesalerPrice().getValue()));
                    psInsert.setBoolean(4, prod.isAvailable());
                    psInsert.setInt(5, prod.getStock());

                    psInsert.addBatch(); // Añadir al batch
                }

                // Ejecutar todas las inserciones en batch
                int[] affectedRows = psInsert.executeBatch();
                System.out.println("Productos insertados en la tabla historical_inventory: " + affectedRows.length);

                return affectedRows.length == product.size(); // Verificar si el número de filas afectadas es correcto
            } catch (SQLException e) {
                System.out.println("Error al insertar en historical_inventory: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar o abrir la conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

