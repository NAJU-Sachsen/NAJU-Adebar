package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.human.Qualification;
import de.naju.adebar.util.conversion.PersonConverter;
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
 * Basic testing of the {@link ReferentFilter}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class ReferentFilterUnitTest extends FilterTestBootstrapper {
    @Autowired private PersonConverter streamConverter;
    private ReferentFilter referentFilter;

    @Test public void testEnforceReferent() {
        List<Person> result = Arrays.asList(berta, fritz);
        referentFilter = new ReferentFilter(streamConverter.convertReferentStream(referentRepo.streamAll()), FilterType.ENFORCE);
        Assert.assertArrayEquals("Should only contain referents", result.toArray(), referentFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testIgnoreReferent() {
        List<Person> result = Arrays.asList(hans, claus, heinz);
        referentFilter = new ReferentFilter(streamConverter.convertReferentStream(referentRepo.streamAll()), FilterType.IGNORE);
        Assert.assertArrayEquals("Should not contain referents", result.toArray(), referentFilter.filter(personRepo.streamAll()).toArray());
    }

    @Test public void testFilterQualifications() {
        Person[] result = {berta};
        List<Qualification> qualifications = Arrays.asList(bertaQualification1, bertaQualification2);
        referentFilter = new ReferentFilter(streamConverter.convertReferentStream(referentRepo.streamAll()), qualifications,
                                            referentManager.getQualifications());
        Assert.assertArrayEquals("Should only contain " + berta, result, referentFilter.filter(personRepo.streamAll()).toArray());
    }
}
