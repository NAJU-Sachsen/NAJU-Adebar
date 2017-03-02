package de.naju.adebar.model.human;

import javax.persistence.Embeddable;
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

    @Override
    public String toString() {
        return "NabuMembership{" +
                "nabuMember=" + nabuMember +
                ", membershipNumber='" + membershipNumber + '\'' +
                '}';
    }
}
