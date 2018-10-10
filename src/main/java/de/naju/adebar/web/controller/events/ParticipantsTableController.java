package de.naju.adebar.web.controller.events;

import java.security.Principal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.events.processing.EventDataProcessingService;
import de.naju.adebar.app.security.user.Username;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.util.Assert2;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableBuilder;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTableFormattingService;
import de.naju.adebar.web.model.events.participation.table.template.ParticipantsTableTemplate;
import de.naju.adebar.web.model.events.participation.table.template.ParticipantsTableTemplateRepository;
import de.naju.adebar.web.services.events.participation.table.CSVServer;
import de.naju.adebar.web.validation.events.participation.ParticipantsTableForm;

/**
 * Handles all requests related to showing and adpating participants tables.
 *
 * @author Rico Bergmann
 * @see ParticipantsTable
 */
@Controller
public class ParticipantsTableController {

  private static final Logger log = LoggerFactory.getLogger(ParticipantsTableController.class);

  private final ParticipantsTableTemplateRepository templateRepo;
  private final EventDataProcessingService eventDataProcessingService;
  private final ParticipantsTableFormattingService participantsTableFormattingService;
  private final CSVServer participantsTableCSVServer;

  /**
   * Full constructor. No parameter may be {@code null}.
   *
   * @param templateRepo repository storing all saved table templates
   * @param eventDataProcessingService service to conveniently extract data from {@link Event *
   *          Events}
   * @param participantsTableFormattingService service to render the contents of the participants
   *          table
   * @param participantsTableCSVServer
   */
  public ParticipantsTableController(ParticipantsTableTemplateRepository templateRepo,
      EventDataProcessingService eventDataProcessingService,
      ParticipantsTableFormattingService participantsTableFormattingService,
      CSVServer participantsTableCSVServer) {
    Assert2.noNullArguments("No argument may be null", templateRepo, eventDataProcessingService,
        participantsTableFormattingService, participantsTableCSVServer);
    this.templateRepo = templateRepo;
    this.eventDataProcessingService = eventDataProcessingService;
    this.participantsTableFormattingService = participantsTableFormattingService;
    this.participantsTableCSVServer = participantsTableCSVServer;
  }

  /**
   * Renders the view to customize the columns in the participants table.
   *
   * @param event the event whose participants table should be edited
   * @param columnSelection whether the column selection dialog should be shown
   * @param model data model to use for the template
   * @return the event details template. It will include the appropriate fragments.
   */
  @GetMapping("/events/{id}/participants/table")
  public String showCustomizeParticipantsTable(@PathVariable("id") Event event,
      @RequestParam(name = "columnSelection", defaultValue = "true") boolean columnSelection,
      Principal principal, Model model) {

    List<ParticipantsTableTemplate> templates =
        templateRepo.findByCreator(Username.of(principal.getName()));

    model.addAttribute("tab", "participantsTable");
    model.addAttribute("showTable", false);
    model.addAttribute("event", event);
    model.addAttribute("templates", templates);
    model.addAttribute("processingService", eventDataProcessingService);

    if (!model.containsAttribute("participantsTableForm")) {
      model.addAttribute("participantsTableForm", new ParticipantsTableForm());
    }

    return "events/eventDetails";
  }

  /**
   * Adapts the participants table.
   *
   * @param event the event whose participants table will be edited
   * @param form form containing which columns should be shown
   * @param model data model to for the resulting template
   * @return the event details template. It will include the appropriate fragments.
   */
  @PostMapping("/events/{id}/participants/table")
  public String customizeParticipantsTable(@PathVariable("id") Event event,
      @ModelAttribute("participantsTable") ParticipantsTableForm form, Principal principal,
      Model model) {

    ParticipantsTable table = form.toParticipantsTable().forEvent(event);
    prepareModel(table, form, event, principal, model);

    return "events/eventDetails";
  }

