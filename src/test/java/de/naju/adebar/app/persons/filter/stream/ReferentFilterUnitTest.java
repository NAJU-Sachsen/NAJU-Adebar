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
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.app.persons.filter.FilterTestBootstrapper;
import de.naju.adebar.app.persons.filter.stream.ReferentFilter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.qualifications.Qualification;

/**
 * Basic testing of the {@link ReferentFilter}
 * 
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
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
    Assert.assertArrayEquals("Should only contain referents", result.toArray(),
        referentFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testIgnoreReferent() {
    List<Person> result = Arrays.asList(claus, heinz, hans);
    referentFilter = new ReferentFilter(FilterType.IGNORE);
    Assert.assertArrayEquals("Should not contain referents", result.toArray(),
        referentFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testFilterQualifications() {
    Person[] result = {berta};
    List<Qualification> qualifications = Arrays.asList(bertaQualification1, bertaQualification2);
    referentFilter = new ReferentFilter(qualifications);
    Assert.assertArrayEquals("Should only contain " + berta, result,
        referentFilter.filter(personRepo.streamAll()).toArray());
  }
}
