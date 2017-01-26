package de.naju.adebar.model.human;

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
        new Person("Hans", "Wurst", "hans@wurst.de", Gender.FEMALE, new Address(), dob);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        new Person("Hans", "Wurst", "hans@.de", Gender.FEMALE, new Address(), dob);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateOfBirth() {
        new Person("Hans", "Wurst", "hans@.de", Gender.FEMALE, new Address(), LocalDate.MAX);
    }
}
