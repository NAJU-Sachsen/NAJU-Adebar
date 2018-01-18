package de.naju.adebar.model.events.rooms.scheduling.slacker;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import de.naju.adebar.TestData;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipationTime;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;
import de.naju.adebar.model.events.rooms.scheduling.slacker.SlackerParticipantListValidator;
import de.naju.adebar.model.persons.Gender;

public class SlackerParticipantListValidatorUnitTest {

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

  private Participant hans =
      new Participant(TestData.getParticipant("hans"), new ParticipationTime(1, 3));

  private Participant martha =
      new Participant(TestData.getParticipant("martha"), new ParticipationTime(2, 5));

  private Participant dieter =
      new Participant(TestData.getParticipant("dieter"), new ParticipationTime(6, 8));

  private Participant nadine =
      new Participant(TestData.getParticipant("nadine"), new ParticipationTime(4, 11));

  private Participant fritz = new Participant(TestData.getParticipant("fritz"),
      new ParticipationTime(4, 5), new ParticipationTime(9, 11));

  private SlackerParticipantListValidator slacker = new SlackerParticipantListValidator();

  @Test()
  public void detectsSchedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.MALE) //
        .addRoom(2, Gender.FEMALE);
    List<Participant> participants = Arrays.asList(hans, dieter, martha, nadine);
    assertThat(slacker.isSchedulable(spec, participants)).isTrue();
  }

  @Test
  public void detectsUnschedulableSpecifications() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(1, Gender.FEMALE) //
        .addRoom(1, Gender.MALE);
    List<Participant> participants = Arrays.asList(hans, dieter, fritz, martha, nadine);
    assertThat(slacker.isSchedulable(spec, participants)).isFalse();
  }

  @Test
  public void handlesPersonsWithMultipleParticipationTimesCorrectly() {
    RoomSpecification spec = new RoomSpecification(2) //
        .addRoom(2, Gender.FEMALE) //
        .addRoom(1, Gender.MALE);
    List<Participant> participants = Arrays.asList(hans, martha, dieter, nadine, fritz);
    assertThat(slacker.isSchedulable(spec, participants)).isTrue();
  }

}
