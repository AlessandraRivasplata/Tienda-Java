

package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import dao.DaoImplJaxb;
import dao.Dao;
import dao.DaoImplFile;
import dao.DaoImplJDBC;
import dao.DaoImplXml;
import model.Client;
import model.Employee;
import model.Product;
import model.Sale;

public class Shop {
    private double cash = 100.0;
    private ArrayList<Product> inventory;
    private ArrayList<Sale> sales;
    //private static final String INVENTORY_FILE_PATH = "xml/inputinventory.xml";  
    static final double TAX_RATE = 1.04;
    private Dao dao =new DaoImplJDBC(); // Atributo dao
   
    
    public Shop() {
        this.cash = 100.0;
        this.inventory = new ArrayList<>();
        this.sales = new ArrayList<>();
        
        
    }
    
    public static void main(String[] args) {
        Shop shop = new Shop();
        if (shop.initSession()) {
            shop.loadInventory();
            shop.showMenu();
            shop.verifyInventoryLoad(); // Llamada para verificar carga del inventario
        } else {
            System.out.println("Credenciales incorrectas. Por favor, inténtelo de nuevo.");
        }
    }

    // Inicio de sesión
    public boolean initSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inicio de sesión:");
        System.out.print("Número de empleado: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");
        return true; 
    }

    /* Cargar inventario desde el DAO
    public void loadInventory() {
        try {
            // Intentar cargar inventario usando el DAO
            List<Product> loadedInventory = dao.getInventory();
            if (loadedInventory == null || loadedInventory.isEmpty()) {
                System.out.println("Advertencia: El inventario está vacío o no se pudo cargar.");
            } else {
                this.inventory = loadedInventory;
                System.out.println("Unmarshalling completado. Inventario cargado correctamente:");
                for (Product product : this.inventory) {
                    System.out.println(product); // Mostrar cada producto cargado
                }
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
            this.inventory = new ArrayList<>(); // Inicializar como lista vacía para evitar problemas
        }
    }
*/
    public void loadInventory() {
		dao.connect();
		setInventory(dao.getInventory());
		dao.disconnect();
	}

    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        do {
        	 System.out.println("\n===========================");
	            System.out.println("Menu principal miTienda.com");
	            System.out.println("===========================");
	            System.out.println("1) Contar caja");
	            System.out.println("2) Añadir producto");
	            System.out.println("3) Añadir stock");
	            System.out.println("4) Marcar producto próxima caducidad");
	            System.out.println("5) Ver inventario");
	            System.out.println("6) Venta");
	            System.out.println("7) Ver ventas");
	            System.out.println("8) Eliminar producto");
	            System.out.println("9) Ver venta TOTAL");
	            System.out.println("10) Salir programa");
            
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea pendiente
	            switch (opcion) {
	            case 1:
	                showCash();
	                System.out.println("Dinero actual:" + this.cash);
	                break;
	            case 2:
	                addProduct();
	                break;
	            case 3:
	                addStock();
	                break;
	            case 4:
	                setExpired();
	                break;
	            case 5:
	                showInventory();
	                break;
	            case 6:
	                sale();
	                break;
	            case 7:
	                showSales();
	                break;
	            case 8:
	                System.out.println("Ingrese el nombre del producto que desea eliminar:");
	                String productNameToRemove = scanner.nextLine();
	                removeProduct(productNameToRemove);
	                break;
	            case 9:
	                // Agregar funcionalidad para ver venta total
                case 10:
                    exit = true;
                    System.out.println("Saliendo del programa");
                    break;
                default:
                    System.out.println("Opción no válida, pruebe otra vez");
                    break;
            }
        } while (!exit);
    }
  
  


/* write inventory 
    
    public boolean writeInventory(List<Product> inventory) throws IOException {
        // Ruta del archivo donde se guardará el inventario
        String filePath = "inputinventory.xml"; 

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Product product : inventory) {
                writer.write(product.toString() + "\n"); // Escribir cada producto en el archivo
            }
        } catch (IOException e) {
            throw new IOException("Error al escribir el archivo de inventario.", e);
        }

        return true;  // Devolver true si todo fue exitoso
    }
*/
  

    public void setInventory(ArrayList<Product> inventory) {
        this.inventory = inventory;
    }

    public double showCash() {
       // System.out.println("Dinero actual:" + this.cash);
    	 return this.cash;
    }

    // add product for console
    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();

        Product exist = findProduct(name);
        if (exist == null) {
            addProduct(new Product(name, wholesalerPrice, true, stock));  
        } else {
            System.out.println("El Producto que estas intentado añadir ya existe");
        }
    }

