package de.naju.adebar.model.events;

import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;

public interface DynamicParticipationTime {

  ParticipationTime getParticipationTime();

  boolean hasParticipationTime();

}
