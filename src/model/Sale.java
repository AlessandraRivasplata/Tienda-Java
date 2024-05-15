package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Sale {
    Client client;
    ArrayList<Product> products;
    double amount;
    LocalDateTime saleDateTime;

    public Sale(Client client, double amount, LocalDateTime saleDateTime) {
        this.client = client;
        this.products = new ArrayList<>();
        this.amount = amount;
        this.saleDateTime = saleDateTime;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.saleDateTime.format(formatter);
        StringBuilder sb = new StringBuilder();
        sb.append("La venta ha sido realizada por ").append(this.client.getName()).append(" y los productos vendidos:\n");
        Iterator<Product> iterator = this.products.iterator();

        while(iterator.hasNext()) {
            Product product = iterator.next();
            sb.append(product.toString()).append("\n");
        }

        sb.append("total: ").append(this.amount);
        return sb.toString();
    }

    public int getFormattedDateTime() {
        return 0;
    }
}

