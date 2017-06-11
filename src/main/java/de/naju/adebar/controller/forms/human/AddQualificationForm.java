package de.naju.adebar.controller.forms.human;

import javax.validation.constraints.NotNull;

/**
 * Model POJO for adding qualifications. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class AddQualificationForm {

    public enum AddType {NEW, EXISTING}

    @NotNull private String addType;
    private String qualification;
    private String name;
    private String description;

    public AddQualificationForm() {}

    public String getAddType() {
        return addType;
    }

    public void setAddType(String addType) {
        this.addType = addType;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "AddQualificationForm{" +
                "addType='" + addType + '\'' +
                ", qualification='" + qualification + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
