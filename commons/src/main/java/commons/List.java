package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class List {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL)
    @JsonManagedReference
    public java.util.List<Card> cards;

    public String name;

    @ManyToOne
    @JoinColumn(name="board_id")
    public Board board;

    public int numberInTheBoard;

    @SuppressWarnings("unused")
    public List() {
        // for object mapper
    }

    public List(String name){
        this.name=name;
    }

    public List(long id, ArrayList<Card>  cards, String name, Board board) {
        this.id = id;
        this.name = name;
        this.cards = cards;
        this.board=board;
    }

    public int getNumberInTheBoard() {
        return numberInTheBoard;
    }

    public void setNumberInTheBoard(int numberInTheBoard) {
        this.numberInTheBoard = numberInTheBoard;
    }

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

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    /**
     * getter for the List's name
     * @return the string name
     */
    public String getName() { return this.name; }

    /**
     * setter for the List's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public Object getID() {
        return id;
    }

}
