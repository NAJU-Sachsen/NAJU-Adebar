package de.naju.adebar.model.events;

public class NewReservationEvent extends AbstractEventRelatedDomainEvent {

	private final Reservation reservation;

	public static NewReservationEvent of(Event event, Reservation reservation) {
		return new NewReservationEvent(event, reservation);
	}

	protected NewReservationEvent(Event event, Reservation reservation) {
		super(event);
		this.reservation = reservation;
	}

	public final Reservation getReservation() {
		return reservation;
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
		result = prime * result + ((reservation == null) ? 0 : reservation.hashCode());
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
		NewReservationEvent other = (NewReservationEvent) obj;
		if (reservation == null) {
			if (other.reservation != null)
				return false;
		} else if (!reservation.equals(other.reservation))
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
		return "NewReservationEvent [event=" + entity + ", reservation=" + reservation + "]";
	}

}
