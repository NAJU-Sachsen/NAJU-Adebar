package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.QPerson;

public class EventParticipationFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;

  private BooleanBuilder basePredicate = new BooleanBuilder();

  public EventParticipationFilter(String eventName) {
    basePredicate.and(person.participatingEvents.any().name.containsIgnoreCase(eventName));
  }

  public EventParticipationFilter(Event event) {
    basePredicate.and(person.participatingEvents.contains(event));
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(basePredicate);
  }

}
