package de.naju.adebar.util.validation;

import jakarta.validation.constraints.NotEmpty;
import org.jspecify.annotations.NonNull;
import org.springframework.validation.Errors;

public class ErrorUtils {

	public static void rejectIndexedKey(@NotEmpty String field, int index, @NotEmpty String errorCode,
			@NonNull Errors errors) {
		String indexedField = String.format("%s[%d]", field, index);
		errors.rejectValue(indexedField, errorCode);
	}

	private ErrorUtils() {}

}
