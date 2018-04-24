package de.naju.adebar.model.events.rooms.scheduling.slacker;

import org.springframework.util.Assert;
import de.naju.adebar.model.events.rooms.scheduling.Participant;
import de.naju.adebar.model.events.rooms.scheduling.ParticipantListValidator;
import de.naju.adebar.model.events.rooms.scheduling.RegisteredParticipants;
import de.naju.adebar.model.events.rooms.scheduling.RoomSpecification;

/**
 * A pretty lazy {@link ParticipantListValidator}
 * <p>
 * The Slacker scheduler will basically life up to his name: it will only perform as few work as
 * possible. Therefore even though some kind of schedule may exist according to the slacker, it may
 * actually not be of much use in practical scenarios - resulting in an usually terrible assessment.
 * </p>
 * <h2>Here is how it works:</h2>
 * <p>
 * For each day the slacker will compare the number of participants {@code P} of a specific gender
 * to the number of beds available for that gender. As soon as {@code P} exceeds this number, the
 * scheduler will return "unschedulable".
 * </p>
 * <h2>Why this approach may be bad in some scenarios:</h2>
 * <p>
 * As already stated above, the slacker may find a theoretically existing schedule which is of no
 * practical use. For example if a participant does not attend an event some time in between,
 * someone else may use his bed in the meantime. This new participant could then be forced to switch
 * between different beds multiple times. Usually this is not acceptable for a real event and a bed
 * will simply be left empty for some time.
 * <p>
 * This also causes this scheduler to be the most accommodating: other schedulers may consider some
 * participant lists to be unschedulable even if the slacker states otherwise. However the opposite
 * will never be true - if a more rigorous scheduler says that a participant list is schedulable the
 * slacker will always confirm this result.
 *
 * @author Rico Bergmann
 */
public class SlackerParticipantListValidator implements ParticipantListValidator {

  private SlackerParticipantListValidatorImpl slackerImpl;

  @Override
  public boolean isSchedulable(RoomSpecification rooms, RegisteredParticipants participants) {
    updateSlackerImplementationIfNecessary(rooms, participants);
    return slackerImpl.isSchedulable();
  }

  @Override
  public int assessScheduleReliablity() {
    Assert.notNull(slackerImpl, "No schedule has been provided");
    return slackerImpl.assessScheduleReliablity();
  }

  /**
   * Creates a new {@link SlackerParticipantListValidatorImpl} instance if the new scheduling data
   * differs from the current one.
   *
   * @param rooms the accommodation details
   * @param participants the participants
   */
  private void updateSlackerImplementationIfNecessary(RoomSpecification rooms,
      Iterable<Participant> participants) {
    SlackerParticipantListValidatorImpl updatedSlackerImpl =
        new SlackerParticipantListValidatorImpl(rooms, participants);

    if (!updatedSlackerImpl.equals(slackerImpl)) {
      this.slackerImpl = updatedSlackerImpl;
    }
  }

}
