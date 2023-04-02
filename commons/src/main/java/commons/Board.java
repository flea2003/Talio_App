package commons;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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

    /**
     * a unique key is created for the board
     */
    @PrePersist
    public void onCreate() {
        this.key = UUID.randomUUID().toString();
    }

    /**
     * constructor
     * @param id the id of the board
     * @param lists the lists of the board
     * @param name the name of the board
     */
    public Board(long id, ArrayList<List> lists, String name) {
        this.id = id;
        this.lists = lists;
        this.name = name;
    }

    /**
     * constructor used for testing
     */
    @SuppressWarnings("unused")
    public Board() {

    }

    /**
     * constructor
     * @param name the name of the board
     */
    public Board(String name) {
        this.name = name;
    }

    /**
     * check if two boards are equal
     * @param obj the object to be checked
     * @return if the boards are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * creates a hashcode for the board
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * creates a string representation of the board
     * @return the string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * sets the list of the board
     * @param lists the lists to be set
     */
    public void setLists(java.util.List<List> lists) {
        this.lists = lists;
    }

    /**
     * sets the name of the board
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * gets the id of the board
     * @return the id of the board
     */
    public long getId(){
        return id;
    }

    /**
     * gets the key of the board
     * @return the key of the board
     */
    public String getKey(){
        return key;
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
    public java.util.List<List> getLists() {
        return this.lists;
    }
}