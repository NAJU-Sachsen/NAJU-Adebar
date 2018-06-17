package de.naju.adebar.web.controller.events;

import de.naju.adebar.model.chapter.LocalGroup;
import de.naju.adebar.model.chapter.LocalGroupRepository;
import de.naju.adebar.model.chapter.Project;
import de.naju.adebar.model.chapter.ProjectRepository;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.EventRepository;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.validation.events.AddEventForm;
import de.naju.adebar.web.validation.events.AddEventFormConverter;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Transactional
public class AddEventController {

  private final EventRepository eventRepo;
  private final LocalGroupRepository localGroupRepo;
  private final ProjectRepository projectRepo;
  private final AddEventFormConverter eventFormConverter;

  public AddEventController(EventRepository eventRepo, LocalGroupRepository localGroupRepo,
      ProjectRepository projectRepo, AddEventFormConverter eventFormConverter) {
    Assert2.noNullArguments("No argument may be null", eventRepo, localGroupRepo, projectRepo,
        eventFormConverter);
    this.eventRepo = eventRepo;
    this.localGroupRepo = localGroupRepo;
    this.projectRepo = projectRepo;
    this.eventFormConverter = eventFormConverter;
  }

  @GetMapping("/events/add")
  public String showAddEventView(Model model) {
    model.addAttribute("localGroups", localGroupRepo.findAll());
    model.addAttribute("projects", projectRepo.findAll());

    if (!model.containsAttribute("form")) {
      model.addAttribute("form", new AddEventForm());
    }

    return "events/addEvent";
  }

  @PostMapping("/events/add")
  public String addEvent(@ModelAttribute("form") @Valid AddEventForm form, BindingResult errors,
      @RequestParam(value = "return-action", defaultValue = "") String returnAction, //
      @RequestParam(value = "return-to", defaultValue = "") String returnPath, //
      RedirectAttributes redirAttr) {

    if (errors.hasErrors()) {

      form.getAccommodation().prepareRooms();

      redirAttr.addFlashAttribute("form", form);
      redirAttr.addFlashAttribute("org.springframework.validation.BindingResult.form", errors);

      if (!returnAction.isEmpty()) {
        redirAttr.addAttribute("return-action", returnAction);
      }
      if (!returnPath.isEmpty()) {
        redirAttr.addAttribute("return-to", returnPath);
      }

      return "redirect:/events/add";
    }

    Event newEvent = eventFormConverter.toEntity(form);
    eventRepo.save(newEvent);

    switch (form.getBelonging()) {
      case LOCAL_GROUP:
        LocalGroup localGroup = localGroupRepo.findById(form.getLocalGroup())
            .orElseThrow(() -> new IllegalArgumentException(
                "No local group with id " + form.getLocalGroup()));
        localGroup.addEvent(newEvent);
        localGroupRepo.save(localGroup);
        break;
      case PROJECT:
        Project project = projectRepo.findById(form.getProject())
            .orElseThrow(() -> new IllegalArgumentException(
                "No project with id " + form.getProject()));
        project.addEvent(newEvent);
        projectRepo.save(project);
        break;
    }

    if (!returnAction.isEmpty()) {
      redirAttr.addAttribute("do", returnAction);
    }

    if (!returnPath.isEmpty()) {
      return "redirect:" + returnPath;
    }

    return "redirect:/events/" + newEvent.getId();
  }

  @InitBinder("form")
  protected void initBinders(WebDataBinder binder) {
    binder.addValidators(eventFormConverter);
  }

}
