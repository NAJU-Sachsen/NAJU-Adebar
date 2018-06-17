package de.naju.adebar.model.events;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import java.time.LocalDateTime;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Factory to create {@link Event} instances
 *
 * @author Rico Bergmann
 */
@Service
public class EventFactory {

  private EventIdGenerator idGenerator;

  @Autowired
  public EventFactory(EventIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * Creates a new event
   *
   * @param name the event's name
   * @param startTime the start time
   * @param endTime the end time
   * @return the event
   */
  public EventBuilder build(String name, LocalDateTime startTime, LocalDateTime endTime) {
    return new EventBuilder(name, startTime, endTime);
  }

  public class EventBuilder {

    private Event event;

    private EventBuilder(String name, LocalDateTime startTime, LocalDateTime endTime) {
      this.event = new Event(idGenerator.next(), name, startTime, endTime);
    }

    public EventBuilder specifyPlace(Address place) {
      event.setPlace(place);
      return this;
    }

    public ParticipationInfoBuilder specifyParticipationInfo() {
      return new ParticipationInfoBuilder(this);
    }

    public ParticipantsListBuilder specifyParticipantsList() {
      return new ParticipantsListBuilder(this);
    }

    public Event create() {
      return event;
    }

    private Event currentBuild() {
      return event;
    }

  }

  public abstract class AbstractInfoBuilder {

    protected EventBuilder parentBuilder;

    protected AbstractInfoBuilder(EventBuilder parentBuilder) {
      this.parentBuilder = parentBuilder;
    }

    public EventBuilder done() {
      return parentBuilder;
    }

    public Event create() {
      return done().create();
    }

  }

  public class ParticipationInfoBuilder extends AbstractInfoBuilder {

    private ParticipationInfo participationInfo;

    private ParticipationInfoBuilder(EventBuilder parentBuilder) {
      super(parentBuilder);
      participationInfo = parentBuilder.currentBuild().getParticipationInfo();
    }

    public ParticipationInfoBuilder specifyMinimumParticipantAge(Age theAge) {
      participationInfo.setMinimumParticipantAge(theAge);
      return this;
    }

    public ParticipationInfoBuilder specifyInternalParticipationFee(Money theFee) {
      participationInfo.setInternalParticipationFee(theFee);
      return this;
    }

    public ParticipationInfoBuilder specifyExternalParticipationFee(Money theFee) {
      participationInfo.setExternalParticipationFee(theFee);
      return this;
    }

    public ParticipationInfoBuilder specifyArrivalOptions(Iterable<ArrivalOption> theOptions) {
      theOptions.forEach(participationInfo::addArrivalOption);
      return this;
    }

    public ParticipationInfoBuilder withFlexibleParticipationTimes() {
      participationInfo.setFlexibleParticipationTimesEnabled(true);
      return this;
    }

  }

  public class ParticipantsListBuilder extends AbstractInfoBuilder {

    private ParticipantsList participantsList;

    private ParticipantsListBuilder(EventBuilder parentBuilder) {
      super(parentBuilder);
      participantsList = parentBuilder.currentBuild().getParticipantsList();
    }

    public ParticipantsListBuilder specifyParticipantsLimit(Capacity theLimit) {
      participantsList.setParticipantsLimit(theLimit);
      return this;
    }

    public ParticipantsListBuilder specifyAccomodation(ExtendedRoomSpecification theAccomodation) {
      participantsList.setAccommodation(theAccomodation);
      return this;
    }

  }

}
