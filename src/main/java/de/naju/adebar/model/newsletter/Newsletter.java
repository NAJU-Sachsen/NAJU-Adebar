package de.naju.adebar.model.newsletter;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * Abstraction of a newsletter. It mainly consists of a name and a list of subscribers.
 * 
 * @author Rico Bergmann
 *
 */
@Entity(name = "newsletter")
public class Newsletter {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private long id;

  /**
   * The name of the newsletter. It does not have to be unique among all newsletters but should
   * describe what the newsletter is about. Therefore different local chapters may have different
   * newsletters with similar names, e.g. one that deals with general announcements.
   */
  @Column(name = "name")
  private String name;

  /**
   * 
   */
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(inverseJoinColumns = @JoinColumn(name = "subscriberId"))
  private List<Subscriber> subscribers;

  // constructors

  /**
   * Default constructor for JPA's sake Not to be used by anything else.
   */
  protected Newsletter() {

  }

  /**
   * @param name the newsletter's name
   * @throws IllegalArgumentException if the name was {@code null} or empty
   */
  public Newsletter(String name) {
    Assert.hasText(name, "Must specify a name for the newsletter, but was: " + name);
    this.name = name;
    this.subscribers = new ArrayList<>(15);
  }

  // basic getter

  /**
   * @return the newsletter's id
   */
  public long getId() {
    return id;
  }

  /**
   * @return the newsletter's name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the newsletter's subscribers
   */
  public Iterable<Subscriber> getSubscribers() {
    return subscribers;
  }

  // basic setter

  /**
   * @param name the newsletter's new name
   * @throws IllegalArgumentException when the {@code name} is empty or {@code null}
   */
  public void setName(String name) {
    Assert.hasText(name, "A newsletters name may not be empty, but was: " + name);
    this.name = name;
  }

  /**
   * Updates the newsletter's id.
   * <p>
   * As the id will be used as primary key in the database, it should not be changed by the user by
   * any means. Only JPA should access this method, which is why {@code setId()} was made
   * {@code protected}.
   * </p>
   * 
   * @param id the newsletter's new id
   */
  protected void setId(long id) {
    this.id = id;
  }

  /**
   * @param subscribers the newsletter's subscribers
   * @throws IllegalArgumentException if {@code subscribers} is {@code null}
   */
  protected void setSubscribers(List<Subscriber> subscribers) {
    Assert.notNull(subscribers, "Subscribers may not be null!");
    this.subscribers = subscribers;
  }

  // checker

  /**
   * @param subscriber the subscriber to check
   * @return {@code true} if the subscriber subscribed to the newsletter
   */
  public boolean hasSubscriber(Subscriber subscriber) {
    return subscribers.contains(subscriber);
  }

  // "advanced" getter

  @Transient
  public int getSubscribersCount() {
    return subscribers.size();
  }

  // modification methods

  /**
   * Adds a new subscriber to the newsletter. He will be able to receive emails after that.
   * 
   * @param subscriber the new subscriber
   * @throws IllegalArgumentException if the {@code subscriber} is {@code null}
   * @throws AlreadySubscribedException if the {@code subscriber} is already a subscriber
   */
  public void addSubscriber(Subscriber subscriber) {
    Assert.notNull(subscriber, "New subscriber may not be null!");
    if (subscribers.contains(subscriber)) {
      throw new AlreadySubscribedException(
          String.format("%s did already subscribe to %s", subscriber.toString(), this.toString()));
    }
    subscribers.add(subscriber);
  }

  /**
   * Removes a subscriber from the newsletter. He will not receive any emails from the newsletter
   * after that.
   * 
   * @param subscriber the subscriber to remove
   * @throws IllegalArgumentException if the {@code subscriber} is {@code null}
   * @throws NoSuchSubscriberException if the {@code subscriber} is not subscribed
   */
  public void removeSubscriber(Subscriber subscriber) {
    Assert.notNull(subscriber, "Subscriber to remove may not be null!");
    if (!subscribers.contains(subscriber)) {
      throw new IllegalArgumentException(
          String.format("%s did not subscribe to %s", subscriber.toString(), this.toString()));
    }
    subscribers.remove(subscriber);
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Newsletter that = (Newsletter) o;

    // if both ID's are set, only compare them
    if (that.id != 0 && this.id != 0) {
      return that.id == this.id;
    }

    // at least one newsletter was not persisted yet => compare attributes
    if (!name.equals(that.name))
      return false;
    return subscribers.equals(that.subscribers);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + subscribers.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("Newsletter [id=%d, name=%s, subscribers=%d]", id, name,
        subscribers.size());
  }
}
