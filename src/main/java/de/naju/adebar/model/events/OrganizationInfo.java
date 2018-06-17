package de.naju.adebar.model.events;

import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.Transient;
import org.springframework.util.Assert;

@Embeddable
public class OrganizationInfo extends AbstractEventInfo {

  private static final int REQUIRED_ORGANIZERS = 2;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "counselors", joinColumns = @JoinColumn(name = "eventId"))
  @MapKeyJoinColumn(name = "counselorId")
  private Map<Person, CounselorInfo> counselors;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "eventOrganizers", inverseJoinColumns = @JoinColumn(name = "organizerId"))
  private List<Person> organizers;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "eventToContact", joinColumns = @JoinColumn(name = "eventId"))
  @MapKeyJoinColumn(name = "personId")
  @Column(name = "contactInfo", length = 511)
  private Map<Person, String> personsToContact;

  OrganizationInfo(Event event) {
    provideRelatedEvent(event);
    this.counselors = new HashMap<>();
    this.organizers = new ArrayList<>();
    this.personsToContact = new HashMap<>();
  }

  @JpaOnly
  private OrganizationInfo() {}

  public Map<Person, CounselorInfo> getCounselors() {
    return Collections.unmodifiableMap(counselors);
  }

  @Transient
  public Collection<Person> getCounselorList() {
    return Collections.unmodifiableCollection(counselors.keySet());
  }

  public CounselorInfo getCounselorInfoFor(Person counselor) {
    if (!isCounselor(counselor)) {
      throw new IllegalArgumentException("Not a counselor: " + counselor);
    }
    return counselors.get(counselor);
  }

  public Collection<Person> getOrganizers() {
    return Collections.unmodifiableCollection(organizers);
  }

  public Collection<Person> getPersonsToContact() {
    return personsToContact.keySet();
  }

  public String getContactInformationFor(Person personToContact) {
    return personsToContact.get(personToContact);
  }

  public boolean hasOrganizers() {
    return !organizers.isEmpty();
  }

  public boolean hasLackOfOrganizers() {
    return organizers.size() < REQUIRED_ORGANIZERS;
  }

  public boolean hasCounselors() {
    return !counselors.isEmpty();
  }

  public boolean isCounselor(Person counselor) {
    return counselors.containsKey(counselor);
  }

  public boolean hasPersonsToContact() {
    return !personsToContact.isEmpty();
  }

  public void addCounselor(Person counselor, CounselorInfo registrationInfo) {
    Assert.notNull(counselor, "counselor may not be null");
    Assert.notNull(registrationInfo, "registrationInfo may not be null");
    Assert.isTrue(counselor.isActivist(), "Counselor must be an activist: " + counselor);
    Assert2.isFalse(counselors.containsKey(counselor),
        "Person is a counselor for the event already: " + counselor);

    counselors.put(counselor, registrationInfo);
    registerDomainEventIfPossible(
        CounselorAddedEvent.of(getRelatedEvent().orElse(null), counselor, registrationInfo));
  }

  public void updateCounselor(Person counselor, CounselorInfo newInfo) {
    Assert.notNull(counselor, "counselor may not be null");
    Assert.notNull(newInfo, "newInfo may not be null");
    Assert.isTrue(counselors.containsKey(counselor),
        String.format("Person %s is on counselor for event %s", counselor, this));

    counselors.put(counselor, newInfo);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void removeCounselor(Person counselor) {
    Assert.notNull(counselor, "counselor may not be null");
    Assert.isTrue(counselors.containsKey(counselor), "Not a counselor: " + counselor);

    counselors.remove(counselor);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void addOrganizer(Person organizer) {
    Assert.notNull(organizer, "organizer may not be null");
    Assert.isTrue(organizer.isActivist(), "organizer must be an activist: " + organizer);
    Assert2.isFalse(organizers.contains(organizer),
        "Person is organizes the event already: " + organizer);

    organizers.add(organizer);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void removeOrganizer(Person organizer) {
    Assert.notNull(organizer, "organizer may not be null");
    Assert.isTrue(organizers.contains(organizer), "Not an organizer: " + organizer);

    organizers.remove(organizer);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void addPersonToContact(Person thePerson, String reason) {
    Assert.notNull(thePerson, "thePerson may not be null");
    Assert.hasText(reason, "reason may not be null nor empty");

    personsToContact.merge(thePerson, reason, (current, newVal) -> current + "; " + newVal);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void updatePersonToContact(Person thePerson, String updatedReason) {
    Assert.notNull(thePerson, "thePerson may not be null");
    Assert.isTrue(personsToContact.containsKey(thePerson),
        "The person is not registered as 'to be contacted': " + thePerson);
    Assert.hasText(updatedReason, "updatedReason may not be null nor empty");

    personsToContact.put(thePerson, updatedReason);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  public void removePersonToContact(Person thePerson) {
    Assert.notNull(thePerson, "thePerson may not be null");
    Assert.isTrue(personsToContact.containsKey(thePerson),
        "The person is not registered as 'to be contacted': " + thePerson);

    personsToContact.remove(thePerson);
    registerGenericEventUpdatedDomainEventIfPossible();
  }

  @JpaOnly
  private void setCounselors(Map<Person, CounselorInfo> counselors) {
    this.counselors = counselors;
  }

  @JpaOnly
  private void setOrganizers(List<Person> organizers) {
    this.organizers = organizers;
  }

  @JpaOnly
  private void setPersonsToContact(Map<Person, String> personsToContact) {
    this.personsToContact = personsToContact;
  }

  @Override
  public String toString() {
    return "OrganizationInfo [" +
        "counselors=" + counselors +
        ", organizers=" + organizers +
        ", personsToContact=" + personsToContact +
        ']';
  }
}
