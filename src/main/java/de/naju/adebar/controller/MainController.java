package de.naju.adebar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Main controller of the application featuring mappings for the homepage and other off-topic views.
 *
 * @author Rico Bergmann
 */
@Controller
public class MainController {

  @RequestMapping({"/", "/index", "/overview"})
  public String showOverview(Model model) {
    return "redirect:/events";
  }

}
