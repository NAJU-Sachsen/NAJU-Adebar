package de.naju.adebar.model.human;

import com.google.common.collect.Iterables;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic testing of the {@link Referent} class
 * @author Rico Bergmann
 */
public class ReferentUnitTest {
    private Referent referent;
    private Qualification qualification;

    @Before public void setUp() {
        this.referent = new Referent(new PersonId());
        this.qualification = new Qualification("Erste Hilfe Kurs",
                "Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullQualification() {
        referent.addQualification(null);
    }

    @Test
    public void testAddQualification() {
        referent.addQualification(qualification);
        Assert.assertTrue(String.format("%s should have qualification %s", referent, qualification),
                referent.hasQualification(qualification));
    }

    @Test
    public void testRemoveQualification() {
        referent.addQualification(qualification);
        referent.removeQualification(qualification);
        Assert.assertFalse(String.format("%s should not have qualification %s any more", referent, qualification),
                referent.hasQualification(qualification));
        Assert.assertFalse(String.format("%s should not have qualification %s any more", referent, qualification),
                Iterables.contains(referent.getQualifications(), qualification));
    }


}
