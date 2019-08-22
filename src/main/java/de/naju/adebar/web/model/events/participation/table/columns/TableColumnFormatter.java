package de.naju.adebar.web.model.events.participation.table.columns;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import org.springframework.util.Assert;

/**
 * A {@code formatter} will be used to render a specific column of the participants table. Depending
 * on the event's data it may not be usable however.
 *
 * @author Rico Bergmann
 */
public interface TableColumnFormatter {

	/**
	 * Checks, whether this formatter may be used on the participants list of a certain event.
	 * <p>
	 * This will usually be the case, but in some situations a formatter may not be usable. E.g. if
	 * this formatter should display the participation time, but participation times may not be
	 * customized, the service is not to be invoked.
	 *
	 * @param event the event to check
	 * @return whether
	 */
	boolean isApplicable(Event event);

	/**
	 * Performs the actual formatting.
	 *
	 * @param participant the participant whose data should be formatted. May not be {@code null}
	 * @param event the event which the participant attends. May not be {@code null}
	 * @return the formatted column
	 * @throws IllegalStateException if the formatter is not applicable for the event
	 */
	String formatColumnFor(Person participant, Event event);

	/**
	 * Gets whether the returned {@code String} should be interpreted as HTML code.
	 */
	default boolean usesHtml() {
		return false;
	}

	/**
	 * @param event the event to check
	 * @throws IllegalStateException if this formatter is not applicable (according to
	 *         {@link #isApplicable(Event)})
	 */
	default void assertIsApplicable(Event event) {
		Assert.state(isApplicable(event), String.format("%s is not applicable for %s",
				this.getClass().getSimpleName(), event.toString()));
	}

}
