package de.naju.adebar.model.events;

import org.springframework.util.Assert;

public class EventCanceledException extends RuntimeException {

	private static final long serialVersionUID = 4045371217682255370L;

	private final Event event;

	public static EventCanceledException forEvent(Event event) {
		return new EventCanceledException(event);
	}

	public EventCanceledException(Event event) {
		Assert.notNull(event, "Event may not be null");
		this.event = event;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public String toString() {
		return "EventCanceledException [event: " + event + "]";
	}

}
