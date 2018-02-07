package de.naju.adebar.app.persons.filter.predicate;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.persons.filter.FilterTestBootstrapper;
import de.naju.adebar.model.persons.Person;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class NameFilterUnitTests extends FilterTestBootstrapper {

  private BooleanBuilder predicate;
  private NameFilter filter;

  @Override
  @Before
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
