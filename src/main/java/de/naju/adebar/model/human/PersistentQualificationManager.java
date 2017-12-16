package de.naju.adebar.model.human;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * A {@link QualificationManager} that persists the data in a database.
 *
 * @author Rico Bergmann
 */
@Service
public class PersistentQualificationManager implements QualificationManager {
  private QualificationRepository qualificationRepo;

  @Autowired
  public PersistentQualificationManager(QualificationRepository qualificationRepo) {
    Assert.notNull(qualificationRepo, "Qualification repository may not be null!");
    this.qualificationRepo = qualificationRepo;
  }

  @Override
  public Qualification createQualification(String name, String description) {
    if (qualificationRepo.exists(name)) {
      throw new ExistingQualificationException(
          "Qualification with name " + name + " already exists!");
    }
    return qualificationRepo.save(new Qualification(name, description));
  }

  @Override
  public Optional<Qualification> findQualification(String name) {
    Qualification qualification = qualificationRepo.findOne(name);
    return qualification == null ? Optional.empty() : Optional.of(qualification);
  }

  @Override
  public boolean hasQualification(String name) {
    return qualificationRepo.exists(name);
  }

  @Override
  public QualificationRepository repository() {
    return qualificationRepo;
  }
}
