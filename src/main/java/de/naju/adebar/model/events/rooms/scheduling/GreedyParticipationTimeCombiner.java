package de.naju.adebar.model.events.rooms.scheduling;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.google.common.collect.Iterables;
import de.naju.adebar.util.Assert2;

/**
 * An inefficient {@link ParticipationTimeCombiner}
 * <p>
 * The Greedy combiner will basically create an array from 0 (the first night) to the last night any
 * of the given {@link ParticipationTime ParticipationTimes} uses. Within this array, all nights
 * occupied by some participation time will be marked and for each marked sequence an new
 * participation time will be created. This does consume a lot of memory and also involves several
 * iterations. Thus this algorithm is inefficient and pretty greedy.
 *
 * @author Rico Bergmann
 */
@Service
public class GreedyParticipationTimeCombiner implements ParticipationTimeCombiner {

  private static final int OCCUPATION_RATIO = 2;
  private static final int INITIAL_CURSOR_POS = -1;

  @Override
  public Iterable<ParticipationTime> combine(Iterable<ParticipationTime> rawTimes) {
    Assert.notNull(rawTimes, "Raw times may not be null");
    Assert2.noNullElements(rawTimes);
    if (Iterables.isEmpty(rawTimes)) {
      return new ArrayList<>();
    }

    boolean[] timeArr = createTimeArray(rawTimes);
    markOccupiedTimes(timeArr, rawTimes);
    return extractMergedParticipationTimes(timeArr);
  }

  /**
   * Constructs the array described by the {@link ParticipationTime ParticipationTimes}. It contains
   * as many elements as there are nights - i.e. starting from the first night (0) and reaching
   * until the maximum/latest night. The array will be initialized with {@code false} indicating
   * that none of the nights are being occupied by some {@link ParticipationTime} initially.
   *
   * @param times the participation times
   * @return the array
   */
  private boolean[] createTimeArray(Iterable<ParticipationTime> times) {
    int latestTime = 0;

    for (ParticipationTime it : times) {
      if (it.getLastNight() > latestTime) {
        latestTime = it.getLastNight();
      }
    }

    return new boolean[latestTime];
  }

  /**
   * Sets all the nights contained in the {@link ParticipationTime ParticipationTimes} to
   * {@code true} indicating that these nights are being occupied by some {@link ParticipationTime}.
   *
   * @param proj the projection of the participation times into an array. This will be filled with
   *        {@code true}
   * @param rawTimes the participation times to use
   */
  private void markOccupiedTimes(boolean[] proj, Iterable<ParticipationTime> rawTimes) {
    for (ParticipationTime it : rawTimes) {
      markParticipationTimeOccupied(proj, it);
    }
  }

  /**
   * Sets all the nights of the projection to {@code true} which are within the range of the
   * {@link ParticipationTime}.
   *
   * @param proj the projection of the participation times into an array. This will be filled with
   *        {@code true}
   * @param time the participation time
   */
  private void markParticipationTimeOccupied(boolean[] proj, ParticipationTime time) {
    for (int night = time.getFirstNight() - 1; night < time.getLastNight(); ++night) {
      markOccupied(proj, night);
    }
  }

  /**
   * Sets the given night in the projection to {@code true}.
   *
   * @param proj the projection of the participation times into an array. This will be filled with
   *        {@code true}
   * @param night the night to mark
   */
  private void markOccupied(boolean[] proj, int night) {
    proj[night] = true;
  }

  /**
   * Combines all the marked nights in the projection into participation times.
   *
   * @param proj the projection of the participation times into an array
   * @return the participation times needed to represent the projection. Their amount will be
   *         minimal.
   */
  private List<ParticipationTime> extractMergedParticipationTimes(boolean[] proj) {
    List<ParticipationTime> combination = new ArrayList<>(proj.length / OCCUPATION_RATIO);

    int idx = scrollToFirstOccupation(proj);
    while (idxIsValid(proj, idx)) {
      int endIdx = findEndOfOccupationIdx(proj, idx);
      combination.add(extractParticipationTime(idx, endIdx));
      idx = scrollToNextOccupation(proj, endIdx);
    }

    return combination;
  }

  /**
   * Provides the index of the first occupied night
   *
   * @param proj the projection to search
   * @return the index of the first occupied night
   */
  private int scrollToFirstOccupation(boolean[] proj) {
    return scrollToNextOccupation(proj, INITIAL_CURSOR_POS);
  }

  /**
   * Provides the index of the next occupied night, using another index as offset
   *
   * @param proj the projection to search
   * @param currentIdx the offset
   * @return the index of the next occupied night or {@code -1} if there is none
   */
  private int scrollToNextOccupation(boolean[] proj, int currentIdx) {
    assertValidIndex(proj, currentIdx);

    int nextOccupationIdx = currentIdx + 1;

    for (; nextOccupationIdx < proj.length; ++nextOccupationIdx) {
      if (proj[nextOccupationIdx]) {
        return nextOccupationIdx;
      }
    }

    return -1;
  }

  /**
   * Provides the index of the first night, which does not have an occupation directly afterwards.
   *
   * @param proj the projection to search
   * @param occupationStartIdx the offset to start the search on
   * @return the night with no occupation afterwards, or {@code -1} if there is no such night
   */
  private int findEndOfOccupationIdx(boolean[] proj, int occupationStartIdx) {
    assertValidIndex(proj, occupationStartIdx);

    int endIdx = occupationStartIdx;

    /*
     * Check for unoccupied fields which appear later
     */
    for (int i = endIdx; i < proj.length; ++i) {
      if (!proj[i]) {
        return i - 1;
      }
    }

    /*
     * If no unoccupied field was found any more, then the last occupation spans to the end of all
     * participation. In this case, the last night is our result
     */
    return proj.length - 1;
  }

  /**
   * Checks, whether an index references a valid element in a projection
   *
   * @param proj the projection to check
   * @param idx the index
   * @return whether the index is valid
   */
  private boolean idxIsValid(boolean[] proj, int idx) {
    return (0 <= idx) && (idx < proj.length);
  }

  /**
   * Converts a time span into a corresponding participation time
   *
   * @param from the first night
   * @param to the last night
   * @return the participation time
   */
  private ParticipationTime extractParticipationTime(int from, int to) {
    return new ParticipationTime(readjustIdxToNight(from), readjustIdxToNight(to));
  }

  /**
   * Transforms a night corresponding to a projection to a night as used by
   * {@link ParticipationTime}.
   * <p>
   * This is necessary as arrays are indexed up from 0 whereas a {@link ParticipationTime} is
   * numbered in a more human-friendly way - i.e. the first night in a projection (which is 0)
   * corresponds to 1 in a {@link ParticipationTime}.
   *
   * @param idx the index to adjust
   * @return the adjusted index
   */
  private int readjustIdxToNight(int idx) {
    return idx + 1;
  }

  /**
   * Throws an exception if an index is invalid.
   * 
   * @param proj the projection to check
   * @param idx the index to check
   * @throws IndexOutOfBoundsException if the index is invalid
   */
  private void assertValidIndex(boolean[] proj, int idx) {
    if (!idxIsValid(proj, idx) && idx != INITIAL_CURSOR_POS) {
      throw new IndexOutOfBoundsException(
          String.format("Index at pos %d for array of size %d", idx, proj.length));
    }
  }

}
