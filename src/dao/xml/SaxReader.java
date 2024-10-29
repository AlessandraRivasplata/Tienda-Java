package dao.xml;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import model.Product;

public class SaxReader extends DefaultHandler {
    private ArrayList<Product> products;
    private Product product;
    private String currentElement;

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public void startDocument() throws SAXException {
        products = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "product":
                product = new Product();
                product.setName(attributes.getValue("name"));
                break;
            case "price":
                currentElement = "price";
                break;
            case "stock":
                currentElement = "stock";
                product.setColor(attributes.getValue("color"));
                product.setStorage(Integer.parseInt(attributes.getValue("storage")));
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (!value.isEmpty() && product != null) {
            switch (currentElement) {
                case "price":
                    product.setPrice(Double.parseDouble(value));
                    break;
                case "stock":
                    product.setStock(Integer.parseInt(value));
                    break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("product".equals(qName) && product != null) {
            products.add(product);
        }
        currentElement = null;
    }

    @Override
    public void endDocument() throws SAXException {
        // Optional: Print products to verify content
        for (Product p : products) {
            System.out.println(p);
        }
    }
}
