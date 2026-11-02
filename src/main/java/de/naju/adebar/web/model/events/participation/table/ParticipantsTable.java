package de.naju.adebar.web.model.events.participation.table;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.Unmodifiable;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.ParticipantsList;
import de.naju.adebar.model.persons.Person;

/**
 * A participants table consists of one row for each participant of an {@link Event}. The columns
 * will contain data about the person itself or some other information about this specific
 * participation. Which columns are displayed varies from table to table.
 * <p>
 * For each available column a static field featuring a column code was created. This code will be
 * recognized by services rendering the table's data.
 *
 * @author Rico Bergmann
 */
public class ParticipantsTable {

	/**
	 * Column displaying the name of the participant.
	 */
	public static final String COLUMN_NAME = "name";

	/**
	 * Column displaying the participant's last name and first name.
	 */
	public static final String COLUMN_NAME_REVERSED = "reversedName";

	/**
	 * Column displaying the email address of the participant.
	 */
	public static final String COLUMN_EMAIL = "email";

	/**
	 * Column displaying the phone number of the participant.
	 */
	public static final String COLUMN_PHONE = "phone";

	/**
	 * Column displaying the complete address of the participant.
	 */
	public static final String COLUMN_ADDRESS = "address";

	/**
	 * Column displaying the city the participant lives in.
	 */
	public static final String COLUMN_CITY = "city";

	/**
	 * Column displaying whether the participant opted into receiving marketing information.
	 */
	public static final String COLUMN_MARKETING_STATUS = "marketingStatus";

	/**
	 * Column displaying the participant's birthday.
	 */
	public static final String COLUMN_DATE_OF_BIRTH = "dateOfBirth";

	/**
	 * Column displaying the participant's age (at the time the event starts).
	 */
	public static final String COLUMN_AGE = "age";

	/**
	 * Column displaying the participant's eating habits.
	 */
	public static final String COLUMN_EATING_HABIT = "eatingHabits";

	/**
	 * Column displaying the health impairments of the participant.
	 */
	public static final String COLUMN_HEALTH_IMPAIRMENTS = "healthImpairments";

	/**
	 * Column displaying whether the participant is a club member of the NABU.
	 */
	public static final String COLUMN_NABU_MEMBERSHIP = "nabu";

	/**
	 * Column displaying additional important information about the person (which is independent of
	 * his/her participation in this event).
	 */
	public static final String COLUMN_PERSON_REMARKS = "personRemarks";

	/**
	 * Column displaying the names of the participant's parents.
	 */
	public static final String COLUMN_PARENTS_NAME = "parents";

	/**
	 * Column displaying the private phone numbers of the participant's parents. Duplicates will be
	 * removed.
	 */
	public static final String COLUMN_PARENTS_PRIVATE_PHONE = "parentsPrivatePhone";

	/**
	 * Column displaying the landline phone numbers of the participant's parents. Duplicates will be
	 * removed.
	 */
	public static final String COLUMN_PARENTS_LANDLINE_PHONE = "parentsLandlinePhone";

	/**
	 * Column displaying the phone numbers of the participant's parents at work. Duplicates will be
	 * removed.
	 */
	public static final String COLUMN_PARENTS_WORK_PHONE = "parentsWorkPhone";

	/**
	 * Column displaying the time the person registered for the event.
	 */
	public static final String COLUMN_REGISTRATION_DATE = "registrationDate";

	/**
	 * Column displaying whether the participant has received the registration form.
	 */
	public static final String COLUMN_REGISTRATION_FORM_SENT = "registrationFormSent";

	/**
	 * Column displaying whether the participant has filled (and signed) the registration form.
	 */
	public static final String COLUMN_REGISTRATION_FORM_FILLED = "registrationFormFilled";

	/**
	 * Column displaying whether the participant has payed the participation fee.
	 */
	public static final String COLUMN_PARTICIPATION_FEE_PAYED = "feePayed";

	/**
	 * Column displaying the selected arrival option of the participant.
	 */
	public static final String COLUMN_ARRIVAL = "arrival";

	/**
	 * Column displaying the selected departure option of the participant.
	 */
	public static final String COLUMN_DEPARTURE = "departure";

	/**
	 * Column displaying whether the participant is allowed to leave from the event on his/her own (or
	 * whether some relative has to pick him/her up).
	 */
	public static final String COLUMN_MAY_GO_HOME_SINGLY = "goHomeSingly";

