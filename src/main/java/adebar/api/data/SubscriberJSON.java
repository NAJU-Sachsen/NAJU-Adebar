package adebar.api.data;

import adebar.model.newsletter.Subscriber;

public class SubscriberJSON {
	
	private Subscriber subscriber;
	private Iterable<Long> subscribedNewsletters;
	
	public SubscriberJSON(Subscriber subscriber, Iterable<Long> subscribedNewsletters) {
		this.subscriber = subscriber;
		this.subscribedNewsletters = subscribedNewsletters;
	}

	/**
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @return the subscribedNewsletters
	 */
	public Iterable<Long> getSubscribedNewsletters() {
		return subscribedNewsletters;
	}
	
	
}
