package commons;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name="board_id")
    public java.util.List<List> lists;

    public String name;


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
}