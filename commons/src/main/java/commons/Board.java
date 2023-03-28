package commons;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @JsonManagedReference
    public java.util.List<List> lists;

    public String name;


    @PrePersist
    public void onCreate() {
        this.key = UUID.randomUUID().toString();
    }

    public Board(long id, ArrayList<List> lists, String name) {
        this.id = id;
        this.lists = lists;
        this.name = name;
    }

    @SuppressWarnings("unused")
    public Board() {

    }

    public Board(String name) {
        this.name = name;
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

    public long getId(){return id;}

    public String getKey(){return key;}

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
    public java.util.List<List> getLists() {
        return this.lists;
    }
}