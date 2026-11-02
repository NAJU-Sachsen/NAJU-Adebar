package de.naju.adebar.web.validation.events;

import de.naju.adebar.web.validation.events.participation.AccommodationForm;

public class AddEventForm extends EditEventForm {

	private boolean customAccommodation;

	private boolean flexibleParticipationTimes;

	private AccommodationForm accommodation = new AccommodationForm();

	public AddEventForm() {}

	public boolean isCustomAccommodation() {
		return customAccommodation;
	}

	public void setCustomAccommodation(boolean customAccommodation) {
		this.customAccommodation = customAccommodation;
	}

	public boolean isFlexibleParticipationTimes() {
		return flexibleParticipationTimes;
	}

	public void setFlexibleParticipationTimes(boolean flexibleParticipationTimes) {
		this.flexibleParticipationTimes = flexibleParticipationTimes;
	}

	public AccommodationForm getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(AccommodationForm accommodation) {
		this.accommodation = accommodation;
	}

	@Override
	public String toString() {
		return "AddEventForm [" + "customAccommodation=" + customAccommodation
				+ ", flexibleParticipationTimes=" + flexibleParticipationTimes + ", accommodation="
				+ accommodation + ", name='" + name + '\'' + ", startDate=" + startDate + ", endDate="
				+ endDate + ", useEventTime=" + useEventTime + ", startTime=" + startTime + ", endTime="
				+ endTime + ", place=" + place + ", participationInfo=" + participationInfo + ", belonging="
				+ belonging + ", localGroup=" + localGroup + ", project=" + project + ']';
	}
}
