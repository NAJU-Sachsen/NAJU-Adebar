package de.naju.adebar.model.persons;

import de.naju.adebar.model.persons.qualifications.ExistingQualificationException;
import de.naju.adebar.model.persons.qualifications.Qualification;
import de.naju.adebar.model.persons.qualifications.QualificationManager;
import de.naju.adebar.model.persons.qualifications.QualificationRepository;
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
    if (qualificationRepo.existsById(name)) {
      throw new ExistingQualificationException(
          "Qualification with name " + name + " already exists!");
    }
    return qualificationRepo.save(new Qualification(name, description));
  }

  @Override
  public Optional<Qualification> findQualification(String name) {
    return qualificationRepo.findById(name);
  }

  @Override
  public boolean hasQualification(String name) {
    return qualificationRepo.existsById(name);
  }

  @Override
  public QualificationRepository repository() {
    return qualificationRepo;
  }
}
