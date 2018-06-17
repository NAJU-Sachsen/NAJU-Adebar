package de.naju.adebar.util.validation;

import javax.validation.constraints.NotEmpty;
import org.springframework.lang.NonNull;
import org.springframework.validation.Errors;

public class ErrorUtils {

  public static void rejectIndexedKey(@NotEmpty String field, int index,
      @NotEmpty String errorCode, @NonNull Errors errors) {
    String indexedField = String.format("%s[%d]", field, index);
    errors.rejectValue(indexedField, errorCode);
  }

  private ErrorUtils() {}

}
