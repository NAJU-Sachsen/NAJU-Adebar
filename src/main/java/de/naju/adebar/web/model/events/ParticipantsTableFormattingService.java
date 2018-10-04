package de.naju.adebar.web.model.events;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.columns.AddressColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.AgeColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ArrivalOptionColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.CityColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.DateOfBirthColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.DepartureOptionColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.EatingHabitsColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.EmailColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.HealthImpairmentsColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.MayGoHomeSinglyColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.NabuMembershipColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.NameColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParentsLandlinePhoneColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParentsNameColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParentsPrivatePhoneColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParentsWorkPhoneColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParticipationFeePayedColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParticipationRemarksColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.ParticipationTimeColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.PersonRemarksColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.PhoneColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.RegistrationDateColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.RegistrationFormFilledColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.RegistrationFormSentColumnFormatter;
import de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A service that provides the appropriate {@link TableColumnFormatter renderer} for a column of a
 * {@link ParticipantsTable} and takes care of actually rendering the columns.
 *
 * @author Rico Bergmann
 */
@Service
public class ParticipantsTableFormattingService {

  private final ApplicationContext ctx;

  /**
   * Full constructor.
   *
   * @param ctx the context containing all the formatting services. May never be {@code null}.
   */
  public ParticipantsTableFormattingService(ApplicationContext ctx) {
    Assert.notNull(ctx, "Application context may not be null");
    this.ctx = ctx;
  }

  /**
   * Fetches the formatter that is responsible for rendering a certain column.
   *
   * @param columnName the code of the column to render (defined in {@link ParticipantsTable}).
   * @return the matching formatter
   * @throws IllegalArgumentException if the code does not correspond to any known column and
   *     thus no formatter exists.
   */
  public TableColumnFormatter getColumnFormatterFor(String columnName) {
    TableColumnFormatter formatter;

    // we will fetch the appropriate renderer dynamically instead of storing them all as fields

    /*
     * TODO refactor: we may simply query for all services that inherit from TableColumnFormatter
     * and then ask each formatter, for which column it is responsible (through a new method)
     */
    switch (columnName) {
      case ParticipantsTable.COLUMN_NAME:
        formatter = ctx.getBean(NameColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_EMAIL:
        formatter = ctx.getBean(EmailColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PHONE:
        formatter = ctx.getBean(PhoneColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_ADDRESS:
        formatter = ctx.getBean(AddressColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_CITY:
        formatter = ctx.getBean(CityColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_DATE_OF_BIRTH:
        formatter = ctx.getBean(DateOfBirthColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_AGE:
        formatter = ctx.getBean(AgeColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_EATING_HABIT:
        formatter = ctx.getBean(EatingHabitsColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_HEALTH_IMPAIRMENTS:
        formatter = ctx.getBean(HealthImpairmentsColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_NABU_MEMBERSHIP:
        formatter = ctx.getBean(NabuMembershipColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PERSON_REMARKS:
        formatter = ctx.getBean(PersonRemarksColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARENTS_NAME:
        formatter = ctx.getBean(ParentsNameColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARENTS_PRIVATE_PHONE:
        formatter = ctx.getBean(ParentsPrivatePhoneColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARENTS_LANDLINE_PHONE:
        formatter = ctx.getBean(ParentsLandlinePhoneColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARENTS_WORK_PHONE:
        formatter = ctx.getBean(ParentsWorkPhoneColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_REGISTRATION_DATE:
        formatter = ctx.getBean(RegistrationDateColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_REGISTRATION_FORM_SENT:
        formatter = ctx.getBean(RegistrationFormSentColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_REGISTRATION_FORM_FILLED:
        formatter = ctx.getBean(RegistrationFormFilledColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARTICIPATION_FEE_PAYED:
        formatter = ctx.getBean(ParticipationFeePayedColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_ARRIVAL:
        formatter = ctx.getBean(ArrivalOptionColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_DEPARTURE:
        formatter = ctx.getBean(DepartureOptionColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_MAY_GO_HOME_SINGLY:
        formatter = ctx.getBean(MayGoHomeSinglyColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARTICIPATION_TIME:
        formatter = ctx.getBean(ParticipationTimeColumnFormatter.class);
        break;
      case ParticipantsTable.COLUMN_PARTICIPATION_REMARKS:
        formatter = ctx.getBean(ParticipationRemarksColumnFormatter.class);
        break;
      default:
        throw new IllegalArgumentException("Unknown column: " + columnName);
    }

    return formatter;
  }

  /**
   * Renders a specific column of the {@link ParticipantsTable}.
   *
   * @param event the event for which the column should be rendered
   * @param participant the participant whose column should be rendered
   * @param columnName the name of the column to render
   * @return the rendered column
   */
  public String getColumnValueFor(Event event, Person participant, String columnName) {
    return getColumnFormatterFor(columnName).formatColumnFor(participant, event);
  }

  /**
   * Checks, if a formatter may render a column for a certain event.
   *
   * @param event the event
   * @param columnName the column to check
   * @return whether the formatter will work
   * @see TableColumnFormatter#isApplicable(Event)
   */
  public boolean formatterIsApplicable(Event event, String columnName) {
    return getColumnFormatterFor(columnName).isApplicable(event);
  }

}
