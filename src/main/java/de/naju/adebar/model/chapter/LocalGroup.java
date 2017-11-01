package de.naju.adebar.model.chapter;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.human.NoActivistException;
import de.naju.adebar.model.human.Person;
import de.naju.adebar.model.newsletter.Newsletter;

// TODO a local group should be able to contain multiple newsletters
// TODO adjust relation of board, contact persons and NABU-board

/**
 * Abstraction of a local group. Each group has a (very likely) unique set of members, i. e.
 * activist who contribute to this certain group. Furthermore a chapter may have a board of
 * directors if it is a more professional one.
 * 
 * @author Rico Bergmann
 */
@Entity(name = "localGroup")
public class LocalGroup {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private long id;

  @Column(name = "name", unique = true)
  private String name;

  @Embedded
  @Column(unique = true)
  private Address address;

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Person> members;

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Person> contactPersons;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Event> events;

  @OneToMany(cascade = CascadeType.ALL)
  private Map<String, Project> projects;

  @OneToOne(cascade = CascadeType.ALL)
  private Board board;

  @OneToMany(cascade = CascadeType.ALL)
  private Set<Newsletter> newsletters;

  @Column(name = "nabuGroup")
  private URL nabuGroupLink;

  // constructors

  /**
   * Full constructor
   * 
   * @param name the chapter's name
   * @param address the address of the group - i. e. the office's address or the like
   */
  public LocalGroup(String name, Address address) {
    Object[] params = {name, address};
    Assert.noNullElements(params, "At least one parameter was null: " + Arrays.toString(params));
    Assert.hasText(name, "Name must have content: " + name);
    this.name = name;
    this.address = address;
    this.members = new LinkedList<>();
    this.contactPersons = new LinkedList<>();
    this.events = new LinkedList<>();
    this.projects = new HashMap<>();
    this.newsletters = new HashSet<>();
  }

  /**
   * Default constructor for JPA
   */
  @SuppressWarnings("unused")
  private LocalGroup() {}

  // basic getter and setter

  /**
   * @return the ID (= primary key) of the local group
   */
  public long getId() {
    return id;
  }

  /**
   * @return the local group's name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the local group's name. Must be unique amongst all local groups
   * @throws IllegalArgumentException if the name is {@code null} or empty
   */
  public void setName(String name) {
    Assert.hasText(name, "Name must have content: " + name);
    this.name = name;
  }

  /**
   * @return the local group's main address - i. e. the address of its office or something
   */
  public Address getAddress() {
    return address;
  }

  /**
   * @param address the local group's address. May not be {@code null}
   * @throws IllegalArgumentException if the address is {@code null}
   */
  public void setAddress(Address address) {
    Assert.notNull(address, "Address may not be null");
    this.address = address;
  }

  /**
   * @return the activist who contribute to the chapter
   */
  public Iterable<Person> getMembers() {
    return members;
  }

  /**
   * @return the local group's contact persons
   */
  public Iterable<Person> getContactPersons() {
    return contactPersons;
  }

  /**
   * @param contactPersons the contact persons for the local group
   */
  public void setContactPersons(List<Person> contactPersons) {
    this.contactPersons = contactPersons;
  }

  /**
   * @return the events the local group hosts
   */
  public Iterable<Event> getEvents() {
    return events;
  }

  /**
   * @return the projects the local group organizes
   */
  public Map<String, Project> getProjects() {
    return Collections.unmodifiableMap(projects);
  }

  /**
   * @return the local group's board of directors
   */
  public Board getBoard() {
    return board;
  }

  /**
   * @param board the local group's board of directors. May be {@code null}
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  public Iterable<Newsletter> getNewsletters() {
    return newsletters;
  }

  /**
   * @return the website of the NABU group this NAJU belongs to
   */
  public URL getNabuGroupLink() {
    return nabuGroupLink;
  }

  /**
   * @param nabuGroupLink the website of the NABU group this NAJU belongs to
   */
  public void setNabuGroupLink(URL nabuGroupLink) {
    this.nabuGroupLink = nabuGroupLink;
  }

  /**
   * @param id the primary key of the local group
   */
  protected void setId(long id) {
    this.id = id;
  }

  /**
   * @param members the local group's members
   */
  protected void setMembers(List<Person> members) {
    this.members = members;
  }

  /**
   * @param events the events the local group hosts
   */
  protected void setEvents(List<Event> events) {
    this.events = events;
  }

  /**
   * @param projects the projects the local group organizes
   */
  protected void setProjects(Map<String, Project> projects) {
    this.projects = projects;
  }

  public void setNewsletters(Set<Newsletter> newsletters) {
    this.newsletters = newsletters;
  }

  // query methods

  /**
   * @return the number of contributors to the chapter
   */
  @Transient
  public int getMemberCount() {
    return members.size();
  }

  /**
   * @return the number of events the chapter hosts
   */
  @Transient
  public int getEventCount() {
    return events.size();
  }

