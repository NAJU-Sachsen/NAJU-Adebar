package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.app.persons.filter.FilterTestBootstrapper;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.persons.Person;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic testing for the {@link AddressFilter}
 *
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Component
public class AddressFilterUnitTest extends FilterTestBootstrapper {

  private Address address;
  private AddressFilter addressFilter;

  @Test(expected = IllegalArgumentException.class)
  public void testNoAtLeastMatch() {
    addressFilter = new AddressFilter(address, MatchType.AT_LEAST);
  }

  @Test
  public void testExactMatch() {
    address = new Address(hansAddress.getStreet(), hansAddress.getZip(), hansAddress.getCity());
    addressFilter = new AddressFilter(address, MatchType.EXACT);
    Person[] result = {hans};
    Assert.assertArrayEquals("Arrays do not match!", result,
        addressFilter.filter(personRepo.streamAll()).toArray());
  }

  @Test
  public void testIfDefinedMatch() {
    address = new Address(hansAddress.getStreet(), hansAddress.getZip(), "");
    Person[] result = {hans};
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude city as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());

    address = new Address(hansAddress.getStreet(), "", hansAddress.getCity());
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude zip as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());

    address = new Address("", hansAddress.getZip(), hansAddress.getCity());
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude street as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());

    address = new Address(hansAddress.getStreet(), "", "");
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude city and zip as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());

    address = new Address("", "", hansAddress.getCity());
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude street and zip as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());

    address = new Address("", hansAddress.getZip(), "");
    addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
    Assert.assertArrayEquals("Should exclude street and city as criteria", result,
        addressFilter.filter(personRepo.streamAll()).toArray());
  }
}
