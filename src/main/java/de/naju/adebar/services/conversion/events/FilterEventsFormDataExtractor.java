package de.naju.adebar.services.conversion.events;

import de.naju.adebar.app.events.filter.AddressFilter;
import de.naju.adebar.app.events.filter.EndTimeFilter;
import de.naju.adebar.app.events.filter.EventFilter;
import de.naju.adebar.app.events.filter.MinimumParticipantAgeFilter;
import de.naju.adebar.app.events.filter.NameFilter;
import de.naju.adebar.app.events.filter.ParticipantsLimitFilter;
import de.naju.adebar.app.events.filter.ParticipationFeeFilter;
import de.naju.adebar.app.events.filter.StartTimeFilter;
import de.naju.adebar.app.filter.ComparableFilterType;
import de.naju.adebar.app.filter.DateTimeFilterType;
import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.web.validation.events.FilterEventsForm;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

/**
 * Service to convert {@link FilterEventsForm} data to corresponding objects
 *
 * @author Rico Bergmann
 */
@Service
public class FilterEventsFormDataExtractor {

  private static final String NO_FILTER = "none";

  private final DateTimeFormatter dateTimeFormatter;

  public FilterEventsFormDataExtractor() {
    this.dateTimeFormatter =
        DateTimeFormatter.ofPattern(FilterEventsForm.DATE_TIME_FORMAT, Locale.GERMAN);
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link AddressFilter}
   */
  public boolean hasAddressFilter(FilterEventsForm eventsForm) {
    return !((eventsForm.getStreet() == null || eventsForm.getStreet().isEmpty())
        && (eventsForm.getZip() == null || eventsForm.getZip().isEmpty())
        && (eventsForm.getCity() == null || eventsForm.getCity().isEmpty()));
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public AddressFilter extractAddressFilter(FilterEventsForm eventsForm) {
    if (!hasAddressFilter(eventsForm)) {
      throw new IllegalStateException("No address filter specified");
    }
    Address address =
        new Address(eventsForm.getStreet(), eventsForm.getZip(), eventsForm.getCity());
    return new AddressFilter(address, MatchType.IF_DEFINED);
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link EndTimeFilter}
   */
  public boolean hasEndTimeFilter(FilterEventsForm eventsForm) {
    return !eventsForm.getEndFilterType().equals(NO_FILTER);
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public EndTimeFilter extractEndTimeFilter(FilterEventsForm eventsForm) {
    if (!hasEndTimeFilter(eventsForm)) {
      throw new IllegalStateException("No end time filter specified");
    }
    LocalDateTime endTime = LocalDateTime.parse(eventsForm.getEnd(), dateTimeFormatter);
    return new EndTimeFilter(endTime, DateTimeFilterType.valueOf(eventsForm.getEndFilterType()));
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link MinimumParticipantAgeFilter}
   */
  public boolean hasMinimumParticipantAgeFilter(FilterEventsForm eventsForm) {
    return eventsForm.getParticipantsAgeFilter();
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public MinimumParticipantAgeFilter extractMinimumParticipantAgeFilter(
      FilterEventsForm eventsForm) {
    if (!hasMinimumParticipantAgeFilter(eventsForm)) {
      throw new IllegalStateException("No minimum participant age filter specified");
    }
    return new MinimumParticipantAgeFilter(Age.of(eventsForm.getParticipantsAge()));
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link NameFilter}
   */
  public boolean hasNameFilter(FilterEventsForm eventsForm) {
    return eventsForm.getName() != null && !eventsForm.getName().isEmpty();
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public NameFilter extractNameFilter(FilterEventsForm eventsForm) {
    if (!hasNameFilter(eventsForm)) {
      throw new IllegalStateException("No name filter specified");
    }
    return new NameFilter(eventsForm.getName());
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link ParticipantsLimitFilter}
   */
  public boolean hasParticipantsLimitFilter(FilterEventsForm eventsForm) {
    return !eventsForm.getParticipantsLimitFilterType().equals(NO_FILTER);
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public ParticipantsLimitFilter extractParticipantsLimitFilter(FilterEventsForm eventsForm) {
    if (!hasParticipantsLimitFilter(eventsForm)) {
      throw new IllegalStateException("No participants limit filter specified");
    }
    return new ParticipantsLimitFilter(eventsForm.getParticipantsLimit(),
        ComparableFilterType.valueOf(eventsForm.getParticipantsLimitFilterType()));
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link ParticipationFeeFilter}
   */
  public boolean hasParticipationFeeFilter(FilterEventsForm eventsForm) {
    return !eventsForm.getFeeFilterType().equals(NO_FILTER);
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public ParticipationFeeFilter extractParticipationFeeFilter(FilterEventsForm eventsForm) {
    if (!hasParticipationFeeFilter(eventsForm)) {
      throw new IllegalStateException("No participation fee filter specified");
    }

    Money internalFee = null;
    Money externalFee = null;
    if (eventsForm.hasInternalFee()) {
      internalFee =
          Money.of(new BigDecimal(eventsForm.getInternalFee()), FilterEventsForm.CURRENCY_UNIT);
    }
    if (eventsForm.hasExternalFee()) {
      externalFee =
          Money.of(new BigDecimal(eventsForm.getExternalFee()), FilterEventsForm.CURRENCY_UNIT);
    }

    return new ParticipationFeeFilter(internalFee, externalFee,
        ComparableFilterType.valueOf(eventsForm.getFeeFilterType()));
  }

  /**
   * @param eventsForm the form to check
   * @return {@code true} if the form contains data for an {@link StartTimeFilter}
   */
  public boolean hasStartTimeFilter(FilterEventsForm eventsForm) {
    return !eventsForm.getStartFilterType().equals(NO_FILTER);
  }

  /**
   * @param eventsForm form containing the data to extract
   * @return the filter encoded by the form
   *
   * @throws IllegalStateException if the form does not contain data for such a filter
   */
  public StartTimeFilter extractStartTimeFilter(FilterEventsForm eventsForm) {
    if (!hasStartTimeFilter(eventsForm)) {
      throw new IllegalStateException("No start time filter specified");
    }
    LocalDateTime startTime = LocalDateTime.parse(eventsForm.getStart(), dateTimeFormatter);
    return new StartTimeFilter(startTime,
        DateTimeFilterType.valueOf(eventsForm.getStartFilterType()));
  }

  /**
   * @param eventsForm the form to extract data from
   * @return all filters which are encoded by the form
   */
  public Iterable<EventFilter> extractAllFilters(FilterEventsForm eventsForm) {
    List<EventFilter> filters = new LinkedList<>();
    if (hasAddressFilter(eventsForm)) {
      filters.add(extractAddressFilter(eventsForm));
    }
    if (hasEndTimeFilter(eventsForm)) {
      filters.add(extractEndTimeFilter(eventsForm));
    }
    if (hasMinimumParticipantAgeFilter(eventsForm)) {
      filters.add(extractMinimumParticipantAgeFilter(eventsForm));
    }
    if (hasNameFilter(eventsForm)) {
      filters.add(extractNameFilter(eventsForm));
    }
    if (hasParticipantsLimitFilter(eventsForm)) {
      filters.add(extractParticipantsLimitFilter(eventsForm));
    }
    if (hasParticipationFeeFilter(eventsForm)) {
      filters.add(extractParticipationFeeFilter(eventsForm));
    }
    if (hasStartTimeFilter(eventsForm)) {
      filters.add(extractStartTimeFilter(eventsForm));
    }
    return filters;
  }
}
