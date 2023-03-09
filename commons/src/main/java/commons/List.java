package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

@Entity
public class List {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name="card_id")
    public java.util.List<Card> cards;

    public String name;

    @ManyToOne
    @JoinColumn(name="board_id")
    public Board board;

    public java.util.List<Card>  getCards() {
        return cards;
    }

    @SuppressWarnings("unused")
    public List() {
        // for object mapper
    }

    public List(long id, ArrayList<Card>  cards, String name,Board board) {
        this.id = id;
        this.name = name;
        this.cards = cards;
        this.board=board;
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
}
