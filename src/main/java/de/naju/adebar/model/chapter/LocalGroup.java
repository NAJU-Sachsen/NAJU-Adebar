package de.naju.adebar.model.chapter;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.newsletter.Newsletter;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.exceptions.NoActivistException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import org.springframework.util.Assert;

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

  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "addressStreet")),
      @AttributeOverride(name = "zip", column = @Column(name = "addressZip")),
      @AttributeOverride(name = "city", column = @Column(name = "addressCity")),
      @AttributeOverride(name = "additionalInfo", column = @Column(name = "addressHints"))})
  private Address address;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "localGroupMembers", //
      joinColumns = @JoinColumn(name = "localGroupId"), //
      inverseJoinColumns = @JoinColumn(name = "memberId"))
  private List<Person> members;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "localGroupContactPersons", //
      joinColumns = @JoinColumn(name = "localGroupId"), //
      inverseJoinColumns = @JoinColumn(name = "personId"))
  private List<Person> contactPersons;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "localGroupEvents", //
      joinColumns = @JoinColumn(name = "localGroupId"), //
      inverseJoinColumns = @JoinColumn(name = "eventId"))
  private List<Event> events;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "localGroup")
  private List<Project> projects;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Board board;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "localGroupNewsletters", //
      joinColumns = @JoinColumn(name = "localGroupId"), //
      inverseJoinColumns = @JoinColumn(name = "newsletterId"))
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
    this.projects = new LinkedList<>();
    this.newsletters = new HashSet<>();
  }

  /**
   * Default constructor for JPA
   */
  protected LocalGroup() {
  }

  // basic getter and setter

  /**
   * @return the ID (= primary key) of the local group
   */
  public long getId() {
    return id;
  }

  /**
   * @param id the primary key of the local group
   */
  protected void setId(long id) {
    this.id = id;
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
   * @param members the local group's members
   */
  protected void setMembers(List<Person> members) {
    this.members = members;
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
   * @param events the events the local group hosts
   */
  protected void setEvents(List<Event> events) {
    this.events = events;
  }

  /**
   * @return the projects the local group organizes
   */
  public List<Project> getProjects() {
    return Collections.unmodifiableList(projects);
  }

  /**
   * @param projects the projects the local group organizes
   */
  protected void setProjects(List<Project> projects) {
    this.projects = projects;
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

  public void setNewsletters(Set<Newsletter> newsletters) {
    this.newsletters = newsletters;
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
   *     optional is empty
   */
  @Transient
  public Optional<Project> getProject(String name) {
    return projects.stream().filter(project -> project.getName().equals(name)).findAny();
  }

  /**
   * @return {@code true} if a board is specified (i.e. different from {@code null}) or {@code
   *     false} otherwise
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
   *     otherwise
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
   * @return {@code true} if the activist is registered as contributor to the chapter or {@code
   *     false} otherwise
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
   * @throws IllegalArgumentException if the event is {@code null} or already hosted by the
   *     local group
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
   *     local group
   * @throws IllegalStateException if the project is already hosted by another chapter
   */
  public void addProject(Project project) {
    Assert.notNull(project, "Project may not be null");
    if (hasProjectWithName(project.getName())) {
      throw new IllegalArgumentException("Local group does already organize project " + project);
    } else if (!this.equals(project.getLocalGroup())) {
      throw new IllegalStateException("Project is already hosted by another local group");
    }
    projects.add(project);
  }

  public boolean hasProjectWithName(String name) {
    return projects.stream().anyMatch(p -> p.getName().equals(name));
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
   * @param newsletter the newsletter to remove from the chapter, if the local group actually
   *     has such a newsletter
   */
  public void removeNewsletter(Newsletter newsletter) {
    newsletters.remove(newsletter);
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    LocalGroup that = (LocalGroup) o;

    if (that.id != 0 && this.id != 0) {
      return that.id == this.id;
    }

    if (!name.equals(that.name)) {
      return false;
    }
    if (!address.equals(that.address)) {
      return false;
    }
    if (!members.equals(that.members)) {
      return false;
    }
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