  /**
   * @return the number of projects the local group organizes
   */
  @Transient
  public int getProjectCount() {
    return projects.size();
  }

  /**
   * @param name the project's name
   * @return an optional if the local group organizes a project with that name. Otherwise the
   *         optional is empty
   */
  @Transient
  public Optional<Project> getProject(String name) {
    for (String projectName : projects.keySet()) {
      if (projectName.equals(name)) {
        return Optional.of(projects.get(name));
      }
    }
    return Optional.empty();
  }

  /**
   * @return {@code true} if a board is specified (i.e. different from {@code null}) or
   *         {@code false} otherwise
   */
  public boolean hasBoard() {
    return board != null;
  }

  /**
   * @return {@code true} if the local group has at least one newsletter, {@code false} otherwise
   */
  public boolean hasNewsletters() {
    return !newsletters.isEmpty();
  }

  /**
   * @return {@code true} if the local group has at least one contact person, {@code false}
   *         otherwise
   */
  public boolean hasContactPersons() {
    return !contactPersons.isEmpty();
  }

  /**
   * @return {@code true} if a link to the related NABU group was specified, {@code false} otherwise
   */
  public boolean hasNabuGroupLink() {
    return nabuGroupLink != null;
  }

  // modification operations

  /**
   * @param person the activist to add to the local group
   * @throws IllegalArgumentException if the activist is {@code null}
   * @throws NoActivistException if the person is no activist
   * @throws ExistingMemberException if the activist is already registered as contributor
   */
  public void addMember(Person person) {
    Assert.notNull(person, "Activist to add may not be null!");
    if (!person.isActivist()) {
      throw new NoActivistException("Person is no activist: " + person);
    } else if (isMember(person)) {
      throw new ExistingMemberException(String.format(
          "Activist %s is already part of local group %s", person.toString(), this.toString()));
    }
    members.add(person);
  }

  /**
   * @param activist the activist to check
   * @return {@code true} if the activist is registered as contributor to the chapter or
   *         {@code false} otherwise
   */
  public boolean isMember(Person activist) {
    return members.contains(activist);
  }

  /**
   * @param activist the activist to remove
   * @throws IllegalArgumentException if the activist does not contribute to the local group
   */
  public void removeMember(Person activist) {
    Assert.isTrue(isMember(activist), "Not a member of the local group: " + activist);
    members.remove(activist);
  }

  /**
   * @param event the event to be hosted by the local group
   * @throws IllegalArgumentException if the event is {@code null} or already hosted by the local
   *         group
   */
  public void addEvent(Event event) {
    Assert.notNull(event, "Event to add may not be null!");
    if (events.contains(event)) {
      throw new IllegalArgumentException("Local group already hosts event " + event);
    }
    events.add(event);
  }

  /**
   * @param project the project to be organized by the local group
   * @throws IllegalArgumentException if the project is {@code null} or already organized by the
   *         local group
   * @throws IllegalStateException if the project is already hosted by another chapter
   */
  public void addProject(Project project) {
    Assert.notNull(project, "Project may not be null");
    if (projects.containsKey(project.getName())) {
      throw new IllegalArgumentException("Local group does already organize project " + project);
    } else if (!this.equals(project.getLocalGroup())) {
      throw new IllegalStateException("Project is already hosted by another local group");
    }
    projects.put(project.getName(), project);
  }

  /**
   * @param project the project to update
   * @throws IllegalArgumentException if the project is {@code null}
   * @throws IllegalStateException if the project is already hosted by another chapter (according to
   *         project.localGroup)
   */
  public void updateProject(Project project) {
    Assert.notNull(project, "Project may not be null");
    if (!this.equals(project.getLocalGroup())) {
      throw new IllegalStateException("Project is already hosted by another local group");
    }
    projects.put(project.getName(), project);
  }

  /**
   * @param newsletter the newsletter to add
   * @throws IllegalArgumentException if the newsletter is {@code null}
   */
  public void addNewsletter(Newsletter newsletter) {
    Assert.notNull(newsletter, "Newsletter to add may not be null!");
    newsletters.add(newsletter);
  }

  /**
   * @param newsletter the newsletter to remove from the chapter, if the local group actually has
   *        such a newsletter
   */
  public void removeNewsletter(Newsletter newsletter) {
    newsletters.remove(newsletter);
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    LocalGroup that = (LocalGroup) o;

    if (that.id != 0 && this.id != 0) {
      return that.id == this.id;
    }

    if (!name.equals(that.name))
      return false;
    if (!address.equals(that.address))
      return false;
    if (!members.equals(that.members))
      return false;
    return board != null ? board.equals(that.board) : that.board == null;
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + address.hashCode();
    result = 31 * result + members.hashCode();
    result = 31 * result + (board != null ? board.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "LocalGroup{" + "id=" + id + ", name='" + name + '\'' + ", address=" + address
        + ", members=" + members + '}';
  }
}
