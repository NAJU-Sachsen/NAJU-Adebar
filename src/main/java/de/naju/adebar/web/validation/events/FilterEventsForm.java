package de.naju.adebar.web.validation.events;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import de.naju.adebar.web.validation.core.AddressForm;

/**
 * Model POJO for filtering events. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class FilterEventsForm extends AddressForm {

  public static final String DATE_TIME_FORMAT = "dd.MM.yyy HH:mm";
  public static final CurrencyUnit CURRENCY_UNIT = Monetary.getCurrency("EUR");

  private String name;

  private String startFilterType;
  private String start;

  private String endFilterType;
  private String end;

  private String participantsLimitFilterType;
  private int participantsLimit;

  private boolean participantsAgeFilter;
  private int participantsAge;

  private String feeFilterType;
  private String internalFee;
  private String externalFee;

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

  public boolean getParticipantsAgeFilter() {
    return participantsAgeFilter;
  }

  public void setParticipantsAgeFilter(boolean participantsAgeFilter) {
    this.participantsAgeFilter = participantsAgeFilter;
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

  public String getInternalFee() {
    return internalFee;
  }

  public void setInternalFee(String internalFee) {
    this.internalFee = internalFee;
  }

  public String getExternalFee() {
    return externalFee;
  }

  public void setExternalFee(String externalFee) {
    this.externalFee = externalFee;
  }

  public boolean hasInternalFee() {
    return internalFee != null && !internalFee.isEmpty();
  }

  public boolean hasExternalFee() {
    return externalFee != null && !externalFee.isEmpty();
  }

  @Override
  public String toString() {
    return "FilterEventsForm{" + "name='" + name + '\'' + ", startFilterType='" + startFilterType
        + '\'' + ", start='" + start + '\'' + ", endFilterType='" + endFilterType + '\'' + ", end='"
        + end + '\'' + ", participantsLimitFilterType='" + participantsLimitFilterType + '\''
        + ", participantsLimit=" + participantsLimit + ", participantsAgeFilter="
        + participantsAgeFilter + ", participantsAge=" + participantsAge + ", feeFilterType='"
        + feeFilterType + '\'' + ", internalFee='" + internalFee + '\'' + ", externalFee='"
        + externalFee + '\'' + ", street='" + getStreet() + '\'' + ", zip='" + getZip() + '\''
        + ", city='" + getCity() + '\'' + '}';
  }
}
