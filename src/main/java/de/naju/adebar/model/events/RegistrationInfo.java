package de.naju.adebar.model.events;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;

/**
 * Information about the participation of a person for a specific event.
 * <p>
 * This value object saves all the information that change for each event, such as whether the
 * participant payed the necessary fee or how she is going to arrive at the event's location.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class RegistrationInfo implements ParticipationInfoWithDynamicTime {

	@Column(name = "acknowledged")
	private boolean acknowledged;

	@Column(name = "feePayed")
	private boolean participationFeePayed;

	@Column(name = "formSent")
	private boolean registrationFormSent;

	@Column(name = "formFilled")
	private boolean registrationFormFilled;

	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "description", column = @Column(name = "arrival")))
	private ArrivalOption arrivalOption;

	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "description",
			column = @Column(name = "departure")))
	private ArrivalOption departureOption;

	@Column(name = "goHomeSingly")
	private boolean mayGoHomeSingly;

	@Column(name = "remarks", length = 511)
	private String remarks;

	@Embedded
	private ParticipationTime participationTime;

	@Column(name = "registrationDate")
	private LocalDateTime registrationDate = LocalDateTime.now();

	/**
	 * Full constructor.
	 */
	private RegistrationInfo(boolean acknowledged, boolean participationFeePayed,
			boolean registrationFormSent, boolean registrationFormFilled, ArrivalOption arrivalOption,
			ArrivalOption departureOption, boolean mayGoHomeSingly, String remarks,
			ParticipationTime participationTime, LocalDateTime registrationDate) {
		this.acknowledged = acknowledged;
		this.participationFeePayed = participationFeePayed;

		// if the participant signed the registrion form, he/she must also have received it before
		this.registrationFormSent = registrationFormSent || registrationFormFilled;

		this.registrationFormFilled = registrationFormFilled;
		this.arrivalOption = arrivalOption;
		this.departureOption = departureOption;
		this.mayGoHomeSingly = mayGoHomeSingly;
		this.remarks = remarks;
		this.participationTime = participationTime;
		this.registrationDate = registrationDate;
	}

	/**
	 * Default constructor.
	 */
	RegistrationInfo() {}

	/**
	 * Starts the construction process for a new registration.
	 *
	 * @return the builder which takes care of the further setup
	 */
	public static RegistrationInfoBuilder generate() {
		return new RegistrationInfoBuilder();
	}

	/**
	 * Checks, whether the participation was acknowledged. This is of special interest to prevent a
	 * sudden overbooking of an event if the organizers keep track of the registrations in places
	 * other than this software. Furthermore, in case of automated online registration, each
	 * registration should be manually confirmed.
	 */
	public boolean isAcknowledged() {
		return acknowledged;
	}


	/**
	 * Checks, whether the registration form has been sent to the participant. This should usually be
	 * the case as the form should be sent to the participant right away, as soon as she voices here
	 * interest in participating. However, if the form is not ready yet, or somebody else than the
	 * person talking to the participant takes care of the registration process, the transmission may
	 * be delayed.
	 */
	public boolean isRegistrationFormSent() {
		return registrationFormSent;
	}

	/**
	 * Checks, whether the participation fee was payed.
	 */
	public boolean isParticipationFeePayed() {
		return participationFeePayed;
	}

	/**
	 * Checks, whether the participant filled and signed the registration form (and whether it was
	 * sent back to the office).
	 */
	public boolean isRegistrationFormFilled() {
		return registrationFormFilled;
	}

	/**
	 * Checks, whether an arrival option was specified for the participant.
	 */
	public boolean hasArrivalOption() {
		return arrivalOption != null;
	}

	/**
	 * Gets preferred way of approaching this event.
	 *
	 * @return the selected option. May be {@code null} if none was selected.
	 */
	public ArrivalOption getArrivalOption() {
		return arrivalOption;
	}

	/**
	 * Gets preferred way of leaving this event.
	 *
	 * @return the selected option. May be {@code null} if none was selected.
	 */
	public ArrivalOption getDepartureOption() {
		return departureOption;
	}

	/**
	 * Checks, whether the participant may leave from this event on her own or whether she has to be
	 * picked up by her parents (or other relatives).
	 */
	public boolean isMayGoHomeSingly() {
		return mayGoHomeSingly;
	}

	/**
	 * Checks, whether the participant may leave from this event on her own or whether she has to be
	 * picked up by her parents (or other relatives).
	 * <p>
	 * This is just an alias for {@link #isMayGoHomeSingly()} which sounds better.
	 */
	public boolean mayGoHomeSingly() {
		return mayGoHomeSingly;
	}

	/**
	 * Gets additional information that are important for this participation.
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Checks whether there are some special remarks about this participation.
	 */
	public boolean hasRemarks() {
		return remarks != null && !remarks.isEmpty();
	}

	/**
	 * Gets the time the participant registered for this event.
	 * <p>
	 * It will usually be equivalent to the timestamp of the creation of {@code this} object.
	 *
	 * @return the registration date. May be {@code null} for registrations created before the {@code
	 *     registrationDate} attribute was added to this class or if it is unknown for some other
	 *         reason.
	 * @since v0.5
	 */
	public LocalDateTime getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * Checks, whether this {@code RegistrationInfo} kept track the registration date.
	 */
	public boolean hasRegistrationDate() {
		return registrationDate != null;
	}

	@Override
	public ParticipationTime getParticipationTime() {
		return participationTime;
	}

	@Override
	public boolean hasParticipationTime() {
		return participationTime != null;
	}

	/**
	 * Updates the registration date.
	 *
	 * @param registrationDate the new date. May be {@code null} to indicate unknown values.
	 */
	void setRegistrationDate(LocalDateTime registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * Updates whether the participant payed the necessary fee (which may either be the internal or
	 * external one - this is not tracked).
	 */
	void setParticipationFeePayed(boolean participationFeePayed) {
		this.participationFeePayed = participationFeePayed;
	}

	/**
	 * Updates the period that the participant attends this event.
	 *
	 * @param participationTime the time. A {@code null} value indicates that the span does not differ
	 *        from the period that the event takes place.
	 */
	void setParticipationTime(ParticipationTime participationTime) {
		this.participationTime = participationTime;
	}

	/**
	 * Sets whether the participation was acknowledged yet.
	 */
	@JpaOnly
	private void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	/**
	 * Updates whether the participant has received the registration form for this event.
	 */
	@JpaOnly
	private void setRegistrationFormSent(boolean registrationFormSent) {
		this.registrationFormSent = registrationFormSent;
	}

	/**
	 * Updates whether the participant has filled the registration form for this event.
	 * <p>
	 * This may influence the {@code registrationFormSent} field as filling the form implies receiving
	 * it first.
	 */
	@JpaOnly
	private void setRegistrationFormFilled(boolean registrationFormFilled) {
		this.registrationFormFilled = registrationFormFilled;

		if (registrationFormFilled) {
			// if the registration form is filled, the participant must also have received it
			setRegistrationFormSent(true);
		}
	}

	/**
	 * Updates the selected arrival option.
	 *
	 * @param arrivalOption the new option. May be {@code null} if none was selected.
	 */
	@JpaOnly
	private void setArrivalOption(ArrivalOption arrivalOption) {
		this.arrivalOption = arrivalOption;
	}

	/**
	 * Updates the selected departure option.
	 *
	 * @param departureOption the new option. May be {@code null} if none was selected.
	 */
	@JpaOnly
	private void setDepartureOption(ArrivalOption departureOption) {
		this.departureOption = departureOption;
	}

	/**
	 * Sets whether the participant may leave this event on his/her own.
	 */
	@JpaOnly
	private void setMayGoHomeSingly(boolean mayGoHomeSingly) {
		this.mayGoHomeSingly = mayGoHomeSingly;
	}

	/**
	 * Sets important information about this participation for the attending person.
	 */
	@JpaOnly
	private void setRemarks(String remarks) {
		if (remarks == null) {
			this.remarks = "";
		} else {
			this.remarks = remarks;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return Objects.hash(acknowledged, participationFeePayed, registrationFormSent,
				registrationFormFilled, arrivalOption, departureOption, mayGoHomeSingly, remarks,
				participationTime, registrationDate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RegistrationInfo that = (RegistrationInfo) o;
		return acknowledged == that.acknowledged && participationFeePayed == that.participationFeePayed
				&& registrationFormSent == that.registrationFormSent
				&& registrationFormFilled == that.registrationFormFilled
				&& mayGoHomeSingly == that.mayGoHomeSingly
				&& Objects.equals(arrivalOption, that.arrivalOption)
				&& Objects.equals(departureOption, that.departureOption)
				&& Objects.equals(remarks, that.remarks)
				&& Objects.equals(participationTime, that.participationTime)
				&& Objects.equals(registrationDate, that.registrationDate);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegistrationInfo [" + "acknowledged=" + acknowledged + ", participationFeePayed="
				+ participationFeePayed + ", registrationFormSent=" + registrationFormSent
				+ ", registrationFormFilled=" + registrationFormFilled + ", arrivalOption=" + arrivalOption
				+ ", departureOption=" + departureOption + ", mayGoHomeSingly=" + mayGoHomeSingly
				+ ", remarks='" + remarks + '\'' + ", participationTime=" + participationTime
				+ ", registrationDate=" + registrationDate + ']';
	}

	/**
	 * The {@code Builder} will take care of creating new {@link RegistrationInfo} instances.
	 */
	public static class RegistrationInfoBuilder {

		private RegistrationInfo infoUnderConstruction = new RegistrationInfo();

		/**
		 * Bases the new information on some existing registration.
		 */
		public RegistrationInfoBuilder fromRegistration(RegistrationInfo existingInfo) {
			// do not assign the existingInfo directly here as the object is mutable internally!
			this.infoUnderConstruction = new RegistrationInfo(existingInfo.acknowledged,
					existingInfo.participationFeePayed, existingInfo.registrationFormSent,
					existingInfo.registrationFormFilled, existingInfo.arrivalOption,
					existingInfo.departureOption, existingInfo.mayGoHomeSingly, existingInfo.remarks,
					existingInfo.participationTime, existingInfo.registrationDate);
			return this;
		}

		/**
		 * Sets the info about the registration form.
		 *
		 * @param wasSent whether the form was sent to the participant
		 * @param wasFilled whether the participant filled the form already
		 */
		public RegistrationInfoBuilder specifyRegistrationForm(boolean wasSent, boolean wasFilled) {
			infoUnderConstruction.setRegistrationFormSent(wasSent);
			infoUnderConstruction.setRegistrationFormFilled(wasFilled);
			return this;
		}

		/**
		 * Sets whether the participant payed the necessary fee.
		 */
		public RegistrationInfoBuilder specifyParticipationFee(boolean wasPayed) {
			infoUnderConstruction.setParticipationFeePayed(wasPayed);
			return this;
		}

		/**
		 * Sets the chosen arrival and departure option.
		 *
		 * @param arrival the arrival option. May be {@code null} if none was chosen.
		 * @param departure the departure option. May be {@code null} if none was chosen.
		 * @param mayGoHomeSingly whether the participant may leave the event on his/her own
		 */
		public RegistrationInfoBuilder withArrivalAndDeparture(ArrivalOption arrival,
				ArrivalOption departure, boolean mayGoHomeSingly) {
			infoUnderConstruction.setArrivalOption(arrival);
			infoUnderConstruction.setDepartureOption(departure);
			infoUnderConstruction.setMayGoHomeSingly(mayGoHomeSingly);
			return this;
		}

		/**
		 * Adds special information about this participation.
		 */
		public RegistrationInfoBuilder withRemarks(String remarks) {
			infoUnderConstruction.setRemarks(remarks);
			return this;
		}

		/**
		 * Sets the participation time.
		 *
		 * @param timeSpan the period. May be {@code null} to indicate that it does not differ from the
		 *        event's time
		 */
		public RegistrationInfoBuilder withParticipationDuring(ParticipationTime timeSpan) {
			infoUnderConstruction.setParticipationTime(timeSpan);
			return this;
		}

		/**
		 * Finishes the construction and provides the resulting info.
		 */
		public RegistrationInfo build() {
			return infoUnderConstruction;
		}

	}

}
