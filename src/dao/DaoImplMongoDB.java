package dao;

import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public DaoImplMongoDB() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("shop");
    }
	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public ArrayList<Product> getInventory() {
	    ArrayList<Product> products = new ArrayList<>();
	    var collection = database.getCollection("inventory");

	    System.out.println("Iniciando la carga del inventario..."); // Mensaje al inicio

	    try {
	        for (var doc : collection.find()) {
	            // Extraer el objeto "wholesalerPrice"
	            Document priceDoc = doc.get("wholesalerPrice", Document.class);
	            
	            // Obtener el valor y asegurarse de que sea un Double
	            Object priceObject = priceDoc.get("value");

	            // Verificar el tipo del valor
	            double priceValue = 0;
	            if (priceObject instanceof Double) {
	                priceValue = (Double) priceObject;
	            } else if (priceObject instanceof Integer) {
	                priceValue = ((Integer) priceObject).doubleValue();
	            }

	            // Crear el objeto Product
	            Product product = new Product(
	                doc.getString("name"),
	                priceValue, // Ahora sí obtiene el precio correctamente
	                doc.getBoolean("available"),
	                doc.getInteger("stock")
	            );

	            products.add(product);
	        }

	        System.out.println("Inventario cargado exitosamente. Total de productos: " + products.size()); // Mensaje de éxito

	    } catch (Exception e) {
	        System.out.println("Error al leer el inventario desde MongoDB: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> product) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addProduct(Product product) {
	    // Crear un documento con los datos del producto
	    Document productDoc = new Document("name", product.getName())
	        .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue()) // Usar el valor directamente
	                                    .append("currency", product.getWholesalerPrice().getCurrency())) // Usar la moneda directamente
	        .append("publicPrice", new Document("value", product.getPublicPrice().getValue()) // Usar el valor directamente
	                                    .append("currency", product.getPublicPrice().getCurrency())) // Usar la moneda directamente
	        .append("available", product.isAvailable())
	        .append("stock", product.getStock())
	        .append("id", product.getId());

	    // Obtener la colección "inventory" de la base de datos
	    var collection = database.getCollection("inventory");

	    try {
	        // Insertar el documento en la colección
	        collection.insertOne(productDoc);
	        System.out.println("Producto agregado correctamente: " + product.getName());
	    } catch (Exception e) {
	        System.out.println("Error al agregar el producto: " + e.getMessage());
	        e.printStackTrace();
	    }
	}



	@Override
	public void updateProduct(Product product) {
	    Document updatedProductDoc = new Document("name", product.getName())
	        .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
	                                    .append("currency", product.getWholesalerPrice().getCurrency()))
	        .append("publicPrice", new Document("value", product.getPublicPrice().getValue())
	                                    .append("currency", product.getPublicPrice().getCurrency()))
	        .append("available", product.isAvailable())
	        .append("stock", product.getStock())
	        .append("id", product.getId());
	    
	    var collection = database.getCollection("inventory");

	    try {
	        // Usamos un filtro para encontrar el producto por su id
	        Bson filter = Filters.eq("id", product.getId());

	        // Realizamos la actualización con el nuevo documento
	        UpdateResult result = collection.updateOne(filter, new Document("$set", updatedProductDoc));

	        // Verificar si la actualización fue buena
	        if (result.getMatchedCount() > 0) {
	            System.out.println("Producto actualizado correctamente: " + product.getName());
	        } else {
	            System.out.println("Producto no encontrado para actualizar: " + product.getName());
	        }
	    } catch (Exception e) {
	        System.out.println("Error al actualizar el producto: " + e.getMessage());
	        e.printStackTrace();
	    }
	}


	@Override
	public void deleteProduct(int productId) {
	    var collection = database.getCollection("inventory");
	    try {   
	        Bson filter = Filters.eq("id", productId);// Creamos un filtro para buscar el producto por su id
	        DeleteResult result = collection.deleteOne(filter); // Eliminamos el producto de la base de datos
	        if (result.getDeletedCount() > 0) {
	            System.out.println("Producto eliminado correctamente con id: " + productId);
	        } else {
	            System.out.println("Producto no encontrado con id: " + productId);
	        }
	    } catch (Exception e) {
	        System.out.println("Error al eliminar el producto: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

}
