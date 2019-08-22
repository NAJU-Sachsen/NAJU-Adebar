package de.naju.adebar.web.model.events.participation;

import de.naju.adebar.model.events.ParticipationManager.Result;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.participation.AddParticipantsForm.AddParticipantForm;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.util.Assert;

public class ParticipationReport {

	private Set<ParticipationReportEntry> report;

	public ParticipationReport() {
		this.report = new HashSet<>();
	}

	public void appendEntry(AddParticipantForm registrationInfo, Result result) {
		Assert.notNull(registrationInfo, "registrationInfo may not be null");

		ParticipationReportEntry reportEntry = new ParticipationReportEntry(registrationInfo, result);

		Assert2.isFalse(report.contains(reportEntry),
				"Report already contains entry for " + registrationInfo.getParticipant());

		report.add(reportEntry);
	}

	public boolean isFailed() {
		return report.stream().anyMatch(entry -> entry.getResult() != Result.OK);
	}

	public Collection<ParticipationReportEntry> getFailedParticipations() {
		return report.stream() //
				.filter(entry -> entry.getResult() != Result.OK) //
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "ParticipationReport [" + report + ']';
	}

}
