package de.naju.adebar.model.persons;

import de.naju.adebar.model.persons.family.Relatives;
import de.naju.adebar.model.persons.family.VitalRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class MarketingManager {

	private final VitalRecord vitalRecord;

	public MarketingManager(VitalRecord vitalRecord) {
		Assert.notNull(vitalRecord, "Vital record may not be null");
		this.vitalRecord = vitalRecord;
	}

	@Transactional
	public void optOut(Person person) {
		setMarketingStatus(false, person, true);
	}

	public void enableMarketingFor(Person person) {
		setMarketingStatus(true, person, true);
	}

	private void setMarketingStatus(boolean mayReceiveMarketing, Person person, boolean cascade) {
		person.setMarketingProhibition(!mayReceiveMarketing);

		if (!cascade) {
			return;
		}

		final Relatives relatives = vitalRecord.findRelativesOf(person);
		if (person.isParticipant() && person.getParticipantProfile().isOfMinorAge()) {
			person.getParents().forEach(parent -> setMarketingStatus(mayReceiveMarketing, parent, false));
			relatives.getSiblings().forEach(sibling -> setMarketingStatus(mayReceiveMarketing, sibling, false));
		}

		relatives.getChildren().stream() //
				.filter(child -> !child.isParticipant() || child.getParticipantProfile().isOfMinorAge()) //
				.forEach(child -> setMarketingStatus(mayReceiveMarketing, child, false));
	}
}
