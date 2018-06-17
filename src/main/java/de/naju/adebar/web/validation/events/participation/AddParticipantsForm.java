package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.events.ArrivalOption;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.events.RegistrationInfo.RegistrationInfoBuilder;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class AddParticipantsForm {

  private List<Person> additionalParticipants = new ArrayList<>();

  private List<Boolean> additionalFormSent = new ArrayList<>();

  private List<Boolean> additionalFormFilled = new ArrayList<>();

  private List<Boolean> additionalFeePayed = new ArrayList<>();

  private List<ArrivalOption> additionalArrival = new ArrayList<>();

  private List<ArrivalOption> additionalDeparture = new ArrayList<>();

  private List<Boolean> additionalMayGoHomeSingly = new ArrayList<>();

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private List<LocalDate> additionalFirstNight = new ArrayList<>();

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private List<LocalDate> additionalLastNight = new ArrayList<>();

  private List<String> additionalRemarks = new ArrayList<>();

  private Person participant;

  private boolean formSent = true;

  private boolean formFilled;

  private boolean feePayed;

  private ArrivalOption arrival;

  private ArrivalOption departure;

  private boolean mayGoHomeSingly;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate firstNight;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate lastNight;

  private String remarks;

  public AddParticipantsForm(Event event) {
    if (event != null && event.getParticipationInfo().isFlexibleParticipationTimesEnabled()) {
      this.firstNight = event.getStartTime().toLocalDate();
      this.lastNight = event.getEndTime().toLocalDate().minusDays(1L);
    }
  }

  private AddParticipantsForm(AddParticipantsForm form) {
    this.firstNight = form.firstNight;
    this.lastNight = form.lastNight;
  }

  public List<Person> getAdditionalParticipants() {
    return additionalParticipants;
  }

  public void setAdditionalParticipants(
      List<Person> additionalParticipants) {
    this.additionalParticipants = additionalParticipants;
  }

  public List<Boolean> getAdditionalFormSent() {
    return additionalFormSent;
  }

  public void setAdditionalFormSent(List<Boolean> additionalFormSent) {
    this.additionalFormSent = additionalFormSent;
  }

  public List<Boolean> getAdditionalFormFilled() {
    return additionalFormFilled;
  }

  public void setAdditionalFormFilled(List<Boolean> additionalFormFilled) {
    this.additionalFormFilled = additionalFormFilled;
  }

  public List<Boolean> getAdditionalFeePayed() {
    return additionalFeePayed;
  }

  public void setAdditionalFeePayed(List<Boolean> additionalFeePayed) {
    this.additionalFeePayed = additionalFeePayed;
  }

  public List<ArrivalOption> getAdditionalArrival() {
    return additionalArrival;
  }

  public void setAdditionalArrival(
      List<ArrivalOption> additionalArrival) {
    this.additionalArrival = additionalArrival;
  }

  public List<ArrivalOption> getAdditionalDeparture() {
    return additionalDeparture;
  }

  public void setAdditionalDeparture(
      List<ArrivalOption> additionalDeparture) {
    this.additionalDeparture = additionalDeparture;
  }

  public List<Boolean> getAdditionalMayGoHomeSingly() {
    return additionalMayGoHomeSingly;
  }

  public void setAdditionalMayGoHomeSingly(List<Boolean> additionalMayGoHomeSingly) {
    this.additionalMayGoHomeSingly = additionalMayGoHomeSingly;
  }

  public List<LocalDate> getAdditionalFirstNight() {
    return additionalFirstNight;
  }

  public void setAdditionalFirstNight(List<LocalDate> additionalFirstNight) {
    this.additionalFirstNight = additionalFirstNight;
  }

  public List<LocalDate> getAdditionalLastNight() {
    return additionalLastNight;
  }

  public void setAdditionalLastNight(List<LocalDate> additionalLastNight) {
    this.additionalLastNight = additionalLastNight;
  }

  public List<String> getAdditionalRemarks() {
    return additionalRemarks;
  }

  public void setAdditionalRemarks(List<String> additionalRemarks) {
    this.additionalRemarks = additionalRemarks;
  }

  public Person getParticipant() {
    return participant;
  }

  public void setParticipant(Person participant) {
    this.participant = participant;
  }

  public boolean isFormSent() {
    return formSent;
  }

  public void setFormSent(boolean formSent) {
    this.formSent = formSent;
  }

  public boolean isFormFilled() {
    return formFilled;
  }

  public void setFormFilled(boolean formFilled) {
    this.formFilled = formFilled;
  }

  public boolean isFeePayed() {
    return feePayed;
  }

  public void setFeePayed(boolean feePayed) {
    this.feePayed = feePayed;
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

  public boolean isMayGoHomeSingly() {
    return mayGoHomeSingly;
  }

  public void setMayGoHomeSingly(boolean mayGoHomeSingly) {
    this.mayGoHomeSingly = mayGoHomeSingly;
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

  public List<AddParticipantForm> getParticipants() {
    List<AddParticipantForm> participants = new ArrayList<>(additionalParticipants.size() + 1);

    /* Depending on the event this form is created for, some fields may not be needed
     * E.g.if the event does not allow flexible participation times, none will be present in the
     * form as well.
     * However if the additional participants should be converted, all potential data needs to be
     * available.
     * Therefore these "optional" lists need to be filled with null elements.
     */
    ensureListsSameSize(additionalParticipants.size(), additionalFormSent, additionalFormFilled,
        additionalFeePayed, additionalArrival, additionalDeparture, additionalMayGoHomeSingly,
        additionalFirstNight, additionalLastNight, additionalRemarks);

    for (int i = 0; i < additionalParticipants.size(); ++i) {
      participants.add(
          new AddParticipantForm(additionalParticipants.get(i), //
              additionalFormSent.get(i), //
              additionalFormFilled.get(i), //
              additionalFeePayed.get(i), //
              additionalArrival.get(i), //
              additionalDeparture.get(i), //
              additionalMayGoHomeSingly.get(i), //
              additionalFirstNight.get(i), //
              additionalLastNight.get(i), //
              additionalRemarks.get(i)));
    }

    if (participant != null) {
      participants.add(
          new AddParticipantForm(participant, //
              formSent, //
              formFilled, //
              feePayed, //
              arrival, //
              departure, //
              mayGoHomeSingly, //
              firstNight, //
              lastNight, //
              remarks));
    }

    return Collections.unmodifiableList(participants);
  }

  public boolean hasParticipants() {
    return !getParticipants().isEmpty();
  }

  public AddParticipantsForm prepareForm() {
    AddParticipantsForm form = new AddParticipantsForm(this);

    /* Depending on the event this form is created for, some fields may not be needed
     * E.g.if the event does not allow flexible participation times, none will be present in the
     * form as well.
     * However if the additional participants should be converted, all potential data needs to be
     * available.
     * Therefore these "optional" lists need to be filled with null elements.
     */
    ensureListsSameSize(additionalParticipants.size(), additionalFormSent, additionalFormFilled,
        additionalFeePayed, additionalArrival, additionalDeparture, additionalMayGoHomeSingly,
        additionalFirstNight, additionalLastNight, additionalRemarks);

    for (AddParticipantForm participant : getParticipants()) {
      form.additionalParticipants.add(participant.participant);
      form.additionalFormSent.add(participant.formSent);
      form.additionalFormFilled.add(participant.formFilled);
      form.additionalFeePayed.add(participant.feePayed);
      form.additionalArrival.add(participant.arrival);
      form.additionalDeparture.add(participant.departure);
      form.additionalMayGoHomeSingly.add(participant.mayGoHomeSingly);
      form.additionalFirstNight.add(participant.firstNight);
      form.additionalLastNight.add(participant.lastNight);
      form.additionalRemarks.add(participant.remarks);
    }

    return form;
  }

  private void ensureListsSameSize(int targetSize, List<?>... lists) {
    for (List<?> l : lists) {
      for (int i = l.size(); i <= targetSize; ++i) {
        l.add(null);
      }
    }
  }

  @Override
  public String toString() {
    return "AddParticipantsForm [" +
        "additionalParticipants=" + additionalParticipants +
        ", additionalFormSent=" + additionalFormSent +
        ", additionalFormFilled=" + additionalFormFilled +
        ", additionalFeePayed=" + additionalFeePayed +
        ", additionalArrival=" + additionalArrival +
        ", additionalDeparture=" + additionalDeparture +
        ", additionalMayGoHomeSingly=" + additionalMayGoHomeSingly +
        ", additionalFirstNight=" + additionalFirstNight +
        ", additionalLastNight=" + additionalLastNight +
        ", additionalRemarks=" + additionalRemarks +
        ", participant=" + participant +
        ", formSent=" + formSent +
        ", formFilled=" + formFilled +
        ", feePayed=" + feePayed +
        ", arrival=" + arrival +
        ", departure=" + departure +
        ", mayGoHomeSingly=" + mayGoHomeSingly +
        ", firstNight=" + firstNight +
        ", lastNight=" + lastNight +
        ", remarks='" + remarks + '\'' +
        ']';
  }

  public static class AddParticipantForm {

    private Person participant;

    private boolean formSent = true;

    private boolean formFilled;

    private boolean feePayed;

    private ArrivalOption arrival;

    private ArrivalOption departure;

    private boolean mayGoHomeSingly;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstNight;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastNight;

    private String remarks;

    public AddParticipantForm(Event event) {
      if (event != null && event.getParticipationInfo().isFlexibleParticipationTimesEnabled()) {
        this.firstNight = event.getStartTime().toLocalDate();
        this.lastNight = event.getEndTime().toLocalDate().minusDays(1L);
      }
    }

    AddParticipantForm(Person participant, boolean formSent, boolean formFilled,
        boolean feePayed, ArrivalOption arrival, ArrivalOption departure, boolean mayGoHomeSingly,
        LocalDate firstNight, LocalDate lastNight, String remarks) {
      this.participant = participant;
      this.formSent = formSent;
      this.formFilled = formFilled;
      this.feePayed = feePayed;
      this.arrival = arrival;
      this.departure = departure;
      this.mayGoHomeSingly = mayGoHomeSingly;
      this.firstNight = firstNight;
      this.lastNight = lastNight;
      this.remarks = remarks;
    }

    public Person getParticipant() {
      return participant;
    }

    public void setParticipant(Person participant) {
      this.participant = participant;
    }

    public boolean isFormSent() {
      return formSent;
    }

    public void setFormSent(boolean formSent) {
      this.formSent = formSent;
    }

    public boolean isFormFilled() {
      return formFilled;
    }

    public void setFormFilled(boolean formFilled) {
      this.formFilled = formFilled;
    }

    public boolean isFeePayed() {
      return feePayed;
    }

    public void setFeePayed(boolean feePayed) {
      this.feePayed = feePayed;
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

    public boolean isMayGoHomeSingly() {
      return mayGoHomeSingly;
    }

    public void setMayGoHomeSingly(boolean mayGoHomeSingly) {
      this.mayGoHomeSingly = mayGoHomeSingly;
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

    public RegistrationInfoBuilder prepareRegistrationInfo() {
      RegistrationInfoBuilder builder = RegistrationInfo.buildNew();
      builder.specifyRegistrationForm(formSent, formFilled) //
          .specifyParticipationFee(feePayed) //
          .withArrivalAndDeparture(arrival, departure, mayGoHomeSingly) //
          .withRemarks(remarks);
      return builder;
    }

    public LocalDateTime generateFirstNightAsLDT() {
      return LocalDateTime.of(firstNight, LocalTime.MIDNIGHT);
    }

    public LocalDateTime generateLastNightAsLDT() {
      return LocalDateTime.of(lastNight, LocalTime.MIDNIGHT);
    }

    @Override
    public String toString() {
      return "AddParticipantForm [" +
          "participant=" + participant +
          ", formSent=" + formSent +
          ", formFilled=" + formFilled +
          ", feePayed=" + feePayed +
          ", arrival=" + arrival +
          ", departure=" + departure +
          ", mayGoHomeSingly=" + mayGoHomeSingly +
          ", firstNight=" + firstNight +
          ", lastNight=" + lastNight +
          ", remarks='" + remarks + '\'' +
          ']';
    }
  }


}
