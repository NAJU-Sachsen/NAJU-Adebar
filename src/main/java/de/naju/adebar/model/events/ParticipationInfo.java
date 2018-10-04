package de.naju.adebar.model.events;

import com.google.common.collect.Lists;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

/**
 * Contains the information persons need to know in order to attend an event.
 * <p>
 * This also includes criteria the participants have to match (i.e. their minimum age).
 *
 * @author Rico Bergmann
 */
@Embeddable
public class ParticipationInfo extends AbstractEventInfo {

  @Embedded
  @AttributeOverrides(@AttributeOverride(name = "value",
      column = @Column(name = "minParticipantAge")))
  private Age minimumParticipantAge;

  @Column(name = "intParticipationFee")
  private Money internalParticipationFee;

  @Column(name = "extParticipationFee")
  private Money externalParticipationFee;

  @ElementCollection
  @CollectionTable(name = "eventArrivalOptions", joinColumns = @JoinColumn(name = "event"))
  private List<ArrivalOption> arrivalOptions;

  @Column(name = "flexibleParticipationTimes")
  private boolean flexibleParticipationTimesEnabled;

  /**
   * @param event the event this info belongs to. May not be {@code null}.
   */
  ParticipationInfo(Event event) {
    provideRelatedEvent(event);
    this.arrivalOptions = new ArrayList<>();
  }

  /**
   * Default constructor just for JPA's sake.
   */
  @JpaOnly
  private ParticipationInfo() {}

  /**
   * Most events have restrictions on the age that the participants have to be of at least.
   *
   * @return the age. May be {@code null} if there is no restriction.
   */
  public Age getMinimumParticipantAge() {
    return minimumParticipantAge;
  }

  /**
   * Gets the participation fee for club members of the NABU conservation association.
   * <p>
   * Due to subsidy related regulations, club members of the NABU need to pay a smaller fee when
   * participating in an event.
   *
   * @return the fee. May be {@code null} if none is necessary
   * @see #getExternalParticipationFee()
   */
  public Money getInternalParticipationFee() {
    return internalParticipationFee;
  }

  /**
   * Gets the participation fee for participants which are not a member of the NABU conservation
   * association.
   * <p>
   * Due to subsidy related regulations, club members of the NABU need to pay a smaller fee when *
   * participating in an event.
   *
   * @return the fee. May be {@code null} if none is necessary
   * @see #getInternalParticipationFee()
   */
  public Money getExternalParticipationFee() {
    return externalParticipationFee;
  }

  /**
   * Gets the available options to approach the event. The options are assumed to be available for
   * both arrival as well as departure.
   */
  public Collection<ArrivalOption> getArrivalOptions() {
    return Collections.unmodifiableCollection(arrivalOptions);
  }

  /**
   * Checks, whether it is possible for participants to attend the event for periods of time other
   * than the whole duration.
   */
  public boolean isFlexibleParticipationTimesEnabled() {
    return flexibleParticipationTimesEnabled;
  }

  /**
   * Checks, whether participants need to be of a certain age in order to attend the event.
   */
  public boolean hasMinimumParticipantAge() {
    return minimumParticipantAge != null;
  }

  /**
   * Checks, whether there is some participation fee for this event. It will usually be for both
   * club members of the NABU conservation association as well as none-members, or just for
   * none-members.
   */
  public boolean hasParticipationFee() {
    return hasExternalParticipationFee() || hasInternalParticipationFee();
  }

  /**
   * Checks, whether there is a participation fee for persons that are club members of the NABU
   * conservation association.
   */
  public boolean hasInternalParticipationFee() {
    return internalParticipationFee != null;
  }

  /**
   * Checks, whether there is a participation fee for persons that are not club members of the
   * NABU.
   */
  public boolean hasExternalParticipationFee() {
    return externalParticipationFee != null;
  }

  /**
   * Checks whether a way of collectively approaching the event is offered.
   */
  public boolean hasArrivalOptions() {
    return !arrivalOptions.isEmpty();
  }

  /**
   * Just an alias for {@link #isFlexibleParticipationTimesEnabled()} which sounds better.
   *
   * @see #isFlexibleParticipationTimesEnabled()
   */
  public boolean hasFlexibleParticipationTimesEnabled() {
    return flexibleParticipationTimesEnabled;
  }