// stock by console
    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = this.findProduct(name);
        if (product != null) {
            System.out.print("Seleccione la cantidad a añadir: ");
            int stock = scanner.nextInt();
            product.setStock(product.getStock() + stock);
            updateProduct(product);
            System.out.println("Se ha añadido " + stock + "unidades al stock del producto" + name);
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getStock());
        } else {
            System.out.println("No se ha encontrado el producto con nombre " + name);
        }

    }

    private void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = this.findProduct(name);
        if (product != null) {
            product.expire();
            System.out.println("El stock del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
        } else {
            System.out.println("No se ha encontrado el prodcuto con nombre" + name);
        }

    }


    // Mostrar inventario
    public void showInventory() {
        if (this.inventory == null || this.inventory.isEmpty()) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("=== Inventario ===");
        for (Product product : this.inventory) {
            System.out.println(product);
        }
    }
    private void sale() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente:");
        String clientName = scanner.nextLine();

        // Crear un objeto de tipo Client con número de cliente y saldo inicial fijos
        Client client = new Client();
        client.setMemberId(456); // Número de cliente fijo
        client.setBalance(50.00); // Saldo inicial fijo

        LocalDateTime saleDateTime = LocalDateTime.now();
        double totalAmount = 0.0;

        // Crear una nueva venta con el objeto Client
        Sale sale = new Sale(client, 0.0, saleDateTime);
        String productName = "";

        while (!productName.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            productName = scanner.nextLine();
            if (productName.equals("0")) {
                break;
            }

            Product product = this.findProduct(productName);
            if (product != null && product.isAvailable()) {
            	totalAmount += product.getPublicPrice().getValue();
                product.setStock(product.getStock() - 1);
                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }

                sale.getProducts().add(product);
                System.out.println("Producto añadido con éxito");
            } else {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        double totalAmountConTax = totalAmount * 1.04;

        // Realizar el pago del cliente, incluso si no tiene saldo suficiente
        boolean paymentStatus = true; // Inicializar en true por defecto
        if (client.getBalance() < totalAmountConTax) {
            System.out.println("Cliente no tiene saldo suficiente para pagar la venta.");
            System.out.println("Continuando con la venta...");
            paymentStatus = false; // Cambiar a false si el cliente no tiene saldo suficiente
        } else {
            // Realizar el pago solo si el cliente tiene saldo suficiente
            paymentStatus = client.pay(totalAmountConTax);
        }

        // Si el pago es exitoso o no es necesario, continuar con la venta
        if (paymentStatus) {
            this.cash += totalAmountConTax;
            sale.setAmount(totalAmountConTax);
            this.sales.add(sale);
            System.out.println("Venta realizada , total: " + totalAmountConTax + "€");
        } else {
            // Si el pago falla, mostrar un mensaje con la cantidad a deber por parte del cliente
            double amountDue = totalAmountConTax - client.getBalance();
            System.out.println("Venta realizada con éxito, total: " + totalAmountConTax + "€");
            System.out.println("Cliente debe: " + amountDue + "€");
        }
    }


    public void showSales() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Desea exportar los datos de ventas a un archivo? (S/N): ");
        String exportChoice = scanner.next();
        if (exportChoice.equalsIgnoreCase("S")) {
            this.exportFile();
        } else {
            System.out.println("Lista de ventas:");
            if (this.sales.isEmpty()) {
                System.out.println("No hay ventas registradas.");
            } else {
                Iterator var4 = this.sales.iterator();

                while(var4.hasNext()) {
                    Sale sale = (Sale)var4.next();
                    int formattedDateTime = sale.getFormattedDateTime();
                    System.out.println("Fecha y hora de venta: " + formattedDateTime);
                    System.out.println(sale);
                }
            }
        }

    }

    
    public void exportFile() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String currentDate = LocalDate.now().format(formatter);
            String directoryPath = "files";
            String fileName = directoryPath + "/inputInventory_" + currentDate + ".txt";

            FileWriter writer = new FileWriter(fileName, false); // Sobrescribe el archivo
            int saleNumber = 1;

            for (Sale sale : this.sales) {
                if (sale != null) {
                    writer.write("" + saleNumber + ";Client=" + sale.getClient() + ";Date=" + sale.getFormattedDateTime() + ";");
                    writer.write("Products=");
                    
                    for (Product product : sale.getProducts()) {
                        writer.write(product.getName() + "," + product.getPublicPrice() + "€;");
                    }
                    
                    writer.write("Amount=" + sale.getAmount() + ";\n");
                    saleNumber++;
                }
            }

            writer.close();
            System.out.println("Datos de ventas exportados correctamente al archivo: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al exportar los datos de ventas: " + e.getMessage());
        }
    }

