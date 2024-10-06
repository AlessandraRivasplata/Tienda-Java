package dao;

import java.util.List;

import model.Employee;
import model.Product;


public interface Dao {
    // Método que se encarga de conectar, no devuelve nada
    void connect();
    
    // Método para obtener un empleado por su ID y contraseña, devuelve un objeto Employee
    Employee getEmployee(int employeeId, String password);
    
    // Método para desconectar, no devuelve nada
    void disconnect();

    // Método que devuelve la lista de productos (inventario)
    List<Product> getInventory(); 
    
    // Método que recibe la lista de productos (inventario) y devuelve un booleano indicando éxito o error
    boolean writeInventory(List<Product> inventory);
}