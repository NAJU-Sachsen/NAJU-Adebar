package de.naju.adebar.app.human.filter;

import de.naju.adebar.model.human.Person;
import de.naju.adebar.util.conversion.PersonStreamConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Basic testing of the {@link ActivistFilter}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class ActivistFilterUnitTest extends FilterTestBootstrapper {
    @Autowired private PersonStreamConverter streamConverter;
    private ActivistFilter activistFilter;

    @Test public void testEnforceActivists() {
        List<Person> result = Arrays.asList(hans, claus, berta);
        activistFilter = new ActivistFilter(streamConverter.convertActivistStream(activistRepo.streamAll()), FilterType.ENFORCE);
        Assert.assertArrayEquals("Should only contain activists", result.toArray(), activistFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testIgnoreActivists() {
        Person[] result = {fritz, heinz};
        activistFilter = new ActivistFilter(streamConverter.convertActivistStream(activistRepo.streamAll()), FilterType.IGNORE);
        Assert.assertArrayEquals("Should not contain activists", result, activistFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testJuleicaExpiryBefore() {
        Person[] result = {claus};
        activistFilter = new ActivistFilter(streamConverter.convertActivistStream(activistRepo.streamAll()),
                hansActivist.getJuleicaExpiryDate(), DateFilterType.BEFORE, activistManager.getJuleicaExpiryDates());
        Assert.assertArrayEquals("Should only contain activists with juleica expiry date before " +
                hansActivist.getJuleicaExpiryDate(), result, activistFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testJuleicaExpiryAfter() {
        Person[] result = {berta};
        activistFilter = new ActivistFilter(streamConverter.convertActivistStream(activistRepo.streamAll()),
                hansActivist.getJuleicaExpiryDate(), DateFilterType.AFTER, activistManager.getJuleicaExpiryDates());
        Assert.assertArrayEquals("Should only contain activists with juleica expiry date after " +
                hansActivist.getJuleicaExpiryDate(), result, activistFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testJuleicaExpiryExact() {
        Person[] result = {hans};
        activistFilter = new ActivistFilter(streamConverter.convertActivistStream(activistRepo.streamAll()),
                hansActivist.getJuleicaExpiryDate(), DateFilterType.EXACT, activistManager.getJuleicaExpiryDates());
        Assert.assertArrayEquals("Should only contain activists with juleica expiry date on " +
                hansActivist.getJuleicaExpiryDate(), result, activistFilter.filter(personRepo.streamAll()).toArray());
    }

}