	/**
	 * Column displaying the time span the participant attends the event.
	 */
	public static final String COLUMN_PARTICIPATION_TIME = "participationTime";

	/**
	 * Column displaying additional information about this specific participation.
	 */
	public static final String COLUMN_PARTICIPATION_REMARKS = "participationRemarks";

	/**
	 * Collection of all available columns. The list may not be modified.
	 */
	@Unmodifiable
	public static final List<String> ALL_COLUMNS =
			Collections.unmodifiableList(Arrays.asList(COLUMN_NAME, COLUMN_NAME_REVERSED, COLUMN_EMAIL,
					COLUMN_PHONE, COLUMN_ADDRESS, COLUMN_CITY, COLUMN_MARKETING_STATUS, COLUMN_DATE_OF_BIRTH,
					COLUMN_AGE, COLUMN_EATING_HABIT, COLUMN_HEALTH_IMPAIRMENTS, COLUMN_NABU_MEMBERSHIP,
					COLUMN_PERSON_REMARKS, COLUMN_PARENTS_NAME, COLUMN_PARENTS_PRIVATE_PHONE,
					COLUMN_PARENTS_LANDLINE_PHONE, COLUMN_PARENTS_WORK_PHONE, COLUMN_REGISTRATION_DATE,
					COLUMN_REGISTRATION_FORM_SENT, COLUMN_REGISTRATION_FORM_FILLED,
					COLUMN_PARTICIPATION_FEE_PAYED, COLUMN_ARRIVAL, COLUMN_DEPARTURE,
					COLUMN_PARTICIPATION_TIME, COLUMN_PARTICIPATION_REMARKS));

	private final Event event;
	private final ParticipantsList participants;
	private final List<String> selectedColumns;

	/**
	 * Generates the most commonly used table.
	 * <p>
	 * It will contain the following columns:
	 * <ul>
	 * <li>the participants' names</li>
	 * <li>the participants' ages</li>
	 * <li>the selected arrival options</li>
	 * <li>whether the participants received the registration form</li>
	 * <li>whether the participants signed the registration form already</li>
	 * <li>whether the participants have payed the participation fee</li>
	 * <li>additional remarks about the participations</li>
	 * </ul>
	 *
	 * @return a builder to further initialize the table
	 */
	public static ParticipantsTableBuilder defaultTable() {
		return with(COLUMN_NAME, COLUMN_AGE, COLUMN_ARRIVAL, COLUMN_REGISTRATION_FORM_SENT,
				COLUMN_REGISTRATION_FORM_FILLED, COLUMN_PARTICIPATION_FEE_PAYED,
				COLUMN_PARTICIPATION_REMARKS);
	}

	/**
	 * Generates a table with custom columns.
	 *
	 * @param columns the columns to show
	 * @return a builder to further initialize the table
	 */
	public static ParticipantsTableBuilder with(List<String> columns) {
		return new ParticipantsTableBuilder(columns);
	}

	/**
	 * Generates a table with custom columns.
	 *
	 * @param columns the columns to show
	 * @return a builder to further initialize the table
	 */
	public static ParticipantsTableBuilder with(String... columns) {
		return new ParticipantsTableBuilder(Arrays.asList(columns));
	}

	/**
	 * Primary constructor.
	 *
	 * @param event the event for which the participants should be displayed
	 * @param columns the columns this table should contain. Each column must have a well-known code.
	 * @see #ALL_COLUMNS
	 */
	ParticipantsTable(Event event, List<String> columns) {
		Assert.notNull(event, "Event may not be null");
		for (String col : columns) {
			// this will check for null cols as well
			Assert.isTrue(ALL_COLUMNS.contains(col), "Unknown column: " + col);
		}
		this.event = event;
		this.participants = event.getParticipantsList();
		this.selectedColumns = columns;
	}

	/**
	 * Gets all columns in this table.
	 */
	public Iterable<String> getColumns() {
		return selectedColumns;
	}

	/**
	 * Gets the event that this table is created for.
	 */
	public Event getEvent() {
		return event;
	}

	/**
	 * Gets the participants (i.e. the rows) in this table.
	 */
	public Iterable<Person> getParticipants() {
		return participants.getParticipantsList();
	}

	@Override
	public String toString() {
		return "ParticipantsTable [" + "event=" + event + ", selectedColumns=" + selectedColumns + ']';
	}
}
