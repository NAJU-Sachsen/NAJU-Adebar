package adebar.controller.api;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import adebar.api.data.SubscriberJSON;
import adebar.model.newsletter.Newsletter;
import adebar.model.newsletter.NewsletterRepository;
import adebar.model.newsletter.SubscriberRepository;

@RestController("api_subscriberController")
@RequestMapping("/api")
public class SubscriberController {
	
	private NewsletterRepository newsletterRepo;
	private SubscriberRepository subscriberRepo;
	
	@Autowired
	public SubscriberController(NewsletterRepository newsletterRepo, SubscriberRepository subscriberRepo) {
		this.newsletterRepo = newsletterRepo;
		this.subscriberRepo = subscriberRepo;
	}
	
	@RequestMapping("/subscriberDetails")
	public SubscriberJSON sendSubscriberDetails(@RequestParam("email") String email) {
		LinkedList<Long> subscribedNewsletters = new LinkedList<>();
		for (Newsletter newsletter : newsletterRepo.findBySubscribersEmail(email)) {
			subscribedNewsletters.add(newsletter.getId());
		}
		return new SubscriberJSON(subscriberRepo.findOne(email), subscribedNewsletters);
	}
	
}
