package de.naju.adebar.app.events.filter;

import java.util.stream.Stream;
import org.javamoney.moneta.Money;
import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.model.events.Event;

/**
 * Filter based on the events' participation fee
 * 
 * @author Rico Bergmann
 */
public class ParticipationFeeFilter implements EventFilter {
  private Money internalParticipationFee;
  private Money externalParticipationFee;
  private ComparableFilterType filterType;

  /**
   * @param internalParticipationFee the participation fee to base the filter on
   * @param filterType how to treat the fee
   */
  public ParticipationFeeFilter(Money internalParticipationFee, Money externalParticipationFee,
      ComparableFilterType filterType) {
    this.internalParticipationFee = internalParticipationFee;
    this.externalParticipationFee = externalParticipationFee;
    this.filterType = filterType;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    if (internalParticipationFee != null) {
      input = input.filter(event -> event.getInternalParticipationFee() != null);
      input = input.filter(event -> filterType.matching(internalParticipationFee,
          event.getInternalParticipationFee()));
    }

    if (externalParticipationFee != null) {
      input = input.filter(event -> event.getExternalParticipationFee() != null);
      input = input.filter(event -> filterType.matching(externalParticipationFee,
          event.getExternalParticipationFee()));
    }

    return input;
  }
}
