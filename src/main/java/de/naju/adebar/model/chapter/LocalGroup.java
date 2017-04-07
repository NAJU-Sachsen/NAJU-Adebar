package de.naju.adebar.model.chapter;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.human.Activist;
import de.naju.adebar.model.human.Address;
import de.naju.adebar.model.newsletter.Newsletter;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.*;

/**
 * Abstraction of a local group. Each group has a (very likely) unique set of members, i. e. activist who contribute to
 * this certain group. Furthermore a chapter may have a board of directors if it is a more professional one.
 * @author Rico Bergmann
 */
@Entity
public class LocalGroup {
    @Id @GeneratedValue private long id;
    @Column(unique = true) private String name;
    @Embedded @Column(unique = true) private Address address;
    @OneToMany(cascade = CascadeType.ALL) private List<Activist> members;
    @OneToMany(cascade = CascadeType.ALL) private List<Event> events;
    @OneToMany(cascade = CascadeType.ALL) private Map<String, Project> projects;
    @OneToOne(cascade = CascadeType.ALL) private Board board;
    @OneToOne(cascade = CascadeType.ALL) private Newsletter newsletter;

    // constructors

    /**
     * Full constructor
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
        this.events = new LinkedList<>();
        this.projects = new HashMap<>();
    }

    /**
     * Default constructor for JPA
     */
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
    public Iterable<Activist> getMembers() {
        return members;
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

    public Newsletter getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(Newsletter newsletter) {
        this.newsletter = newsletter;
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
    protected void setMembers(List<Activist> members) {
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

    // query methods

    /**
     * @return the number of contributors to the chapter
     */
    @Transient  public int getMemberCount() {
        return members.size();
    }

    /**
     * @return the number of events the chapter hosts
     */
    @Transient public int getEventCount() {
        return events.size();
    }

    /**
     * @return the number of projects the local group organizes
     */
    @Transient public int getProjectCount() {
        return projects.size();
    }


    @Transient public Optional<Project> getProject(String name) {
        for (String projectName : projects.keySet()) {
            if (projectName.equals(name)) {
                return Optional.of(projects.get(name));
            }
        }
        return Optional.empty();
    }

    /**
     * @return {@code true} if a board is specified (i.e. different from {@code null}) or {@code false} otherwise
     */
    public boolean hasBoard() {
        return board != null;
    }

    // modification operations

    /**
     * @param activist the activist to add to the local group
     * @throws IllegalArgumentException if the activist is {@code null}
     * @throws ExistingMemberException if the activist is already registered as contributor
     */
    public void addMember(Activist activist) {
        Assert.notNull(activist, "Activist to add may not be null!");
        if (isMember(activist)) {
            throw new ExistingMemberException(String.format("Activist %s is already part of local group %s", activist.toString(), this.toString()));
        }
        members.add(activist);
    }

    /**
     * @param activist the activist to check
     * @return {@code true} if the activist is registered as contributor to the chapter or {@code false} otherwise
     */
    public boolean isMember(Activist activist) {
        return members.contains(activist);
    }

    /**
     * @param activist the activist to remove
     * @throws IllegalArgumentException if the activist does not contribute to the local group
     */
    public void removeMember(Activist activist) {
        Assert.isTrue(isMember(activist), "Not a member of the local group: " + activist);
        members.remove(activist);
    }

    /**
     * @param event the event to be hosted by the local group
     * @throws IllegalArgumentException if the event is {@code null} or already hosted by the local group
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
     * @throws IllegalArgumentException if the project is {@code null} or already organized by the local group
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

    public void updateProject(Project project) {
        Assert.notNull(project, "Project may not be null");
        if (!this.equals(project.getLocalGroup())) {
            throw new IllegalStateException("Project is already hosted by another local group");
        }
        projects.put(project.getName(), project);
    }

    // overridden from Object

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalGroup that = (LocalGroup) o;

        if (!name.equals(that.name)) return false;
        if (!address.equals(that.address)) return false;
        if (!members.equals(that.members)) return false;
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
        return "LocalGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", members=" + members +
                '}';
    }
}
