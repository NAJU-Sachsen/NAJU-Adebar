package de.naju.adebar.model.human;

import org.junit.Test;

/**
 * Basic testing of the {@link Person} class
 * @author Rico Bergmann
 */
public class PersonUnitTest {
    @Test
    public void testValidEmail() {
        new Person(new PersonId(),"Hans", "Wurst", "hans@wurst.de");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail() {
        new Person(new PersonId(),"Hans", "Wurst", "hans@.de");
    }
}
