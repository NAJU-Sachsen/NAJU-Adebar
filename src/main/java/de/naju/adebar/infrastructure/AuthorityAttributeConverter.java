package de.naju.adebar.infrastructure;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author Rico Bergmann
 */
@Converter(autoApply = true)
public class AuthorityAttributeConverter
    implements AttributeConverter<SimpleGrantedAuthority, String> {

  @Override
  public String convertToDatabaseColumn(SimpleGrantedAuthority attribute) {
    return attribute.getAuthority();
  }

  @Override
  public SimpleGrantedAuthority convertToEntityAttribute(String dbData) {
    return new SimpleGrantedAuthority(dbData);
  }
}
