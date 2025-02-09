package model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)  // autoApply=true aplica el conversor automáticamente
public class AmountConverter implements AttributeConverter<Amount, Double> {

    @Override
    public Double convertToDatabaseColumn(Amount attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();  // Convertimos el valor de Amount a Double
    }

    @Override
    public Amount convertToEntityAttribute(Double dbData) {
        if (dbData == null) {
            return null;
        }
        return new Amount(dbData);  // Convertimos el valor Double a Amount
    }
}
