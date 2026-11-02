package de.naju.adebar.web.model.events.participation;

import de.naju.adebar.model.events.ParticipationManager;
import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm.AddParticipantForm;
import java.util.Objects;

public class ParticipationReportEntry {

	private final AddParticipantForm registrationInfo;
	private final ParticipationManager.Result result;

	ParticipationReportEntry(AddParticipantForm registrationInfo, Result result) {
		Assert2.noNullArguments("No argument may be null", registrationInfo, result);
		this.registrationInfo = registrationInfo;
		this.result = result;
	}

	public Person getParticipant() {
		return registrationInfo.getParticipant();
	}

	public AddParticipantForm getRegistrationInfo() {
		return registrationInfo;
	}

	public Result getResult() {
		return result;
	}

	@Override
	public int hashCode() {

		return Objects.hash(registrationInfo.getParticipant());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ParticipationReportEntry that = (ParticipationReportEntry) o;
		return Objects.equals(registrationInfo.getParticipant(),
				that.registrationInfo.getParticipant());
	}

	@Override
	public String toString() {
		return "ParticipationReportEntry [" + "participant=" + registrationInfo.getParticipant()
				+ ", registrationInfo=" + registrationInfo + ", result=" + result + ']';
	}

}
