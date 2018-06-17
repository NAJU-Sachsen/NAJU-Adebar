package de.naju.adebar.web.controller.api;

import de.naju.adebar.api.data.SimpleEventJSON;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.model.events.EventRepository;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to access event data
 *
 * @author Rico Bergmann
 * @see <a href= "https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_eventController")
@RequestMapping("/api/events")
public class EventController {

  private final EventRepository eventRepo;
  private final LocalGroupManager groupManager;

  @Autowired
  public EventController(EventRepository eventRepo, LocalGroupManager groupManager) {
    Object[] params = {eventRepo, groupManager};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
    this.eventRepo = eventRepo;
    this.groupManager = groupManager;
  }

  /**
   * Provides all events of a local group
   *
   * @param groupId the id of the local group
   * @return all events that are hosted by that group
   */
  @RequestMapping("/localGroup")
  @Deprecated
  public Iterable<SimpleEventJSON> sendEventsForLocalGroup(@RequestParam("groupId") long groupId) {
    return eventRepo
        .findByLocalGroup(
            groupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new))
        .stream().map(SimpleEventJSON::new).collect(Collectors.toList());
  }

  /**
   * Response code to indicate that an event does not have enough capacity for new participants or
   * reservations of a certain size.
   *
   * @author Rico Bergmann
   */
  @Deprecated
  private static class OverbookedResponse extends JsonResponse {

    /**
     * Default String to indicate an overbooked event
     */
    public static final String RETURN_OVERBOOKED = "overbooked";

    private int slotsAvailable;

    public static OverbookedResponse withRemainingSlots(int slotsAvailable) {
      return new OverbookedResponse(slotsAvailable);
    }

    /**
     * Constructor to specify the number of available slots
     *
     * @param slotsAvailable the unused capacity
     */
    private OverbookedResponse(int slotsAvailable) {
      super(RETURN_OVERBOOKED);
      this.slotsAvailable = slotsAvailable;
    }

    /**
     * @return the unused capacity
     */
    public int getSlotsAvailable() {
      return slotsAvailable;
    }

    /**
     * @param slotsAvailable the unused capacity
     */
    public void setSlotsAvailable(int slotsAvailable) {
      this.slotsAvailable = slotsAvailable;
    }
  }

}
