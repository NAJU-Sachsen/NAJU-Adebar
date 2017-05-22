package de.naju.adebar.model.human;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

/**
 * Basic testing of the {@link Person} class
 * @author Rico Bergmann
 */
public class PersonUnitTest {
    private LocalDate dob = LocalDate.now().minusDays(1);

    @Test
    public void testValidEmail() {
        new Person(new PersonId(),"Hans", "Wurst", "hans@wurst.de");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        new Person(new PersonId(),"Hans", "Wurst", "hans@.de");
    }
}
