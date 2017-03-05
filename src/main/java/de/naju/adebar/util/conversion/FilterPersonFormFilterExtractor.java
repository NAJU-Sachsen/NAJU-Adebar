package de.naju.adebar.util.conversion;

import com.google.common.collect.Lists;
import de.naju.adebar.app.human.filter.*;
import de.naju.adebar.controller.forms.FilterPersonForm;
import de.naju.adebar.model.human.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service to convert {@link FilterPersonForm} data to the corresponding objects
 * @author Rico Bergmann
 */
@Service
public class FilterPersonFormFilterExtractor {
    private final static String NO_FILTER = "none";

    private DateTimeFormatter dateFormatter;

    private HumanManager humanManager;
    private QualificationRepository qualificationRepo;
    private PersonStreamConverter streamConverter;

    @Autowired
    public FilterPersonFormFilterExtractor(HumanManager humanManager, QualificationRepository qualificationRepo,
                                           PersonStreamConverter streamConverter) {
        Object[] params = {humanManager, qualificationRepo, qualificationRepo};
        Assert.noNullElements(params, "No parameter may be null!");
        this.humanManager = humanManager;
        this.qualificationRepo = qualificationRepo;
        this.streamConverter = streamConverter;
        this.dateFormatter = DateTimeFormatter.ofPattern(FilterPersonForm.DATE_FORMAT, Locale.GERMAN);
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the form contains data for an {@link ActivistFilter}
     */
    public boolean hasActivistFilter(FilterPersonForm personForm) {
        return !personForm.getActivistFilterType().equals(NO_FILTER);
    }

    /**
     * @param personForm form containing the data to extract
     * @return the {@link ActivistFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link ActivistFilter}
     */
    public ActivistFilter extractActivistFilter(FilterPersonForm personForm) {
        if (!hasActivistFilter(personForm)) {
            throw new IllegalStateException("Form did not specify an activist filter: " + personForm);
        }
        if (personForm.getActivistJuleicaFilterType().equals(NO_FILTER)) {
            List<Activist> activists = humanManager.activistManager().repository().streamAll().collect(Collectors.toList());
            Stream<Person> convertedActivists = streamConverter.convertActivistStream(activists.stream());
            return new ActivistFilter(convertedActivists, FilterType.valueOf(personForm.getActivistFilterType()));
        } else {
            Stream<Person> personStream = streamConverter.convertActivistStream(humanManager.activistManager().repository().streamAll());
            LocalDate juleicaExpiryDate = LocalDate.parse(personForm.getJuleicaExpiryDate(), dateFormatter);
            DateFilterType dateFilterType = DateFilterType.valueOf(personForm.getActivistJuleicaExpiryFilterType());
            Map<Person, LocalDate> expiryDates = humanManager.activistManager().getJuleicaExpiryDates();
            return new ActivistFilter(personStream, juleicaExpiryDate, dateFilterType, expiryDates);
        }
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the form contains data for an {@link AddressFilter}
     */
    public boolean hasAddressFilter(FilterPersonForm personForm) {
        return !((personForm.getStreet() == null || personForm.getStreet().isEmpty())
                && (personForm.getZip() == null || personForm.getZip().isEmpty())
                && (personForm.getCity() == null || personForm.getCity().isEmpty()));
    }

    /**
     * @param personForm form containing the data to extract
     * @return the {@link AddressFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link AddressFilter}
     */
    public AddressFilter extractAddressFilter(FilterPersonForm personForm) {
        if (!hasAddressFilter(personForm)) {
            throw  new IllegalStateException("No address filter specified: " + personForm);
        }
        Address address = new Address(personForm.getStreet(), personForm.getZip(), personForm.getCity());
        return new AddressFilter(address, MatchType.IF_DEFINED);
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the form contains data for an {@link DateOfBirthFilter}
     */
    public boolean hasDateOfBirthFilter(FilterPersonForm personForm) {
        return !personForm.getDobFilterType().equals(NO_FILTER);
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link DateOfBirthFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link DateOfBirthFilter}
     */
    public DateOfBirthFilter extractDateOfBirthFilter(FilterPersonForm personForm) {
        if (!hasDateOfBirthFilter(personForm)) {
            throw new IllegalStateException("No date of birth filter specified: " + personForm);
        }
        return new DateOfBirthFilter(LocalDate.parse(personForm.getDob(), dateFormatter), DateFilterType.valueOf(personForm.getDobFilterType()));
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the form contains data for an {@link EatingHabitFilter}
     */
    public boolean hasEatingHabitFilter(FilterPersonForm personForm) {
        return stringIsSet(personForm.getEatingHabit());
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link EatingHabitFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link EatingHabitFilter}
     */
    public EatingHabitFilter extractEatingHabitFilter(FilterPersonForm personForm) {
        if (!hasEatingHabitFilter(personForm)) {
            throw new IllegalStateException("No eating habit filter was specified: " + personForm);
        }
        return new EatingHabitFilter(personForm.getEatingHabit());
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the contains data for an {@link EmailFilter}
     */
    public boolean hasEmailFilter(FilterPersonForm personForm) {
        return stringIsSet(personForm.getEmail());
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link EmailFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link EmailFilter}
     */
    public EmailFilter extractEmailFilter(FilterPersonForm personForm) {
        if (!hasEmailFilter(personForm)) {
            throw new IllegalStateException("No email filter was specified: " + personForm);
        }
        return new EmailFilter(personForm.getEmail());
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the contains data for an {@link GenderFilter}
     */
    public boolean hasGenderFilter(FilterPersonForm personForm) {
        return !personForm.getGenderFilterType().equals(NO_FILTER);
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link GenderFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link GenderFilter}
     */
    public GenderFilter extractGenderFilter(FilterPersonForm personForm) {
        if (!hasGenderFilter(personForm)) {
            throw new IllegalStateException("No gender filter was specified: " + personForm);
        }
        return new GenderFilter(Gender.valueOf(personForm.getGender()), FilterType.valueOf(personForm.getGenderFilterType()));
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the contains data for an {@link HealthImpairmentsFilter}
     */
    public boolean hasHealthImpairmentsFilter(FilterPersonForm personForm) {
        return stringIsSet(personForm.getHealthImpairments());
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link HealthImpairmentsFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link HealthImpairmentsFilter}
     */
    public HealthImpairmentsFilter extractHealthImpairmentsFilter(FilterPersonForm personForm) {
        if (!hasHealthImpairmentsFilter(personForm)) {
            throw  new IllegalStateException("No health impairments filter was specified: " + personForm);
        }
        return new HealthImpairmentsFilter(personForm.getHealthImpairments());
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the contains data for an {@link NameFilter}
     */
    public boolean hasNameFilter(FilterPersonForm personForm) {
        return !((personForm.getFirstName() == null || personForm.getFirstName().isEmpty())
                && (personForm.getLastName() == null || personForm.getLastName().isEmpty()));
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link NameFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link NameFilter}
     */
    public NameFilter extractNameFilter(FilterPersonForm personForm) {
        if (!hasNameFilter(personForm)) {
            throw new IllegalStateException("No name filter was specified: " + personForm);
        }
        return new NameFilter(personForm.getFirstName(), personForm.getLastName());
    }

    /**
     * @param personForm the form to check
     * @return {@code true} if the contains data for an {@link ReferentFilter}
     */
    public boolean hasReferentFilter(FilterPersonForm personForm) {
        return !personForm.getReferentsFilterType().equals(NO_FILTER);
    }

    /**
     * @param personForm the form containing the data to extract
     * @return the {@link ReferentFilter} encoded by the form
     * @throws IllegalStateException if the form does not contain data for an {@link ReferentFilter}
     */
    public ReferentFilter extractReferentFilter(FilterPersonForm personForm) {
        if (!hasReferentFilter(personForm)) {
            throw new IllegalStateException("No referent filter was specified: " + personForm);
        }
        if (personForm.getReferentQualifications().isEmpty()) {
            Stream<Person> referents = streamConverter.convertReferentStream(humanManager.referentManager().repository().streamAll());
            return new ReferentFilter(referents, FilterType.valueOf(personForm.getReferentsFilterType()));
        } else {
            Stream<Person> referents = streamConverter.convertReferentStream(humanManager.referentManager().repository().streamAll());
            List<Qualification> qualifications = Lists.newLinkedList(qualificationRepo.findAll(personForm.getReferentQualifications()));
            Map<Person, Iterable<Qualification>> referentQualifications = humanManager.referentManager().getQualifications();
            return new ReferentFilter(referents, qualifications, referentQualifications);
        }
    }

    public boolean hasNabuMembershipFilter(FilterPersonForm personForm) {
        return !personForm.getNabuMembershipFilterType().equals(NO_FILTER);
    }

    public NabuMembershipFilter extractNabuMembershipFilter(FilterPersonForm personForm) {
        if (!hasNabuMembershipFilter(personForm)) {
            throw new IllegalStateException("No NABU membership filter was specified: " + personForm);
        }
        FilterType filterType = FilterType.valueOf(personForm.getNabuMembershipFilterType());
        if (filterType == FilterType.ENFORCE && !personForm.getNabuMembershipNumber().isEmpty()) {
            return new NabuMembershipFilter(personForm.getNabuMembershipNumber());
        } else {
            return new NabuMembershipFilter(filterType);
        }
    }

    /**
     * @param personForm the form to extract data from
     * @return all filters which are encoded by the form
     */
    public Iterable<PersonFilter> extractAllFilters(FilterPersonForm personForm) {
        List<PersonFilter> filters = new LinkedList<>();
        if (hasActivistFilter(personForm)) filters.add(extractActivistFilter(personForm));
        if (hasAddressFilter(personForm)) filters.add(extractAddressFilter(personForm));
        if (hasDateOfBirthFilter(personForm)) filters.add(extractDateOfBirthFilter(personForm));
        if (hasEatingHabitFilter(personForm)) filters.add(extractEatingHabitFilter(personForm));
        if (hasEmailFilter(personForm)) filters.add(extractEmailFilter(personForm));
        if (hasGenderFilter(personForm)) filters.add(extractGenderFilter(personForm));
        if (hasHealthImpairmentsFilter(personForm)) filters.add(extractHealthImpairmentsFilter(personForm));
        if (hasNameFilter(personForm)) filters.add(extractNameFilter(personForm));
        if (hasReferentFilter(personForm)) filters.add(extractReferentFilter(personForm));
        if (hasNabuMembershipFilter(personForm)) filters.add(extractNabuMembershipFilter(personForm));
        return filters;
    }

    /**
     * @param str the string to check
     * @return {@code true \u21D4 \u00AC(str = null) \u2227 \u00AC(str = "")}
     */
    private boolean stringIsSet(String str) {
        return str != null && !str.isEmpty();
    }
}
