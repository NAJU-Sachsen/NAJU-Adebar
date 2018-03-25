package de.naju.adebar.app.persons.filter.stream;

import java.util.List;
import java.util.stream.Stream;
import com.google.common.collect.Lists;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.qualifications.Qualification;

/**
 * Filter for persons depending on their referent status. It features two constructors depending on
 * what one actually wants to filter. Keep in mind that it is generally filtered for persons. That's
 * why actually no activists are needed here. For easy conversion there is a dedicated package
 * 
 * @author Rico Bergmann
 * @see de.naju.adebar.model.persons.ReferentProfile
 * @see de.naju.adebar.util.conversion conversion
 */
public class ReferentFilter implements PersonFilter {
  private FilterType filterType;
  private List<Qualification> qualifications;

  /**
   * This constructor should be used if just a filter on general referent status is needed. That is,
   * whether persons have to be referents, or whether they may not.
   * 
   * @param filterType the kind of filter we want (enforcing or ignoring)
   */
  public ReferentFilter(FilterType filterType) {
    this.filterType = filterType;
    this.qualifications = null;
  }

  /**
   * This constructor should be used if only referents with specific qualifications are wanted
   * 
   * @param qualifications the qualifications to filter for
   */
  public ReferentFilter(List<Qualification> qualifications) {
    this.filterType = null;
    this.qualifications = qualifications;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    // as the internals are quite simple we do not use the state pattern
    // here
    // although it would have been a cleaner solution

    // if the filter type is not null, we only need to filter for the
    // general referent status
    if (filterType != null) {
      if (filterType == FilterType.ENFORCE) {
        return personStream.filter(Person::isReferent);
      } else if (filterType == FilterType.IGNORE) {
        return personStream.filter(p -> !p.isReferent());
      }
    } else {
      // if we filter for referent with specific qualifications, we should
      // only consider referents
      Stream<Person> referents = personStream.filter(Person::isReferent);
      return referents
          .filter(person -> Lists.newLinkedList(person.getReferentProfile().getQualifications())
              .containsAll(qualifications));
    }
    return null;
  }
}