// delete product
    public void removeProduct(String productNameToRemove) {
        Product productFound = findProduct(productNameToRemove);
        if (productFound != null) {
            inventory.remove(productFound);
            deleteProduc(productFound);
            System.out.println("El producto \"" + productNameToRemove + "\" ha sido eliminado del inventario.");
        } else {
            System.out.println("El producto \"" + productNameToRemove + "\" no ha sido encontrado en el inventario.");
        }
    }
   
    
    /*public Product findProduct(String name) {
        Iterator var3 = this.inventory.iterator();

        while(var3.hasNext()) {
            Product product = (Product)var3.next();
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        return null;
    }
*/
    public Product findProduct(String name) {
        if (inventory == null) {
            System.out.println("Inventario no cargado.");
            return null;
        }
        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        System.out.println("Producto no encontrado.");
        return null;
    }

  
    /*public void addStockToProduct(String productName, int additionalStock) {
        // Buscar el producto en el inventario
        Product existingProduct = findProduct(productName);
        
        if (existingProduct != null) {
            // Si el producto existe, actualizar su stock
            existingProduct.setStock(existingProduct.getStock() + additionalStock);
            System.out.println("Stock del producto '" + productName + "' actualizado a: " + existingProduct.getStock());
            updateProduct(existingProduct);
        } else {
            // Si el producto no existe, mostrar un mensaje de error
            System.out.println("El producto '" + productName + "' no existe en el inventario.");
        }
    }
   */

    // There is not UT to verify the inventory was loaded properly, e.g. it could be printed on console 
    public void verifyInventoryLoad() {
        loadInventory(); // Carga el inventario usando el método existente
        if (inventory != null && !inventory.isEmpty()) {
            System.out.println("Inventario cargado correctamente:");
            for (Product product : inventory) {
                System.out.println(product); // Imprime cada producto para verificar
            }
        } else {
            System.out.println("Error: El inventario está vacío o no se pudo cargar.");
        }
    }
   /* public void exportInventory() {
        if (this.inventory == null || this.inventory.isEmpty()) {
            System.out.println("No hay inventario para exportar.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = LocalDate.now().format(formatter);
        String fileName = "jaxb/inputInventory" + date + ".xml";

        try (FileWriter writer = new FileWriter(fileName)) {
            for (Product product : this.inventory) {
                writer.write(product.toString() + "\n");
            }
            System.out.println("Inventario exportado a: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al exportar inventario: " + e.getMessage());
        }
    }*/
    public void exportInventory() {
        if (this.inventory == null || this.inventory.isEmpty()) {
            System.out.println("No hay inventario para exportar.");
            return;
        }

        boolean isExported = dao.writeInventory(this.inventory);  // Usamos el método de Dao para exportar a la base de datos
        if (isExported) {
            System.out.println("Inventario exportado correctamente a la base de datos.");
        } else {
            System.out.println("Error al exportar el inventario.");
        }
    }
    public boolean writeInventory() {
		dao.connect();
		boolean isExported = dao.writeInventory(inventory);
		dao.disconnect();
		return isExported;
	}


    public ArrayList<Product> getInventory() {
        return inventory;
    }
    // add product to inventory
    
    public void addProduct(Product product) {
        inventory.add(product);
        dao.connect(); 
        dao.addProduct(product); 
        dao.disconnect(); 
        System.out.println("Producto agregado al inventario");
    }

	

	public void updateProduct(Product product) {
		dao.connect();
		dao.updateProduct(product);
		dao.disconnect();
		 System.out.println("Stock actualizado en  el inventario");
	}

	private void deleteProduc(Product product) {
		dao.connect();
		dao.deleteProduct(product);
		dao.disconnect();
		System.out.println("Producto eliminado en  el inventario");
	}
	

}