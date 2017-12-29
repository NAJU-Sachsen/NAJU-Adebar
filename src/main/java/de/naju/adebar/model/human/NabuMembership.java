package de.naju.adebar.model.human;

import javax.persistence.Column;
import javax.persistence.Embeddable;

// TODO rename to NabuMembershipInfo once compile errors are gone
// TODO make it immutable
// TODO add boolean nabuMember

/**
 * A person may be a club member of the NABU. For now we are just interested in its membership
 * number.
 *
 * @author Rico Bergmann
 */
@Embeddable
public class NabuMembership {

  @Column(name = "membershipNumber")
  private String membershipNumber;

  /**
   * Full constructor
   *
   * @param membershipNumber the membership number
   */
  public NabuMembership(String membershipNumber) {
    this.membershipNumber = membershipNumber;
  }

  /**
   * Default constructor
   */
  public NabuMembership() {}

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

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    NabuMembership that = (NabuMembership) o;

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
}
