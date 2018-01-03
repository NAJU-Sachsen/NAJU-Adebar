package de.naju.adebar.app.news;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Service that stores its {@link ReleaseNotes} in a database
 * 
 * @author Rico Bergmann
 */
@Service
public class PersistentReleaseNotesManager implements ReleaseNotesManager {

  private final ReleaseNewsRepository releaseNotesRepo;

  public PersistentReleaseNotesManager(ReleaseNewsRepository releaseNotesRepo) {
    Assert.notNull(releaseNotesRepo, "Repository may not be null");
    this.releaseNotesRepo = releaseNotesRepo;
  }

  @Override
  public ReleaseNotes specifyLatestNotes(ReleaseNotes notes) {
    markLatestNotesOutdated();
    return releaseNotesRepo.save(notes);
  }

  @Override
  public void markLatestNotesOutdated() {
    releaseNotesRepo.findFirstByActiveIsTrue().ifPresent(notes -> {
      notes.archive();
      releaseNotesRepo.save(notes);
    });
  }

  @Override
  public Iterable<ReleaseNotes> findOutdated() {
    return releaseNotesRepo.findAllByActiveIsFalseOrderByDateDesc();
  }

  @Override
  public Optional<ReleaseNotes> findLatest() {
    return releaseNotesRepo.findFirstByActiveIsTrue();
  }

}
