package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class CounselorInfo implements DynamicParticipationTime {

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

  public static CounselorInfoBuilder buildNew() {
    return new CounselorInfoBuilder();
  }

  public CounselorInfo(ArrivalOption arrivalOption, ArrivalOption departureOption, String remarks,
      ParticipationTime participationTime) {
    this.arrivalOption = arrivalOption;
    this.departureOption = departureOption;
    this.remarks = remarks;
    this.participationTime = participationTime;
  }

  CounselorInfo() {}

  public ArrivalOption getArrivalOption() {
    return arrivalOption;
  }

  public ArrivalOption getDepartureOption() {
    return departureOption;
  }

  public String getRemarks() {
    return remarks;
  }

  public boolean hasRemarks() {
    return remarks != null && !remarks.isEmpty();
  }

  void setParticipationTime(
      ParticipationTime participationTime) {
    this.participationTime = participationTime;
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
  private void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  @Override
  public ParticipationTime getParticipationTime() {
    return participationTime;
  }


  @Override
  public boolean hasParticipationTime() {
    return participationTime != null;
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

  public static class CounselorInfoBuilder {

    private CounselorInfo infoUnderConstruction;

    private CounselorInfoBuilder() {
      this.infoUnderConstruction = new CounselorInfo();
    }


    public CounselorInfoBuilder withArrivalAndDeparture(ArrivalOption arrival,
        ArrivalOption departure) {
      infoUnderConstruction.setArrivalOption(arrival);
      infoUnderConstruction.setDepartureOption(departure);
      return this;
    }

    public CounselorInfoBuilder withRemarks(String remarks) {
      infoUnderConstruction.setRemarks(remarks);
      return this;
    }

    public CounselorInfoBuilder withParticipationDuring(ParticipationTime timeSpan) {
      infoUnderConstruction.setParticipationTime(timeSpan);
      return this;
    }

    public CounselorInfo build() {
      return infoUnderConstruction;
    }

  }

}
