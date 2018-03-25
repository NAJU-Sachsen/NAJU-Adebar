package de.naju.adebar.model.persons.family;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.PersonRepository;
import de.naju.adebar.util.Assert2;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * A {@link VitalRecord} that operates on a database.
 *
 * @author Rico Bergmann
 */
@Service
class PersistentVitalRecord implements VitalRecord {

  private final FamilyRelationsRepository familyRelationsRepo;
  private final PersonRepository personRepo;

  public PersistentVitalRecord(FamilyRelationsRepository familyRelationsRepo,
      PersonRepository personRepo) {
    Assert2.noNullArguments("No parameter may not be null", familyRelationsRepo, personRepo);
    this.familyRelationsRepo = familyRelationsRepo;
    this.personRepo = personRepo;
  }

  @Override
  public Relatives findRelativesOf(Person person) {
    List<Person> parents = familyRelationsRepo.findParentsOf(person);
    List<Person> children = familyRelationsRepo.findChildrenOf(person);
    List<Person> siblings = familyRelationsRepo.findSiblingsOf(person);

    return Relatives.buildRelativesFor(person) //
        .withParents(parents) //
        .withChildren(children) //
        .withSiblings(siblings) //
        .done();
  }

  @Override
  public void addChildTo(Person parent, Person theChild) {
    theChild.connectParentProfile(parent);
    personRepo.save(theChild);
  }

  @Override
  public void addParentTo(Person child, Person theParent) {
    child.connectParentProfile(theParent);
    personRepo.save(child);
  }

  @Override
  public void addSiblingTo(Person person, Person theSibling) {
    familyRelationsRepo //
        .findParentsOf(person) //
        .forEach(theSibling::connectParentProfile);
    personRepo.save(theSibling);
  }
  
}
