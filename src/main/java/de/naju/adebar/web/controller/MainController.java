package de.naju.adebar.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import de.naju.adebar.app.news.ReleaseNotesManager;

/**
 * Main controller of the application featuring mappings for the homepage and other off-topic views.
 *
 * @author Rico Bergmann
 */
@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);

	private final ReleaseNotesManager releaseNotesManager;

	public MainController(ReleaseNotesManager releaseNotesManager) {
		Assert.notNull(releaseNotesManager, "Release notes manager may not be null");
		this.releaseNotesManager = releaseNotesManager;
	}

	/**
	 * Currently redirects to the events overview-page
	 *
	 * @param redirAttr attributes for the events overview model
	 * @return the redirection string
	 */
	@GetMapping({"/", "/index", "/overview"})
	public String showOverview(@RequestHeader("User-Agent") String userAgent,
			@CookieValue(value="Continue-If-Unsupported", defaultValue = "false") String continueEvenIfUnsupported,
			RedirectAttributes redirAttr) {
		if (requestFromSafariBrowser(userAgent) && (continueEvenIfUnsupported.equals("false") )) {
			// we only want to catch Safari, so if there are false-positives we need to know their User
			// agents
			log.debug("Access with unsupported browser: " + userAgent);
			return "redirect:/unsupported-browser";
		}

		redirAttr.addFlashAttribute("releaseNotes", releaseNotesManager.findLatest().orElse(null));
		return "redirect:/events";
	}

	/**
	 * Displays an error page if an unsupported browser is used.
	 * <p>
	 * As some browsers do not support some web-technologies - especially HTML5 - to the extend that
	 * we need it, those browsers may not access Adebar. The checks performed to filter for those
	 * browsers are not full-proof and may fail from time to time.
	 *
	 * @return the unsupported browser template
	 */
	@GetMapping("/unsupported-browser")
	public String showUnsupportedBrowserPage() {
		return "unsupportedBrowser";
	}

	/**
	 * Displays the site's imprint (German law thing...)
	 *
	 * @return the imprint template
	 */
	@GetMapping("/imprint")
	public String showImprint() {
		return "imprint";
	}

	/**
	 * Checks, whether Safari is used to access Adebar
	 *
	 * @param userAgent the userAgent of the accessing browser
	 * @return whether the user could be using Safari
	 */
	private boolean requestFromSafariBrowser(String userAgent) {
		return userAgent.contains("Safari") && !userAgent.contains("Chrome");
	}

}
