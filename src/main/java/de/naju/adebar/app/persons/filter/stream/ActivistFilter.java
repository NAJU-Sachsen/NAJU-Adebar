package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their activist status.
 * <p>
 * There are currently three filters available:
 * <ol>
 * <li>a general filter for persons who are/are not activists</li>
 * <li>a filter for activists who do/do not have a Juleica card</li>
 * <li>a filter for persons whose Juleica card expires on/before/after a specific date</li>
 * </ol>
 * 
 * @author Rico Bergmann
 * @see de.naju.adebar.model.persons.ActivistProfile
 * @see de.naju.adebar.util.conversion conversion
 */
public class ActivistFilter implements PersonFilter {
  private PersonFilter concreteFilter;

  /**
   * Creates a filter for "general activist status", i. e. whether persons have to activists
   * (ENFORCE) or whether they may not be activists (IGNORE).
   * 
   * @param filterType the kind of filter we want (enforcing or ignoring)
   * @return the activist filter
   */
  public static ActivistFilter forActivistStatus(FilterType filterType) {
    return new ActivistFilter(filterType);
  }

  /**
   * Creates a filter for activists depending on whether they possess a Juleica card or not
   * (ignoring whether the card is still valid or not)
   * 
   * @param hasJuleica whether activists have to have a (potentially invalid) Juleica card, or
   *        whether they may not
   * @return the activist filter
   */
  public static ActivistFilter forJuleicaStatus(boolean hasJuleica) {
    return new ActivistFilter(hasJuleica);
  }

  /**
   * Creates a filter for activists depending on the expiry dates of their Juleica cards.
   * 
   * @param juleicaExpiryDate the date to filter for
   * @param dateFilterType the way the expiry date should be treated (as minimum, maximum or exact)
   * @return the activist filter
   */
  public static ActivistFilter forJuleicaExpiryDate(LocalDate expiryDate,
      DateFilterType dateFilterType) {
    return new ActivistFilter(expiryDate, dateFilterType);
  }

  /**
   * This constructor should be used if just a filter on general activist status is needed. That is,
   * whether persons have to be activists, or whether they may not.
   * 
   * @param filterType the kind of filter we want (enforcing or ignoring)
   */
  public ActivistFilter(FilterType filterType) {
    this.concreteFilter = new GeneralActivistFilter(filterType);
  }

  /**
   * This constructor should be used if activists should be filtered depending on whether they have
   * a Juleica card or not
   * 
   * @param hasJuleica whether activists have to have a (potentially invalid) Juleica card, or
   *        whether they may not
   */
  public ActivistFilter(boolean hasJuleica) {
    this.concreteFilter = new JuleicaStatusFilter(hasJuleica);
  }

  /**
   * This constructor should be used if only activists with a specific juleica expiry date are
   * wanted
   * 
   * @param juleicaExpiryDate the date to filter for
   * @param dateFilterType the way the expiry date should be treated (as minimum, maximum or exact)
   */
  public ActivistFilter(LocalDate juleicaExpiryDate, DateFilterType dateFilterType) {
    this.concreteFilter = new JuleicaExpiryDateFilter(juleicaExpiryDate, dateFilterType);
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    return concreteFilter.filter(personStream);
  }

  /**
   * Filter implementation for "general activist status", i. e. whether persons have to activists
   * (ENFORCE) or whether they may not be activists (IGNORE).
   * 
   * @author Rico Bergmann
   */
  private class GeneralActivistFilter implements PersonFilter {
    private FilterType filterType;

    GeneralActivistFilter(FilterType filterType) {
      this.filterType = filterType;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
      switch (filterType) {
        case ENFORCE:
          return personStream.filter(Person::isActivist);
        case IGNORE:
          return personStream.filter(p -> !p.isActivist());
        default:
          throw new AssertionError(filterType);
      }
    }
  }

  /**
   * Filter implementation for activists depending on whether they possess a Juleica card or not
   * (ignoring whether the card is still valid or not)
   * 
   * @author Rico Bergmann
   */
  private class JuleicaStatusFilter implements PersonFilter {
    private boolean hasJuleica;

    JuleicaStatusFilter(boolean hasJuleica) {
      this.hasJuleica = hasJuleica;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
      // if we filter for activists with a specific kind of Juleica expiry
      // date, we should only consider activists
      Stream<Person> activists = personStream.filter(Person::isActivist);

      activists = activists.filter(a -> a.getActivistProfile().hasJuleica() == hasJuleica);
      return activists;
    }
  }

  /**
   * Filter implementation for activists depending on the expiry dates of their Juleica cards.
   * 
   * @author Rico Bergmann
   */
  private class JuleicaExpiryDateFilter implements PersonFilter {
    private LocalDate juleicaExpiryDate;
    private DateFilterType dateFilterType;

    JuleicaExpiryDateFilter(LocalDate expiryDate, DateFilterType dateFilterType) {
      this.juleicaExpiryDate = expiryDate;
      this.dateFilterType = dateFilterType;
    }

    @Override
    public Stream<Person> filter(Stream<Person> personStream) {
      // if we filter for activists with a specific kind of Juleica expiry
      // date, we should only consider activists
      Stream<Person> activists = personStream.filter(Person::isActivist);

      // furthermore we should only filter for activists that actually
      // have a JuLeiCa
      activists = activists.filter(a -> a.getActivistProfile().hasJuleica());
      return activists.filter(person -> dateFilterType.matching(juleicaExpiryDate,
          person.getActivistProfile().getJuleicaCard().getExpiryDate()));
    }

  }
}
