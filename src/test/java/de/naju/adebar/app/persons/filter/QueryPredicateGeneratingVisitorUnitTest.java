package de.naju.adebar.app.persons.filter;

import static org.assertj.core.api.Assertions.assertThat;
import com.querydsl.jpa.impl.JPAQueryFactory;
import de.naju.adebar.TestData;
import de.naju.adebar.app.filter.ComparingFilter;
import de.naju.adebar.app.filter.ComparingFilter.Comparison;
import de.naju.adebar.app.filter.querydsl.QueryPredicateGeneratingVisitor;
import de.naju.adebar.app.filter.ContainmentFilter;
import de.naju.adebar.app.filter.EqualityFilter;
import de.naju.adebar.app.filter.InvertableFilter;
import de.naju.adebar.app.filter.JoiningFilter;
import de.naju.adebar.app.filter.ListFilter;
import de.naju.adebar.documentation.Internal;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.ReferentProfile;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.qualifications.QQualification;
import de.naju.adebar.model.persons.qualifications.Qualification;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class QueryPredicateGeneratingVisitorUnitTest {

	@Autowired
	private PersonRepository personRepo;

	@Autowired
	private PersonFieldToPathConverter pathConverter;

	@Autowired
	private EntityManager entityManager;

	private QueryPredicateGeneratingVisitor<Person> predicateGeneratingvisitor;

	private PersonFilterFields personFilterFields = new PersonFilterFields();
	private ParticipantFilterFields participantFilterFields = new ParticipantFilterFields();
	private ReferentFilterFields referentFilterFields = new ReferentFilterFields();

	@Before
	public void setUp() {
		personRepo.saveAll(TestData.getAllParticipants());

		this.predicateGeneratingvisitor = new QueryPredicateGeneratingVisitor<>( //
				pathConverter, //
				entityManager, //
				QPerson.person);
	}

	@Test
	public void generatesEqualityPredicate() {
		Person expectedPerson = TestData.getParticipant(TestData.PERSON_HANS);
		EqualityFilter filterUnderTest = personFilterFields.getEmailFilter();
		filterUnderTest.provideValue(expectedPerson.getEmail());
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactly(expectedPerson);
	}

	@Test
	public void generatesContainmentPredicate() {
		Person berta = TestData.getParticipant(TestData.PERSON_BERTA);
		Person marta = TestData.getParticipant(TestData.PERSON_MARTA);
		ContainmentFilter filterUnderTest = personFilterFields.getFirstNameFilter();
		filterUnderTest.provideValue("rta");
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrder(berta, marta);
	}

	@Test
	public void generatesComparingPredicateForLessThan() {
		LocalDate medianDate = TestData.getAllParticipants().stream() //
				.map(p -> p.getParticipantProfile().getDateOfBirth()) //
				.sorted() //
				.skip(TestData.TEST_ENTRIES_COUNT / 2) //
				.findFirst() //
				.get(); // if this fails our test data is broken -- we want it to fail in this case!
		List<Person> participantsBornBefore = TestData.getAllParticipants().stream() //
				.sorted(Comparator.comparing(p -> p.getParticipantProfile().getDateOfBirth())) //
				.limit(TestData.TEST_ENTRIES_COUNT / 2) //
				.collect(Collectors.toList());

		ComparingFilter filterUnderTest = participantFilterFields.getDateOfBirthFilter();
		filterUnderTest.setComparison(Comparison.LESS_THAN);
		filterUnderTest.provideValue(medianDate);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(participantsBornBefore);
	}

	@Test
	public void generatesComparingPredicateForGreaterThan() {
		LocalDate medianDate = TestData.getAllParticipants().stream() //
				.map(p -> p.getParticipantProfile().getDateOfBirth()) //
				.sorted() //
				.skip(TestData.TEST_ENTRIES_COUNT / 2) //
				.findFirst() //
				.get(); // if this fails our test data is broken -- we want it to fail in this case!
		List<Person> participantsBornAfter = TestData.getAllParticipants().stream() //
				.sorted(Comparator.comparing(p -> p.getParticipantProfile().getDateOfBirth())) //
				.skip(TestData.TEST_ENTRIES_COUNT / 2 + 1) //
				.collect(Collectors.toList());

		ComparingFilter filterUnderTest = participantFilterFields.getDateOfBirthFilter();
		filterUnderTest.setComparison(Comparison.GREATER_THAN);
		filterUnderTest.provideValue(medianDate);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(participantsBornAfter);
	}

	@Test
	public void respectsResultInversionIfRequested() {
		LocalDate minDate = TestData.getAllParticipants().stream() //
				.map(p -> p.getParticipantProfile().getDateOfBirth()) //
				.min(Comparator.naturalOrder()) //
				.get(); // if this fails our test data is broken -- we want it to fail in this case!
		List<Person> participantsBornAfter = TestData.getAllParticipants().stream() //
				.filter(p -> !p.getParticipantProfile().getDateOfBirth().equals(minDate)) //
				.collect(Collectors.toList());

		ComparingFilter matchingDateOfBirthFilter = participantFilterFields.getDateOfBirthFilter();
		matchingDateOfBirthFilter.setComparison(Comparison.EQUAL);
		matchingDateOfBirthFilter.provideValue(minDate);
		InvertableFilter filterUnderTest = InvertableFilter.of(matchingDateOfBirthFilter);
		filterUnderTest.setInvertResults(true);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(participantsBornAfter);
	}

	@Test
	public void ignoresResultInversionIfNotRequested() {
		Person expectedPerson = TestData.getParticipant(TestData.PERSON_HANS);
		EqualityFilter matchingEmailFilter = personFilterFields.getEmailFilter();
		matchingEmailFilter.provideValue(expectedPerson.getEmail());
		InvertableFilter filterUnderTest = InvertableFilter.of(matchingEmailFilter);
		filterUnderTest.setInvertResults(false);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactly(expectedPerson);
	}

	@Test
	public void andJoinEnforcesBothFilters() {
		List<Person> maleVegans = TestData.getAllParticipants().stream() //
				.filter(p -> p.getParticipantProfile().getGender().equals(Gender.MALE)) //
				.filter( //
						p -> p.getParticipantProfile().getEatingHabits().equals(TestData.EATING_HABIT_VEGAN)) //
				.collect(Collectors.toList());

		InvertableFilter maleFilter = participantFilterFields.getGenderFilter();
		maleFilter.setInvertResults(false);
		maleFilter.provideValue(Gender.MALE);

		ContainmentFilter veganFilter = participantFilterFields.getEatingHabitsFilter();
		veganFilter.provideValue(TestData.EATING_HABIT_VEGAN);

		JoiningFilter filterUnderTest = JoiningFilter.of(maleFilter).and(veganFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(maleVegans);
	}

	@Test
	public void andJoinFailsIfOneFilterDoesNotMatch() {
		InvertableFilter femaleFilter = participantFilterFields.getGenderFilter();
		femaleFilter.setInvertResults(false);
		femaleFilter.provideValue(Gender.FEMALE);

		ContainmentFilter firstNameFilter = personFilterFields.getFirstNameFilter();
		firstNameFilter.provideValue("Nonexistent first name");

		JoiningFilter filterUnderTest = JoiningFilter.of(femaleFilter).and(firstNameFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).isEmpty();
	}

	@Test
	public void orJoinEnforcesAtLeastOneFilter() {
		List<Person> expectedResult = TestData.getAllParticipants().stream() //
				.filter(p -> p.getParticipantProfile().getGender().equals(Gender.FEMALE)) //
				.collect(Collectors.toList());
		expectedResult.add(TestData.getParticipant(TestData.PERSON_HANS));

		InvertableFilter femaleFilter = participantFilterFields.getGenderFilter();
		femaleFilter.setInvertResults(false);
		femaleFilter.provideValue(Gender.FEMALE);

		ContainmentFilter firstNameFilter = personFilterFields.getFirstNameFilter();
		firstNameFilter.provideValue(TestData.getParticipant(TestData.PERSON_HANS).getFirstName());

		JoiningFilter filterUnderTest = JoiningFilter.of(femaleFilter).or(firstNameFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(expectedResult);
	}

	@Test
	public void orJoinDoesNotFailIfBothMatch() {
		List<Person> expectedPersons = TestData.getAllParticipants().stream() //
				.filter(p -> p.getParticipantProfile().getGender().equals(Gender.FEMALE)) //
				.collect(Collectors.toList());

		InvertableFilter femaleFilter = participantFilterFields.getGenderFilter();
		femaleFilter.setInvertResults(false);
		femaleFilter.provideValue(Gender.FEMALE);

		ContainmentFilter firstNameFilter = personFilterFields.getFirstNameFilter();
		firstNameFilter.provideValue(TestData.getParticipant(TestData.PERSON_BERTA).getFirstName());

		JoiningFilter filterUnderTest = JoiningFilter.of(femaleFilter).or(firstNameFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrderElementsOf(expectedPersons);
	}

	@Test
	public void orJoinFailsIfNoFilterMatches() {
		LocalDate maxDateOfBirth = TestData.getAllParticipants().stream() //
				.map(p -> p.getParticipantProfile().getDateOfBirth()) //
				.max(Comparator.naturalOrder()) //
				.get(); // if this fails our test data is broken -- we want it to fail in this case!

		ComparingFilter dateOfBirthFilter = participantFilterFields.getDateOfBirthFilter();
		dateOfBirthFilter.setComparison(Comparison.GREATER_THAN);
		dateOfBirthFilter.provideValue(maxDateOfBirth.plusDays(1L));

		ContainmentFilter eatingHabitsFilter = participantFilterFields.getEatingHabitsFilter();
		eatingHabitsFilter.provideValue(TestData.EATING_HABIT_VEGETARIAN); // there are no vegetarians!!

		JoiningFilter filterUnderTest = JoiningFilter.of(dateOfBirthFilter).or(eatingHabitsFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);
		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).isEmpty();
	}

	@Test
	@Transactional
	public void usesJoinsIfNecessary() {

		/*
		 * INITIALIZATION
		 */

		Pair<Person, Qualification> referentSetup = setUpReferents();
		Person expectedPerson = referentSetup.getFirst();
		Qualification expectedQualification = referentSetup.getSecond();


		/*
		 * FILTER SETUP
		 */

		ListFilter filterUnderTest = referentFilterFields.getQualificationFilter();
		filterUnderTest
				.addValue(referentFilterFields.generateSingleQualificationFilter(expectedQualification));
		filterUnderTest.accept(predicateGeneratingvisitor);

		/*
		 * RESULT COMPARISON
		 */

		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrder(expectedPerson);
	}

	@Test
	public void supportsArbitraryConditionsInJoins() {

		/*
		 * INITIALIZATION
		 */

		Pair<Person, Qualification> referentSetup = setUpReferents();
		Person expectedPerson = referentSetup.getFirst();
		Qualification expectedQualification = referentSetup.getSecond();

		/*
		 * FILTER SETUP
		 */

		ListFilter filterUnderTest = referentFilterFields.getQualificationFilter();
		ContainmentFilter qualificationDescriptionFilter =
				referentFilterFields.getQualificationDescriptionFilter();
		qualificationDescriptionFilter.provideValue(expectedQualification.getDescription()
				.substring(expectedQualification.getDescription().length() / 2));
		filterUnderTest.addValue(qualificationDescriptionFilter);
		filterUnderTest.accept(predicateGeneratingvisitor);

		/*
		 * RESULT COMPARISON
		 */

		List<Person> result = predicateGeneratingvisitor.getResultingQuery().fetch();
		assertThat(result).containsExactlyInAnyOrder(expectedPerson);
	}

	/**
	 * This test is just to ensure that join predicates work at all
	 */
	@Test
	@Internal
	@Transactional
	public void simpleJoinTest() {
		Pair<Person, Qualification> referentSetup = setUpReferents();
		Person expectedPerson = referentSetup.getFirst();
		Qualification expectedQualification = referentSetup.getSecond();

		QQualification qualification = QQualification.qualification;

		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		List<Person> result = queryFactory.selectFrom(QPerson.person)
				.join(QPerson.person.referentProfile.qualifications, qualification)
				.where(qualification.eq(expectedQualification)).fetch();

		assertThat(result).containsExactlyInAnyOrder(expectedPerson);
	}

	@Transactional
	protected Pair<Person, Qualification> setUpReferents() {
		Person expectedPerson = TestData.getParticipant(TestData.PERSON_HANS);

		// qualifications are compared by their name so it is OK to instantiate multiple of them...
		Qualification expectedQualification = new Qualification("Foo", "Bar baz");

		// this is a bit ugly
		if (expectedPerson.isReferent()) {
			return Pair.of(expectedPerson, expectedQualification);
		}

		expectedPerson.makeReferent().addQualification(expectedQualification);

		// to make sure the predicate actually filters, we will add another referent with another
		// qualification

		Person anotherReferent = TestData.getParticipant(TestData.PERSON_BERTA);
		ReferentProfile anotherProfile = anotherReferent.makeReferent();
		Qualification anotherQualification =
				new Qualification("Another qual", "Just another qualification");
		Qualification yetAnotherQualification = new Qualification("And one more", "No description!!");
		anotherProfile.addQualification(anotherQualification);
		anotherProfile.addQualification(yetAnotherQualification);

		personRepo.saveAll(Arrays.asList(expectedPerson, anotherReferent));

		return Pair.of(expectedPerson, expectedQualification);
	}

}
