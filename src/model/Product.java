package model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="product")
@XmlType(propOrder= {"id", "name", "available", "wholesalerPrice", "publicPrice", "stock"})
@Entity // Indica que esta clase es una entidad JPA
@Table(name = "inventory") // Relaciona la clase con la tabla "inventory"
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincrementa el ID
    private int id;
    
    @Column(name = "name") // Relaciona el atributo "name" con la columna "name" de la tabla
    private String name;
    
    @Column(name = "wholesalerPrice")
    @Convert(converter = AmountConverter.class)  // Usamos el conversor de Amount a Double
    private Amount wholesalerPrice;
    
    @Column(name = "publicPrice")
    @Convert(converter = AmountConverter.class)  // Usamos el conversor de Amount a Double
    private Amount publicPrice;
    
    @Column(name = "available") // Relaciona el atributo "available" con la columna "available"
    private boolean available;
    
    @Column(name = "stock") // Relaciona el atributo "stock" con la columna "stock"
    private int stock;

    @Transient // Este atributo no será mapeado a una columna de la tabla
    private static int totalProducts = 0;

    static double EXPIRATION_RATE = 0.60;
    
    // Constructor de la clase
    public Product(String name, double wholesalerPrice, boolean available, int stock) {
        this.id = ++totalProducts; // Aseguramos IDs únicos
        this.name = name;
        this.wholesalerPrice = new Amount(wholesalerPrice);
        this.available = available;
        this.stock = stock;
        this.publicPrice = new Amount(wholesalerPrice * 2);  // Precio público calculado
    }

    public Product() {
        this.id = ++totalProducts; // Constructor vacío con ID autogenerado
        this.available = true;
    }

    @XmlAttribute(name="id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlAttribute(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name="publicPrice")
    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    @XmlElement(name="wholesalerPrice")
    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    @XmlElement(name="available")
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @XmlElement(name="stock")
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void expire() {
        publicPrice.setValue(publicPrice.getValue() * EXPIRATION_RATE);
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", publicPrice=" + publicPrice.getValue() + publicPrice.getCurrency() +
                ", wholesalerPrice=" + wholesalerPrice.getValue() + wholesalerPrice.getCurrency() + ", stock=" + stock + "]";
    }
}

