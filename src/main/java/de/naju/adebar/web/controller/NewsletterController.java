package de.naju.adebar.web.controller;

import de.naju.adebar.app.chapter.LocalGroupManager;
import de.naju.adebar.app.newsletter.NewsletterDataProcessor;
import de.naju.adebar.app.newsletter.NewsletterManager;
import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.newsletter.NewsletterRepository;
import de.naju.adebar.model.newsletter.Subscriber;
import de.naju.adebar.model.newsletter.SubscriberRepository;
import de.naju.adebar.web.validation.newsletter.AddNewsletterForm;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Newsletter related controller mappings
 *
 * @author Rico Bergmann
 * @see Newsletter
 */
@Controller
public class NewsletterController {

  private NewsletterRepository newsletterRepo;
  private SubscriberRepository subscriberRepo;
  private NewsletterManager newsletterManager;
  private NewsletterDataProcessor dataProcessor;
  private LocalGroupManager localGroupManager;

  @Autowired
  public NewsletterController(NewsletterRepository newsletterRepo,
      SubscriberRepository subscriberRepo, NewsletterManager newsletterManager,
      NewsletterDataProcessor dataProcessor, LocalGroupManager localGroupManager) {
    this.newsletterRepo = newsletterRepo;
    this.subscriberRepo = subscriberRepo;
    this.newsletterManager = newsletterManager;
    this.dataProcessor = dataProcessor;
    this.localGroupManager = localGroupManager;
  }

  /**
   * Displays the newsletter overview
   *
   * @param model model to display the data in
   * @return the newsletters' overview view
   */
  @RequestMapping("/newsletters")
  public String showNewsletters(Model model) {
    model.addAttribute("newsletters", newsletterRepo.findAll());
    model.addAttribute("subscribers", subscriberRepo.findFirst10ByOrderByEmail());
    model.addAttribute("localGroups", dataProcessor.getLocalGroupBelonging());
    model.addAttribute("events", dataProcessor.getEventBelonging());
    model.addAttribute("projects", dataProcessor.getProjectBelonging());
    model.addAttribute("noBelonging", dataProcessor.getNewslettersWithoutBelonging());

    model.addAttribute("allChapters", localGroupManager.repository().findAll());
    model.addAttribute("addNewsletterForm", new AddNewsletterForm());

    return "newsletters";
  }

  /**
   * Creates a new newsletter
   *
   * @param form the posted newsletter data
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter overview view
   */
  @RequestMapping("/newsletters/add")
  public String addNewsletter(@ModelAttribute("addNewsletterForm") AddNewsletterForm form,
      RedirectAttributes redirAttr) {
    Newsletter newsletter = newsletterManager.createNewsletter(form.getName());

    if (form.belongsToChapter()) {
      LocalGroup chapter = localGroupManager.findLocalGroup(form.getLocalGroup())
          .orElseThrow(IllegalArgumentException::new);
      localGroupManager.addNewsletterToLocalGroup(chapter, newsletter);
    }

    redirAttr.addFlashAttribute("newsletterAdded", true);
    return "redirect:/newsletters";
  }

  /**
   * Displays detailed information for a specific newsletter
   *
   * @param newsletterId the newsletter's id
   * @param model model to display the data in
   * @return the newsletter detail view
   */
  @RequestMapping("/newsletters/{nid}")
  public String newsletterDetails(@PathVariable("nid") Long newsletterId, Model model) {
    Newsletter newsletter = newsletterRepo.findById(newsletterId)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + newsletterId));

    model.addAttribute("newsletter", newsletter);
    model.addAttribute("recipients", dataProcessor.getSubscriberEmails(newsletter));
    model.addAttribute("sender", dataProcessor.getNewsletterEmail());

    Optional<LocalGroup> localGroup =
        localGroupManager.repository().findByNewslettersContains(newsletter);
    if (localGroup.isPresent()) {
      model.addAttribute("localGroup", localGroup.get());
    } else {
      model.addAttribute("noBelonging", true);
    }

    model.addAttribute("newSubscriber", new Subscriber());
    return "newsletterDetails";
  }

  /**
   * Removes a newsletter
   *
   * @param newsletterId the newsletter's id
   * @param redirAttr attributes for the view to display some result information
   * @return the newsletter overview view
   */
  @RequestMapping("/newsletters/{nid}/delete")
  public String deleteNewsletter(@PathVariable("nid") Long newsletterId,
      RedirectAttributes redirAttr) {
    Newsletter newsletter = newsletterRepo.findById(newsletterId)
        .orElseThrow(() -> new IllegalArgumentException("No newsletter with ID " + newsletterId));

    localGroupManager.repository().findByNewslettersContains(newsletter)
        .ifPresent(l -> localGroupManager.removeNewsletter(l, newsletter));
    newsletterManager.deleteNewsletter(newsletterId);

    redirAttr.addFlashAttribute("newsletterDeleted", true);
    return "redirect:/newsletters";
  }

}
