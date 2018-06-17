package de.naju.adebar.web.validation.events.organization;

import de.naju.adebar.model.events.ArrivalOption;
import de.naju.adebar.model.events.CounselorInfo;
import de.naju.adebar.model.events.CounselorInfo.CounselorInfoBuilder;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

public class CounselorForm {

  private Person counselor;

  private ArrivalOption arrival;

  private ArrivalOption departure;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate firstNight;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate lastNight;

  private String remarks;

  public CounselorForm(Event event) {
    if (event != null && event.getParticipationInfo().isFlexibleParticipationTimesEnabled()) {
      this.firstNight = event.getStartTime().toLocalDate();
      this.lastNight = event.getEndTime().toLocalDate().minusDays(1L);
    }
  }

  public Person getCounselor() {
    return counselor;
  }

  public void setCounselor(Person counselor) {
    this.counselor = counselor;
  }

  public ArrivalOption getArrival() {
    return arrival;
  }

  public void setArrival(ArrivalOption arrival) {
    this.arrival = arrival;
  }

  public ArrivalOption getDeparture() {
    return departure;
  }

  public void setDeparture(ArrivalOption departure) {
    this.departure = departure;
  }

  public LocalDate getFirstNight() {
    return firstNight;
  }

  public void setFirstNight(LocalDate firstNight) {
    this.firstNight = firstNight;
  }

  public LocalDate getLastNight() {
    return lastNight;
  }

  public void setLastNight(LocalDate lastNight) {
    this.lastNight = lastNight;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public LocalDateTime generateFirstNightAsLDT() {
    if (firstNight == null) {
      return null;
    }
    return LocalDateTime.of(firstNight, LocalTime.MIDNIGHT);
  }

  public LocalDateTime generateLastNightAsLDT() {
    if (lastNight == null) {
      return null;
    }
    return LocalDateTime.of(lastNight, LocalTime.MIDNIGHT);
  }

  public CounselorInfoBuilder prepareCounselorInfo() {
    return CounselorInfo.buildNew() //
        .withArrivalAndDeparture(arrival, departure) //
        .withRemarks(remarks);
  }

  @Override
  public String toString() {
    return "CounselorForm [" +
        "counselor=" + counselor +
        ", arrival=" + arrival +
        ", departure=" + departure +
        ", firstNight=" + firstNight +
        ", lastNight=" + lastNight +
        ", remarks='" + remarks + '\'' +
        ']';
  }

}
