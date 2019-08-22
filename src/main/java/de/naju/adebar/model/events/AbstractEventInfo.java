package de.naju.adebar.model.events;

import java.util.Optional;
import javax.persistence.Transient;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * Base class for all additional information attached to an {@link Event}.
 * <p>
 * It provides essential functionality all related classes likely want to access.
 *
 * @author Rico Bergmann
 */
abstract class AbstractEventInfo {

	@Transient
	private Event relatedEvent;

	/**
	 * Sets the event {@code this} information is attached to.
	 *
	 * @param event the event. May never be {@code null}.
	 */
	protected void provideRelatedEvent(@NonNull Event event) {
		Assert.notNull(event, "event may not be null");
		this.relatedEvent = event;
	}

	/**
	 * Checks, whether it is known to which event {@code this} event is attached.
	 */
	protected boolean hasRelatedEvent() {
		return relatedEvent != null;
	}

	/**
	 * Provides the event {@code this} information is attached to (if it exists).
	 */
	protected Optional<Event> getRelatedEvent() {
		return Optional.ofNullable(relatedEvent);
	}

	/**
	 * Tries to persist an domain event at the {@link Event} this information is attached to. If no
	 * {@code Event} is attached, the domain event will be discarded. The same applies if the event
	 * does not permit to persist multiple instances of the same
	 * {@link AbstractEventRelatedDomainEvent}.
	 *
	 * @param domainEvent the event. May never be {@code null}.
	 */
	protected <E extends AbstractEventRelatedDomainEvent> void registerDomainEventIfPossible(
			@NonNull E domainEvent) {
		if (!hasRelatedEvent()) {
			return;
		}

		if (domainEvent.aggregateMayContainMultipleInstances()) {
			relatedEvent.registerEvent(domainEvent);
		} else if (!relatedEvent.hasRegisteredEventOf(domainEvent.getClass())) {
			relatedEvent.registerEvent(domainEvent);
		}
	}

	/**
	 * Shortcut to register an {@link EventUpdatedDomainEvent} on the related {@link Event} if
	 * possible.
	 *
	 * @see #registerDomainEventIfPossible(AbstractEventRelatedDomainEvent)
	 */
	protected void registerGenericEventUpdatedDomainEventIfPossible() {
		registerDomainEventIfPossible(EventUpdatedDomainEvent.forEvent(relatedEvent));
	}

	/**
	 * Checks, whether the corresponding {@link Event} may persist (another) domain event.
	 *
	 * @param domainEvent the event to check.
	 */
	protected <E extends AbstractEventRelatedDomainEvent> boolean mayRegisterEventOf(
			@NonNull E domainEvent) {
		return domainEvent.aggregateMayContainMultipleInstances()
				|| !relatedEvent.hasRegisteredEventOf(domainEvent.getClass());
	}

	/**
	 * Tries to assert that the corresponding {@link Event} is not yet canceled. <b>Beware: if the
	 * event is not present, the assertion will be skipped.</b>
	 */
	protected void assertEventNotCanceled() {
		getRelatedEvent().ifPresent(Event::assertNotCanceled);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((relatedEvent == null) ? 0 : relatedEvent.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractEventInfo other = (AbstractEventInfo) obj;
		if (relatedEvent == null) {
			if (other.relatedEvent != null) {
				return false;
			}
		} else if (!relatedEvent.equals(other.relatedEvent)) {
			return false;
		}
		return true;
	}

}
