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
        new Person("Hans", "Wurst", "hans@wurst.de", "", Gender.FEMALE, new Address(), dob);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        new Person("Hans", "Wurst", "hans@.de", "", Gender.FEMALE, new Address(), dob);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDateOfBirth() {
        new Person("Hans", "Wurst", "hans@.de", "", Gender.FEMALE, new Address(), LocalDate.MAX);
    }

    @Test
    public void testCalculateAge() {
        LocalDate dob = LocalDate.now().minusDays(1);
        Person hans = new Person("Hans", "Wurst", "hans@web.de", "", Gender.FEMALE, new Address(), dob);

        int age = 0;
        Assert.assertEquals(age, hans.calculateAge());

        age = 5;
        hans.setDateOfBirth(dob.minusYears(age));
        Assert.assertEquals(age, hans.calculateAge());

        age = 42;
        hans.setDateOfBirth(dob.minusYears(age));
        Assert.assertEquals(age, hans.calculateAge());

    }
}
