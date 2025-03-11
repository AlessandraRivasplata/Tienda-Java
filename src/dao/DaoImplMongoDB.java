package dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
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
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
	    if (database == null) {
	        System.out.println("Error: mongoDatabase no ha sido inicializado.");
	        return null;
	    }	    
	    System.out.println("Conectando a MongoDB");
	    Employee employee = null;
	    try {
	        MongoCollection<Document> employeeCollection = database.getCollection("users");
	        if (employeeCollection == null) {
	            System.out.println("Error: No se encontró la colección 'users'.");
	            return null;
	        }
	        Bson filter = Filters.and(Filters.eq("employeeId", employeeId), Filters.eq("password", password));
	        Document employeeDoc = employeeCollection.find(filter).first();
	        if (employeeDoc != null) {
	            System.out.println("Empleado encontrado: " + employeeDoc.toJson());
	            employee = new Employee();
	            employee.setEmployeeId(employeeDoc.getInteger("employeeId"));
	            employee.setName(employeeDoc.getString("name"));
	            employee.setPassword(employeeDoc.getString("password"));
	        } else {
	            System.out.println("No se encontró el empleado con esa contraseña");
	        }
	    } catch (Exception e) {
	        System.out.println("Error al obtener el empleado desde MongoDB:");
	        e.printStackTrace();
	    }

	    return employee;
	}
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub	
	}
	@Override
	public ArrayList<Product> getInventory() {
	    ArrayList<Product> products = new ArrayList<>();
	    var collection = database.getCollection("inventory");
	    System.out.println("Iniciando la carga del inventario..."); 
	    try {
	        for (var doc : collection.find()) {
	            Document priceDoc = doc.get("wholesalerPrice", Document.class);
	            Object priceObject = priceDoc.get("value");
	            double priceValue = 0;
	            if (priceObject instanceof Double) {
	                priceValue = (Double) priceObject;
	            } else if (priceObject instanceof Integer) {
	                priceValue = ((Integer) priceObject).doubleValue();
	            }
	            Product product = new Product(
	                doc.getString("name"),
	                priceValue, 
	                doc.getBoolean("available"),
	                doc.getInteger("stock")
	            );
	            products.add(product);
	            // Imprimir cada producto cargado
	            System.out.println("Producto cargado: " + product.getName() +
	                               " | Precio: " + priceValue + 
	                               " | Disponible: " + product.isAvailable() +
	                               " | Stock: " + product.getStock());
	        }
	        System.out.println("Inventario conectado. Total de productos: " + products.size()); 
	    } catch (Exception e) {
	        System.out.println("Error al leer el inventario desde MongoDB: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return products;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
	    int insertedCount = 0;
	    try {
	        MongoCollection<Document> inventoryCollection = database.getCollection("historical_inventory");
	        ArrayList<Document> documentsToInsert = new ArrayList<>();

	        for (Product product : products) {
	            Document productDoc = new Document()
	                .append("id", product.getId())
	                .append("name", product.getName())
	                .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
	                    .append("currency", product.getWholesalerPrice().getCurrency()))
	                .append("available", product.isAvailable())
	                .append("stock", product.getStock())
	                .append("publicPrice", new Document("value", product.getPublicPrice().getValue())
	                    .append("currency", product.getPublicPrice().getCurrency()))
	                .append("created_at", new Date());

	            documentsToInsert.add(productDoc);
	            insertedCount++;
	        }
	        if (!documentsToInsert.isEmpty()) {
	            inventoryCollection.insertMany(documentsToInsert);
	            System.out.println("Productos insertados en la tabla historical_inventory: " + insertedCount);
	            return true; 
	        } else {
	            System.out.println("No se insertaron productos nuevos.");
	            return false;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return false; 
	    }
	}



	@Override
	public void addProduct(Product product) {
	    Document productDoc = new Document("name", product.getName())
	        .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue()) // Usar el valor directamente
	                                    .append("currency", product.getWholesalerPrice().getCurrency())) // Usar la moneda directamente
	        .append("publicPrice", new Document("value", product.getPublicPrice().getValue()) // Usar el valor directamente
	                                    .append("currency", product.getPublicPrice().getCurrency())) // Usar la moneda directamente
	        .append("available", product.isAvailable())
	        .append("stock", product.getStock())
	        .append("id", product.getId());
	    var collection = database.getCollection("inventory");
	    try {
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
	        Bson filter = Filters.eq("id", product.getId());
	        UpdateResult result = collection.updateOne(filter, new Document("$set", updatedProductDoc));
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
	        Bson filter = Filters.eq("id", productId);
	        DeleteResult result = collection.deleteOne(filter); 
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
