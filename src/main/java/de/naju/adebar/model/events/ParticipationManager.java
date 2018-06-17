package de.naju.adebar.model.events;

import de.naju.adebar.model.persons.Person;

public interface ParticipationManager {

  enum Result {
    OK, TOO_YOUNG, BOOKED_OUT
  }

  Result addParticipant(Event event, Person participant);

  Result addParticipant(Event event, Person participant, boolean ignoreAgeRestriction);

  Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo);

  Result addParticipant(Event event, Person participant, RegistrationInfo registrationInfo,
      boolean ignoreAgeRestrictions);

  Result addCounselor(Event event, Person counselor);

  Result addCounselor(Event event, Person counselor, CounselorInfo registrationInfo);

  Result updateParticipation(Event event, Person participant, RegistrationInfo newInfo);

  Result updateCounselor(Event event, Person counselor, CounselorInfo newInfo);

  void removeParticipant(Event event, Person participant);

  void removeCounselor(Event event, Person counselor);

  Result movePersonFromWaitingListToParticipants(Event event, Person person);

  Result movePersonFromWaitingListToParticipants(Event event, Person person,
      RegistrationInfo registrationInfo);

}
