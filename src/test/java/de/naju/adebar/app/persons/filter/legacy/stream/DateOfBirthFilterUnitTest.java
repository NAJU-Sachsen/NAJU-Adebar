package de.naju.adebar.app.persons.filter.legacy.stream;

import de.naju.adebar.app.filter.legacy.DateFilterType;
import de.naju.adebar.app.persons.filter.legacy.FilterTestBootstrapper;
import de.naju.adebar.app.persons.legacy.filter.stream.DateOfBirthFilter;
import de.naju.adebar.model.persons.Person;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

/**
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
@Rollback
@Component
public class DateOfBirthFilterUnitTest extends FilterTestBootstrapper {
	private DateOfBirthFilter dateOfBirthFilter;

	@Test
	public void testDobExact() {
		Person[] result = {berta, heinz};
		dateOfBirthFilter = new DateOfBirthFilter(bertaDob, DateFilterType.EXACT);
		assertSameElements(result, dateOfBirthFilter.filter(personRepo.streamAll()).toArray(),
				"Arrays do not match");
	}

	@Test
	public void testDobBefore() {
		Person[] result = {claus, fritz};
		dateOfBirthFilter = new DateOfBirthFilter(bertaDob, DateFilterType.BEFORE);
		System.out.println(Arrays.toString(dateOfBirthFilter.filter(personRepo.streamAll()).toArray()));
		assertSameElements(result, dateOfBirthFilter.filter(personRepo.streamAll()).toArray(),
				"Arrays do not match");
	}

	@Test
	public void testDobAfter() {
		Person[] result = {hans};
		dateOfBirthFilter = new DateOfBirthFilter(bertaDob, DateFilterType.AFTER);
		assertSameElements(result, dateOfBirthFilter.filter(personRepo.streamAll()).toArray(),
				"Arrays do not match");
	}

}
