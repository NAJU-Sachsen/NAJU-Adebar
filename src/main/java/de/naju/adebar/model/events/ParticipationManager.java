package de.naju.adebar.model.events;

import de.naju.adebar.model.persons.Person;

/**
 * The {@code ParticipationManager} takes care of the whole participation lifecycle. This includes
 * taking care of normal persons who attend the event as well as the counselors.
 * <p>
 * The manager will assert that whenever no more persons may attend an event, this is actually
 * enforced. Hence it is the single point of entrance for changing participations.
 *
 * @author Rico Bergmann
 */
public interface ParticipationManager {

  /**
   * An enumeration of the reasons why the participants of an event may not be performed.
   */
  enum Result {

    /**
     * There was no error and everything went fine.
     */
    OK,

    /**
     * The participant was too young to attend the event.
     */
    TOO_YOUNG,

    /**
     * The event has no more slots available and thus the participant may not be accommodated.
     */
    BOOKED_OUT;

    /**
     * Checks, whether everything went fine.
     */
    public boolean isOk() {
      return this == OK;
    }

  }

  /**
   * Tries to accommodate a new participant for the whole duration of the event.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addParticipant(Event event, Person participant);

  /**
   * Tries to accommodate a new participant for the whole duration of the event.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param ignoreAgeRestriction whether the participant should not be rejected if he/she is too
   *        young
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addParticipant(Event event, Person participant, boolean ignoreAgeRestriction);

  /**
   * Tries to accommodate a new participant.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param registrationInfo a more detailed description about how the {@code participant} plans to
   *        attend the {@code event}. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo);

  /**
   * Tries to accommodate a new participant.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param registrationInfo a more detailed description about how the {@code participant} plans to
   *        attend the {@code event}. May not be {@code null}.
   * @param ignoreAgeRestriction whether the participant should not be rejected if he/she is too
   *        young
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo,
      boolean ignoreAgeRestrictions);

  /**
   * Tries to accommodate a new counselor for the whole duration of the event. Age restrictions do
   * not apply and capacity restrictions only matter if the counselors are housed together with the
   * participants.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param counselor the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addCounselor(Event event, Person counselor);

  /**
   * Tries to accommodate a new counselor. Age restrictions do not apply and capacity restrictions
   * only matter if the counselors are housed together with the participants.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param counselor the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param registrationInfo a more detailed description about how the {@code counselor} plans to
   *        attend the {@code event}. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result addCounselor(Event event, Person counselor, CounselorInfo registrationInfo);

  /**
   * Tries to update the participation info of an existing participant. This may fail if the
   * participation times changed (assuming the event enables that).
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param newInfo the new data. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result updateParticipation(Event event, Person participant, RegistrationInfo newInfo);

  /**
   * Tries to update the counselor info of an existing participant. This may fail if the
   * participation times changed (assuming the event enables that). Age restrictions do not apply
   * and capacity restrictions only matter if the counselors are housed together with the
   * participants.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param counselor the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param newInfo the new data. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result updateCounselor(Event event, Person counselor, CounselorInfo newInfo);

  /**
   * Deletes a participant from an event.
   */
  void removeParticipant(Event event, Person participant);

  /**
   * Deletes a counselor from an event.
   */
  void removeCounselor(Event event, Person counselor);

  /**
   * Tries to accommodate a new participant who was part of the waiting list previously.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param registrationInfo a more detailed description about how the {@code participant} plans to
   *        attend the {@code event}. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result movePersonFromWaitingListToParticipants(Event event, Person person);

  /**
   * Tries to accommodate a new participant who was part of the waiting list previously.
   *
   * @param event the event that the {@code participant} wants to attend. May not be {@code null}.
   * @param participant the person that wants to participate in the {@code event}. May not be
   *        {@code null}.
   * @param registrationInfo a more detailed description about how the {@code participant} plans to
   *        attend the {@code event}. May not be {@code null}.
   * @return whether the person was accommodated successfully or the error that occurred
   */
  Result movePersonFromWaitingListToParticipants(Event event, Person person,
      RegistrationInfo registrationInfo);

}
