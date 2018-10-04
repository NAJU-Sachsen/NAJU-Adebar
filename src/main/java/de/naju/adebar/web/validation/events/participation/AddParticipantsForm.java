package de.naju.adebar.web.validation.events.participation;

import de.naju.adebar.model.events.ArrivalOption;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.events.RegistrationInfo.RegistrationInfoBuilder;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.persons.participant.AddParticipantFormConverter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * POJO representation of the data inside the 'add participants' form.
 * <p>
 * As multiple participants may be added within one go, this form supports two different "modes":
 * <ul>
 * <li>
 * The most recently added participant's data will be directly available through the normal fields.
 * </li>
 * <li>
 * The data of all participant's that were added before (hereafter referred to as "in queue") will
 * be stored in lists, with one list dedicated to each field.
 * </li>
 * </ul>
 * E.g. the selected arrival option for each participant ({@code arrival} field) will be stored in
 * the {@code additionalArrival} field). All these lists will be sorted according to insertion, i.e.
 * {@code additionalFormSent[i]} will correspond to the {@code i}th participant added (and thus
 * {@code additionalFormSent[i]}, {@code additionalParticipants[i]}, etc. all correspond to the same
 * person).
 * <p>
 * In order to not overwrite the current (i.e. that is last-added) participant, the form has to be
 * prepared before adding another one. For this purpose, the {@link #prepareForm()} method has been
 * created. In short it will create a new form, which has the current participant moved to the
 * additional participants list (and thus the fields reserved by the current participant may be set
 * to values for the new participant).
 * <p>
 * This dimorphism however makes it a bit complicated to access the participants in the form as both
 * the current, as well as all additional participants have to be queried. To provide a more
 * convenient access, the {@link #getParticipants()} method exists. It will generate all the
 * participants wrapped in a dedicated POJO ({@link AddParticipantForm}).
 * <p>
 * We may not use this POJO throughout the form, because Spring currently does not provide good
 * support for nested forms in a {@code List} so far.
 *
 * @author Rico Bergmann
 * @see AddParticipantForm
 */
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

  /**
   * Primary constructor.
   * <p>
   * Each form has to be created based on some event to enable further validation of participation
   * times (however the validation itself will not be performed in this class, but in the {@link
   * AddParticipantFormConverter} instead. The scope of this class is purely to capture data, not to
   * do anything with it).
   *
   * @param event the event. If it is {@code null} neither {@code firstNight} nor {@code
   *     lastNight} will be set.
   */
  public AddParticipantsForm(Event event) {
    if (event != null && event.getParticipationInfo().isFlexibleParticipationTimesEnabled()) {
      this.firstNight = event.getStartTime().toLocalDate();
      this.lastNight = event.getEndTime().toLocalDate().minusDays(1L);
    }
  }

  /**
   * Secondary constructor to create a form based on another form. This form will reuse {@code
   * firstNight} and {@code lastNight} from the existing one but not copy any other data.
   *
   * @param form the form. May not be {@code null}.
   */
  private AddParticipantsForm(AddParticipantsForm form) {
    this.firstNight = form.firstNight;
    this.lastNight = form.lastNight;
  }

  /**
   * @return persons which were already enqueued to become participants
   */
  public List<Person> getAdditionalParticipants() {
    return additionalParticipants;
  }

  /**
   * @param additionalParticipants persons which were already enqueued to become participants
   */
  public void setAdditionalParticipants(List<Person> additionalParticipants) {
    this.additionalParticipants = additionalParticipants;
  }

  /**
   * @return whether the persons in the queue have received the registration form already
   */
  public List<Boolean> getAdditionalFormSent() {
    return additionalFormSent;
  }

  /**
   * @param additionalFormSent whether the persons in the queue have received the registration
   *     form already
   */
  public void setAdditionalFormSent(List<Boolean> additionalFormSent) {
    this.additionalFormSent = additionalFormSent;
  }

  /**
   * @return whether the persons in the queue have signed the registration form already
   */
  public List<Boolean> getAdditionalFormFilled() {
    return additionalFormFilled;
  }

  /**
   * @param additionalFormFilled whether the persons in the queue have signed the registration
   *     form already
   */
  public void setAdditionalFormFilled(List<Boolean> additionalFormFilled) {
    this.additionalFormFilled = additionalFormFilled;
  }

  /**
   * @return whether the persons in the queue have payed the participation fee already
   */
  public List<Boolean> getAdditionalFeePayed() {
    return additionalFeePayed;
  }

  /**
   * @param additionalFeePayed whether the persons in the queue have payed the participation fee
   *     already
   */
  public void setAdditionalFeePayed(List<Boolean> additionalFeePayed) {
    this.additionalFeePayed = additionalFeePayed;
  }

  /**
   * @return the selected arrival options of the participants in the queue
   */
  public List<ArrivalOption> getAdditionalArrival() {
    return additionalArrival;
  }

  /**
   * @param additionalArrival the selected arrival options of the participants in the queue
   */
  public void setAdditionalArrival(List<ArrivalOption> additionalArrival) {
    this.additionalArrival = additionalArrival;
  }

  /**
   * @return the selected departure options of the participants in the queue
   */
  public List<ArrivalOption> getAdditionalDeparture() {
    return additionalDeparture;
  }

  /**
   * @param additionalDeparture the selected arrival options of the participants in the queue
   */
  public void setAdditionalDeparture(List<ArrivalOption> additionalDeparture) {
    this.additionalDeparture = additionalDeparture;
  }

  /**
   * @return whether the participants in the queue may leave the event on their own
   */
  public List<Boolean> getAdditionalMayGoHomeSingly() {
    return additionalMayGoHomeSingly;
  }

  /**
   * @param additionalMayGoHomeSingly whether the participants in the queue may leave the event
   *     on their own
   */
  public void setAdditionalMayGoHomeSingly(List<Boolean> additionalMayGoHomeSingly) {
    this.additionalMayGoHomeSingly = additionalMayGoHomeSingly;
  }

  /**
   * @return the first night the participants in the queue attend the event
   */
  public List<LocalDate> getAdditionalFirstNight() {
    return additionalFirstNight;
  }

  /**
   * @param additionalFirstNight the first night the participants in the queue attend the event
   */
  public void setAdditionalFirstNight(List<LocalDate> additionalFirstNight) {
    this.additionalFirstNight = additionalFirstNight;
  }

  /**
   * @return the last night the participants in the queue attend the event
   */
  public List<LocalDate> getAdditionalLastNight() {
    return additionalLastNight;
  }

  /**
   * @param additionalLastNight the last night the participants in the queue attend the event
   */
  public void setAdditionalLastNight(List<LocalDate> additionalLastNight) {
    this.additionalLastNight = additionalLastNight;
  }

  /**
   * @return additional information about the participation for the persons in queue
   */
  public List<String> getAdditionalRemarks() {
    return additionalRemarks;
  }

  /**
   * @param additionalRemarks additional information about the participation for the persons in
   *     queue
   */
  public void setAdditionalRemarks(List<String> additionalRemarks) {
    this.additionalRemarks = additionalRemarks;
  }

  /**
   * @return the current new participant
   */
  public Person getParticipant() {
    return participant;
  }

  /**
   * @param participant the current new participant
   */
  public void setParticipant(Person participant) {
    this.participant = participant;
  }

  /**
   * @return whether the current participant has received the registration form
   */
  public boolean isFormSent() {
    return formSent;
  }

  /**
   * @param formSent whether the current participant has received the registration form
   */
  public void setFormSent(boolean formSent) {
    this.formSent = formSent;
  }

  /**
   * @return whether the current participant has filled and signed the registration form
   */
  public boolean isFormFilled() {
    return formFilled;
  }

  /**
   * @param formFilled whether the current participant has filled and signed the registration
   *     form
   */
  public void setFormFilled(boolean formFilled) {
    this.formFilled = formFilled;
  }

  /**
   * @return whether the current participant has payed the participation fee
   */
  public boolean isFeePayed() {
    return feePayed;
  }

  /**
   * @param feePayed whether the current participant has payed the participation fee
   */
  public void setFeePayed(boolean feePayed) {
    this.feePayed = feePayed;
  }

  /**
   * @return the selected arrival option of the current participant
   */
  public ArrivalOption getArrival() {
    return arrival;
  }

  /**
   * @param arrival the selected arrival option of the current participant
   */
  public void setArrival(ArrivalOption arrival) {
    this.arrival = arrival;
  }

  /**
   * @return the selected departure option of the current participant
   */
  public ArrivalOption getDeparture() {
    return departure;
  }

  /**
   * @param departure the selected departure option of the current participant
   */
  public void setDeparture(ArrivalOption departure) {
    this.departure = departure;
  }

  /**
   * @return whether the current participant may leave the event on his/her own
   */
  public boolean isMayGoHomeSingly() {
    return mayGoHomeSingly;
  }

  /**
   * @param mayGoHomeSingly whether the current participant may leave the event on his/her own
   */
  public void setMayGoHomeSingly(boolean mayGoHomeSingly) {
    this.mayGoHomeSingly = mayGoHomeSingly;
  }

  /**
   * @return the first night the current participant attends the event
   */
  public LocalDate getFirstNight() {
    return firstNight;
  }

  /**
   * @param firstNight the first night the current participant attends the event
   */
  public void setFirstNight(LocalDate firstNight) {
    this.firstNight = firstNight;
  }

  /**
   * @return the last night the current participant attends the event
   */
  public LocalDate getLastNight() {
    return lastNight;
  }

  /**
   * @param lastNight the last night the current participant attends the event
   */
  public void setLastNight(LocalDate lastNight) {
    this.lastNight = lastNight;
  }

  /**
   * @return additional remarks about the participation of the current participant
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @param remarks additional remarks about the participation of the current participant
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /**
   * @return all participants that are in queue as well as the current participant (if there is one)
   */
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

  /**
   * @return whether there are any participants in this form
   */
  public boolean hasParticipants() {
    return !additionalParticipants.isEmpty() || participant != null;
  }

  /**
   * Enqueues the current participant making the fields available for a new participant.
   *
   * @return the resulting form. {@code This} form is left unchanged.
   */
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

    // TODO Refactor: this is just ugly
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

  /**
   * Fills all passed lists with {@code null} elements until they all contain a certain number of
   * items.
   *
   * @param targetSize the number of elements to expand the lists to
   * @param lists the lists. If any of the lists is bigger already, it will be left unchanged.
   */
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

  /**
   * Wrapper to easily access the data of one specific new participant.
   */
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

    /**
     * Primary constructor. Bases the new participation time on the duration of some event.
     *
     * @param event the event. May be {@code null} to skip intialization of {@code firstNight}
     *     and {@code lastNight}.
     */
    public AddParticipantForm(Event event) {
      if (event != null && event.getParticipationInfo().isFlexibleParticipationTimesEnabled()) {
        this.firstNight = event.getStartTime().toLocalDate();
        this.lastNight = event.getEndTime().toLocalDate().minusDays(1L);
      }
    }

    /**
     * Full constructor.
     *
     * @param participant the person which wants to participate
     * @param formSent whether the participant received the registration form
     * @param formFilled whether the participant filled the registration form
     * @param feePayed whether the participant payed the participation fee already
     * @param arrival the selected arrival option
     * @param departure the selected departure option
     * @param mayGoHomeSingly whether the participant may leave the event on his/her own
     * @param firstNight the first night the participant attends the event
     * @param lastNight the last night the participant attends the event
     * @param remarks additional information regarding this participation
     */
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

    /**
     * @return this participant
     */
    public Person getParticipant() {
      return participant;
    }

    /**
     * @param participant this participant
     */
    public void setParticipant(Person participant) {
      this.participant = participant;
    }

    /**
     * @return whether this participant received the registration form
     */
    public boolean isFormSent() {
      return formSent;
    }

    /**
     * @param formSent whether this participant received the registration form
     */
    public void setFormSent(boolean formSent) {
      this.formSent = formSent;
    }

    /**
     * @return whether this participant filled the registration form
     */
    public boolean isFormFilled() {
      return formFilled;
    }

    /**
     * @param formFilled whether this participant filled the registration form
     */
    public void setFormFilled(boolean formFilled) {
      this.formFilled = formFilled;
    }

    /**
     * @return whether this participant payed the participation fee
     */
    public boolean isFeePayed() {
      return feePayed;
    }

    /**
     * @param feePayed whether this participant payed the participation fee
     */
    public void setFeePayed(boolean feePayed) {
      this.feePayed = feePayed;
    }

    /**
     * @return this participant's selected arrival option
     */
    public ArrivalOption getArrival() {
      return arrival;
    }

    /**
     * @param arrival this participant's selected arrival option
     */
    public void setArrival(ArrivalOption arrival) {
      this.arrival = arrival;
    }

    /**
     * @return this participant's selected departure option
     */
    public ArrivalOption getDeparture() {
      return departure;
    }

    /**
     * @param departure this participant's selected departure option
     */
    public void setDeparture(ArrivalOption departure) {
      this.departure = departure;
    }

    /**
     * @return whether this participant may leave the event on his/her own
     */
    public boolean isMayGoHomeSingly() {
      return mayGoHomeSingly;
    }

    /**
     * @param mayGoHomeSingly whether this participant may leave the event on his/her own
     */
    public void setMayGoHomeSingly(boolean mayGoHomeSingly) {
      this.mayGoHomeSingly = mayGoHomeSingly;
    }

    /**
     * @return the first night this participant attends the event
     */
    public LocalDate getFirstNight() {
      return firstNight;
    }

    /**
     * @param firstNight the first night this participant attends the event
     */
    public void setFirstNight(LocalDate firstNight) {
      this.firstNight = firstNight;
    }

    /**
     * @return the last night this participant attends the event
     */
    public LocalDate getLastNight() {
      return lastNight;
    }

    /**
     * @param lastNight the last night this participant attends the event
     */
    public void setLastNight(LocalDate lastNight) {
      this.lastNight = lastNight;
    }

    /**
     * @return additional information regarding this participation
     */
    public String getRemarks() {
      return remarks;
    }

    /**
     * @param remarks additional information regarding this participation
     */
    public void setRemarks(String remarks) {
      this.remarks = remarks;
    }

    /**
     * Intializes a builder to convert this form into a real {@link RegistrationInfo}.
     * <p>
     * The builder will receive all info except the participation time as this may be {@code null}
     * if the event this person wants to attend does not allow flexible participation times.
     * Therefore it must be set manually (although this form contains still contains the necessary
     * info) from outside, where the context is fully understood (i.e. the event is known).
     *
     * @see #generateFirstNightAsLDT()
     * @see #generateLastNightAsLDT()
     */
    public RegistrationInfoBuilder prepareRegistrationInfo() {
      RegistrationInfoBuilder builder = RegistrationInfo.generate();
      builder.specifyRegistrationForm(formSent, formFilled) //
          .specifyParticipationFee(feePayed) //
          .withArrivalAndDeparture(arrival, departure, mayGoHomeSingly) //
          .withRemarks(remarks);
      return builder;
    }

    /**
     * @return the first night the participant wants to attend the event, converted to an instance
     *     of {@link LocalDateTime}.
     */
    public LocalDateTime generateFirstNightAsLDT() {
      return LocalDateTime.of(firstNight, LocalTime.MIDNIGHT);
    }

    /**
     * @return the last night the participant wants to attend the event, converted to an instance *
     *     of {@link LocalDateTime}.
     */
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
