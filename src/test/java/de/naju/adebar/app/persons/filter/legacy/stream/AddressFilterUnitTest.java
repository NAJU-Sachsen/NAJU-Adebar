package de.naju.adebar.app.persons.filter.legacy.stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

import de.naju.adebar.app.filter.legacy.MatchType;
import de.naju.adebar.app.persons.filter.legacy.FilterTestBootstrapper;
import de.naju.adebar.app.persons.legacy.filter.stream.AddressFilter;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.persons.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic testing for the {@link AddressFilter}
 *
 * @author Rico Bergmann
 */
@SpringBootTest
@Transactional
@Rollback
@Component
public class AddressFilterUnitTest extends FilterTestBootstrapper {

	private Address address;
	private AddressFilter addressFilter;

	@Test
	public void testNoAtLeastMatch() {
		assertThrows(IllegalArgumentException.class, () -> {
			addressFilter = new AddressFilter(address, MatchType.AT_LEAST);
		});
	}

	@Test
	public void testExactMatch() {
		address = new Address(hansAddress.getStreet(), hansAddress.getZip(), hansAddress.getCity());
		addressFilter = new AddressFilter(address, MatchType.EXACT);
		Person[] result = {hans};
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Arrays do not match!");
	}

	@Test
	public void testIfDefinedMatch() {
		address = new Address(hansAddress.getStreet(), hansAddress.getZip(), "");
		Person[] result = {hans};
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude city as criteria");

		address = new Address(hansAddress.getStreet(), "", hansAddress.getCity());
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude zip as criteria");

		address = new Address("", hansAddress.getZip(), hansAddress.getCity());
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude street as criteria");

		address = new Address(hansAddress.getStreet(), "", "");
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude city and zip as criteria");

		address = new Address("", "", hansAddress.getCity());
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude street and zip as criteria");

		address = new Address("", hansAddress.getZip(), "");
		addressFilter = new AddressFilter(address, MatchType.IF_DEFINED);
		assertSameElements(result, addressFilter.filter(personRepo.streamAll()).toArray(),
				"Should exclude street and city as criteria");
	}
}
