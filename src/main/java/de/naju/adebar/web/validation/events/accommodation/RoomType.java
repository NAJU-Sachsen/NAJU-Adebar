package de.naju.adebar.web.validation.events.accommodation;

import de.naju.adebar.model.persons.details.Gender;

public enum RoomType {
  FEMALE,
  MALE,
  FLEX,
  FALLBACK;

  public static RoomType fromGender(Gender gender) {
    switch (gender) {
      case FEMALE:
        return FEMALE;
      case MALE:
        return MALE;
      default:
        throw new AssertionError(gender);
    }
  }

}
