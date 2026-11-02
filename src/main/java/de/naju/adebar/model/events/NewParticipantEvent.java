package de.naju.adebar.model.events;

import de.naju.adebar.model.persons.Person;

public class NewParticipantEvent extends AbstractEventRelatedDomainEvent {

	private final Person participant;
	private final RegistrationInfo registrationInfo;

	public static NewParticipantEvent of(Event event, Person participant,
			RegistrationInfo registrationInfo) {
		return new NewParticipantEvent(event, participant, registrationInfo);
	}

	protected NewParticipantEvent(Event event, Person participant,
			RegistrationInfo registrationInfo) {
		super(event);
		this.participant = participant;
		this.registrationInfo = registrationInfo;
	}

	public final Person getParticipant() {
		return participant;
	}

	public final RegistrationInfo getRegistrationInfo() {
		return registrationInfo;
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

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.model.EntityUpdatedEvent#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((participant == null) ? 0 : participant.hashCode());
		result = prime * result + ((registrationInfo == null) ? 0 : registrationInfo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.model.EntityUpdatedEvent#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		NewParticipantEvent other = (NewParticipantEvent) obj;
		if (participant == null) {
			if (other.participant != null)
				return false;
		} else if (!participant.equals(other.participant))
			return false;
		if (registrationInfo == null) {
			if (other.registrationInfo != null)
				return false;
		} else if (!registrationInfo.equals(other.registrationInfo))
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
		return "NewParticipantEvent [event=" + entity + ", participant=" + participant
				+ ", registrationInfo=" + registrationInfo + "]";
	}

}
