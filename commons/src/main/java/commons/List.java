package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;

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
    public java.util.List<Card>  getCards() {
        return cards;
    }

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

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


    public void setCards(ArrayList<Card>  cards) {
        this.cards = cards;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getID() {
        return id;
    }

}
