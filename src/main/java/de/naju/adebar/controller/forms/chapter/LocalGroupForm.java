package de.naju.adebar.controller.forms.chapter;

/**
 * Model POJO for local groups. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class LocalGroupForm {
    private String name;
    private String street;
    private String zip;
    private String city;

    public LocalGroupForm(String name, String street, String zip, String city) {
        this.name = name;
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public LocalGroupForm() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
