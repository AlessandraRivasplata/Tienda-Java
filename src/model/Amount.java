package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Amount {

    private double value;
    private String currency;

    // Constructor
    public Amount() {}

    public Amount(double value) {
        this.value = value;
        this.currency = "€"; // Moneda predeterminada
    }

    // Getter y Setter
    @XmlElement
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @XmlAttribute
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

