package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Email;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

public interface PersonDetails {

	@Value("#{target.firstName + ' ' + target.lastName}")
	String getName();

	Email getEmail();

	LocalDate getDateOfBirth();

	String getCity();

	boolean isActivist();

	boolean isReferent();

}
