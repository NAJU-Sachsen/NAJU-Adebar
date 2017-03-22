package de.naju.adebar.controller.forms.events;

/**
 * Model POJO for events. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class EventForm {
    public final static String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";

    private String name;
    private String startTime, endTime;
    private String participantsLimit;
    private String participantsAge;
    private String participationFee;
    private String street, zip, city;

    public EventForm() {
    }

    public EventForm(String name, String startTime, String endTime, String participantsLimit, String participantsAge, String participationFee, String street, String zip, String city) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participantsLimit = participantsLimit;
        this.participantsAge = participantsAge;
        this.participationFee = participationFee;
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(String participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public String getParticipantsAge() {
        return participantsAge;
    }

    public void setParticipantsAge(String participantsAge) {
        this.participantsAge = participantsAge;
    }

    public String getParticipationFee() {
        return participationFee;
    }

    public void setParticipationFee(String participationFee) {
        this.participationFee = participationFee;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean hasParticipantsLimit() {
        return participantsLimit != null && !participantsLimit.isEmpty();
    }

    public boolean hasParticipantsAge() {
        return participantsAge != null && !participantsAge.isEmpty();
    }

    public boolean hasParticipationFee() {
        return participationFee != null && !participationFee.isEmpty();
    }

    @Override
    public String toString() {
        return "EventForm{" +
                "name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", participantsLimit='" + participantsLimit + '\'' +
                ", participantsAge='" + participantsAge + '\'' +
                ", participationFee='" + participationFee + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
