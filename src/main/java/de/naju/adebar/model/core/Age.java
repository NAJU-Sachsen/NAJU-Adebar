package de.naju.adebar.model.core;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.ddd.ValueObject;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

/**
 * Wrapper for age statements
 *
 * @author Rico Bergmann
 */
@ValueObject
@Embeddable
public class Age implements Comparable<Age> {

  /**
   * The minimum age of persons that are of legal age in Germany.
   */
  public static final Age LEGAL_AGE = Age.of(18);

  @Column(name = "age")
  private int value;

  /**
   * Full constructor
   *
   * @param value the age's value
   * @throws IllegalArgumentException if the age is negative
   */
  private Age(@Nonnegative int value) {
    Assert.isTrue(value >= 0, "Value may not be negative");
    this.value = value;
  }

  /**
   * Default constructor just for JPA's sake
   */
  @JpaOnly
  private Age() {}

  /**
   * Constructs a new age object
   *
   * @param value the age
   * @return the wrapped age value
   */
  @Nonnull
  public static Age of(@Nonnegative int value) {
    return new Age(value);
  }

  /**
   * Computes the age of persons based on their date of birth
   *
   * @param date the date of birth
   * @return the age
   */
  @Nonnull
  public static Age forDateOfBirth(@Nonnull LocalDate date) {
    Period timeDifference = Period.between(date, LocalDate.now());
    return new Age(timeDifference.getYears());
  }

  /**
   * Computes the age based on two points in time
   *
   * @param from the start of the time period
   * @param to the end of the time period
   * @return the difference of the two dates
   */
  @Nonnull
  public static Age forPeriod(@Nonnull LocalDate from, @Nonnull LocalDate to) {
    Period timeDifference = Period.between(from, to);
    return new Age(timeDifference.getYears());
  }

  /**
   * @return the age
   */
  @Nonnegative
  public int getValue() {
    return value;
  }

  /**
   * @param value the age
   */
  @JpaOnly
  private void setValue(@Nonnegative int value) {
    Assert.isTrue(value >= 0, "Value may not be negative");
    this.value = value;
  }

  /**
   * @return whether the age is considered of legal age in Germany.
   */
  @Transient
  public boolean isOfLegalAge() {
    return LEGAL_AGE.compareTo(this) <= -1;
  }

  /**
   * Compares two ages
   *
   * @param other the age to compare
   * @return whether this age is older than the other age
   */
  @Transient
  public boolean isOlderThan(@Nonnull Age other) {
    return this.compareTo(other) > 0;
  }

  /**
   * Compares two ages
   *
   * @param other the age to compare
   * @return whether this age is younger than the other age
   */
  @Transient
  public boolean isYoungerThan(@Nonnull Age other) {
    return this.compareTo(other) < 0;
  }

  @Override
  public int compareTo(@NonNull Age other) {
    return Integer.compare(this.value, other.value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Age age = (Age) o;
    return value == age.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

}
