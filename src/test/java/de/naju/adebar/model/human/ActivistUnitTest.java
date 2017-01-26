package de.naju.adebar.model.human;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Basic testing of the {@link Activist} class
 * @author Rico Bergmann
 */
public class ActivistUnitTest {
    private PersonId personId;

    @Before public void setUp() {
        this.personId = new PersonId();
    }

    @Test public void testHasJuleica() {
        Activist activist = new Activist(personId, LocalDate.now());
        Assert.assertTrue("Activist should have a JuLeiCa card", activist.hasJuleica());
    }

    @Test public void testHasNoJuleica() {
        Activist activist = new Activist(personId, null);
        Assert.assertFalse("Activist should have no JuLeiCa card", activist.hasJuleica());
    }

    @Test public void testAddJuleica() {
        Activist activist = new Activist(personId, null);
        activist.setJuleicaExpiryDate(LocalDate.now());
        Assert.assertTrue("Activist should have a JuLeiCa card", activist.hasJuleica());
    }

    @Test public void testRemoveJuleica() {
        Activist activist = new Activist(personId, LocalDate.now());
        activist.setJuleicaExpiryDate(null);
        Assert.assertFalse("Activist should have no JuLeiCa card", activist.hasJuleica());
    }


}
