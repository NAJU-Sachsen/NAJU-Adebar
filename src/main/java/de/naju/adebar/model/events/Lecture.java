package de.naju.adebar.model.events;

import de.naju.adebar.model.human.Referent;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Abstraction of a lecture
 * @author Rico Bergmann
 */
@Embeddable
public class Lecture {
    private String title;
    private Referent referent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private double educationalUnits;

    /**
     * Simplified constructor initializing only the most important data
     * @param referent the referent who gives the lecture
     * @param title the lecture's title
     * @param startTime the time the lecture starts
     * @param endTime the time the lecture ends
     */
    public Lecture(Referent referent, String title, LocalDateTime startTime, LocalDateTime endTime) {
        this(referent, title, startTime, endTime, "", 0);
    }

    /**
     * Full constructor
     * @param referent the referent who gives the lecture
     * @param title the lecture's title
     * @param startTime the time the lecture starts
     * @param endTime the time the lecture ends
     * @param description a description of the lectures's topics
     * @param educationalUnits the number of educational units the lecture covers
     * @throws IllegalArgumentException if any of the parameters was {@code null} or a contract of a field is violated.
     * See the documentation of the setter methods for details about those contracts
     */
    public Lecture(Referent referent, String title, LocalDateTime startTime, LocalDateTime endTime, String description, double educationalUnits) {
        Object[] params = {referent, title, startTime, endTime, description};
        Assert.noNullElements(params, "No parameter may be null, but at least one was: " + Arrays.toString(params));
        Assert.isTrue(timesAreValid(startTime, endTime), "Start time may not be after end time!");
        Assert.isTrue(educationalUnits >= 0d, "Educational units may not be negative");
        this.referent = referent;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.educationalUnits = educationalUnits;
    }

    /**
     * Default constructor just for JPA's sake. Not to be used from outside, hence {@code private}.
     */
    private Lecture() {}

    /**
     * @return the referent responsible for the lecture
     */
    public Referent getReferent() {
        return referent;
    }

    /**
     * @param referent the referent responsible for the lecture
     * @throws IllegalArgumentException if the referent was {@code null}
     */
    public void setReferent(Referent referent) {
        Assert.notNull(referent, "Referent may not be null!");
        this.referent = referent;
    }

    /**
     * @return the lecture's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the lecture's title
     * @throws IllegalArgumentException if the title was empty or {@code null}
     */
    public void setTitle(String title) {
        Assert.hasText(title, "Title may not be null nor empty, but was: " + title);
        this.title = title;
    }

    /**
     * @return the time the lecture starts
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the time the lecture starts
     * @throws IllegalArgumentException if the time was {@code null} or after the end time
     */
    public void setStartTime(LocalDateTime startTime) {
        Assert.notNull(startTime, "Start time may not be null!");

        // If an instance is re-initialized from database, JPA will use it's empty constructor and set all the fields
        // afterwards. Therefore end time may be null and we must check this before validating the start date
        if (endTime != null) {
            Assert.isTrue(timesAreValid(startTime, endTime), "Start time may not be after end time");
        }
        this.startTime = startTime;
    }

    /**
     * @return the time the lecture ends
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the time the lecture ends
     * @throws IllegalArgumentException if the time was {@code null} or before the end time
     */
    public void setEndTime(LocalDateTime endTime) {
        Assert.notNull(endTime, "End time may not be null!");

        // If an instance is re-initialized from database, JPA will use it's empty constructor and set all the fields
        // afterwards. Therefore end time may be null and we must check this before validating the end date
        if (startTime != null) {
            Assert.isTrue(timesAreValid(startTime, endTime), "Start time may not be after end time");
        }
        this.endTime = endTime;
    }

    /**
     * @return the lecture's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the lecture's description
     * @throws IllegalArgumentException if the description was {@code null}
     */
    public void setDescription(String description) {
        Assert.notNull(description, "Description may not be null!");
        this.description = description;
    }

    /**
     * @return the number of educational units the lecture covers
     */
    public double getEducationalUnits() {
        return educationalUnits;
    }

    /**
     * @param educationalUnits the number of educational units the lecture covers
     * @throws IllegalArgumentException if the number is not positive
     */
    public void setEducationalUnits(double educationalUnits) {
        Assert.isTrue(educationalUnits >= 0d, "Educational units may not be negative");
        this.educationalUnits = educationalUnits;
    }

    /**
     * @param startTime the earlier time
     * @param endTime the later time
     * @return {@code true} if the end time is not before the start time, {@code false} otherwise
     */
    private boolean timesAreValid(LocalDateTime startTime, LocalDateTime endTime) {
        return !endTime.isBefore(startTime);
    }

    // overridden from Object

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Lecture lecture = (Lecture) o;

        if (Double.compare(lecture.educationalUnits, educationalUnits) != 0) return false;
        if (!title.equals(lecture.title)) return false;
        if (!referent.equals(lecture.referent)) return false;
        if (!startTime.equals(lecture.startTime)) return false;
        if (!endTime.equals(lecture.endTime)) return false;
        return description.equals(lecture.description);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = title.hashCode();
        result = 31 * result + referent.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        result = 31 * result + description.hashCode();
        temp = Double.doubleToLongBits(educationalUnits);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "title='" + title + '\'' +
                ", referent=" + referent +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                ", educationalUnits=" + educationalUnits +
                '}';
    }
}