  /**
   * Just a better-sounding alias for {@link #isFlexibleParticipationTimesEnabled()}.
   *
   * @see #isFlexibleParticipationTimesEnabled()
   */
  public boolean supportsFlexibleParticipationTimes() {
    return hasFlexibleParticipationTimesEnabled();
  }

  /**
   * Replaces the minimum age required to attend the event.
   *
   * @param newAge the new age. May be {@code null} to remove all restrictions.
   * @return this info. Just for easy method chaining.
   */
  public ParticipationInfo updateMinimumParticipantAge(Age newAge) {
    setMinimumParticipantAge(newAge);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  /**
   * Replaces the current fee club members have to pay in order to attend the event.
   *
   * @param newFee the new fee. May be {@code null} in order to make this event free.
   * @return this info. Just for easy method chaining.
   */
  public ParticipationInfo updateInternalParticipationFee(Money newFee) {
    final boolean feeCreated = !hasParticipationFee() && newFee != null;
    final boolean feeIncreased =
        feeCreated || (hasInternalParticipationFee() && newFee != null && newFee
            .isGreaterThan(internalParticipationFee));

    setInternalParticipationFee(newFee);

    Optional<Event> relatedEvent = getRelatedEvent();
    if (feeIncreased && relatedEvent.isPresent()) {
      registerDomainEventIfPossible(ParticipationFeeIncreasedEvent.of(relatedEvent.get()));
    } else {
      registerGenericEventUpdatedDomainEventIfPossible();
    }

    return this;
  }

  /**
   * Replaces the current fee persons who are not club members of the NABU have to pay.
   *
   * @param newFee the new fee. May be {@code null} in order to make this event free.
   * @return this info. Just for easy method chaining.
   */
  public ParticipationInfo updateExternalParticipationFee(Money newFee) {
    final boolean feeCreated = !hasParticipationFee() && newFee != null;
    final boolean feeIncreased =
        feeCreated || (hasExternalParticipationFee() && newFee != null && newFee
            .isGreaterThan(externalParticipationFee));

    setExternalParticipationFee(newFee);

    Optional<Event> relatedEvent = getRelatedEvent();
    if (feeIncreased && relatedEvent.isPresent()) {
      registerDomainEventIfPossible(ParticipationFeeIncreasedEvent.of(relatedEvent.get()));
    } else {
      registerGenericEventUpdatedDomainEventIfPossible();
    }

    return this;
  }

  /**
   * Sets whether participants have to attend the event for the whole duration, or if other times
   * are possible as well.
   *
   * @param flexibleParticipationTimesEnabled whether participants may attend the event during
   *     individual time spans
   * @return this info. Just for easy method chaining.
   */
  public ParticipationInfo updateFlexibleParticipationTimesEnabled(
      boolean flexibleParticipationTimesEnabled) {
    setFlexibleParticipationTimesEnabled(flexibleParticipationTimesEnabled);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  /**
   * Replaces the current arrival options. <strong>Participants who chose an option that is no
   * longer available will be left unchanged for now.</strong>
   *
   * @param options the new options
   * @return this info. Just for easy method chaining.
   */
  public ParticipationInfo updateArrivalOptions(Collection<ArrivalOption> options) {
    setArrivalOptions(Lists.newArrayList(options));
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  /**
   * Saves a new arrival option.
   *
   * @param option the option. May not be {@code null}.
   * @throws IllegalArgumentException if the option is already available
   */
  public void addArrivalOption(ArrivalOption option) {
    Assert.notNull(option, "option may not be null");
    Assert2.isFalse(arrivalOptions.contains(option), "Option already exists: " + option);

    arrivalOptions.add(option);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Deletes an arrival option. <strong>Participants who chose the option will be left unchanged for
   * now.</strong>
   *
   * @param option the option. May not be {@code null}.
   * @throws IllegalArgumentException if the option is not avaiable.
   */
  public void removeArrivalOption(ArrivalOption option) {
    Assert.notNull(option, "option may not be null");
    Assert.isTrue(arrivalOptions.contains(option), "No such option available: " + option);

    arrivalOptions.remove(option);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  /**
   * Sets the minimum age participants have to be of in order to attend this event.
   *
   * @param minimumParticipantAge the age. May be {@code null} if there are no age
   *     restrictions.
   */
  void setMinimumParticipantAge(Age minimumParticipantAge) {
    this.minimumParticipantAge = minimumParticipantAge;
  }

  /**
   * Sets the fee club members of the NABU have to pay to attend the event.
   *
   * @param internalParticipationFee the new fee. May be {@code null} to indicate that no fee is
   *     necessary.
   * @throws IllegalArgumentException if the fee is negative
   */
  void setInternalParticipationFee(Money internalParticipationFee) {
    Assert.isTrue(internalParticipationFee == null || internalParticipationFee.isPositiveOrZero(),
        "Fee may not be negative but was " + internalParticipationFee);
    if (internalParticipationFee == null || internalParticipationFee.isZero()) {
      // we don't want to have two values with equivalent meaning ("no fee set"). Therefore a value
      // of 0 will be mapped to null
      this.internalParticipationFee = null;
    } else {
      this.internalParticipationFee = internalParticipationFee;
    }
  }

  /**
   * Sets the fee persons who are not a club member of the NABU have to pay in order to attend the
   * event.
   *
   * @param externalParticipationFee the new fee. May be {@code null} to indicate that no fee is
   *     necessary.
   * @throws IllegalArgumentException if the fee is negative
   */
  void setExternalParticipationFee(Money externalParticipationFee) {
    Assert.isTrue(externalParticipationFee == null || externalParticipationFee.isPositiveOrZero(),
        "Fee may not be negative but was " + externalParticipationFee);
    if (externalParticipationFee == null || externalParticipationFee.isZero()) {
      // we don't want to have two values with equivalent meaning ("no fee set"). Therefore a value
      // of 0 will be mapped to null
      this.externalParticipationFee = null;
    } else {
      this.externalParticipationFee = externalParticipationFee;
    }
  }

  /**
   * Sets whether persons may attend for time spans other than the whole event's duration.
   */
  void setFlexibleParticipationTimesEnabled(boolean flexibleParticipationTimesEnabled) {
    this.flexibleParticipationTimesEnabled = flexibleParticipationTimesEnabled;
  }

  /**
   * Sets the arrival options.
   */
  @JpaOnly
  private void setArrivalOptions(List<ArrivalOption> arrivalOptions) {
    if (arrivalOptions == null) {
      this.arrivalOptions = new ArrayList<>();
    } else {
      this.arrivalOptions = arrivalOptions;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    ParticipationInfo that = (ParticipationInfo) o;

    if (flexibleParticipationTimesEnabled != that.flexibleParticipationTimesEnabled) {
      return false;
    }
    if (minimumParticipantAge != null ? !minimumParticipantAge.equals(that.minimumParticipantAge)
        : that.minimumParticipantAge != null) {
      return false;
    }
    if (internalParticipationFee != null ? !internalParticipationFee
        .equals(that.internalParticipationFee) : that.internalParticipationFee != null) {
      return false;
    }
    if (externalParticipationFee != null ? !externalParticipationFee
        .equals(that.externalParticipationFee) : that.externalParticipationFee != null) {
      return false;
    }
    return arrivalOptions.equals(that.arrivalOptions);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (minimumParticipantAge != null ? minimumParticipantAge.hashCode() : 0);
    result =
        31 * result + (internalParticipationFee != null ? internalParticipationFee.hashCode() : 0);
    result =
        31 * result + (externalParticipationFee != null ? externalParticipationFee.hashCode() : 0);
    result = 31 * result + arrivalOptions.hashCode();
    result = 31 * result + (flexibleParticipationTimesEnabled ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ParticipationInfo{" +
        "minimumParticipantAge=" + minimumParticipantAge +
        ", internalParticipationFee=" + internalParticipationFee +
        ", externalParticipationFee=" + externalParticipationFee +
        ", arrivalOptions=" + arrivalOptions +
        ", flexibleParticipationTimesEnabled=" + flexibleParticipationTimesEnabled +
        '}';
  }

}
