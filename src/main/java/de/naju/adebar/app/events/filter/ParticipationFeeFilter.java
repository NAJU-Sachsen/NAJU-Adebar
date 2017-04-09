package de.naju.adebar.app.events.filter;

import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.model.events.Event;
import org.javamoney.moneta.Money;

import java.util.stream.Stream;

/**
 * Filter based on the events' participation fee
 * @author Rico Bergmann
 */
public class ParticipationFeeFilter implements EventFilter {
    private Money participationFee;
    private ComparableFilterType filterType;

    /**
     * @param participationFee the participation fee to base the filter on
     * @param filterType how to treat the fee
     */
    public ParticipationFeeFilter(Money participationFee, ComparableFilterType filterType) {
        this.participationFee = participationFee;
        this.filterType = filterType;
    }

    @Override
    public Stream<Event> filter(Stream<Event> input) {
        input = input.filter(event -> event.getParticipationFee() != null);
        return input.filter(event -> filterType.matching(participationFee, event.getParticipationFee()));
    }
}
