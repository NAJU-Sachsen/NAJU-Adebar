package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.app.persons.filter.FilterTestBootstrapper;
import de.naju.adebar.app.persons.filter.stream.DateOfBirthFilter;
import de.naju.adebar.model.persons.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;

/**
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
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
    Assert.assertArrayEquals("Arrays do not match", result,
        dateOfBirthFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testDobBefore() {
    Person[] result = {claus, fritz};
    dateOfBirthFilter = new DateOfBirthFilter(bertaDob, DateFilterType.BEFORE);
    System.out.println(Arrays.toString(dateOfBirthFilter.filter(personRepo.streamAll()).toArray()));
    Assert.assertArrayEquals("Arrays do not match", result,
        dateOfBirthFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testDobAfter() {
    Person[] result = {hans};
    dateOfBirthFilter = new DateOfBirthFilter(bertaDob, DateFilterType.AFTER);
    Assert.assertArrayEquals("Arrays do not match", result,
        dateOfBirthFilter.filter(personRepo.streamAll()).toArray());
  }

}
