package de.naju.adebar.model.human;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * Basic testing of the {@link ActivistRepository} and {@link ReadOnlyActivistRepository}
 * @author Rico Bergmann
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Component
public class ActivistRepositoryUnitTest {
    @Autowired private ActivistRepository activistRepo;
    @Autowired private ReadOnlyActivistRepository roActivistRepo;
    private Activist[] activists;
    private Activist hans, berta, claus;

    @Before public void setUp() {
        hans = new Activist(new PersonId("hans"), null);
        berta = new Activist(new PersonId("berta"), null);
        claus = new Activist(new PersonId("claus"), null);
        activists = new Activist[]{hans, berta, claus};
        activistRepo.save(Arrays.asList(activists));
    }

    @Test public void testStreamAll() {
        Assert.assertArrayEquals(activists, activistRepo.streamAll().toArray());
        Assert.assertArrayEquals(activists, roActivistRepo.streamAll().toArray());
    }
}
