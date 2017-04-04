package de.naju.adebar.controller.forms.chapter;

import de.naju.adebar.model.human.PersonId;

/**
 * Model POJO for projects. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class ProjectForm {
    public final static String DATE_FORMAT = "DD.MM.yyyy";

    private String name;
    private String start, end;
    private PersonId personInCharge;

    public ProjectForm() {}

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

    public PersonId getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(PersonId personInCharge) {
        this.personInCharge = personInCharge;
    }
}
