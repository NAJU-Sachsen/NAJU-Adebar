package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Stores information about an activist for one specific {@link Event} he/she attends as counselor.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class CounselorInfo implements ParticipationInfoWithDynamicTime {

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "description", column = @Column(name = "arrival")))
  private ArrivalOption arrivalOption;

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "description",
      column = @Column(name = "departure")))
  private ArrivalOption departureOption;

  @Column(name = "remarks", length = 511)
  private String remarks;

  @Embedded
  private ParticipationTime participationTime;

  /**
   * Full constructor.
   */
  private CounselorInfo(ArrivalOption arrivalOption, ArrivalOption departureOption, String remarks,
      ParticipationTime participationTime) {
    this.arrivalOption = arrivalOption;
    this.departureOption = departureOption;
    this.remarks = remarks;
    this.participationTime = participationTime;
  }

  /**
   * Default constructor.
   */
  CounselorInfo() {}

  /**
   * Starts the construction process for a new counselor.
   *
   * @return the builder which takes care of the further setup
   */
  public static CounselorInfoBuilder buildNew() {
    return new CounselorInfoBuilder();
  }

  /**
   * Gets the selected approach to this event.
   *
   * @return the option. May be {@code null} if none was chosen.
   */
  public ArrivalOption getArrivalOption() {
    return arrivalOption;
  }

  /**
   * Gets the selected leave from this event.
   *
   * @return the option. May be {@code null} if none was chosen.
   */
  public ArrivalOption getDepartureOption() {
    return departureOption;
  }

  /**
   * Gets additional information that are important for the counselor's participation.
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * Checks, whether there are any important information for this participation.
   */
  public boolean hasRemarks() {
    return remarks != null && !remarks.isEmpty();
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
   * Updates the period that the counselor attends this event.
   *
   * @param participationTime the time. If {@code null} is passed, it will be treated as "does
   *     not differ from the span the event takes place".
   */
  void setParticipationTime(ParticipationTime participationTime) {
    this.participationTime = participationTime;
  }

  /**
   * Updates the selected arrival option.
   *
   * @param arrivalOption the option. A {@code null} value means "none chosen".
   */
  @JpaOnly
  private void setArrivalOption(ArrivalOption arrivalOption) {
    this.arrivalOption = arrivalOption;
  }

  /**
   * Updates the selected departure option.
   *
   * @param departureOption the option. A {@code null} value means "none chosen".
   */
  @JpaOnly
  private void setDepartureOption(ArrivalOption departureOption) {
    this.departureOption = departureOption;
  }

  /**
   * Sets important information concerning this participation for the attending counselor.
   */
  @JpaOnly
  private void setRemarks(String remarks) {
    if (remarks == null) {
      this.remarks = "";
    } else {
      this.remarks = remarks;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(arrivalOption, departureOption, remarks, participationTime);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CounselorInfo that = (CounselorInfo) o;
    return Objects.equals(arrivalOption, that.arrivalOption) &&
        Objects.equals(departureOption, that.departureOption) &&
        Objects.equals(remarks, that.remarks) &&
        Objects.equals(participationTime, that.participationTime);
  }

  @Override
  public String toString() {
    return "CounselorInfo [" +
        "arrivalOption=" + arrivalOption +
        ", departureOption=" + departureOption +
        ", remarks='" + remarks + '\'' +
        ", participationTime=" + participationTime +
        ']';
  }

  /**
   * The {@code Builder} takes care of creating new {@link CounselorInfo} instances.
   */
  public static class CounselorInfoBuilder {

    private CounselorInfo infoUnderConstruction = new CounselorInfo();

    /**
     * Bases the new information on some existing data.
     */
    public CounselorInfoBuilder fromInfo(CounselorInfo existingInfo) {
      this.infoUnderConstruction = new CounselorInfo(
          existingInfo.arrivalOption,
          existingInfo.departureOption,
          existingInfo.remarks,
          existingInfo.participationTime
      );
      return this;
    }

    /**
     * Sets the chosen arrival and departure options.
     *
     * @param departure the departure option. May be {@code null} if none was selected.
     * @param arrival the arrival option. May be {@code null} if none was selected.
     */
    public CounselorInfoBuilder withArrivalAndDeparture(ArrivalOption arrival,
        ArrivalOption departure) {
      infoUnderConstruction.setArrivalOption(arrival);
      infoUnderConstruction.setDepartureOption(departure);
      return this;
    }

    /**
     * Adds special information about the participation of this counselor.
     */
    public CounselorInfoBuilder withRemarks(String remarks) {
      infoUnderConstruction.setRemarks(remarks);
      return this;
    }

    /**
     * Sets the participation time.
     *
     * @param timeSpan the period. May be {@code null} to indicate that it does not differ from
     *     the event's time
     */
    public CounselorInfoBuilder withParticipationDuring(ParticipationTime timeSpan) {
      infoUnderConstruction.setParticipationTime(timeSpan);
      return this;
    }

    /**
     * Finishes the construction and provides the resulting info.
     */
    public CounselorInfo build() {
      return infoUnderConstruction;
    }

  }

}
