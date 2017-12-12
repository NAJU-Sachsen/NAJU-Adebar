package de.naju.adebar.infrastructure;

import java.sql.Date;
import java.time.LocalDate;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Rico Bergmann
 * @see <a href= "https://www.thoughts-on-java.org/persist-localdate-localdatetime-jpa/">Blog
 *      article about the why and how</a>
 */
@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDate attribute) {
    return attribute == null ? null : Date.valueOf(attribute);
  }

  @Override
  public LocalDate convertToEntityAttribute(Date dbData) {
    return dbData == null ? null : dbData.toLocalDate();
  }
}
