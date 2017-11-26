package de.naju.adebar.app.filter;

import java.time.LocalDateTime;

/**
 * Enum describing how to filter dates, i.e. whether they must be before/after or exactly at a given
 * time
 * 
 * @author Rico Bergmann
 */
public enum DateTimeFilterType {
  BEFORE {
    @Override
    public boolean matching(LocalDateTime date, LocalDateTime toCheck) {
      return toCheck.isBefore(date);
    }
  },
  EXACT {
    @Override
    public boolean matching(LocalDateTime date, LocalDateTime toCheck) {
      return date.equals(toCheck);
    }
  },
  AFTER {
    @Override
    public boolean matching(LocalDateTime date, LocalDateTime toCheck) {
      return toCheck.isAfter(date);
    }
  };

  /**
   * @param date the date to use as an "offset"
   * @param toCheck the date to check whether it is before/after etc. the "offset" date
   * @return {@code true} if the date to check is before/after or exactly the first date
   */
  public abstract boolean matching(LocalDateTime date, LocalDateTime toCheck);
}
