package adebar.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import adebar.model.newsletter.Newsletter;
import adebar.model.newsletter.NewsletterManager;
import adebar.model.newsletter.NewsletterRepository;
import adebar.model.newsletter.Subscriber;
import adebar.model.newsletter.SubscriberRepository;
import adebar.util.Iteration;

@Controller
public class SubscriberController {
	private NewsletterRepository newsletterRepo;
	private SubscriberRepository subscriberRepo;
	private NewsletterManager newsletterManager;
	
	@Autowired
	public SubscriberController(NewsletterRepository newsletterRepo,
			SubscriberRepository newsletterSubscriberRepo, NewsletterManager newsletterManager) {
		this.newsletterRepo = newsletterRepo;
		this.subscriberRepo = newsletterSubscriberRepo;
		this.newsletterManager = newsletterManager;
	}
	
	@RequestMapping("/newsletters/subscribers")
	public String showNewsletterSubscribers(Model model) {
		model.addAttribute("subscribers", subscriberRepo.findAll());
		return "subscribers";
	}
	
	@RequestMapping("/newsletters/subscribers/{sid}")
	public String subscriberDetails(@PathVariable("sid") String subscriberEmail, Model model) {
		Subscriber subscriber = subscriberRepo.findOne(subscriberEmail);
		model.addAttribute("subscriber", subscriber);
		model.addAttribute("newsletters", newsletterRepo.findBySubscribersEmail(subscriber.getEmail()));
		return "subscriberDetails";
	}
	
	@RequestMapping(value="/newsletters/{nid}/subscribe", method=RequestMethod.POST)
	public String subscribe(@PathVariable("nid") Long newsletterId, @ModelAttribute("newSubscriber") Subscriber subscriber, RedirectAttributes redirAttr) {
		Newsletter newsletter = newsletterRepo.findOne(newsletterId);	
		Subscriber existingSubscriber = subscriberRepo.findOne(subscriber.getEmail());
		Subscriber subscriberToAdd;
		
		if (existingSubscriber == null || !existingSubscriber.hasName()) {
			subscriberRepo.save(subscriber);
			subscriberToAdd = subscriber;
		} else if (!subscriber.hasName()){
			subscriberToAdd = existingSubscriber;
		} else {
			redirAttr.addFlashAttribute("emailExists", true);
			redirAttr.addFlashAttribute("existingSubscriber", existingSubscriber);
			return "redirect:/newsletters/" + newsletterId;
		}
		
		try {
			newsletter.addSubscriber(subscriberToAdd);
			newsletterRepo.save(newsletter);
			redirAttr.addFlashAttribute("subscribed", true);
		} catch (IllegalArgumentException e) {
			redirAttr.addFlashAttribute("alreadySubscribed", true);
		}
		
		return "redirect:/newsletters/" + newsletterId;
	}
	
	@RequestMapping(value="/newsletters/{nid}/unsubscribe", method=RequestMethod.POST)
	public String unsubscribe(@PathVariable("nid") Long newsletterId, @RequestParam("email") String email, RedirectAttributes redirAttr) {
		Newsletter newsletter = newsletterRepo.findOne(newsletterId);
		newsletter.removeSubscriber(subscriberRepo.findOne(email));
		newsletterRepo.save(newsletter);
		
		if (Iteration.countElements(newsletterRepo.findBySubscribersEmail(email)) == 0) {
			subscriberRepo.delete(email);
		}
		
		redirAttr.addFlashAttribute("subscriberRemoved", true);
		return "redirect:/newsletters/" + newsletterId;
	}
	
	@RequestMapping(value="/newsletters/subscribers/add", method=RequestMethod.POST)
	public String addSubscriber(@RequestParam("firstName") Optional<String> firstName,
			@RequestParam("lastName") Optional<String> lastName, @RequestParam("email") String email,
			@RequestParam("newsletters") List<Long> newsletters, RedirectAttributes redirAttr) {
		redirAttr.addFlashAttribute("tab", "subscribers");
		
		if (subscriberRepo.exists(email)) {
			redirAttr.addFlashAttribute("emailExists", true);
			return "redirect:/newsletters";
		}
		Subscriber subscriber = new Subscriber(email);
		firstName.ifPresent(name -> subscriber.setFirstName(name));
		lastName.ifPresent(name -> subscriber.setLastName(name));
		subscriberRepo.save(subscriber);
		
		Newsletter newsletter;
		for (Long id : newsletters) {
			newsletter = newsletterRepo.findOne(id);
			newsletter.addSubscriber(subscriber);
			newsletterRepo.save(newsletter);
		}
		
		redirAttr.addFlashAttribute("subscriberCreated", true);
		return "redirect:/newsletters";
	}
	
	@RequestMapping(value="/newsletters/subscribers/update", method=RequestMethod.POST)
	public String updateSubscriber(@RequestParam("id") String id,
			@RequestParam("edit-firstName") Optional<String> firstName,
			@RequestParam("edit-lastName") Optional<String> lastName, @RequestParam("edit-email") String email,
			@RequestParam("edit-subscribed") List<Long> newsletterIds, RedirectAttributes redirAttr) {
		
		if (!id.equals(email)) {
			subscriberRepo.delete(id);
		}
		
		Iterable<Newsletter> newsletters = newsletterRepo.findAll(newsletterIds);
		Subscriber subscriber = new Subscriber(email);
		firstName.ifPresent(name -> subscriber.setFirstName(name));
		lastName.ifPresent(name -> subscriber.setLastName(name));
		
		newsletterManager.updateSubscriptions(subscriber, newsletters);
		subscriberRepo.save(subscriber);
		newsletterRepo.save(newsletters);
		
		redirAttr.addFlashAttribute("tab", "subscribers");
		redirAttr.addFlashAttribute("subscriberUpdated", true);
		return "redirect:/newsletters";
	}
	
	@RequestMapping(value="/newsletters/subscribers/delete", method=RequestMethod.POST)
	public String deleteSubscriber(@RequestParam("id") String id, RedirectAttributes redirAttr) {
		Subscriber subscriber = subscriberRepo.findOne(id);
		Iterable<Newsletter> subscribedNewsletters = newsletterRepo.findBySubscribersContains(subscriber);
		
		subscribedNewsletters.forEach(n -> n.removeSubscriber(subscriber));
		newsletterRepo.save(subscribedNewsletters);
		subscriberRepo.delete(subscriber);
		
		redirAttr.addFlashAttribute("tab", "subscribers");
		redirAttr.addFlashAttribute("subscriberDeleted", true);
		return "redirect:/newsletters";
	}
}

