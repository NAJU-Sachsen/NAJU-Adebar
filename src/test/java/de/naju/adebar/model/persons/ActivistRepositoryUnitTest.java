package de.naju.adebar.model.persons;

import com.google.common.collect.Lists;
import de.naju.adebar.model.core.Email;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic testing of the {@link PersonRepository} and {@link ReadOnlyPersonRepository}
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Component
public class ActivistRepositoryUnitTest {

  @Autowired
  @Qualifier("personRepo")
  private PersonRepository activistRepo;
  @Autowired
  @Qualifier("ro_personRepo")
  private ReadOnlyPersonRepository roActivistRepo;
  private List<Person> activists;
  private Person hans, berta, claus;

  @Before
  public void setUp() {
    System.out.println("Saved persons: " + activistRepo.findAll());
    hans = new Person(new PersonId(), "Hans", "Wurst", Email.of("hw@web.de"));
    hans.makeActivist();
    berta = new Person(new PersonId(), "Berta", "Beate", Email.of("bb@gmx.net"));
    berta.makeActivist();
    claus = new Person(new PersonId(), "Claus", "Störtebecker", Email.of("caeptn@aol.com"));
    claus.makeActivist();
    activists = Arrays.asList(hans, berta, claus);
    activistRepo.saveAll(activists);
  }

  @Test
  public void testFindAll() {
    List<Person> found = Lists.newLinkedList(activistRepo.findAllActivists());
    System.out.println(found);
    Assert.assertTrue(activists.containsAll(found) && found.containsAll(activists));
    found = Lists.newLinkedList(roActivistRepo.findAllActivists());
    Assert.assertTrue(activists.containsAll(found) && found.containsAll(activists));
  }
}
