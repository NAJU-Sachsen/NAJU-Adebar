package de.naju.adebar.app.news;

import java.util.Optional;

/**
 * Manager to take care of {@link ReleaseNotes} instances
 * 
 * @author Rico Bergmann
 */
public interface ReleaseNotesManager {

  /**
   * Saves the latest release notes
   * 
   * @param notes the release notes
   * @return the saved notes. This instance should be used for further calls.
   */
  ReleaseNotes specifyLatestNotes(ReleaseNotes notes);

  /**
   * Archives the current notes
   */
  void markLatestNotesOutdated();

  /**
   * @return all archived notes
   */
  Iterable<ReleaseNotes> findOutdated();

  /**
   * @return the current release notes
   */
  Optional<ReleaseNotes> findLatest();
}
