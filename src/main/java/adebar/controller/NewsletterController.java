package adebar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import adebar.model.newsletter.Newsletter;
import adebar.model.newsletter.NewsletterDataProcessor;
import adebar.model.newsletter.NewsletterRepository;
import adebar.model.newsletter.Subscriber;
import adebar.model.newsletter.SubscriberRepository;

@Controller
public class NewsletterController {
	private NewsletterRepository newsletterRepo;
	private SubscriberRepository subscriberRepo;
	private NewsletterDataProcessor dataProcessor;

	@Autowired
	public NewsletterController(NewsletterRepository newsletterRepo, SubscriberRepository subscriberRepo, NewsletterDataProcessor dataProcessor) {
		this.newsletterRepo = newsletterRepo;
		this.subscriberRepo = subscriberRepo;
		this.dataProcessor = dataProcessor;
	}

	@RequestMapping("/newsletters")
	public String showNewsletters(Model model) {
		model.addAttribute("newsletters", newsletterRepo.findAll());
		model.addAttribute("subscribers", subscriberRepo.findAll());
		return "newsletters";
	}

	@RequestMapping("/newsletters/add")
	public String addNewsletter(@RequestParam("name") String name, RedirectAttributes redirAttr) {
		Newsletter newsletter = new Newsletter(name);
		newsletterRepo.save(newsletter);
		redirAttr.addFlashAttribute("newsletterAdded", true);
		return "redirect:/newsletters";
	}

	@RequestMapping("/newsletters/{nid}")
	public String newsletterDetails(@PathVariable("nid") Long newsletterId, Model model) {
		Newsletter newsletter = newsletterRepo.findOne(newsletterId);
		model.addAttribute("newsletter", newsletter);
		model.addAttribute("recipients", dataProcessor.getSubscriberEmails(newsletter));
		model.addAttribute("sender", dataProcessor.getNewsletterEmail());
		model.addAttribute("newSubscriber", new Subscriber());
		return "newsletterDetails";
	}


	@RequestMapping("/newsletters/{nid}/delete")
	public String deleteNewsletter(@PathVariable("nid") Long newsletterId, RedirectAttributes redirAttr) {
		newsletterRepo.delete(newsletterId);
		redirAttr.addFlashAttribute("newsletterDeleted", true);
		return "redirect:/newsletters";
	}


}
