package de.naju.adebar.model.events;

import javax.persistence.*;

/**
 * Meta-data needed for the participating persons
 * @author Rico Bergmann
 */
@Entity
public class ParticipationInfo {

    @Id @GeneratedValue private long id;
    private boolean acknowledged;
    private boolean participationFeePayed;
    private boolean registrationFormReceived;

    /**
     * Only a default constructor is needed. All participation info looks the same at the beginning.
     */
    ParticipationInfo() {}

    /**
     * @return the ID (= primary key) of the dataset
     */
    public long getId() {
        return id;
    }

    /**
     * @return whether the participation was acknowledged
     */
    public boolean isAcknowledged() {
        return acknowledged;
    }

    /**
     * @param acknowledged whether the participation was acknowledged
     */
    public void setAcknowledged(boolean acknowledged) {
        this.acknowledged = acknowledged;
    }

    /**
     * @return whether the participation fee was payed
     */
    public boolean isParticipationFeePayed() {
        return participationFeePayed;
    }

    /**
     * @param participationFeePayed whether the participation fee was payed
     */
    public void setParticipationFeePayed(boolean participationFeePayed) {
        this.participationFeePayed = participationFeePayed;
    }

    /**
     * @return whether the registration form (featuring signature, etc.) was received
     */
    public boolean isRegistrationFormReceived() {
        return registrationFormReceived;
    }

    /**
     * @param registrationFormReceived whether the registration form (featuring signature, etc.) was received
     */
    public void setRegistrationFormReceived(boolean registrationFormReceived) {
        this.registrationFormReceived = registrationFormReceived;
    }

    /**
     * @param id the ID (= primary key) of the dataset
     */
    protected void setId(long id) {
        this.id = id;
    }

    // overridden from object

    @Override
    public String toString() {
        return "ParticipationInfo{" +
                "acknowledged=" + acknowledged +
                ", participationFeePayed=" + participationFeePayed +
                ", registrationFormReceived=" + registrationFormReceived +
                '}';
    }
}
