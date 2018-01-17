package de.naju.adebar.model.events.rooms.scheduling;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.Test;

public class ParticipationTimeUnitTests {

  private ParticipationTime pivot = new ParticipationTime(3, 5);
  private ParticipationTime startsBefore = new ParticipationTime(1, 4);
  private ParticipationTime startsAfter = new ParticipationTime(4, 5);
  private ParticipationTime endsBefore = new ParticipationTime(3, 4);
  private ParticipationTime endsAfter = new ParticipationTime(3, 6);
  private ParticipationTime startsEqual = new ParticipationTime(3, 6);
  private ParticipationTime endsEqual = new ParticipationTime(1, 5);
  private ParticipationTime completelyBefore = new ParticipationTime(1, 2);
  private ParticipationTime completelyAfter = new ParticipationTime(6, 7);

  @Test
  public void rejectsNegativeNights() {
    assertThatExceptionOfType(IllegalArgumentException.class) //
        .isThrownBy(() -> new ParticipationTime(-1, 5));

    assertThatExceptionOfType(IllegalArgumentException.class) //
        .isThrownBy(() -> new ParticipationTime(3, -4));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsLastNightBeforeFirstNight() {
    new ParticipationTime(5, 3);
  }

  @Test
  public void doesNotRejectEphemeralStays() {
    new ParticipationTime(3, 3);
  }

  @Test
  public void detectsSoonerStartingTimesCorrectly() {
    assertThat(pivot.startsSoonerThan(startsAfter)) //
        .describedAs("The pivot starts sooner!") //
        .isTrue();

    assertThat(pivot.startsSoonerThan(startsBefore)) //
        .describedAs("The pivot starts later!") //
        .isFalse();

    assertThat(pivot.startsSoonerThan(startsEqual)) //
        .describedAs("The pivot does not start sooner!") //
        .isFalse();
  }

  @Test
  public void detectsLaterStartingTimesCorrectly() {
    assertThat(pivot.startsLaterThan(startsBefore)) //
        .describedAs("The pivot starts later!") //
        .isTrue();

    assertThat(pivot.startsLaterThan(startsAfter)) //
        .describedAs("The pivot starts sooner!") //
        .isFalse();

    assertThat(pivot.startsLaterThan(startsEqual)) //
        .describedAs("The pivot does not start later!") //
        .isFalse();
  }

  @Test
  public void detectsSoonerEndingTimesCorrectly() {
    assertThat(pivot.endsSoonerThan(endsAfter)) //
        .describedAs("The pivot ends sooner!") //
        .isTrue();

    assertThat(pivot.endsSoonerThan(endsBefore)) //
        .describedAs("The pivot ends later!") //
        .isFalse();

    assertThat(pivot.endsSoonerThan(endsEqual)) //
        .describedAs("The pivot does not end sooner!") //
        .isFalse();
  }

  @Test
  public void detectsLaterEndingTimesCorrectly() {
    assertThat(pivot.endsLaterThan(endsBefore)) //
        .describedAs("The pivot ends later!") //
        .isTrue();

    assertThat(pivot.endsLaterThan(endsAfter)) //
        .describedAs("The pivot ends sooner!") //
        .isFalse();

    assertThat(pivot.endsLaterThan(endsEqual)) //
        .describedAs("The pivot does not end later!") //
        .isFalse();
  }

  @Test
  public void detectsOverlappingTimesCorrectly() {
    assertThat(pivot.overlapsWith(startsAfter)) //
        .describedAs("The other time starts within the given time frame") //
        .isTrue();

    assertThat(pivot.overlapsWith(endsBefore)) //
        .describedAs("The other time ends within the given time frame") //
        .isTrue();
  }

  @Test
  public void detectsNonOverlappingTimesCorrectly() {
    assertThat(pivot.overlapsWith(completelyAfter)) //
        .describedAs("The other time starts after the pivot has ended!") //
        .isFalse();

    assertThat(pivot.overlapsWith(completelyBefore)) //
        .describedAs("The other time ends before the pivot has started") //
        .isFalse();
  }

}
