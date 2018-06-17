package de.naju.adebar.web.validation.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.events.participation.ParticipationInfoForm;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

public class EditEventForm {

  public enum Belonging {
    LOCAL_GROUP,
    PROJECT;

    public static Belonging getBelongingFor(Event event) {
      if (event.isForLocalGroup()) {
        return LOCAL_GROUP;
      } else if (event.isForProject()) {
        return PROJECT;
      }
      return null;
    }

  }

  protected String name;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  protected LocalDate startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  protected LocalDate endDate;

  protected boolean useEventTime;

  @DateTimeFormat(pattern = "HH:mm")
  protected LocalTime startTime;

  @DateTimeFormat(pattern = "HH:mm")
  protected LocalTime endTime;

  protected AddressForm place;

  protected ParticipationInfoForm participationInfo;

  protected Belonging belonging = Belonging.LOCAL_GROUP;

  protected long localGroup = -1;

  protected long project = -1;

  public EditEventForm() {}

  public EditEventForm(String name, LocalDate startDate, LocalDate endDate, boolean useEventTime,
      LocalTime startTime, LocalTime endTime, AddressForm place,
      ParticipationInfoForm participationInfo,
      Belonging belonging, long localGroup, long project) {
    this.name = name;
    this.startDate = startDate;
    this.endDate = endDate;
    this.useEventTime = useEventTime;
    this.startTime = startTime;
    this.endTime = endTime;
    this.place = place;
    this.participationInfo = participationInfo;
    this.belonging = belonging;
    this.localGroup = localGroup;
    this.project = project;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public boolean isUseEventTime() {
    return useEventTime;
  }

  public void setUseEventTime(boolean useEventTime) {
    this.useEventTime = useEventTime;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(LocalTime endTime) {
    this.endTime = endTime;
  }

  public AddressForm getPlace() {
    return place;
  }

  public void setPlace(AddressForm place) {
    this.place = place;
  }

  public ParticipationInfoForm getParticipationInfo() {
    return participationInfo;
  }

  public void setParticipationInfo(ParticipationInfoForm participationInfo) {
    this.participationInfo = participationInfo;
  }

  public Belonging getBelonging() {
    return belonging;
  }

  public void setBelonging(Belonging belonging) {
    this.belonging = belonging;
  }

  public long getLocalGroup() {
    return localGroup;
  }

  public void setLocalGroup(long localGroup) {
    this.localGroup = localGroup;
  }

  public long getProject() {
    return project;
  }

  public void setProject(long project) {
    this.project = project;
  }

  @Override
  public String toString() {
    return "EditEventForm [" +
        "name='" + name + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", useEventTime=" + useEventTime +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", place=" + place +
        ", participationInfo=" + participationInfo +
        ", belonging=" + belonging +
        ", localGroup=" + localGroup +
        ", project=" + project +
        ']';
  }

}
