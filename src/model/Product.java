package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "product") // La raíz de cada producto en el XML
@XmlType(propOrder = {"id", "name", "available", "wholesalerPrice", "publicPrice", "stock"})
public class Product {

    private int id;
    private String name;
    private boolean available;
    private Amount wholesalerPrice;
    private Amount publicPrice;
    private int stock;

    public Product(String productName, double productPrice, boolean b, int productStock) {
		// TODO Auto-generated constructor stub
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	// Getters y setters
    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @XmlElement
    public Amount getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(Amount wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    @XmlElement
    public Amount getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(Amount publicPrice) {
        this.publicPrice = publicPrice;
    }

    @XmlElement
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

	public char[] getPrice() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPrice(double parseDouble) {
		// TODO Auto-generated method stub
		
	}

	public void expire() {
		// TODO Auto-generated method stub
		
	}
}



