package adebar.model.newsletter;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.util.Assert;

/**
 * Abstraction of a newsletter. It mainly consists of a name and a list of subscribers.
 * 
 * @author Rico Bergmann
 *
 */
@Entity
public class Newsletter {
	
	@Id @GeneratedValue private long id;
	
	/**
	 * The name of the newsletter. It does not have to be unique among all newsletters but should describe what the
	 * newsletter is about. Therefore different local chapters may have different newsletters with similar names, e.g.
	 * one that deals with general announcements.
	 */
	private String name;
	
	/**
	 * 
	 */
	@ManyToMany(cascade=CascadeType.ALL) private List<Subscriber> subscribers;
	
	// constructors
	
	/**
	 * Default constructor for JPA's sake
	 * Not to be used by anything else.
	 */
	protected Newsletter() {
		
	}
	
	/**
	 * @param name the newsletter's name
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
	 * @return 
	 */
	public Iterable<Subscriber> getSubscribers() {
		return subscribers;
	}
	
	// basic setter
	
	/**
	 * @param name the newsletter's new name
	 * @throws IllegalArgumentException when the {@literal name} is empty or {@code null}
	 */
	public void setName(String name) {
		Assert.hasText(name, "A newsletters name may not be empty, but was: " + name);
		this.name = name;
	}
	
	/**
	 * Updates the newsletter's id.
	 * <p>
	 * As the id will be used as primary key in the database, it should not be changed by the user by any means.
	 * Only JPA should access this method, which is why {@code setId()} was made {@code protected}.
	 * </p>
	 * 
	 * @param id the newsletter's new id
	 */
	protected void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @param subscribers the newsletter's subscribers
	 * @throws IllegalArgumentException if {@literal subscribers} is {@code null}
	 */
	protected void setSubscribers(List<Subscriber> subscribers) {
		Assert.notNull(subscribers, "Subscribers may not be null!");
		this.subscribers = subscribers;
	}
	
	// checker
	
	public boolean hasSubscriber(Subscriber subscriber) {
		return subscribers.contains(subscriber);
	}
	
	// "advanced" getter
	
	public int getSubscribersCount() {
		return subscribers.size();
	}
	
	// modification methods
	
	/**
	 * Adds a new subscriber to the newsletter. He will be able to receive emails after that.
	 * @param subscriber the new subscriber
	 * @throws IllegalArgumentException if the {@literal subscriber} is {@code null} or already a subscriber
	 */
	public void addSubscriber(Subscriber subscriber) {
		Assert.notNull(subscriber, "New subscriber may not be null!");
		if (subscribers.contains(subscriber)) {
			throw new IllegalArgumentException(
					String.format("%s did already subscribe to %s", subscriber.toString(), this.toString()));
		}
		subscribers.add(subscriber);	
	}
	
	
	/**
	 * Removes a subscriber from the newsletter. He will not receive any emails from the newsletter after that.
	 * @param subscriber the subscriber to remove
	 * @throws IllegalArgumentException if the {@literal subscriber} is {@code null} or did not subscribe
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
	
	/**
	 * 
	 */
	@Override public String toString() {
		return String.format("Newsletter %d: %s (%d subscribers)", id, name, subscribers.size());
	}
}
