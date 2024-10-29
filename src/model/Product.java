package model;

public class Product {
    private int id;
    private String name;
    private double publicPrice;
    private double wholesalerPrice;
    private boolean available;
    private int stock;
    private static int totalProducts;
    static final double PUBLIC_PRICE_MULTIPLIER = 2.0;
    static double EXPIRATION_RATE = 0.6;

    
    public Product() {
        this.id = totalProducts + 1;
        this.name = "default";
        this.wholesalerPrice = 0.0;
        this.publicPrice = 0.0;
        this.available = true;
        this.stock = 0;
        ++totalProducts;
    }

    public Product(String name, double wholesalerPrice, boolean available, int stock) {
        this.id = totalProducts + 1;
        this.name = name;
        this.wholesalerPrice = wholesalerPrice;
        this.publicPrice = wholesalerPrice * 2.0;
        this.available = available;
        this.stock = stock;
        ++totalProducts;
    }

    private double calculatePublicPrice(double wholesalerPrice) {
        return wholesalerPrice * 2.0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPublicPrice() {
        return this.publicPrice;
    }

    public void setPublicPrice(double publicPrice) {
        this.publicPrice = publicPrice;
    }

    public double getWholesalerPrice() {
        return this.wholesalerPrice;
    }

    public void setWholesalerPrice(double wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public static void setTotalProducts(int totalProducts) {
        Product.totalProducts = totalProducts;
    }

    public void expire() {
        this.publicPrice = this.getPublicPrice() * EXPIRATION_RATE;
    }

    public double getPrice() {
        return this.publicPrice; // assuming publicPrice is the price to return
    }

	public void setColor(String value) {
		// TODO Auto-generated method stub
		
	}

	public void setStorage(int parseInt) {
		// TODO Auto-generated method stub
		
	}

	public void setPrice(double parseDouble) {
		// TODO Auto-generated method stub
		
	}

	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getStorage() {
		// TODO Auto-generated method stub
		return null;
	}
}

