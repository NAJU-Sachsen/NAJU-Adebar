package de.naju.adebar.app.human.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.human.QPerson;

public class HealthImpairmentsFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private String healthImpairments;

  public HealthImpairmentsFilter(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input
        .and(person.participantProfile.healthImpairments.containsIgnoreCase(healthImpairments));
  }

}
