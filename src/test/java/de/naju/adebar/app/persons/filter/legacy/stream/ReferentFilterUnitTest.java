package de.naju.adebar.app.persons.filter.legacy.stream;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.app.filter.legacy.FilterType;
import de.naju.adebar.app.persons.filter.legacy.FilterTestBootstrapper;
import de.naju.adebar.app.persons.legacy.filter.stream.ReferentFilter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.qualifications.Qualification;

/**
 * Basic testing of the {@link ReferentFilter}
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
@Rollback
@Component
public class ReferentFilterUnitTest extends FilterTestBootstrapper {
	private ReferentFilter referentFilter;

	@Test
	public void testEnforceReferent() {
		List<Person> result = Arrays.asList(berta, fritz);
		referentFilter = new ReferentFilter(FilterType.ENFORCE);
		assertSameElements(result.toArray(), referentFilter.filter(personRepo.streamAll()).toArray(),
				"Should only contain referents");
	}

	@Test
	public void testIgnoreReferent() {
		List<Person> result = Arrays.asList(claus, heinz, hans);
		referentFilter = new ReferentFilter(FilterType.IGNORE);
		assertSameElements(result.toArray(), referentFilter.filter(personRepo.streamAll()).toArray(),
				"Should not contain referents");
	}

	@Test
	public void testFilterQualifications() {
		Person[] result = {berta};
		List<Qualification> qualifications = Arrays.asList(bertaQualification1, bertaQualification2);
		referentFilter = new ReferentFilter(qualifications);
		assertSameElements(result, referentFilter.filter(personRepo.streamAll()).toArray(),
				"Should only contain " + berta);
	}
}
