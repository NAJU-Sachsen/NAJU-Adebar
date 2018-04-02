package de.naju.adebar.api.util;

import de.naju.adebar.api.forms.FilterPersonForm;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.app.persons.filter.stream.ActivistFilter;
import de.naju.adebar.app.persons.filter.stream.AddressFilter;
import de.naju.adebar.app.persons.filter.stream.NameFilter;
import de.naju.adebar.app.persons.filter.stream.PersonFilter;
import de.naju.adebar.app.persons.filter.stream.ReferentFilter;
import de.naju.adebar.model.core.Address;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Service to convert {@link FilterPersonForm} data to the corresponding objects
 *
 * @author Rico Bergmann
 */
@Service
public class PersonFilterDataExtractor {

  private static final int MAX_FILTER_COUNT = 4;

  /**
   * @param form the form to extract data from
   * @return all filters which are encoded by the form
   */
  public Iterable<PersonFilter> extractAllFilters(FilterPersonForm form) {
    ArrayList<PersonFilter> filters = new ArrayList<>(MAX_FILTER_COUNT);

    filters.add(extractNameFilter(form));
    filters.add(extractAddressFilter(form));

    if (form.hasActivistInfo()) {
      filters.add(extractActivistFilter(form));
    }

    if (form.hasReferentInfo()) {
      filters.add(extractReferentFilter(form));
    }

    filters.trimToSize();
    return filters;
  }

  /**
   * Creates the name filter
   *
   * @param form the data to use for the filter
   * @return the filter
   */
  protected NameFilter extractNameFilter(FilterPersonForm form) {
    return new NameFilter(form.getFirstName(), form.getLastName());
  }

  /**
   * Creates the address filter
   *
   * @param form the data to use for the filter
   * @return the filter
   */
  protected AddressFilter extractAddressFilter(FilterPersonForm form) {
    return new AddressFilter(new Address("", "", form.getCity()), MatchType.IF_DEFINED);
  }

  /**
   * Creates the activist filter
   *
   * @param form the data to use for the filter
   * @return the filter
   */
  protected ActivistFilter extractActivistFilter(FilterPersonForm form) {
    if (form.isActivist()) {
      return new ActivistFilter(FilterType.ENFORCE);
    } else {
      return new ActivistFilter(FilterType.IGNORE);
    }
  }

  /**
   * Creates the referent filter
   *
   * @param form the data to use for the filter
   * @return the filter
   */
  protected ReferentFilter extractReferentFilter(FilterPersonForm form) {
    if (form.isReferent()) {
      return new ReferentFilter(FilterType.ENFORCE);
    } else {
      return new ReferentFilter(FilterType.IGNORE);
    }
  }

}
