package dao.xml;

import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Product;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxReader extends DefaultHandler {
    private ArrayList<Product> products;
    private Product product;
    private String currentElement;
    private String currency;

    public ArrayList<Product> getProducts() {
        return products;
    }

    // Método para iniciar el proceso de parseo
    public void parse(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(filePath), this);

            System.out.println("Inventario cargado correctamente desde el archivo: " + filePath);
        } catch (SAXException e) {
            System.err.println("Error de parseo al leer el archivo XML: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al leer el archivo XML: " + e.getMessage());
        }
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
            case "wholeSalerPrice":
                currentElement = "wholeSalerPrice";
                currency = attributes.getValue("currency"); // Guardar la moneda
                break;
            case "stock":
                currentElement = "stock";
                break;
            default:
                currentElement = null; // Resetear currentElement si es un elemento inesperado
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (currentElement != null && !value.isEmpty() && product != null) {
            switch (currentElement) {
                case "wholeSalerPrice":
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
            product = null;
        }
        currentElement = null;
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Total de productos cargados: " + products.size());
        
        // Imprimir cada producto al final del documento
        for (Product p : products) {
            System.out.println("Producto: " + p.getName() + ", Precio mayorista: " + p.getPrice() + " " + currency + ", Stock: " + p.getStock());
        }
    }
}


