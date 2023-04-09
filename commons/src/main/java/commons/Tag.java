package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.awt.Color;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id; // the tag's id

    private String name; // the name of a tag
    private Color color; // the color code of a tag
    // ways to initialize:
    // 1: Color red = new Color(255, 0, 0);
    // 2: Color red = Color.decode("#FF0000");

    /**
     * to link the tag to a specific board
     */
    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    /**
     * to link the tag to multiple cards
     * a tag has a list of cards
     */
    @ManyToMany
    @JoinTable(name = "card_tag",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "card_id"))
//    @JsonManagedReference
    public java.util.List<Card> cards;

    /**
     * constructs a tag
     * @param id the long id
     * @param name the string name
     * @param color the color code
     * @param board the board it connects to
     */
    public Tag(long id, String name, Color color, Board board) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.board = board;
    }

    /**
     * getter for an id
     * @return the long id
     */
    public long getId() {
        return id;
    }

    /**
     * a setter for id
     * @param id a long
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getter for name
     * @return the String name
     */
    public String getName() {
        return name;
    }

    /**
     * a setter for name
     * @param name a String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for a color
     * @return a Color object
     */
    public Color getColor() {
        return color;
    }

    /**
     * a setter for color
     * @param color a Color object
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * getter for the task's Board
     * @return the Board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * a setter for board
     * @param board the task's Board
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * getter for cards
     * @return the list of cards of a tag
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * a setter for cards
     * @param cards the list of cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * compares an object to the tag
     * @param obj the object compared to
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * the hashcode created from the tag's attributes
     * @return a hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
