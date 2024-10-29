package dao;

import model.Employee;
import model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean writeInventory(List<Product> inventory) {
		// TODO Auto-generated method stub
		return false;
	}
}
