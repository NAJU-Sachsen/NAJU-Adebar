package de.naju.adebar.model.chapter;

import de.naju.adebar.model.human.Activist;
import de.naju.adebar.util.Validation;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstraction of a board of directors
 * @author Rico Bergmann
 */
@Entity
public class Board {
    @Id @GeneratedValue private long id;
    @OneToOne private Activist chairman;
    private String email;
    @ManyToMany private List<Activist> members;

    /**
     * Minimalistic constructor. The only thing each board needs is a chairman
     * @param chairman the board's chairman
     */
    public Board(Activist chairman) {
        this(chairman, null);
    }

    /**
     * Full constructor
     * @param chairman the board's chairman
     * @param email central email address for the whole board, may be {@code null}
     * @throws IllegalArgumentException if chairman is {@code null}
     * @throws IllegalArgumentException if the email was given (i.e. not {@code null}) but invalid (i.e. not a valid email address)
     */
    public Board(Activist chairman, String email) {
        Assert.notNull(chairman, "Chairman may not be null");
        if (email != null && !Validation.isEmail(email)) {
            throw new IllegalArgumentException("Not a valid email: " + email);
        }
        this.chairman = chairman;
        this.email = email;
        this.members = new LinkedList<>();
    }

    private Board() {}

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
    public Activist getChairman() {
        return chairman;
    }

    /**
     * @param chairman the board's chairman
     * @throws IllegalArgumentException if the chairman is {@code null}
     */
    public void setChairman(Activist chairman) {
        Assert.notNull(chairman, "Chairman may not be null");
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
    public Iterable<Activist> getMembers() {
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
    protected void setMembers(List<Activist> members) {
        this.members = members;
    }

    // query methods

    /**
     * @param activist the activist to check
     * @return {@code true} if the activist is the board's current chairman, or {@code false} otherwise
     */
    private boolean isChairman(Activist activist) {
        return chairman.equals(activist);
    }

    // modification operations

    /**
     * @param activist the activist to add to the board
     * @throws IllegalArgumentException if the activist is {@code null} or already a member of the board
     */
    public void addBoardMember(Activist activist) {
        Assert.notNull(activist, "Activist to add may not be null");
        if (isBoardMember(activist)) {
            throw new IllegalArgumentException("Activist is already board member: " + activist);
        }
        members.add(activist);
    }

    /**
     * @param activist the activist to check
     * @return {@code true} if the activist is a board-member or {@code false} otherwise
     */
    public boolean isBoardMember(Activist activist) {
        return members.contains(activist);
    }

    /**
     * @param activist the activist to remove from the board
     * @throws IllegalArgumentException if the activist was not a member of the board
     * @throws IllegalStateException if the activist is the board's chairman
     */
    public void removeBoardMember(Activist activist) {
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        if (!chairman.equals(board.chairman)) return false;
        if (email != null ? !email.equals(board.email) : board.email != null) return false;
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
        return "Board{" +
                "id=" + id +
                ", chairman=" + chairman +
                ", email='" + email + '\'' +
                ", members=" + members +
                '}';
    }
}
