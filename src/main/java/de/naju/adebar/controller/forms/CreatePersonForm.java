package de.naju.adebar.controller.forms;

import java.util.List;

/**
 * Model POJO for new persons. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class CreatePersonForm extends EditPersonForm {

    private boolean activist, referent, nabuMember;
    private String juleicaExpiryDate;
    private List<String> qualifications;
    private String nabuNumber;

    public CreatePersonForm() {
    }

    public boolean isActivist() {
        return activist;
    }

    public void setActivist(boolean activist) {
        this.activist = activist;
    }

    public boolean isReferent() {
        return referent;
    }

    public void setReferent(boolean referent) {
        this.referent = referent;
    }

    public boolean isNabuMember() {
        return nabuMember;
    }

    public void setNabuMember(boolean nabuMember) {
        this.nabuMember = nabuMember;
    }

    public boolean hasJuleicaExpiryDate() {
        return juleicaExpiryDate != null && !juleicaExpiryDate.isEmpty();
    }

    public String getJuleicaExpiryDate() {
        return juleicaExpiryDate;
    }

    public void setJuleicaExpiryDate(String juleicaExpiryDate) {
        this.juleicaExpiryDate = juleicaExpiryDate;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    public String getNabuNumber() {
        return nabuNumber;
    }

    public void setNabuNumber(String nabuNumber) {
        this.nabuNumber = nabuNumber;
    }

    @Override
    public String toString() {
        return "CreatePersonForm{" +
                super.toString() +
                " activist=" + activist +
                ", referent=" + referent +
                ", nabuMember=" + nabuMember +
                ", juleicaExpiryDate='" + juleicaExpiryDate + '\'' +
                ", qualifications=" + qualifications +
                ", nabuNumber='" + nabuNumber + '\'' +
                '}';
    }
}
