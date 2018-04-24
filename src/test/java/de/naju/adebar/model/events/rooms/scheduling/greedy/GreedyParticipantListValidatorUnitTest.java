package de.naju.adebar.model.events.rooms.scheduling.greedy;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import de.naju.adebar.model.events.rooms.scheduling.AbstractParticipantsListValidatorTest;
import de.naju.adebar.model.events.rooms.scheduling.ExtendedRoomSpecification;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.persons.details.Gender;

public class GreedyParticipantListValidatorUnitTest extends AbstractParticipantsListValidatorTest {

  private GreedyParticipantListValidator validator = new GreedyParticipantListValidator();

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

  @Test
  public void detectsSchedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.MALE) //
        .addRoom(2, Gender.FEMALE);
    RegisteredParticipants participants = RegisteredParticipants.of(hans, dieter, martha, nadine);
    assertThat(validator.isSchedulable(spec, participants)).isTrue();
  }

  @Test
  public void detectsUnschedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addRoom(1, Gender.MALE);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, dieter, fritz, martha, nadine);
    assertThat(validator.isSchedulable(spec, participants)).isFalse();
  }

  @Test
  public void usesFlexRoomsCorrectly() {
    ExtendedRoomSpecification spec = new ExtendedRoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addRoom(1, Gender.MALE) //
        .addFlexRoom(1) //
        .addFlexRoom(2);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, dieter, fritz, martha, nadine);
    assertThat(validator.isSchedulableWithExtendedSpec(spec, participants)).isTrue();
  }

  @Test
  public void usesFallbackRoomsCorrectly() {
    ExtendedRoomSpecification spec = new ExtendedRoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addRoom(1, Gender.MALE) //
        .addFallbackRoom(2);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, dieter, fritz, martha, nadine);
    assertThat(validator.isSchedulableWithExtendedSpec(spec, participants)).isTrue();
  }

  @Test
  public void detectsUnschedulableSpecificationsWithFlexRooms() {
    ExtendedRoomSpecification spec = new ExtendedRoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addFlexRoom(1);
    RegisteredParticipants participants =
        RegisteredParticipants.of(hans, dieter, fritz, martha, nadine);
    assertThat(validator.isSchedulableWithExtendedSpec(spec, participants)).isFalse();
  }

}
