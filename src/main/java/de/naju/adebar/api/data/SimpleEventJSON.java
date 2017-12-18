package de.naju.adebar.api.data;

import java.time.LocalDateTime;
import de.naju.adebar.model.events.Event;

/**
 * JSON-object for representing events in a simplified form. In contrast to an {@link Event}, this
 * class only provides access to the most important data: id, name, time and place. Instances are
 * immutable
 *
 * @author Rico Bergmann
 */
public class SimpleEventJSON {

  private String id;
  private String name;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private String place;

  /**
   * Objects will be created in terms of an existing {@link Event} instance
   *
   * @param event the event to simplify
   */
  public SimpleEventJSON(Event event) {
    this.id = event.getId().toString();
    this.name = event.getName();
    this.startDate = event.getStartTime();
    this.endDate = event.getEndTime();
    this.place = event.getPlace().getCity();
  }

  /**
   * @return the event's id
   */
  public String getId() {
    return id;
  }

  /**
   * @return the event's name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the time the event starts
   */
  public LocalDateTime getStartDate() {
    return startDate;
  }

  /**
   * @return the time the event ends at
   */
  public LocalDateTime getEndDate() {
    return endDate;
  }

  /**
   * @return the place (i. e. city) the event takes place
   */
  public String getPlace() {
    return place;
  }
}
