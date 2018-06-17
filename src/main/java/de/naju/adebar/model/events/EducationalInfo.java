package de.naju.adebar.model.events;

import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;

@Embeddable
public class EducationalInfo extends AbstractEventInfo {

  @ElementCollection(fetch = FetchType.LAZY)
  private List<Lecture> lectures;

  // TODO implementation and usage

}
