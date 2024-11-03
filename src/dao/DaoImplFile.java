package dao;

import model.Product;
import model.Employee;
import main.Shop;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class DaoImplFile implements Dao {
    private Shop shop;

    public DaoImplFile(Shop shop) {
        this.shop = shop;
    }



    @Override
    public List<Product> getInventory() {
       
        try {
          
            return shop.getInventory(); 
        } catch (Exception e) {
            System.err.println("Error al obtener el inventario: " + e.getMessage());
            return new ArrayList<>();  // Devolver una lista vacía en caso de error
        }
    }


    public boolean writeInventory(List<Product> inventory) {
        // Formar el nombre del archivo con la fecha actual
        LocalDate currentDate = LocalDate.now();
        String directoryPath = "xml";
        String filename = directoryPath + "/inputinventory_" + currentDate.toString() + ".xml";

        try {
            // Crear la carpeta si no existe
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdir();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                // Escribir cada producto en el archivo con el formato requerido
                for (Product product : inventory) {
                    writer.write(product.getId() + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";");
                    writer.newLine();
                }
                // Escribir el número total de productos
                writer.write("Numero total de productos:" + inventory.size() + ";");
                return true;
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el inventario: " + e.getMessage());
            return false;
        }
    }
    @Override
    public void connect() {
        
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
       
        return null;
    }

    @Override
    public void disconnect() {
        
    }
}
