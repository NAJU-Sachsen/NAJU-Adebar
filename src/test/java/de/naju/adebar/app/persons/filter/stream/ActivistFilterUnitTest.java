package de.naju.adebar.app.persons.filter.stream;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.app.persons.filter.FilterTestBootstrapper;
import de.naju.adebar.app.persons.filter.stream.ActivistFilter;
import de.naju.adebar.model.persons.Person;

/**
 * Basic testing of the {@link ActivistFilter}
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
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
    Assert.assertArrayEquals("Should only contain activists", expected.toArray(), result);
  }

  @Test
  public void testIgnoreActivists() {
    Person[] result = {fritz, heinz};
    activistFilter = new ActivistFilter(FilterType.IGNORE);
    Object[] actualResult = activistFilter.filter(personRepo.streamAll()).toArray();
    Assert.assertArrayEquals("Should not contain activists", result, actualResult);
  }

  @Test
  public void testJuleicaExpiryBefore() {
    Person[] result = {claus};
    activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        DateFilterType.BEFORE);
    Assert.assertArrayEquals(
        "Should only contain activists with juleica expiry date before "
            + hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        result, activistFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testJuleicaExpiryAfter() {
    Person[] result = {berta};
    activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        DateFilterType.AFTER);
    Assert.assertArrayEquals(
        "Should only contain activists with juleica expiry date after "
            + hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        result, activistFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testJuleicaExpiryExact() {
    Person[] result = {hans};
    activistFilter = new ActivistFilter(hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        DateFilterType.EXACT);
    Assert.assertArrayEquals(
        "Should only contain activists with juleica expiry date on "
            + hans.getActivistProfile().getJuleicaCard().getExpiryDate(),
        result, activistFilter.filter(personRepo.streamAll()).toArray());
  }

}
