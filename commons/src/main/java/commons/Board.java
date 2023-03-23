package commons;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String key;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name="board_id")
    public java.util.List<List> lists;

    public String name;


    @PrePersist
    public void onCreate() {
        this.key = UUID.randomUUID().toString();
    }
    @SuppressWarnings("unused")
    public Board(int id, ArrayList<Board> boards, String a) {}

    public Board(long id, ArrayList<List> lists, String name) {
        this.id = id;
        this.lists = lists;
        this.name = name;
    }

    @SuppressWarnings("unused")
    public Board() {

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

    public void setLists(java.util.List<List> lists) {
        this.lists = lists;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for the string name
     * @return the board's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for lists
     * @return the List type of lists
     */
    public java.util.List getLists() {
        return this.lists;
    }
}