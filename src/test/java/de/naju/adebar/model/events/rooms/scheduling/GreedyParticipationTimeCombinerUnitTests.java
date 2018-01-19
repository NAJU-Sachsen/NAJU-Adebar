package de.naju.adebar.model.events.rooms.scheduling;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class GreedyParticipationTimeCombinerUnitTests {

  private GreedyParticipationTimeCombiner combiner;

  @Before
  public void setUp() {
    combiner = new GreedyParticipationTimeCombiner();
  }

  @Test(expected = IllegalArgumentException.class)
  public void nullParticipationTimesResultInException() {
    combiner.combine(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void participationTimesWithNullElementResultInException() {
    List<ParticipationTime> times =
        Arrays.asList(new ParticipationTime(1, 3), null, new ParticipationTime(7, 10));
    combiner.combine(times);
  }

  @Test
  public void emptyParticipationTimesResultInEmptyList() {
    List<ParticipationTime> times = new ArrayList<>(0);
    assertThat(combiner.combine(times)).isEmpty();
  }

  @Test
  public void nonOverlappingTimesDoNotGetCombined() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(1, 3),
        new ParticipationTime(5, 7), new ParticipationTime(9, 11));
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(rawTimes.toArray(new ParticipationTime[0]));
  }

  @Test
  public void combinesSubsequentTimes() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(2, 3),
        new ParticipationTime(4, 5), new ParticipationTime(7, 10));

    ParticipationTime[] expexted = {new ParticipationTime(2, 5), new ParticipationTime(7, 10)};
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(expexted);
  }

  @Test
  public void combinesPartlyOverlappingTimes() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(1, 3),
        new ParticipationTime(2, 5), new ParticipationTime(7, 10));

    ParticipationTime[] expexted = {new ParticipationTime(1, 5), new ParticipationTime(7, 10)};
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(expexted);
  }

  @Test
  public void combinesCompletelyOverlappingTimes() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(1, 3),
        new ParticipationTime(1, 3), new ParticipationTime(7, 10));

    ParticipationTime[] expexted = {new ParticipationTime(1, 3), new ParticipationTime(7, 10)};
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(expexted);
  }

  @Test
  public void combinesTimesIndependentOfOrder() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(1, 3),
        new ParticipationTime(2, 5), new ParticipationTime(10, 13), new ParticipationTime(15, 17),
        new ParticipationTime(5, 8));

    ParticipationTime[] expexted =
        {new ParticipationTime(1, 8), new ParticipationTime(10, 13), new ParticipationTime(15, 17)};
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(expexted);
  }

  @Test
  public void performsMultipleCombinations() {
    List<ParticipationTime> rawTimes = Arrays.asList(new ParticipationTime(1, 3),
        new ParticipationTime(4, 7), new ParticipationTime(9, 11), new ParticipationTime(12, 15));

    ParticipationTime[] expexted = {new ParticipationTime(1, 7), new ParticipationTime(9, 15)};
    Iterable<ParticipationTime> combinedTimes = combiner.combine(rawTimes);
    assertThat(combinedTimes).containsExactly(expexted);
  }

}
