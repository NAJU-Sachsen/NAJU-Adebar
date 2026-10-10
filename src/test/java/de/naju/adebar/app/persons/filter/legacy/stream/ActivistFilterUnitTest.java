package de.naju.adebar.app.persons.filter.legacy.stream;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.app.filter.legacy.DateFilterType;
import de.naju.adebar.app.filter.legacy.FilterType;
import de.naju.adebar.app.persons.filter.legacy.FilterTestBootstrapper;
import de.naju.adebar.app.persons.legacy.filter.stream.ActivistFilter;
import de.naju.adebar.model.persons.Person;

/**
 * Basic testing of the {@link ActivistFilter}
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
@Rollback
@Component
public class ActivistFilterUnitTest extends FilterTestBootstrapper {
	private ActivistFilter activistFilter;

	@Test
	public void testEnforceActivists() {
		List<Person> expected = Arrays.asList(claus, berta, hans);
		activistFilter = new ActivistFilter(FilterType.ENFORCE);
		Object[] result = activistFilter.filter(personRepo.streamAll()).toArray();
		assertSameElements(expected.toArray(), result, "Should only contain activists");
	}

	@Test
	public void testIgnoreActivists() {
		Person[] result = {fritz, heinz};
		activistFilter = new ActivistFilter(FilterType.IGNORE);
		Object[] actualResult = activistFilter.filter(personRepo.streamAll()).toArray();
		assertSameElements(result, actualResult, "Should not contain activists");
	}

	@Test
	public void testJuleicaExpiryBefore() {
		Person[] result = {claus};
		activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
				DateFilterType.BEFORE);
		assertSameElements(
				result,
				activistFilter.filter(personRepo.streamAll()).toArray(), "Should only contain activists with juleica expiry date before "
						+ hans.getActivistProfile().getJuleicaCard().getExpiryDate());
	}

	@Test
	public void testJuleicaExpiryAfter() {
		Person[] result = {berta};
		activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
				DateFilterType.AFTER);
		assertSameElements(
				result,
				activistFilter.filter(personRepo.streamAll()).toArray(), "Should only contain activists with juleica expiry date after "
						+ hans.getActivistProfile().getJuleicaCard().getExpiryDate());
	}

	@Test
	public void testJuleicaExpiryExact() {
		Person[] result = {hans};
		activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
				DateFilterType.EXACT);
		assertSameElements(
				result,
				activistFilter.filter(personRepo.streamAll()).toArray(), "Should only contain activists with juleica expiry date on "
						+ hans.getActivistProfile().getJuleicaCard().getExpiryDate());
	}

}
