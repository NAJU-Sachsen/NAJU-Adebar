package de.naju.adebar.model.human;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @author Rico Bergmann
 */
@Embeddable
public class NabuMembership implements Serializable {
    private boolean nabuMember;
    private String membershipNumber;

    public NabuMembership() {
        this.nabuMember = false;
        this.membershipNumber = new String();
    }

    public NabuMembership(String membershipNumber) {
        this.nabuMember = true;
        this.membershipNumber = membershipNumber;
    }

    public boolean isNabuMember() {
        return nabuMember;
    }

    public void setNabuMember(boolean nabuMember) {
        if (!nabuMember) {
            membershipNumber = null;
        } else if (nabuMember && !this.nabuMember) {
            membershipNumber = new String();
        }
        this.nabuMember = nabuMember;
    }

    public String getMembershipNumber() {
        return membershipNumber;
    }

    public void setMembershipNumber(String membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    @Transient
    public boolean hasMembershipNumber() {
        if (!isNabuMember()) {
            throw new IllegalStateException("Not a NABU member");
        }
        return membershipNumber != null && !membershipNumber.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NabuMembership that = (NabuMembership) o;

        if (nabuMember != that.nabuMember) return false;
        return membershipNumber != null ? membershipNumber.equals(that.membershipNumber) : that.membershipNumber == null;
    }

    @Override
    public int hashCode() {
        int result = (nabuMember ? 1 : 0);
        result = 31 * result + (membershipNumber != null ? membershipNumber.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NabuMembership{" +
                "nabuMember=" + nabuMember +
                ", membershipNumber='" + membershipNumber + '\'' +
                '}';
    }
}
