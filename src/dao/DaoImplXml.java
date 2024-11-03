package dao;

import dao.xml.DomWriter;
import dao.xml.SaxReader;
import model.Product;
import model.Employee;

import java.util.ArrayList;
import java.util.List;

public class DaoImplXml implements Dao {
    private final String filePath;

    public DaoImplXml(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void connect() {
        throw new UnsupportedOperationException("Conexión no requerida para archivos XML.");
    }

    @Override
    public void disconnect() {
        throw new UnsupportedOperationException("Desconexión no requerida para archivos XML.");
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        throw new UnsupportedOperationException("Método getEmployee no implementado en DaoImplXml.");
    }

    @Override
    public List<Product> getInventory() {
        SaxReader saxReader = new SaxReader();
        saxReader.parse("xml/inputinventory.xml");  // Cargar desde el archivo especificado
        List<Product> products = saxReader.getProducts();
        return (products != null) ? products : new ArrayList<>();
    }

    @Override
    public boolean writeInventory(List<Product> inventory) {
        try {
            DomWriter domWriter = new DomWriter();
            domWriter.writeInventoryToXml(inventory);
            return true;
        } catch (Exception e) {
            System.out.println("Error al escribir el inventario XML: " + e.getMessage());
            return false;
        }
    }
}

