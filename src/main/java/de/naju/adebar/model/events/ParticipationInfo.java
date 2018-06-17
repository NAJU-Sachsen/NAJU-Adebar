package de.naju.adebar.model.events;

import com.google.common.collect.Lists;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

  ParticipationInfo(Event event) {
    provideRelatedEvent(event);
    this.arrivalOptions = new ArrayList<>();
  }

  ParticipationInfo(Event event, Age minimumParticipantAge, Money internalFee, Money externalFee,
      List<ArrivalOption> arrivalOptions) {
    provideRelatedEvent(event);
    this.minimumParticipantAge = minimumParticipantAge;
    this.internalParticipationFee = internalFee;
    this.externalParticipationFee = externalFee;
    this.arrivalOptions = arrivalOptions;
  }

  @JpaOnly
  private ParticipationInfo() {}

  public Age getMinimumParticipantAge() {
    return minimumParticipantAge;
  }

  public Money getInternalParticipationFee() {
    return internalParticipationFee;
  }

  public Money getExternalParticipationFee() {
    return externalParticipationFee;
  }

  public Collection<ArrivalOption> getArrivalOptions() {
    return Collections.unmodifiableCollection(arrivalOptions);
  }

  public boolean isFlexibleParticipationTimesEnabled() {
    return flexibleParticipationTimesEnabled;
  }

  public boolean hasMinimumParticipantAge() {
    return minimumParticipantAge != null;
  }

  public boolean hasInternalParticipationFee() {
    return internalParticipationFee != null;
  }

  public boolean hasExternalParticipationFee() {
    return externalParticipationFee != null;
  }

  public boolean hasArrivalOptions() {
    return !arrivalOptions.isEmpty();
  }

  public boolean hasFlexibleParticipationTimesEnabled() {
    return flexibleParticipationTimesEnabled;
  }

  public boolean supportsFlexibleParticipationTimes() {
    return hasFlexibleParticipationTimesEnabled();
  }

  public ParticipationInfo updateMinimumParticipantAge(Age newAge) {
    setMinimumParticipantAge(newAge);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public ParticipationInfo updateInternalParticipationFee(Money newFee) {
    setInternalParticipationFee(newFee);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public ParticipationInfo updateExternalParticipationFee(Money newFee) {
    setExternalParticipationFee(newFee);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public ParticipationInfo updateFlexibleParticipationTimesEnabled(
      boolean flexibleParticipationTimesEnabled) {
    setFlexibleParticipationTimesEnabled(flexibleParticipationTimesEnabled);
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public ParticipationInfo updateArrivalOptions(Collection<ArrivalOption> options) {
    setArrivalOptions(Lists.newArrayList(options));
    registerGenericEventUpdatedDomainEventIfPossible();
    return this;
  }

  public void addArrivalOption(ArrivalOption option) {
    Assert.notNull(option, "option may not be null");
    Assert2.isFalse(arrivalOptions.contains(option), "Option already exists: " + option);

    arrivalOptions.add(option);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void removeArrivalOption(ArrivalOption option) {
    Assert.notNull(option, "option may not be null");
    Assert.isTrue(arrivalOptions.contains(option), "No such option available: " + option);

    arrivalOptions.remove(option);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  void setMinimumParticipantAge(Age minimumParticipantAge) {
    this.minimumParticipantAge = minimumParticipantAge;
  }

  void setInternalParticipationFee(Money internalParticipationFee) {
    this.internalParticipationFee = internalParticipationFee;
  }

  void setExternalParticipationFee(Money externalParticipationFee) {
    this.externalParticipationFee = externalParticipationFee;
  }

  void setFlexibleParticipationTimesEnabled(boolean flexibleParticipationTimesEnabled) {
    this.flexibleParticipationTimesEnabled = flexibleParticipationTimesEnabled;
  }

  @JpaOnly
  private void setArrivalOptions(List<ArrivalOption> arrivalOptions) {
    this.arrivalOptions = arrivalOptions;
  }

}
