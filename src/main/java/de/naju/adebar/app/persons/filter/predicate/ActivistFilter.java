package de.naju.adebar.app.persons.filter.predicate;

import java.time.LocalDate;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.QPerson;

public class ActivistFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private PersonFilter filterImpl;

  public ActivistFilter(FilterType filterType) {
    this.filterImpl = new GeneralActivistFilter(filterType);
  }

  public ActivistFilter(boolean hasJuleica) {
    this.filterImpl = new JuleicaStatusFilter(hasJuleica);
  }

  public ActivistFilter(LocalDate expiryDate, DateFilterType filterType) {
    this.filterImpl = new JuleicaExpiryDateFilter(expiryDate, filterType);
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return filterImpl.filter(input);
  }

  private class GeneralActivistFilter implements PersonFilter {
    private FilterType filterType;

    public GeneralActivistFilter(FilterType filterType) {
      this.filterType = filterType;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      switch (filterType) {
        case ENFORCE:
          return input.and(person.activist.isTrue());
        case IGNORE:
          return input.and(person.activist.isFalse());
        default:
          throw new AssertionError(filterType);
      }
    }
  }

  private class JuleicaStatusFilter implements PersonFilter {
    private boolean hasJuleica;

    public JuleicaStatusFilter(boolean hasJuleica) {
      this.hasJuleica = hasJuleica;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      if (hasJuleica) {
        return input.and(person.activistProfile.juleicaCard.isNotNull());
      } else {
        return input.and(person.activistProfile.juleicaCard.isNull());
      }
    }
  }

  private class JuleicaExpiryDateFilter implements PersonFilter {
    private LocalDate expiryDate;
    private DateFilterType filterType;

    public JuleicaExpiryDateFilter(LocalDate expiryDate, DateFilterType filterType) {
      this.expiryDate = expiryDate;
      this.filterType = filterType;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      switch (filterType) {
        case AFTER:
          return input.and(person.activistProfile.juleicaCard.expiryDate.after(expiryDate));
        case EXACT:
          return input.and(person.activistProfile.juleicaCard.expiryDate.eq(expiryDate));
        case BEFORE:
          return input.and(person.activistProfile.juleicaCard.expiryDate.before(expiryDate));
        default:
          throw new AssertionError(filterType);
      }
    }

  }

}
