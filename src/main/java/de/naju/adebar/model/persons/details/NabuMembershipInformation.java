package de.naju.adebar.model.persons.details;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import de.naju.adebar.documentation.infrastructure.JpaOnly;

/**
 * A person may be a club member of the NABU. For now we are just interested in its membership
 * number.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class NabuMembershipInformation {

  private static final String NO_NABU_MEMBER_MSG = "Not a NABU member";
  @Column(name = "nabuMember")
  private boolean nabuMember;
  @Column(name = "membershipNumber", nullable = true)
  private String membershipNumber;

  /**
   * Creates some arbitrary information for a person that may or may not be a NABU member. Even if
   * the person is a NABU member, its membership number is unknown
   *
   * @param nabuMember whether the person is a NABU member
   */
  public NabuMembershipInformation(boolean nabuMember) {
    this.nabuMember = nabuMember;
    this.membershipNumber = "";
  }

  /**
   * Creates a information for a person that is a NABU member
   *
   * @param membershipNumber the membership number
   */
  public NabuMembershipInformation(String membershipNumber) {
    this.nabuMember = true;
    this.membershipNumber = membershipNumber != null //
        ? membershipNumber //
        : "";
  }

  /**
   * Default constructor
   */
  @JpaOnly
  private NabuMembershipInformation() {}

  /**
   * @return whether the person is a NABU member
   */
  public boolean isNabuMember() {
    return nabuMember;
  }

  /**
   * @param nabuMember whether the person is a NABU member
   */
  protected void setNabuMember(boolean nabuMember) {
    this.nabuMember = nabuMember;
  }

  /**
   * @return the membership number
   */
  public String getMembershipNumber() {
    return membershipNumber;
  }

  /**
   * @param membershipNumber the membership number
   */
  protected void setMembershipNumber(String membershipNumber) {
    this.membershipNumber = membershipNumber;
  }

  public boolean hasMembershipNumber() {
    if (!isNabuMember()) {
      throw new IllegalStateException(NO_NABU_MEMBER_MSG);
    }
    return !membershipNumber.isEmpty();
  }

  /**
   * @return the membership status that is described by this instance. Will never be
   *         {@link MembershipStatus#UNKNOWN}
   */
  @Transient
  public MembershipStatus getStatus() {
    return nabuMember //
        ? MembershipStatus.IS_MEMBER //
        : MembershipStatus.NO_MEMBER;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NabuMembershipInformation that = (NabuMembershipInformation) o;

    return membershipNumber != null ? membershipNumber.equals(that.membershipNumber)
        : that.membershipNumber == null;
  }

  @Override
  public int hashCode() {
    return membershipNumber != null ? membershipNumber.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "NabuMembership{" + "membershipNumber='" + membershipNumber + '\'' + '}';
  }

  public enum MembershipStatus {
    IS_MEMBER, NO_MEMBER, UNKNOWN
  }
}
