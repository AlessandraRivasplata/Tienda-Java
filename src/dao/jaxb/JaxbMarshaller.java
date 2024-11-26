package dao.jaxb;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import model.Product;
import model.ProductList;

public class JaxbMarshaller {

    public boolean exportInventory(ArrayList<Product> arrayListInventory) {
        // Obtener la fecha actual en el formato "yyyy-MM-dd"
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "jaxb/inputInventory_" + date + ".xml";

        try {
            // Verificar y crear el directorio si no existe
            File directory = new File("jaxb");
            if (!directory.exists() && !directory.mkdirs()) {
                throw new RuntimeException("No se pudo crear el directorio: " + directory.getAbsolutePath());
            }

            // Crear contexto JAXB para la clase ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Marshaller marshaller = context.createMarshaller();

            // Configurar el marshaller para que el XML sea legible
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Exportar el inventario al archivo XML
            marshaller.marshal(arrayListInventory, new File(fileName));

            System.out.println("Inventario exportado a: " + fileName);
            return true;

        } catch (JAXBException e) {
            System.err.println("Error durante la exportación del inventario:");
            e.printStackTrace();
            return false;
        }
    }
}
