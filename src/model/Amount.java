package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name="currency")
public class Amount {
    private double value;
    private final String currency = "€";  // Puedes cambiarlo si necesitas otras monedas

    public Amount(double value) {
        this.value = value;
    }

    public Amount() {}

    @XmlAttribute(name="currency")
    public String getCurrency() {
        return currency;
    }

    @XmlValue
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value + currency;  // Esto imprime el valor seguido de la moneda (por ejemplo, "20.0€")
    }
}


