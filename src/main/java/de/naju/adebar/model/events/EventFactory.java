package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.core.Capacity;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;

/**
 * Facility to create new {@link Event}s.
 * <p>
 * This factory provides the only (public) way of instantiating new {@code events} and offers a
 * fluid construction process through a builder mechanism.
 * <p>
 * As the construction of an event involves quite a lot of setup, not all functionality will be
 * provided by a single builder. Instead, {@link AbstractInfoBuilder more dedicated builders} are
 * used to configure complex properties of the event.
 *
 * @author Rico Bergmann
 * @see Event
 */
@Service
public class EventFactory {

  private EventIdGenerator idGenerator;

  @Autowired
  public EventFactory(EventIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * Starts the construction process for a new {@link Event} instance.
   * <p>
   * Further modifications of the event's initial state may be performed through the builder
   * instance.
   *
   * @param name the event's name. May not be {@code null}.
   * @param startTime the start time. May not be {@code null} nor after the {@code endTime}.
   * @param endTime the end time. May not be {@code null} nor before the {@code startTime}.
   * @return a builder to continue the construction through a fluid interface.
   */
  public EventBuilder build(@NotEmpty String name, LocalDateTime startTime, LocalDateTime endTime) {
    return new EventBuilder(name, startTime, endTime);
  }

  /**
   * The {@code EventBuilder} takes care of transforming the {@link Event} under construction into a
   * well-defined state as specified by the user. To achieve this in a natural and intuitive way,
   * the Builder-pattern is used.
   *
   * @author Rico Bergmann
   * @see <a href="">Builder-Pattern</a> // TODO insert link to pattern (PAEEE or Wikipedia)
   */
  public class EventBuilder {

    private Event event;

    /**
     * Default constructor.
     *
     * @see EventFactory#build(String, LocalDateTime, LocalDateTime)
     */
    private EventBuilder(String name, LocalDateTime startTime, LocalDateTime endTime) {
      this.event = new Event(idGenerator.next(), name, startTime, endTime);
    }

    /**
     * Sets the location where the event should take place.
     *
     * @param place the location. May be {@code null} to leave this information unspecified.
     * @return {@code this} builder for easy method chaining.
     */
    public EventBuilder specifyPlace(Address place) {
      event.setPlace(place);
      return this;
    }

    /**
     * Starts the configuration process for the event's {@link ParticipationInfo}.
     * <p>
     * For doing so, a "sub-builder" is used.
     */
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
