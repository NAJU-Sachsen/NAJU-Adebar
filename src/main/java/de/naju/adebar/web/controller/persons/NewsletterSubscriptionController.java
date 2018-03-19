package de.naju.adebar.web.controller.persons;

import de.naju.adebar.model.persons.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Handles all requests to the newsletter subscriptions of persons
 *
 * <p> <b>Right now this functionality is delegated to the newsletter-subsystem for the most
 * part</b>
 *
 * @author Rico Bergmann
 * @see de.naju.adebar.web.controller.NewsletterController
 */
@Controller
public class NewsletterSubscriptionController {

  /**
   * Renders the newsletter subscription template for a specific person
   *
   * <p> <b>Right now nothing is going on here, all functionality still resides in the
   * newsletter-subsystem</b>
   *
   * @param person the person
   * @param model model to put the data to render into
   * @return the newsletter subscription template
   */
  @GetMapping("/persons/{id}/newsletters")
  public String showSubscribedNewsletters(@PathVariable("id") Person person, Model model) {

    model.addAttribute("person", person);

    model.addAttribute("tab", "newsletters");
    return "persons/personDetails";
  }

}
