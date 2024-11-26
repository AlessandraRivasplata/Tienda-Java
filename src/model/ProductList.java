package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

@XmlRootElement(name = "products")  // El nodo raíz en el XML es <products>
public class ProductList {

    private int total;
    private List<Product> products;

    // Constructor
    public ProductList() {}

    public ProductList(List<Product> products, int total) {
        this.products = products;
        this.total = total;
    }

    @XmlElement(name = "product")  // Cada producto se etiqueta como <product>
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @XmlAttribute
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
