package dao.jaxb;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Product;
import model.ProductList;

public class JaxbUnMarshaller {

    public ProductList importInventory(String filePath) {
        try {
            File file = new File(filePath);
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ProductList) unmarshaller.unmarshal(file);
        } catch (JAXBException e) {
            System.err.println("Error al importar el inventario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

	public ArrayList<Product> init(String string) {
		// TODO Auto-generated method stub
		return null;
	}
}

