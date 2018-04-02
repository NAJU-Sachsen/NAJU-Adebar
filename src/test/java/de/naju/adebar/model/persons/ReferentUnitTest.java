package de.naju.adebar.model.persons;

import com.google.common.collect.Iterables;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.qualifications.Qualification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Basic testing of the {@link ReferentProfile} class
 *
 * @author Rico Bergmann
 */
public class ReferentUnitTest {

  private Person referent;
  private Qualification qualification;

  @Before
  public void setUp() {
    referent = new Person(new PersonId(), "Hans", "Wurst", Email.of("hw@web.de"));
    referent.makeReferent();
    this.qualification = new Qualification("Erste Hilfe Kurs",
        "Hat die Qualifikation, einen Erste-Hilfe Kurs zu leiten");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddNullQualification() {
    referent.getReferentProfile().addQualification(null);
  }

  @Test
  public void testAddQualification() {
    referent.getReferentProfile().addQualification(qualification);
    Assert.assertTrue(String.format("%s should have qualification %s", referent, qualification),
        referent.getReferentProfile().hasQualification(qualification));
  }

  @Test
  public void testRemoveQualification() {
    referent.getReferentProfile().addQualification(qualification);
    referent.getReferentProfile().removeQualification(qualification);

    Assert.assertFalse(
        String.format("%s should not have qualification %s any more", referent, qualification),
        referent.getReferentProfile().hasQualification(qualification));
    Assert.assertFalse(
        String.format("%s should not have qualification %s any more", referent, qualification),
        Iterables.contains(referent.getReferentProfile().getQualifications(), qualification));
  }

}
