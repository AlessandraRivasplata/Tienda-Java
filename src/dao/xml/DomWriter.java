package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Product;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomWriter {
    private Document document;

    public DomWriter() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            System.err.println("Error al crear el documento XML: " + e.getMessage());
        }
    }

    public void writeInventoryToXml(List<Product> products) {
        System.out.println("Iniciando la exportación del inventario a XML...");
        Element root = document.createElement("products");
        document.appendChild(root);

        for (Product product : products) {
            Element productElement = document.createElement("product");
            productElement.setAttribute("name", product.getName());
            root.appendChild(productElement);

            Element priceElement = document.createElement("wholeSalerPrice");
            priceElement.setTextContent(String.valueOf(product.getPrice()));
            productElement.appendChild(priceElement);

            Element stockElement = document.createElement("stock");
            stockElement.setTextContent(String.valueOf(product.getStock()));
            productElement.appendChild(stockElement);

            System.out.println("Producto exportado: " + product.getName() + " (Precio: " + product.getPrice() + ", Stock: " + product.getStock() + ")");
        }

        saveXmlToFile();
    }

    private void saveXmlToFile() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(document);
            String filePath = "xml/inputinventory" + LocalDate.now() + ".xml";
            File file = new File(filePath);

            file.getParentFile().mkdirs(); // Crear directorios si no existen

            Result result = new StreamResult(new FileWriter(file));
            transformer.transform(source, result);

            System.out.println("Inventario exportado correctamente a: " + filePath);
        } catch (IOException | TransformerException e) {
            System.err.println("Error al guardar el inventario XML: " + e.getMessage());
        }
    }
}

