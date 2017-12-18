package de.naju.adebar.controller.forms.human;

import java.util.List;

/**
 * Model POJO for new persons. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class CreatePersonForm extends EditPersonForm {

  private boolean activist;
  private boolean referent;
  private boolean hasJuleica;
  private String juleicaExpiryDate;
  private List<String> qualifications;
  private List<Long> localGroups;

  public boolean isActivist() {
    return activist;
  }

  public void setActivist(boolean activist) {
    this.activist = activist;
  }

  public boolean isReferent() {
    return referent;
  }

  public void setReferent(boolean referent) {
    this.referent = referent;
  }

  public boolean getHasJuleica() {
    return hasJuleica;
  }

  public void setHasJuleica(boolean hasJuleica) {
    this.hasJuleica = hasJuleica;
  }

  public boolean hasJuleicaExpiryDate() {
    return juleicaExpiryDate != null && !juleicaExpiryDate.isEmpty();
  }

  public String getJuleicaExpiryDate() {
    return juleicaExpiryDate;
  }

  public void setJuleicaExpiryDate(String juleicaExpiryDate) {
    this.juleicaExpiryDate = juleicaExpiryDate;
  }

  public List<String> getQualifications() {
    return qualifications;
  }

  public void setQualifications(List<String> qualifications) {
    this.qualifications = qualifications;
  }

  public List<Long> getLocalGroups() {
    return localGroups;
  }

  public void setLocalGroups(List<Long> localGroups) {
    this.localGroups = localGroups;
  }

  @Override
  public String toString() {
    return "CreatePersonForm{" + "activist=" + activist + ", referent=" + referent
        + ", juleicaCard=" + hasJuleica + ", juleicaExpiryDate='" + juleicaExpiryDate + '\''
        + ", qualifications=" + qualifications + '}';
  }
}
