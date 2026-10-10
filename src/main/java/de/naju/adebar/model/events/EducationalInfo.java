package de.naju.adebar.model.events;

import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;

@Embeddable
public class EducationalInfo extends AbstractEventInfo {

	@ElementCollection(fetch = FetchType.LAZY)
	private List<Lecture> lectures;

	// TODO implementation and usage

}
