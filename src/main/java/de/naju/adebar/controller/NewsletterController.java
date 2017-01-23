package de.naju.adebar.controller;

import de.naju.adebar.app.newsletter.NewsletterDataProcessor;
import de.naju.adebar.model.newsletter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Newsletter related controller mappings
 * @author Rico Bergmann
 * @see Newsletter
 */
@Controller
public class NewsletterController {
    private NewsletterRepository newsletterRepo;
    private SubscriberRepository subscriberRepo;
    private NewsletterManager newsletterManager;
    private NewsletterDataProcessor dataProcessor;

    @Autowired
    public NewsletterController(NewsletterRepository newsletterRepo, SubscriberRepository subscriberRepo, NewsletterManager newsletterManager, NewsletterDataProcessor dataProcessor) {
        this.newsletterRepo = newsletterRepo;
        this.subscriberRepo = subscriberRepo;
        this.newsletterManager = newsletterManager;
        this.dataProcessor = dataProcessor;
    }

    /**
     * Displays the newsletter overview
     * @param model model to display the data in
     * @return the newsletters' overview view
     */
    @RequestMapping("/newsletters")
    public String showNewsletters(Model model) {
        model.addAttribute("newsletters", newsletterRepo.findAll());
        model.addAttribute("subscribers", subscriberRepo.findFirst10ByOrderByEmail());
        return "newsletters";
    }

    /**
     * Creates a new newsletter
     * @param name the newsletter's name
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter overview view
     */
    @RequestMapping("/newsletters/add")
    public String addNewsletter(@RequestParam("name") String name, RedirectAttributes redirAttr) {
        newsletterManager.createNewsletter(name);
        redirAttr.addFlashAttribute("newsletterAdded", true);
        return "redirect:/newsletters";
    }

    /**
     * Displays detailed information for a specific newsletter
     * @param newsletterId the newsletter's id
     * @param model model to display the data in
     * @return the newsletter detail view
     */
    @RequestMapping("/newsletters/{nid}")
    public String newsletterDetails(@PathVariable("nid") Long newsletterId, Model model) {
        Newsletter newsletter = newsletterRepo.findOne(newsletterId);
        model.addAttribute("newsletter", newsletter);
        model.addAttribute("recipients", dataProcessor.getSubscriberEmails(newsletter));
        model.addAttribute("sender", dataProcessor.getNewsletterEmail());
        model.addAttribute("newSubscriber", new Subscriber());
        return "newsletterDetails";
    }

    /**
     * Removes a newsletter
     * @param newsletterId the newsletter's id
     * @param redirAttr attributes for the view to display some result information
     * @return the newsletter overview view
     */
    @RequestMapping("/newsletters/{nid}/delete")
    public String deleteNewsletter(@PathVariable("nid") Long newsletterId, RedirectAttributes redirAttr) {
        newsletterManager.deleteNewsletter(newsletterId);
        redirAttr.addFlashAttribute("newsletterDeleted", true);
        return "redirect:/newsletters";
    }


}
