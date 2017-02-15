package de.naju.adebar.controller.forms;

/**
 * Model POJO for editing activists. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class EditActivistForm {
    public static String DATE_FORMAT = "dd.MM.yyy";

    private boolean activist;
    private boolean owningJuleica;
    private String juleicaExpiryDate;

    public EditActivistForm() {}

    public EditActivistForm(boolean activist, boolean owningJuleica, String juleicaExpiryDate) {
        this.activist = activist;
        this.owningJuleica = owningJuleica;
        this.juleicaExpiryDate = juleicaExpiryDate;
    }

    public boolean isActivist() {
        return activist;
    }

    public void setActivist(boolean activist) {
        this.activist = activist;
    }

    public boolean isOwningJuleica() {
        return owningJuleica;
    }

    public void setOwningJuleica(boolean owningJuleica) {
        this.owningJuleica = owningJuleica;
    }

    public String getJuleicaExpiryDate() {
        return juleicaExpiryDate;
    }

    public void setJuleicaExpiryDate(String juleicaExpiryDate) {
        this.juleicaExpiryDate = juleicaExpiryDate;
    }

    @Override
    public String toString() {
        return "EditActivistForm{" +
                "activist=" + activist +
                ", juleicaExpiryDate='" + juleicaExpiryDate + '\'' +
                '}';
    }
}
