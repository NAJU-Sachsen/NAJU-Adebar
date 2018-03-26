package de.naju.adebar.web.validation.persons.referent;

import java.util.List;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.web.validation.AbstractForm;
import de.naju.adebar.web.validation.persons.AddPersonForm;

/**
 * POJO representation of the referent-related data of the {@link AddPersonForm}
 *
 * @author Rico Bergmann
 */
public class AddReferentForm implements AbstractForm {

  private List<Qualification> qualifications;

  /**
   * Full constructor
   *
   * @param qualifications the refernt's qualifications
   */
  public AddReferentForm(List<Qualification> qualifications) {
    this.qualifications = qualifications;
  }

  /**
   * Default constructor
   */
  public AddReferentForm() {}

  /**
   * @return the referent's qualifications
   */
  public List<Qualification> getQualifications() {
    return qualifications;
  }

  /**
   * @param qualifications the referent's qualifications
   */
  public void setQualifications(List<Qualification> qualifications) {
    this.qualifications = qualifications;
  }

  @Override
  public boolean hasData() {
    return qualifications != null && !qualifications.isEmpty();
  }

  @Override
  public String toString() {
    return "AddReferentForm [" + "qualifications=" + qualifications + ']';
  }

}
