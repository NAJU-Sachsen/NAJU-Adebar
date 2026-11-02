package de.naju.adebar.app.persons.filter.legacy.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.persons.filter.legacy.FilterTestBootstrapper;
import de.naju.adebar.app.persons.legacy.filter.predicate.NameFilter;
import de.naju.adebar.model.persons.Person;

@SpringBootTest
@Transactional
public class NameFilterUnitTests extends FilterTestBootstrapper {

	private BooleanBuilder predicate;
	private NameFilter filter;

	@Override
	@BeforeEach
	public void setUp() {
		super.setUp();
		this.predicate = new BooleanBuilder();
	}

	@Test
	public void usesFirstNameAndLastName() {
		filter = new NameFilter("Hans", "Wurst");
		List<Person> matches = personRepo.findAll(filter.filter(predicate));
		assertThat(matches).containsExactly(hans);
	}

	@Test
	public void ignoresFirstNameIfNull() {
		filter = new NameFilter(null, "Käse");
		List<Person> matches = personRepo.findAll(filter.filter(predicate));
		assertThat(matches).containsExactly(fritz);
	}

	@Test
	public void ignoresFirstNameIfEmpty() {
		filter = new NameFilter("", "Meinz");
		List<Person> matches = personRepo.findAll(filter.filter(predicate));
		assertThat(matches).containsExactly(heinz);
	}

	@Test
	public void ignoresLastNameIfNull() {
		filter = new NameFilter("Claus", null);
		List<Person> matches = personRepo.findAll(filter.filter(predicate));
		assertThat(matches).containsExactly(claus);
	}

	@Test
	public void ignoresLastNameIfEmpty() {
		filter = new NameFilter("Berta", "");
		List<Person> matches = personRepo.findAll(filter.filter(predicate));
		assertThat(matches).containsExactly(berta);
	}

}
