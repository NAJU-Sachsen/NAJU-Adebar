package de.naju.adebar.services.conversion.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ReadOnlyEventRepository;
import de.naju.adebar.model.events.Reservation;
import de.naju.adebar.model.support.NumericEntityId;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ReservationConverter implements Converter<String, Reservation> {

  private final ReadOnlyEventRepository eventRepo;

  public ReservationConverter(ReadOnlyEventRepository eventRepo) {
    Assert.notNull(eventRepo, "reservationRepo may not be null");
    this.eventRepo = eventRepo;
  }

  @Override
  public Reservation convert(@Nonnull String source) {
    if (source.isEmpty()) {
      return null;
    }

    NumericEntityId id = new NumericEntityId(Long.parseLong(source));
    Optional<Event> event = eventRepo.findByReservation(Long.parseLong(source));

    return event.map(e -> e.getParticipantsList().getReservationWithId(id).orElse(null)) //
        .orElse(null);

  }
}
