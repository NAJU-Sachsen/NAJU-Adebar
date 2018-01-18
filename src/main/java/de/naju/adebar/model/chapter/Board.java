package de.naju.adebar.model.chapter;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import org.springframework.util.Assert;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.util.Validation;

/**
 * Abstraction of a board of directors
 *
 * @author Rico Bergmann
 */
@Entity(name = "board")
public class Board {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private long id;

  @OneToOne
  private Person chairman;

  @Column(name = "email")
  private String email;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(inverseJoinColumns = @JoinColumn(name = "memberId"))
  private List<Person> members;

  /**
   * Minimalistic constructor. The only thing each board needs is a chairman
   *
   * @param chairman the board's chairman
   */
  public Board(Person chairman) {
    this(chairman, null);
  }

  /**
   * Full constructor
   *
   * @param chairman the board's chairman
   * @param email central email address for the whole board, may be {@code null}
   * @throws IllegalArgumentException if the chairman is no activist or {@code null}
   * @throws IllegalArgumentException if the email was given (i.e. not {@code null}) but invalid
   *         (i.e. not a valid email address)
   */
  public Board(Person chairman, String email) {
    Assert.notNull(chairman, "Chairman may not be null");
    Assert.isTrue(chairman.isActivist(), "Chairman has to be an activist");
    if (email != null && !Validation.isEmail(email)) {
      throw new IllegalArgumentException("Not a valid email: " + email);
    }

    this.chairman = chairman;
    this.email = email;
    this.members = new LinkedList<>();
    members.add(chairman);
  }

  /**
   * Default constructor just for JPA's sake
   */
  protected Board() {}

  // basic getter and setter

  /**
   * @return the board's ID (= primary key)
   */
  public long getId() {
    return id;
  }

  /**
   * @return the board's chairman
   */
  public Person getChairman() {
    return chairman;
  }

  /**
   * @param chairman the board's chairman
   * @throws IllegalArgumentException if the chairman is no activist or {@code null}
   */
  public void setChairman(Person chairman) {
    Assert.notNull(chairman, "Chairman may not be null");
    Assert.isTrue(chairman.isActivist(), "Chairman has to be an activist");
    this.chairman = chairman;

    if (!isBoardMember(chairman)) {
      addBoardMember(chairman);
    }
  }

  /**
   * @return the board's email if specified, otherwise {@code null}
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the board's email, may be {@code null}
   * @throws IllegalArgumentException if the email is not a valid email address
   */
  public void setEmail(String email) {
    if (email != null && !Validation.isEmail(email)) {
      throw new IllegalArgumentException("Not a valid email: " + email);
    }
    this.email = email;
  }

  /**
   * @return the boards' members
   */
  public Iterable<Person> getMembers() {
    return members;
  }

  /**
   * @param id the board's primary key
   */
  protected void setId(long id) {
    this.id = id;
  }

  /**
   * @param members the boards' members
   */
  protected void setMembers(List<Person> members) {
    this.members = members;
  }

  // query methods

  /**
   * @param activist the activist to check
   * @return {@code true} if the activist is the board's current chairman, or {@code false}
   *         otherwise
   */
  private boolean isChairman(Person activist) {
    return chairman.equals(activist);
  }

  // modification operations

  /**
   * @param person the activist to add to the board
   * @throws IllegalArgumentException if the person is no activist, {@code null} or already a member
   *         of the board
   */
  public void addBoardMember(Person person) {
    Assert.notNull(person, "Activist to add may not be null");
    Assert.isTrue(person.isActivist(), "New board member has to be an activist");
    if (isBoardMember(person)) {
      throw new IllegalArgumentException("Activist is already board member: " + person);
    }
    members.add(person);
  }

  /**
   * @param activist the activist to check
   * @return {@code true} if the activist is a board-member or {@code false} otherwise
   */
  public boolean isBoardMember(Person activist) {
    return members.contains(activist);
  }

  /**
   * @param activist the activist to remove from the board
   * @throws IllegalArgumentException if the activist was not a member of the board
   * @throws IllegalStateException if the activist is the board's chairman
   */
  public void removeBoardMember(Person activist) {
    if (isChairman(activist)) {
      throw new IllegalStateException("Chairman may not be removed!");
    } else if (!isBoardMember(activist)) {
      throw new IllegalArgumentException("Activist is no board member: " + activist);
    }
    members.remove(activist);
  }

  // overridden from Object

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Board board = (Board) o;

    if (board.id != 0 && this.id != 0) {
      return board.id == this.id;
    }

    if (!chairman.equals(board.chairman))
      return false;
    if (email != null ? !email.equals(board.email) : board.email != null)
      return false;
    return members.equals(board.members);
  }

  @Override
  public int hashCode() {
    int result = chairman.hashCode();
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + members.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "Board{" + "id=" + id + ", chairman=" + chairman + ", email='" + email + '\''
        + ", members=" + members + '}';
  }
}
