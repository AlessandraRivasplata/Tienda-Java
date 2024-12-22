package dao;

import model.Employee;
import model.Product;
import java.util.List;

public interface Dao {
    void connect();
    void disconnect();
    Employee getEmployee(int employeeId, String password);
    List<Product> getInventory();
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(Product product);
    boolean writeInventory(List<Product> inventory);
}
