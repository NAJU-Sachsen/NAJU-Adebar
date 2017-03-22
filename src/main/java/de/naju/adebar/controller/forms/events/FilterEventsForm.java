package de.naju.adebar.controller.forms.events;

/**
 * Model POJO for filtering events. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class FilterEventsForm {
    private String name;
    private String startFilterType, start;
    private String endFilterType, end;
    private String participantsLimitFilterType;
    private int participantsLimit;
    private boolean participantsAgeFilterType;
    private int participantsAge;
    private String feeFilterType, fee;
    private String street, zip, city;

    public FilterEventsForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartFilterType() {
        return startFilterType;
    }

    public void setStartFilterType(String startFilterType) {
        this.startFilterType = startFilterType;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEndFilterType() {
        return endFilterType;
    }

    public void setEndFilterType(String endFilterType) {
        this.endFilterType = endFilterType;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getParticipantsLimitFilterType() {
        return participantsLimitFilterType;
    }

    public void setParticipantsLimitFilterType(String participantsLimitFilterType) {
        this.participantsLimitFilterType = participantsLimitFilterType;
    }

    public int getParticipantsLimit() {
        return participantsLimit;
    }

    public void setParticipantsLimit(int participantsLimit) {
        this.participantsLimit = participantsLimit;
    }

    public boolean getParticipantsAgeFilterType() {
        return participantsAgeFilterType;
    }

    public void setParticipantsAgeFilterType(boolean participantsAgeFilterType) {
        this.participantsAgeFilterType = participantsAgeFilterType;
    }

    public int getParticipantsAge() {
        return participantsAge;
    }

    public void setParticipantsAge(int participantsAge) {
        this.participantsAge = participantsAge;
    }

    public String getFeeFilterType() {
        return feeFilterType;
    }

    public void setFeeFilterType(String feeFilterType) {
        this.feeFilterType = feeFilterType;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
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

    @Override
    public String toString() {
        return "FilterEventsForm{" +
                "name='" + name + '\'' +
                ", startFilterType='" + startFilterType + '\'' +
                ", start='" + start + '\'' +
                ", endFilterType='" + endFilterType + '\'' +
                ", end='" + end + '\'' +
                ", participantsLimitFilterType='" + participantsLimitFilterType + '\'' +
                ", participantsLimit='" + participantsLimit + '\'' +
                ", participantsAgeFilterType='" + participantsAgeFilterType + '\'' +
                ", participantsAge='" + participantsAge + '\'' +
                ", feeFilterType='" + feeFilterType + '\'' +
                ", fee='" + fee + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
