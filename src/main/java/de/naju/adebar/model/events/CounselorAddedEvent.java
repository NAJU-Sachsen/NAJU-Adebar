package de.naju.adebar.model.events;

import de.naju.adebar.model.persons.Person;

public class CounselorAddedEvent extends AbstractEventRelatedDomainEvent {

	private final Person counselor;
	private final CounselorInfo registrationInfo;

	public static CounselorAddedEvent of(Event event, Person counselor,
			CounselorInfo registrationInfo) {
		return new CounselorAddedEvent(event, counselor, registrationInfo);
	}

	protected CounselorAddedEvent(Event event, Person counselor, CounselorInfo registrationInfo) {
		super(event);
		this.counselor = counselor;
		this.registrationInfo = registrationInfo;
	}

	public final Person getCounselor() {
		return counselor;
	}

	public final CounselorInfo getRegistrationInfo() {
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
		result = prime * result + ((counselor == null) ? 0 : counselor.hashCode());
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
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CounselorAddedEvent other = (CounselorAddedEvent) obj;
		if (counselor == null) {
			if (other.counselor != null) {
				return false;
			}
		} else if (!counselor.equals(other.counselor)) {
			return false;
		}
		if (registrationInfo == null) {
			if (other.registrationInfo != null) {
				return false;
			}
		} else if (!registrationInfo.equals(other.registrationInfo)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.naju.adebar.model.EntityUpdatedEvent#toString()
	 */
	@Override
	public String toString() {
		return "CounselorAddedEvent [event=" + entity + ", counselor=" + counselor
				+ ", registrationInfo=" + registrationInfo + "]";
	}

}
