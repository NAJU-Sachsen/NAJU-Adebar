package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Meta-data needed for the participating persons
 *
 * @author Rico Bergmann
 */
@Embeddable
public class RegistrationInfo implements DynamicParticipationTime {

  @Column(name = "acknowledged")
  private boolean acknowledged;

  @Column(name = "feePayed")
  private boolean participationFeePayed;

  @Column(name = "formSent")
  private boolean registrationFormSent;

  @Column(name = "formFilled")
  private boolean registrationFormFilled;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "description", column = @Column(name = "arrival")))
  private ArrivalOption arrivalOption;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "description",
      column = @Column(name = "departure")))
  private ArrivalOption departureOption;

  @Column(name = "goHomeSingly")
  private boolean mayGoHomeSingly;

  @Column(name = "remarks", length = 511)
  private String remarks;

  @Embedded
  private ParticipationTime participationTime;

  @Column(name = "registrationDate")
  private LocalDateTime registrationDate = LocalDateTime.now();

  public static RegistrationInfoBuilder buildNew() {
    return new RegistrationInfoBuilder();
  }

  public RegistrationInfo(boolean registrationFormSent, String remarks) {
    this.registrationFormSent = registrationFormSent;
    this.remarks = remarks;
  }

  public RegistrationInfo(boolean registrationFormSent, ArrivalOption arrival,
      ArrivalOption departure, String remarks) {
    this.registrationFormSent = registrationFormSent;
    this.arrivalOption = arrival;
    this.departureOption = departure;
    this.remarks = remarks;
  }

  public RegistrationInfo(boolean participationFeePayed,
      boolean registrationFormSent, boolean registrationFormFilled, ArrivalOption arrivalOption,
      ArrivalOption departureOption, boolean mayGoHomeSingly, ParticipationTime participationTime,
      String remarks) {
    this.participationFeePayed = participationFeePayed;
    this.registrationFormSent = registrationFormFilled || registrationFormSent;
    this.registrationFormFilled = registrationFormFilled;
    this.arrivalOption = arrivalOption;
    this.departureOption = departureOption;
    this.mayGoHomeSingly = mayGoHomeSingly;
    this.participationTime = participationTime;
    this.remarks = remarks;
  }

  RegistrationInfo() {}

  /**
   * @return whether the participation was acknowledged
   */
  public boolean isAcknowledged() {
    return acknowledged;
  }


  public boolean isRegistrationFormSent() {
    return registrationFormSent;
  }

  /**
   * @return whether the participation fee was payed
   */
  public boolean isParticipationFeePayed() {
    return participationFeePayed;
  }

  /**
   * @return whether the registration form (featuring signature, etc.) was received
   */
  public boolean isRegistrationFormFilled() {
    return registrationFormFilled;
  }

  public ArrivalOption getArrivalOption() {
    return arrivalOption;
  }

  public ArrivalOption getDepartureOption() {
    return departureOption;
  }

  public boolean isMayGoHomeSingly() {
    return mayGoHomeSingly;
  }

  @Override
  public ParticipationTime getParticipationTime() {
    return participationTime;
  }

  @Override
  public boolean hasParticipationTime() {
    return participationTime != null;
  }

  /**
   * @return special remarks for the participation
   */
  public String getRemarks() {
    return remarks;
  }

  public LocalDateTime getRegistrationDate() {
    return registrationDate;
  }

  void setRegistrationDate(LocalDateTime registrationDate) {
    this.registrationDate = registrationDate;
  }

  void setParticipationTime(ParticipationTime participationTime) {
    this.participationTime = participationTime;
  }

  /**
   * @param acknowledged whether the participation was acknowledged
   */
  @JpaOnly
  private void setAcknowledged(boolean acknowledged) {
    this.acknowledged = acknowledged;
  }

  @JpaOnly
  private void setRegistrationFormSent(boolean registrationFormSent) {
    this.registrationFormSent = registrationFormSent;
  }

  /**
   * @param participationFeePayed whether the participation fee was payed
   */
  @JpaOnly
  private void setParticipationFeePayed(boolean participationFeePayed) {
    this.participationFeePayed = participationFeePayed;
  }

  /**
   * @param registrationFormFilled whether the registration form (featuring signature, etc.) was
   *     received
   */
  @JpaOnly
  private void setRegistrationFormFilled(boolean registrationFormFilled) {
    this.registrationFormFilled = registrationFormFilled;

    if (registrationFormFilled) {
      setRegistrationFormSent(true);
    }

  }

  @JpaOnly
  private void setArrivalOption(ArrivalOption arrivalOption) {
    this.arrivalOption = arrivalOption;
  }

  @JpaOnly
  private void setDepartureOption(ArrivalOption departureOption) {
    this.departureOption = departureOption;
  }

  @JpaOnly
  private void setMayGoHomeSingly(boolean mayGoHomeSingly) {
    this.mayGoHomeSingly = mayGoHomeSingly;
  }

  /**
   * @param remarks special remarks for the participation
   */
  @JpaOnly
  private void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {

    return Objects
        .hash(acknowledged, participationFeePayed, registrationFormSent, registrationFormFilled,
            arrivalOption, departureOption, mayGoHomeSingly, remarks, participationTime,
            registrationDate);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegistrationInfo that = (RegistrationInfo) o;
    return acknowledged == that.acknowledged &&
        participationFeePayed == that.participationFeePayed &&
        registrationFormSent == that.registrationFormSent &&
        registrationFormFilled == that.registrationFormFilled &&
        mayGoHomeSingly == that.mayGoHomeSingly &&
        Objects.equals(arrivalOption, that.arrivalOption) &&
        Objects.equals(departureOption, that.departureOption) &&
        Objects.equals(remarks, that.remarks) &&
        Objects.equals(participationTime, that.participationTime) &&
        Objects.equals(registrationDate, that.registrationDate);
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RegistrationInfo [" +
        "acknowledged=" + acknowledged +
        ", participationFeePayed=" + participationFeePayed +
        ", registrationFormSent=" + registrationFormSent +
        ", registrationFormFilled=" + registrationFormFilled +
        ", arrivalOption=" + arrivalOption +
        ", departureOption=" + departureOption +
        ", mayGoHomeSingly=" + mayGoHomeSingly +
        ", remarks='" + remarks + '\'' +
        ", participationTime=" + participationTime +
        ", registrationDate=" + registrationDate +
        ']';
  }

  public static class RegistrationInfoBuilder {

    private RegistrationInfo infoUnderConstruction;

    private RegistrationInfoBuilder() {
      this.infoUnderConstruction = new RegistrationInfo();
    }

    public RegistrationInfoBuilder specifyRegistrationForm(boolean wasSent, boolean wasFilled) {
      infoUnderConstruction.setRegistrationFormSent(wasSent);
      infoUnderConstruction.setRegistrationFormFilled(wasFilled);
      return this;
    }

    public RegistrationInfoBuilder specifyParticipationFee(boolean wasPayed) {
      infoUnderConstruction.setParticipationFeePayed(wasPayed);
      return this;
    }

    public RegistrationInfoBuilder withArrivalAndDeparture(ArrivalOption arrival,
        ArrivalOption departure, boolean mayGoHomeSingly) {
      infoUnderConstruction.setArrivalOption(arrival);
      infoUnderConstruction.setDepartureOption(departure);
      infoUnderConstruction.setMayGoHomeSingly(mayGoHomeSingly);
      return this;
    }

    public RegistrationInfoBuilder withRemarks(String remarks) {
      infoUnderConstruction.setRemarks(remarks);
      return this;
    }

    public RegistrationInfoBuilder withParticipationDuring(ParticipationTime timeSpan) {
      infoUnderConstruction.setParticipationTime(timeSpan);
      return this;
    }

    public RegistrationInfo build() {
      return infoUnderConstruction;
    }

  }

}
