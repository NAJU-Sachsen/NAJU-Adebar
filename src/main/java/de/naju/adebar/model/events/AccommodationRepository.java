package de.naju.adebar.model.events;

import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.support.NumericEntityId;
import org.springframework.data.repository.CrudRepository;

public interface AccommodationRepository
		extends CrudRepository<RoomSpecification, NumericEntityId> {

}
