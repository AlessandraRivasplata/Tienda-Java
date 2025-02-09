
package dao;

import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	public void connect();

	public Employee getEmployee(int employeeId, String password);

	public void disconnect();

	public ArrayList<Product> getInventory();

	public boolean writeInventory(ArrayList<Product> product);
	
	public void addProduct(Product product);
	
	public void updateProduct(Product product);
	
	public void deleteProduct(int productId);

}
