package de.naju.adebar.controller.api;

import de.naju.adebar.api.data.SimpleEventJSON;
import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.events.EventManager;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.events.BookedOutException;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.Reservation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to access event data
 *
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Representational_State_Transfer">REST Services</a>
 */
@RestController("api_eventController")
@RequestMapping("/api/events")
public class EventController {

  private EventManager eventManager;
  private LocalGroupManager groupManager;

  @Autowired
  public EventController(EventManager eventManager, LocalGroupManager groupManager) {
    Object[] params = {eventManager, groupManager};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
    this.eventManager = eventManager;
    this.groupManager = groupManager;
  }

  /**
   * Provides all events of a local group
   *
   * @param groupId the id of the local group
   * @return all events that are hosted by that group
   */
  @RequestMapping("/localGroup")
  public Iterable<SimpleEventJSON> sendEventsForLocalGroup(@RequestParam("groupId") long groupId) {
    LocalGroup localGroup =
        groupManager.findLocalGroup(groupId).orElseThrow(IllegalArgumentException::new);
    List<SimpleEventJSON> events = new LinkedList<>();
    for (Event event : localGroup.getEvents()) {
      events.add(new SimpleEventJSON(event));
    }
    return events;
  }

  /**
   * Creates a new reservation for an event
   *
   * @param eventId the event to add the reservation to
   * @param description the description of the reservation. Must be present
   * @param slots the number of slots to reserve. Must be present
   * @param email an email address to contact for the reservation. Optional
   * @return a response code
   */
  @RequestMapping("/addReservation")
  public JsonResponse addReservation(@RequestParam("event") String eventId,
      @RequestParam(name = "description", required = true) String description,
      @RequestParam(name = "slots", required = true) int slots,
      @RequestParam(name = "email", defaultValue = "") String email) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    try {
      event.addReservationFor(description, slots, email);
      eventManager.updateEvent(eventId, event);
    } catch (BookedOutException e) {
      return new OverbookedResponse(event.getRemainingCapacity());
    } catch (IllegalArgumentException e) {
      return new ErrorResponse(e.getMessage());
    }

    return new JsonResponse(JsonResponse.RETURN_OK);
  }

  /**
   * Updates a reservation
   *
   * @param eventId the event to which the reservation belongs
   * @param id the ID (= current description) of the reservation
   * @param description the new description
   * @param slots the number of slots to reserve
   * @param email email an email address to contact for issues concerning the reservation
   * @return a response code
   */
  @RequestMapping("/editReservation")
  public JsonResponse editReservation(@RequestParam("event") String eventId,
      @RequestParam("description") String id, @RequestParam("description") String description,
      @RequestParam("slots") int slots, @RequestParam("email") String email) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    try {
      event.updateReservation(id, new Reservation(description, slots, email));
      eventManager.updateEvent(eventId, event);
    } catch (BookedOutException e) {
      return new OverbookedResponse(event.getRemainingCapacity());
    } catch (IllegalArgumentException e) {
      return new ErrorResponse(e.getMessage());
    }

    return new JsonResponse(JsonResponse.RETURN_OK);
  }

  /**
   * Deletes a reservation from an event
   *
   * @param eventId the event to which the reservation belongs
   * @param description the description (= ID) of the reservation
   * @return a response code
   */
  @RequestMapping("/removeReservation")
  public JsonResponse removeReservation(@RequestParam("event") String eventId,
      @RequestParam("id") String description) {
    Event event = eventManager.findEvent(eventId).orElseThrow(IllegalArgumentException::new);

    try {
      event.removeReservation(description);
      eventManager.updateEvent(eventId, event);
    } catch (IllegalArgumentException e) {
      return new ErrorResponse(e.getMessage());
    }

    return new JsonResponse(JsonResponse.RETURN_OK);
  }

  /**
   * Response code to indicate that an event does not have enough capacity for new participants or
   * reservations of a certain size.
   *
   * @author Rico Bergmann
   */
  private class OverbookedResponse extends JsonResponse {

    /**
     * Default String to indicate an overbooked event
     */
    public final static String RETURN_OVERBOOKED = "overbooked";

    private int slotsAvailable;

    /**
     * Constructor to specify the number of available slots
     *
     * @param slotsAvailable the unused capacity
     */
    public OverbookedResponse(int slotsAvailable) {
      super(RETURN_OVERBOOKED);
      this.slotsAvailable = slotsAvailable;
    }

    /**
     * @return the unused capacity
     */
    @SuppressWarnings("unused")
    public int getSlotsAvailable() {
      return slotsAvailable;
    }

    /**
     * @param slotsAvailable the unused capacity
     */
    @SuppressWarnings("unused")
    public void setSlotsAvailable(int slotsAvailable) {
      this.slotsAvailable = slotsAvailable;
    }
  }

}
