

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

import dao.Dao;
import dao.DaoImplFile;

import model.Client;
import model.Employee;
import model.Product;
import model.Sale;

public class Shop {
    private double cash = 100.0;
    private List<Product> inventory;
    private ArrayList<Sale> sales;
    private static final String INVENTORY_FILE_PATH = "files/inputInventory.txt";
    static final double TAX_RATE = 1.04;
    private Dao dao; // Atributo dao
    public Shop() {
        this.cash = 100.0;
        this.inventory = new ArrayList();
        this.sales = new ArrayList();
       this.dao = new DaoImplFile(this); // Pasa el objeto Shop al constructor de DaoImplFile
        loadInventory();
    }
    public static void main(String[] args) {
        Shop shop = new Shop();
        if (shop.initSession()) {
            shop.loadInventory();
            shop.showMenu();
        } else {
            System.out.println("Credenciales incorrectas. Por favor, inténtelo de nuevo.");
        }
    }
    
    public boolean initSession() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inicio de sesión:");
        System.out.print("Número de empleado: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();

        Employee employee = new Employee();
        boolean loggedIn = employee.login(employeeId, password);
        if (loggedIn) {
            System.out.println("Inicio de sesión exitoso. ¡Bienvenido!");
        }
        return loggedIn;
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
  
    /*public void loadInventory() {
        try (BufferedReader br = new BufferedReader(new FileReader(INVENTORY_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = "", price = "", stock = "";
                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    switch (keyValue[0].trim()) {
                        case "Wholesaler Price":
                            price = keyValue[1].trim();
                            break;
                        case "Stock":
                            stock = keyValue[1].trim();
                            break;
                        case "Product":
                            name = keyValue[1].trim();
                            break;
                    }
                }
                // Validar que el nombre, el precio y el stock no estén vacíos
                if (!name.isEmpty() && !price.isEmpty() && !stock.isEmpty()) {
                    Product product = new Product(name, Double.parseDouble(price), true, Integer.parseInt(stock));
                    this.inventory.add(product);
                } else {
                    System.err.println("");
                }
            }
            System.out.println("Inventario cargado con éxito:");
            for (Product product : this.inventory) {
                System.out.println(product);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: El archivo de inventario contiene valores numéricos incorrectos. " + e.getMessage());
        }
    }*/
    /*public void loadInventory() {
        try {
            // Obtener lista de productos desde el DAO
            List<Product> productList = dao.getInventory();
            
            // Limpiar inventario actual y añadir productos del DAO
            this.inventory.clear();
            this.inventory.addAll(productList);
            
            System.out.println("Inventario cargado con éxito:");
            for (Product product : this.inventory) {
                System.out.println(product);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        }
    }*/
    // Método modificado para leer el inventario
    public void loadInventory() {
        try {
            // Aquí podrías leer desde un archivo y cargar el inventario en la variable "inventory" de Shop
            List<Product> inventory = dao.getInventory(); // Invoca solo una vez a getInventory
            setInventory(inventory); // Asigna el inventario leído a la variable de la tienda
        } catch (Exception e) {
            System.err.println("Error al cargar el inventario: " + e.getMessage());
        }
    }


// write inventory 
    // Método para escribir el inventario en un archivo
    public boolean writeInventory(List<Product> inventory) throws IOException {
        // Ruta del archivo donde se guardará el inventario
        String filePath = "inventario.txt"; 

        try (FileWriter writer = new FileWriter(filePath)) {
            for (Product product : inventory) {
                writer.write(product.toString() + "\n"); // Escribir cada producto en el archivo
            }
        } catch (IOException e) {
            throw new IOException("Error al escribir el archivo de inventario.", e);
        }

        return true;  // Devolver true si todo fue exitoso
    }

    public List<Product> getInventory() {
        return inventory;
    }

    public void setInventory(List<Product> inventory) {
        this.inventory = inventory;
    }

    public double showCash() {
       // System.out.println("Dinero actual:" + this.cash);
    	 return this.cash;
    }

    public void addProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine();
        System.out.print("Precio mayorista: ");
        double wholesalerPrice = scanner.nextDouble();
        System.out.print("Stock: ");
        int stock = scanner.nextInt();
        Product newProduct = new Product(name, wholesalerPrice, true, stock);
        this.addProduct(newProduct);
        System.out.println("Producto agregado exitosamente al inventario.");
    }


    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Seleccione un nombre de producto: ");
        String name = scanner.next();
        Product product = this.findProduct(name);
        if (product != null) {
            System.out.print("Seleccione la cantidad a añadir: ");
            int stock = scanner.nextInt();
            product.setStock(product.getStock() + stock);
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

    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        Iterator var2 = this.inventory.iterator();

        while(var2.hasNext()) {
            Product product = (Product)var2.next();
            if (product != null) {
                PrintStream var10000 = System.out;
                int var10001 = product.getId();
                var10000.println("ID:" + var10001);
                var10000 = System.out;
                String var3 = product.getName();
                var10000.println("Nombre" + var3);
                System.out.println("Disponible para venta: " + (product.isAvailable() ? "Sí" : "No"));
                System.out.println("Stock: " + product.getStock());
                System.out.println("Precio Público: " + product.getPublicPrice());
                System.out.println("Precio Mayorista: " + product.getWholesalerPrice());
            }
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
                totalAmount += product.getPublicPrice();
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

    public void addProduct(Product product) {
        this.inventory.add(product);
    }

    public void exportFile() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName = "files/inputInventory.txt";
            FileWriter writer = new FileWriter(fileName, true);
            int saleNumber = 1;
            Iterator var6 = this.sales.iterator();

            while(true) {
                Sale sale;
                do {
                    if (!var6.hasNext()) {
                        writer.close();
                        System.out.println("Datos de ventas exportados correctamente al archivo: " + fileName);
                        return;
                    }

                    sale = (Sale)var6.next();
                } while(sale == null);

                writer.write("" + saleNumber + ";Client=" + sale.getClient() + ";Date=" + sale.getFormattedDateTime() + ";");
                writer.write("" + saleNumber + ";Products=");
                Iterator var8 = sale.getProducts().iterator();

                while(var8.hasNext()) {
                    Product product = (Product)var8.next();
                    String var10001 = product.getName();
                    writer.write(var10001 + "," + product.getPublicPrice() + "€;");
                }

                writer.write("" + saleNumber + ";Amount=" + sale.getAmount() + ";\n");
                ++saleNumber;
            }
        } catch (IOException var9) {
            System.out.println("Error al exportar los datos de ventas: " + var9.getMessage());
        }
    }
    public void removeProduct(String productNameToRemove) {
        Product productFound = findProduct(productNameToRemove);
        if (productFound != null) {
            inventory.remove(productFound);
            System.out.println("El producto \"" + productNameToRemove + "\" ha sido eliminado del inventario.");
        } else {
            System.out.println("El producto \"" + productNameToRemove + "\" no ha sido encontrado en el inventario.");
        }
    }
    /*public void exportInventory() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName = "files/inventory_" + LocalDate.now().format(formatter) + ".txt"; // Nombre del archivo
            FileWriter writer = new FileWriter(fileName);

            int productNumber = 1; // Contador de productos

            // Iterar sobre el inventario y escribir en el archivo
            for (Product product : this.inventory) {
                if (product != null) {
                    writer.write(productNumber + ";Product:" + product.getName() + ";Stock:" + product.getStock() + ";\n");
                    productNumber++;
                }
            }

            // Escribir el total de productos al final del archivo
            writer.write("Numero total de productos:" + (productNumber - 1) + ";\n");

            writer.close();
            System.out.println("Inventario exportado correctamente al archivo: " + fileName);
        } catch (IOException e) {
            System.out.println("Error al exportar los datos de inventario: " + e.getMessage());
        }
    }*/

    public boolean exportInventory() {
        return dao.writeInventory(inventory);  // Invoca al método de exportación
    }


   
    
    public Product findProduct(String name) {
        Iterator var3 = this.inventory.iterator();

        while(var3.hasNext()) {
            Product product = (Product)var3.next();
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }

        return null;
    }

  
    public void addStockToProduct(String productName, int additionalStock) {
        // Buscar el producto en el inventario
        Product existingProduct = findProduct(productName);
        
        if (existingProduct != null) {
            // Si el producto existe, actualizar su stock
            existingProduct.setStock(existingProduct.getStock() + additionalStock);
            System.out.println("Stock del producto '" + productName + "' actualizado a: " + existingProduct.getStock());
        } else {
            // Si el producto no existe, mostrar un mensaje de error
            System.out.println("El producto '" + productName + "' no existe en el inventario.");
        }
    }
   

}