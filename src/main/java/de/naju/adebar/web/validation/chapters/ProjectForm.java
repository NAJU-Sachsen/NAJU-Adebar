package de.naju.adebar.web.validation.chapters;

import javax.validation.constraints.NotNull;

/**
 * Model POJO for projects. The fields are set by Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class ProjectForm {

  public static final String DATE_FORMAT = "dd.MM.yyyy";

  @NotNull
  private String name;
  private String start;
  private String end;

  @NotNull
  private String personInCharge;
  private long localGroupId;

  public ProjectForm(String name, String start, String end, String personInCharge,
      long localGroupId) {
    this.name = name;
    this.start = start;
    this.end = end;
    this.personInCharge = personInCharge;
    this.localGroupId = localGroupId;
  }

  public ProjectForm() {
    this.name = "";
    this.personInCharge = "";
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getPersonInCharge() {
    return personInCharge;
  }

  public void setPersonInCharge(String personInCharge) {
    this.personInCharge = personInCharge;
  }

  public long getLocalGroupId() {
    return localGroupId;
  }

  public void setLocalGroupId(long localGroupId) {
    this.localGroupId = localGroupId;
  }

  public boolean hasStart() {
    return start != null && !start.isEmpty();
  }

  public boolean hasEnd() {
    return end != null && !start.isEmpty();
  }

  public boolean hasPersonInCharge() {
    return personInCharge != null && !personInCharge.isEmpty();
  }
}
