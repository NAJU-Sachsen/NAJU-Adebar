package de.naju.adebar.app.persons.filter;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import de.naju.adebar.app.filter.AbstractFilterableField;
import de.naju.adebar.app.filter.querydsl.FieldToPathConverter;
import de.naju.adebar.app.persons.filter.ActivistFilterFields.Counselor;
import de.naju.adebar.app.persons.filter.ActivistFilterFields.Juleica;
import de.naju.adebar.app.persons.filter.ActivistFilterFields.JuleicaExpiry;
import de.naju.adebar.app.persons.filter.ActivistFilterFields.JuleicaLevel;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.DateOfBirth;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.EatingHabits;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.Event;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.Gender;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.HealthImpairments;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.NabuMembership;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.NabuMembershipNumber;
import de.naju.adebar.app.persons.filter.ParticipantFilterFields.Remarks;
import de.naju.adebar.app.persons.filter.PersonFilterFields.Activist;
import de.naju.adebar.app.persons.filter.PersonFilterFields.Email;
import de.naju.adebar.app.persons.filter.PersonFilterFields.FirstName;
import de.naju.adebar.app.persons.filter.PersonFilterFields.LastName;
import de.naju.adebar.app.persons.filter.PersonFilterFields.Participant;
import de.naju.adebar.app.persons.filter.PersonFilterFields.PhoneNumber;
import de.naju.adebar.app.persons.filter.PersonFilterFields.Referent;
import de.naju.adebar.app.persons.filter.ReferentFilterFields.QualificationDescription;
import de.naju.adebar.app.persons.filter.ReferentFilterFields.QualificationField;
import de.naju.adebar.model.events.QEvent;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.qualifications.QQualification;
import de.naju.adebar.util.Functional;
import org.springframework.stereotype.Service;

/**
 * @author Rico Bergmann
 */
@Service
public class PersonFieldToPathConverter implements FieldToPathConverter {

	private static final QPerson person = QPerson.person;
	private static final QEvent event = QEvent.event;
	private static final QQualification qualification = QQualification.qualification;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.querydsl.FieldToPathConverter#getPathFor(de.naju.adebar.app.filter.
	 * AbstractFilterableField)
	 */
	@Override
	public Path<?> getPathFor(AbstractFilterableField field) {
		return Functional.<Path<?>> //
				match(field) //

				// person filter fields
				.caseOf(FirstName.class, person.firstName) //
				.caseOf(LastName.class, person.lastName) //
				.caseOf(Email.class, person.email) //
				.caseOf(PhoneNumber.class, person.phoneNumber) //
				.caseOf(Participant.class, person.participant) //
				.caseOf(Activist.class, person.activist) //
				.caseOf(Referent.class, person.referent) //

				// participant filter fields
				.caseOf(Gender.class, person.participantProfile.gender) //
				.caseOf(DateOfBirth.class, person.participantProfile.dateOfBirth) //
				.caseOf(EatingHabits.class, person.participantProfile.eatingHabits) //
				.caseOf(HealthImpairments.class, person.participantProfile.healthImpairments) //
				.caseOf(NabuMembership.class, person.participantProfile.nabuMembership.nabuMember) //
				.caseOf(NabuMembershipNumber.class,
						person.participantProfile.nabuMembership.membershipNumber) //
				.caseOf(Remarks.class, person.participantProfile.remarks) //
				.caseOf(Event.class, person.participatingEvents.any().id.id) //

				// activist filter fields
				.caseOf(Juleica.class, person.activistProfile.juleicaCard) //
				.caseOf(JuleicaLevel.class, person.activistProfile.juleicaCard.level) //
				.caseOf(JuleicaExpiry.class, person.activistProfile.juleicaCard.expiryDate) //
				.caseOf(Counselor.class, person.activistProfile.counseledEvents.any().id.id) //

				// referent filter fields
				.caseOf(QualificationField.class, qualification) //
				.caseOf(QualificationDescription.class, qualification.description) //

				.runOrThrow();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.querydsl.FieldToPathConverter#getEntityFor(de.naju.adebar.app.filter.
	 * AbstractFilterableField)
	 */
	@Override
	public EntityPath<?> getEntityFor(AbstractFilterableField field) {
		return Functional.<EntityPath<?>> //
				match(field) //

				.caseOf(Event.class, event) //
				.caseOf(Counselor.class, event) //
				.caseOf(QualificationField.class, qualification) //
				.caseOf(QualificationDescription.class, qualification) //

				.runOrThrow();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.querydsl.FieldToPathConverter#getNecessaryJoinFor(de.naju.adebar.app.
	 * filter.AbstractFilterableField)
	 */
	@Override
	public Join getNecessaryJoinFor(AbstractFilterableField field) {
		return Functional.<Join> //
				match(field) //

				.caseOf(Event.class, Join.of(person.participatingEvents, event)) //
				.caseOf(Counselor.class, Join.of(person.activistProfile.counseledEvents, event)) //
				.caseOf(QualificationField.class,
						Join.of(person.referentProfile.qualifications, qualification)) //
				.caseOf(QualificationDescription.class,
						Join.of(person.referentProfile.qualifications, qualification)) //

				.runOrThrow();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.naju.adebar.app.filter.querydsl.FieldToPathConverter#needsJoinFor(de.naju.adebar.app.filter.
	 * AbstractFilterableField)
	 */
	@Override
	public boolean needsJoinFor(AbstractFilterableField field) {
		return Functional.<Boolean> //
				match(field) //

				.caseOf(Event.class, true) //
				.caseOf(Counselor.class, true) //
				.caseOf(QualificationField.class, true) //
				.caseOf(QualificationDescription.class, true) //

				.defaultCase(false) //
				.runOrThrow();
	}

}