  /**
   * Stores a participants table as template for later use.
   *
   * @param event the event that was shown when the template was created. Just to redirect to it
   *          again after the template was saved.
   * @param form the form that contains the columns which should be part of the template
   * @param templateName the template's name
   * @param principal the user that wants to store the template
   * @param redirAttr attributes for the model and URL to use after redirection
   * @return a redirection to the current participants table
   */
  @PostMapping("/events/{id}/participants/table/save")
  public String saveParticipantsTable(@PathVariable("id") Event event,
      @ModelAttribute("participantsTable") ParticipantsTableForm form,
      @RequestParam("templateName") String templateName, Principal principal,
      RedirectAttributes redirAttr) {

    Username creator = Username.of(principal.getName());
    ParticipantsTableBuilder table = form.toParticipantsTable();

    ParticipantsTableTemplate template = ParticipantsTableTemplate //
        .by(creator) //
        .called(templateName) //
        .with(table);

    template = templateRepo.save(template);

    redirAttr.addAttribute("columnSelection", false);

    log.info("Saved template {}", template);
    return "redirect:/events/" + event.getId() + "/participants/table";
  }

  /**
   * Applies a template to an event to show its participants table with a pre-defined set of
   * columns.
   *
   * @param event the event whose table should be shown
   * @param templateName the name of the template to use
   * @param principal the user who requested the template. This parameter is mandatory as templates
   *          are user-specific.
   * @param model data model to use for the template
   * @return the event details template. It will include the appropriate fragments.
   */
  @GetMapping("/events/{id}/participants/table/template")
  public String loadParticipantsTable(@PathVariable("id") Event event,
      @RequestParam("template") String templateName, Principal principal, Model model) {

    ParticipantsTableTemplate template = templateRepo //
        .findByCreatorAndName(Username.of(principal.getName()), templateName) //
        .orElseThrow(IllegalArgumentException::new);

    ParticipantsTable table = template.applyTo(event);
    prepareModel(table, new ParticipantsTableForm(table), event, principal, model);

    model.addAttribute("fromTemplate", true);
    return "events/eventDetails";
  }

  /**
   * Prints a participants table.
   *
   * @param event the event whose table should be printed
   * @param form form containing the columns to be shown
   * @param principal the user who requested the print action
   * @param model data model to use for the template
   * @return the print view template
   */
  @GetMapping("/events/{id}/participants/table/print")
  public String printParticipantsTable(@PathVariable("id") Event event,
      @ModelAttribute("participantsTable") ParticipantsTableForm form, Principal principal,
      Model model) {

    ParticipantsTable table = form.toParticipantsTable().forEvent(event);
    prepareModel(table, form, event, principal, model);

    return "events/printParticipantsTable";
  }

  /**
   * Provides a downloadable CSV file for a participants table.
   *
   * @param event the event for which the table should be printed
   * @param form the form which contains the columns to print
   * @return the CSV file
   */
  @GetMapping("/events/{id}/participants/table/export")
  @ResponseBody
  public ResponseEntity<Resource> exportParticipantsTable(@PathVariable("id") Event event,
      @ModelAttribute("participantsTable") ParticipantsTableForm form) {

    ParticipantsTable table = form.toParticipantsTable().forEvent(event);
    Resource csvFile = new FileSystemResource(participantsTableCSVServer.generateCSV(table));

    return ResponseEntity //
        .ok() //
        .header(HttpHeaders.CONTENT_DISPOSITION, //
            "attachment; filename=\"" + csvFile.getFilename() + "\"") //
        .contentType(MediaType.parseMediaType("text/csv")) //
        .body(csvFile);
  }

  /**
   * Sets up the model for the resulting view.
   *
   * @param table the table to display
   * @param form the form that contains the selected table columns
   * @param event the event which contains the table
   * @param model the model to set up
   */
  private void prepareModel(ParticipantsTable table, ParticipantsTableForm form, Event event,
      Principal principal, Model model) {
    List<ParticipantsTableTemplate> templates =
        templateRepo.findByCreator(Username.of(principal.getName()));

    model.addAttribute("participantsTable", table);
    model.addAttribute("participantsTableForm", form);

    model.addAttribute("templates", templates);
    model.addAttribute("formattingService", participantsTableFormattingService);
    model.addAttribute("tab", "participantsTable");
    model.addAttribute("showTable", true);
    model.addAttribute("event", event);
    model.addAttribute("processingService", eventDataProcessingService);
  }

}
