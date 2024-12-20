package dao;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import model.Employee;
import model.Product;


public interface Dao {
   
    void connect();
    void disconnect();

    // Método para obtener un empleado por su ID y contraseña, devuelve un objeto Employee
    Employee getEmployee(int employeeId, String password);
    // Método que devuelve la lista de productos (inventario)
    List<Product> getInventory() throws ParserConfigurationException, SAXException, IOException; 
    // Método que recibe la lista de productos (inventario) y devuelve un booleano indicando éxito o error
    boolean writeInventory(List<Product> inventory);
}