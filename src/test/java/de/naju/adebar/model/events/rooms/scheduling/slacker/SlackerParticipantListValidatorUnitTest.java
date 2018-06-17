package de.naju.adebar.model.events.rooms.scheduling.slacker;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import de.naju.adebar.model.events.rooms.scheduling.AbstractParticipantsListValidatorTest;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.persons.details.Gender;

public class SlackerParticipantListValidatorUnitTest extends AbstractParticipantsListValidatorTest {

  /*
   * Slackers should work quickly o_O
   *
   * If they are that lazy, they should at least be done quickly
   */
  @Rule
  public Timeout timeoutRule = Timeout.millis(1500L);

  /*
   * @formatter:off
   *
   * The participation times look like this:
   *
   * +------+----+----+----+----+----+----+----+----+----+----+----+
   * |      |  1 |  2 |  3 |  4 |  5 |  6 |  7 |  8 |  9 | 10 | 11 |
   * +------+----+----+----+----+----+----+----+----+----+----+----+
   * |hans  | == | == | == |    |    |    |    |    |    |    |    |
   * |martha|    | == | == | == | == |    |    |    |    |    |    |
   * |dieter|    |    |    |    |    | == | == | == |    |    |    |
   * |nadine|    |    |    | == | == | == | == | == | == | == | == |
   * |fritz |    |    |    | == | == |    |    |    | == | == | == |
   * +------+----+----+----+----+----+----+----+----+----+----+----+
   *
   * @formatter:on
   */

  private SlackerParticipantListValidator slacker = new SlackerParticipantListValidator();

  @Test
  public void detectsSchedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.MALE) //
        .addRoom(2, Gender.FEMALE);
    RegisteredParticipants participants = RegisteredParticipants.of(hans, dieter, martha, nadine);
    assertThat(slacker.isSchedulable(spec, participants)).isTrue();
  }

  @Test
  public void detectsUnschedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addRoom(1, Gender.MALE);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, dieter, fritz, martha, nadine);
    assertThat(slacker.isSchedulable(spec, participants)).isFalse();
  }

  @Test
  public void handlesPersonsWithMultipleParticipationTimesCorrectly() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(2, Gender.FEMALE) //
        .addRoom(1, Gender.MALE);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, martha, dieter, nadine, fritz);
    assertThat(slacker.isSchedulable(spec, participants)).isTrue();
  }

}
