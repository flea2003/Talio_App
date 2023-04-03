package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;


@Entity
public class List {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    @JsonManagedReference
    public java.util.List<Card> cards;

    public String name;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    public Board board;

    public int numberInTheBoard;

    /**
     * constructor used for testing
     */
    @SuppressWarnings("unused")
    public List() {
        // for object mapper
    }

    /**
     * constructor
     * @param name the name of the list
     */
    public List(String name){
        this.name=name;
    }

    /**
     * constructor
     * @param id the id of the list
     * @param cards the cards of the list
     * @param name the name of the list
     * @param board the board the list is in
     */
    public List(long id, ArrayList<Card>  cards, String name, Board board) {
        this.id = id;
        this.name = name;
        this.cards = cards;
        this.board=board;
    }

    /**
     * constructor
     * @param name the name of the list
     * @param board the board the list is in
     */
    public List(String name,Board board){
        this.name = name;
        this.board = board;
    }

    /**
     * constructor
     * @param cards the cards of the list
     * @param name the name of the list
     * @param board the board the list is in
     * @param numberInTheBoard the number of the list in the board
     */
    public List(java.util.List<Card> cards, String name, Board board, int numberInTheBoard) {
        this.cards = cards;
        this.name = name;
        this.board = board;
        this.numberInTheBoard = numberInTheBoard;
    }

    /**
     * gets the number of the list in the board
     * @return the number of the list in the board
     */
    public int getNumberInTheBoard() {
        return numberInTheBoard;
    }

    /**
     * sets the number of the list in the board
     * @param numberInTheBoard the number to be set
     */
    public void setNumberInTheBoard(int numberInTheBoard) {
        this.numberInTheBoard = numberInTheBoard;
    }

    /**
     * checks if two lists are equal
     * @param obj the object to be checked
     * @return if the two lists are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * getter for cards
     * @return the list of cards
     */
    public java.util.List<Card> getCards() {
        return cards;
    }

    /**
     * creates a hashcode based on the list's fields
     * @return the hashcode created
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, cards, name, board);
    }

    /**
     * to string method of a list
     * @return the string created
     */
    @Override
    public String toString() {
        return "List{" +
                "id=" + id +
                ", cards=" + cards +
                ", name='" + name + '\'' +
                ", board=" + board +
                '}';
    }

    /**
     * sets the cards of the list
     * @param cards the cards to be set
     */
    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * getter for the List's name
     * @return the string name
     */
    public String getName() {
        return this.name;
    }

    /**
     * setter for the List's name
     * @param name the name of the list
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the id of the list
     * @return the id of the list
     */
    public Long getID() {
        return id;
    }

    /**
     * sets the board the list is in
     * @param board the board to be set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * gets the board the list is in
     * @return the board the list is in
     */
    public Board getBoard() {
        return board;
    }


}
