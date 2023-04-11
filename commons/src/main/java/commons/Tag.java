package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id; // the tag's id

    private String name; // the name of a tag

    private int green;
    private int blue;
    private int red;

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
    @JsonBackReference(value = "defaultReference2")
    public List<Card> cards;

    /**
     * constructs a tag
     * @param id the long id
     * @param name the string name
     * @param board the board it connects to
     */
    public Tag(long id, String name, Board board) {
        this.id = id;
        this.name = name;
        this.board = board;
    }

    /**
     * constructs a tag
     * @param name the string name
     * @param green int value for Color
     * @param blue int value for Color
     * @param red int value for Color
     * @param board the board it connects to
     * @param cards the cards of the tag
     */
    public Tag(String name, int green, int blue, int red, Board board, ArrayList<Card> cards) {
        this.name = name;
        this.green = green;
        this.blue = blue;
        this.red = red;
        this.board = board;
        this.cards = cards;
    }

    /**
     * constructs a tag
     * @param name the string name
     * @param green int value for Color
     * @param blue int value for Color
     * @param red int value for Color
     */
    public Tag(String name, int green, int blue, int red) {
        this.name = name;
        this.green = green;
        this.blue = blue;
        this.red = red;
    }


    /**
     * default constructor
     */
    @SuppressWarnings("unused")
    public Tag() {}


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
     * getter for green
     * @return the green int in RGB
     */
    public int getGreen(){
        return green;
    }

    /**
     * getter for blue
     * @return the blue int in RGB
     */
    public int getBlue(){
        return blue;
    }

    /**
     * getter for red
     * @return the red int in RGB
     */
    public int getRed(){
        return red;
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

    /**
     * setter for green
     * @param green an int for Color
     */
    public void setGreen(int green) {
        this.green = green;
    }

    /**
     * setter for blue
     * @param blue an int for Color
     */
    public void setBlue(int blue) {
        this.blue = blue;
    }

    /**
     * setter for red
     * @param red an int for Color
     */
    public void setRed(int red) {
        this.red = red;
    }

    /**
     * to string representation of a tag
     *
     * @return a string of its variables
     */
    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", green=" + green +
                ", blue=" + blue +
                ", red=" + red +
                ", board=" + board +
                ", cards=" + cards +
                '}';
    }
}
