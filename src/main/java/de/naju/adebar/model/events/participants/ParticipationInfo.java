package de.naju.adebar.model.events.participants;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Meta-data needed for the participating persons
 *
 * @author Rico Bergmann
 */
@Embeddable
public class ParticipationInfo {

  @Column(name = "acknowledged")
  private boolean acknowledged;

  @Column(name = "feePayed")
  private boolean participationFeePayed;

  @Column(name = "formFilled")
  private boolean registrationFormFilled;

  @Column(name = "remarks")
  private String remarks;

  /**
   * Only a default constructor is needed. All participation info looks the same at the beginning.
   */
  ParticipationInfo() {}

  /**
   * @return whether the participation was acknowledged
   */
  public boolean isAcknowledged() {
    return acknowledged;
  }

  /**
   * @param acknowledged whether the participation was acknowledged
   */
  public void setAcknowledged(boolean acknowledged) {
    this.acknowledged = acknowledged;
  }

  /**
   * @return whether the participation fee was payed
   */
  public boolean isParticipationFeePayed() {
    return participationFeePayed;
  }

  /**
   * @param participationFeePayed whether the participation fee was payed
   */
  public void setParticipationFeePayed(boolean participationFeePayed) {
    this.participationFeePayed = participationFeePayed;
  }

  /**
   * @return whether the registration form (featuring signature, etc.) was received
   */
  public boolean isRegistrationFormFilled() {
    return registrationFormFilled;
  }

  /**
   * @param registrationFormReceived whether the registration form (featuring signature, etc.)
   *     was received
   */
  public void setRegistrationFormFilled(boolean registrationFormReceived) {
    this.registrationFormFilled = registrationFormReceived;
  }

  /**
   * @return special remarks for the participation
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @param remarks special remarks for the participation
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  // overridden from object

  @Override
  public String toString() {
    return "ParticipationInfo{" + "acknowledged=" + acknowledged + ", participationFeePayed="
        + participationFeePayed + ", registrationFormFilled=" + registrationFormFilled + '}';
  }
}
