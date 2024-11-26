package model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "products")  // Define el nombre de la raíz del XML
public class ProductList {
    
    private int total;
    private ArrayList<Product> products = new ArrayList<>();  // Lista de productos
    
    // Constructor por defecto (necesario para JAXB)
    public ProductList() {}
    
    // Constructor con parámetros
    public ProductList(ArrayList<Product> products, int total) {
        this.products = products;
        this.total = total;
    }

    // Getter y setter para 'products'
    @XmlElement(name = "product")  // 'product' será el nombre de cada elemento en la lista
    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    // Getter y setter para 'total'
    @XmlAttribute(name = "total")  // 'total' será un atributo del elemento raíz
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

