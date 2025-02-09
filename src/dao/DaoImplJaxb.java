package dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJaxb implements Dao {

    @Override
    public void connect() {
        System.out.println("Conexión establecida con JAXB.");
    }

    @Override
    public void disconnect() {
        System.out.println("Desconexión realizada.");
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        // Este método no está implementado aquí, pero podrías añadir lógica según tus necesidades
        return null;
    }

    @Override
    public ArrayList<Product> getInventory() {
        // Utilizamos JAXB para leer el archivo XML y obtener el inventario
        try {
            File file = new File("jaxb/inputInventory.xml");  // Ruta al archivo XML
            if (!file.exists()) {
                System.out.println("El archivo inputInventory.xml no se encuentra.");
                return new ArrayList<>();
            }

            // Crear el contexto JAXB y el Unmarshaller
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Deserializar el XML en un objeto ProductList
            ProductList productList = (ProductList) unmarshaller.unmarshal(file);

            // Devolver la lista de productos
            return productList.getProducts();
        } catch (JAXBException e) {
            System.err.println("Error al leer el archivo XML: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        // Lógica para guardar el inventario en un archivo XML
        return false;  // Este método puede ser implementado si lo necesitas
    }

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(int productId) {
		// TODO Auto-generated method stub
		
	}
}



