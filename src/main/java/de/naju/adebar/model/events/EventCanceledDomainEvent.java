package de.naju.adebar.model.events;

public class EventCanceledDomainEvent extends EventUpdatedDomainEvent {

	public static EventCanceledDomainEvent forEvent(Event event) {
		return new EventCanceledDomainEvent(event);
	}

	private EventCanceledDomainEvent(Event event) {
		super(event);
	}

}
