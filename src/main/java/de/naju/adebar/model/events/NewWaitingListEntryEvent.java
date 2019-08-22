package de.naju.adebar.model.events;

import de.naju.adebar.model.persons.Person;

public class NewWaitingListEntryEvent extends AbstractEventRelatedDomainEvent {

	private final Person participant;

	public static NewWaitingListEntryEvent of(Event event, Person participant) {
		return new NewWaitingListEntryEvent(event, participant);
	}

	protected NewWaitingListEntryEvent(Event event, Person participant) {
		super(event);
		this.participant = participant;
	}

	public final Person getParticipant() {
		return participant;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.naju.adebar.model.support.AbstractDomainEvent#aggregateMayContainMultipleInstances()
	 */
	@Override
	public boolean aggregateMayContainMultipleInstances() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((participant == null) ? 0 : participant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewWaitingListEntryEvent other = (NewWaitingListEntryEvent) obj;
		if (participant == null) {
			if (other.participant != null)
				return false;
		} else if (!participant.equals(other.participant))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.naju.adebar.model.EntityUpdatedEvent#toString()
	 */
	@Override
	public String toString() {
		return "NewWaitingListEntryEvent [event=" + entity + ", participant=" + participant + "]";
	}

}
