package de.naju.adebar.web.validation.persons.relatives;

import de.naju.adebar.model.core.PhoneNumber;

/**
 * POJO representation of the parent related information in the 'edit person' form
 *
 * @author Rico Bergmann
 */
public class EditParentProfileForm {

  private String workPhone;
  private String landlinePhone;

  /**
   * Full constructor
   *
   * @param workPhone the parent's phone number at work. May be {@code null} if the parent does
   *     not have one
   * @param landlinePhone the parent's phone number at home. May be {@code null} if the parent
   *     does not have one
   */
  public EditParentProfileForm(PhoneNumber workPhone, PhoneNumber landlinePhone) {
    this.workPhone = workPhone != null ? workPhone.getValue() : null;
    this.landlinePhone = landlinePhone != null ? landlinePhone.getValue() : null;
  }

  /**
   * Default constructor
   */
  public EditParentProfileForm() {}

  /**
   * @return the parent's phone number at work. May be {@code null} if the parent does not have one.
   */
  public String getWorkPhone() {
    return workPhone;
  }

  /**
   * @param workPhone the parent's phone number at work. May be {@code null} if the parent does
   *     not have one.
   */
  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }

  /**
   * @return whether the parent has a special phone number when at work
   */
  public boolean hasWorkPhone() {
    return workPhone != null && !workPhone.isEmpty();
  }

  /**
   * @return the parent's phone number at home. May be {@code null} if the parent does not have one.
   */
  public String getLandlinePhone() {
    return landlinePhone;
  }

  /**
   * @param landlinePhone the parent's phone number at home. May be {@code null} if the parent
   *     does not have one.
   */
  public void setLandlinePhone(String landlinePhone) {
    this.landlinePhone = landlinePhone;
  }

  /**
   * @return whether the parent has a specila phone number when at home
   */
  public boolean hasLandlinePhone() {
    return landlinePhone != null && !landlinePhone.isEmpty();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EditParentForm [workPhone=" + workPhone + ", landlinePhone=" + landlinePhone + "]";
  }

}
