package de.naju.adebar.model.chapter;

import com.google.common.collect.Lists;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.human.Activist;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstraction of a project
 * @author Rico Bergmann
 */
@Entity
public class Project {
    @Id @GeneratedValue private long id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    @ManyToOne private LocalGroup localGroup;
    private Activist personInCharge;
    @OneToMany private List<Activist> contributors;
    @OneToMany private List<Event> events;

    // constructors

    /**
     * Reduced constructor
     * @param name the project's name
     * @param localGroup the local group that host's the project
     */
    public Project(String name, LocalGroup localGroup) {
        this(name, null, null, localGroup, null);
    }

    /**
     * Full constructor
     * @param name the project's name
     * @param startTime the time the project starts, may be {@code null}
     * @param endTime the time the project ends, may be {@code null}
     * @param localGroup the local group that host's the project
     * @param personInCharge the person responsible for the project, may be {@code null}
     * @throws IllegalArgumentException if name or local group were {@code null}
     */
    public Project(String name, LocalDate startTime, LocalDate endTime, LocalGroup localGroup, Activist personInCharge) {
        Object[] params = {name, localGroup};
        Assert.noNullElements(params, "No parameter may be null: " + Arrays.toString(params));
        Assert.hasText(name, "Name may not be empty: " + name);
        if (startTime != null && endTime != null) {
            Assert.isTrue(!endTime.isBefore(startTime), String.format("Illegal combination of start time (%s) and end time (%s)", startTime, endTime));
        }

        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.localGroup = localGroup;
        this.personInCharge = personInCharge;
    }

    // basic setter and getter

    /**
     * @return the project's id (= primary key)
     */
    public long getId() {
        return id;
    }

    /**
     * @return the project's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the project's name
     * @throws IllegalArgumentException if the name is {@code null}
     */
    public void setName(String name) {
        Assert.hasText(name, "Name may not be null or empty, but was: " + name);
        this.name = name;
    }

    /**
     * @return the time the project starts
     */
    public LocalDate getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the time the project starts
     */
    public void setStartTime(LocalDate startTime) {
        if (startTime != null && endTime != null) {
            Assert.isTrue(!endTime.isBefore(startTime), String.format("Illegal combination of start time (%s) and end time (%s)", startTime, endTime));
        }
        this.startTime = startTime;
    }

    /**
     * @return the time the project ends at
     */
    public LocalDate getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the time the project ends at
     */
    public void setEndTime(LocalDate endTime) {
        if (startTime != null && endTime != null) {
            Assert.isTrue(!endTime.isBefore(startTime), String.format("Illegal combination of start time (%s) and end time (%s)", startTime, endTime));
        }
        this.endTime = endTime;
    }

    /**
     * @return the local group that hosts the project
     */
    public LocalGroup getLocalGroup() {
        return localGroup;
    }

    /**
     * @param localGroup the local group that hosts the project
     * @throws IllegalArgumentException if the chapter is {@code null}
     */
    public void setLocalGroup(LocalGroup localGroup) {
        this.localGroup = localGroup;
    }

    /**
     * @return the person who is in charge the project
     */
    public Activist getPersonInCharge() {
        return personInCharge;
    }

    /**
     * @param personInCharge the person in charge for the project
     */
    public void setPersonInCharge(Activist personInCharge) {
        this.personInCharge = personInCharge;
    }

    /**
     * @return the activists who contribute to the project
     */
    public Iterable<Activist> getContributors() {
        return contributors;
    }

    /**
     * @return the events that are hosted within the project
     */
    public Iterable<Event> getEvents() {
        return events;
    }

    /**
     * @param id updates the project's primary key
     */
    protected void setId(long id) {
        this.id = id;
    }

    /**
     * @param events the events that are hosted within the project
     */
    protected void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * @param contributors the activists who contribute to the project
     */
    protected void setContributors(List<Activist> contributors) {
        this.contributors = contributors;
    }

    // query methods

    /**
     * @return the number of persons that contribute to the project
     */
    @Transient
    public int getContributorsCount() {
        return contributors.size();
    }

    /**
     * @return the event that is about to take place next
     */
    @Transient
    public Event getNextEvent() {
        return Collections.min(events, Comparator.comparing(Event::getStartTime));
    }

    // overridden from Object

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!name.equals(project.name)) return false;
        if (startTime != null ? !startTime.equals(project.startTime) : project.startTime != null) return false;
        if (endTime != null ? !endTime.equals(project.endTime) : project.endTime != null) return false;
        if (!localGroup.equals(project.localGroup)) return false;
        if (personInCharge != null ? !personInCharge.equals(project.personInCharge) : project.personInCharge != null)
            return false;
        if (!contributors.equals(project.contributors)) return false;
        return events.equals(project.events);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + localGroup.hashCode();
        result = 31 * result + (personInCharge != null ? personInCharge.hashCode() : 0);
        result = 31 * result + contributors.hashCode();
        result = 31 * result + events.hashCode();
        return result;
    }
}
