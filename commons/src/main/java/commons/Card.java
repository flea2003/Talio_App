package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.ArrayList;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String description;

    public String name;
    public int numberInTheList;

    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference
    private List list;

    /**
     * link the card to its tags
     */
    @ManyToMany
    @JoinTable(name = "card_tag",
        joinColumns = @JoinColumn(name = "card_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonBackReference(value = "defaultReference2")
    private java.util.List<Tag> tags;

    /**
     * link the card to its subtasks
     */
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonManagedReference
    public java.util.List<Subtask> subtasks;

    /**
     * constructor
     * @param description the description of the card
     * @param name the name of the card
     * @param list the list the card is in
     * @param numberInTheList the number of the card in the list
     */
    public Card(String description, String name, List list, int numberInTheList) {
        subtasks = new ArrayList<>();
        tags = new ArrayList<>();
        this.description = description;
        this.name = name;
        this.list = list;
        this.numberInTheList = numberInTheList;
    }

    /**
     * constructor used for testing
     */
    @SuppressWarnings("unused")
    public Card() {}

    /**
     * constructor used for testing
     * @param id the id of the card
     * @param name the name of the card
     * @param id the id of the card
     */
    public Card(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * gets the list the card is in
     * @return the list the card is in
     */
    public List getList() {
        return list;
    }

    /**
     * sets the list the card is in
     * @param list the list to be set
     */
    public void setList(List list) {
        this.list = list;
    }

    /**
     * a getter for the list of subtasks
     * @return the subtasks
     */
    public java.util.List<Subtask> getSubtasks() {
        return subtasks;
    }

    /**
     * a setter for the list of subtasks
     * @param subtasks the tasks of a card
     */
    public void setSubtasks(java.util.List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    /**
     * checks if two cards are equal
     * @param obj the object to be checked
     * @return if the cards are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * creates a hashcode for the card
     * @return the hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * creates a string representation of the card
     * @return the string
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    /**
     * sets the number of the card in the list
     * @param numberInTheList the number of the card in the list to be set
     */
    public void setNumberInTheList(int numberInTheList) {
        this.numberInTheList = numberInTheList;
    }

    /**
     * gets the number of the card in the list
     * @return the number of the card in the list
     */
    public int getNumberInTheList() {
        return numberInTheList;
    }

    /**
     * gets the description of the card
     * @return the description of the card
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of the card
     * @param description the description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the name of the card
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter for name
     * @return the string name
     */
    public String getName() {
        return this.name;
    }

    /**
     * getter for id
     * @return the long id
     */
    public long getId() {
        return id;
    }

    /**
     * getter for tags
     * @return the list of tags
     */
    public java.util.List<Tag> getTags() {
        return tags;
    }

    /**
     * setter for tags
     * @param tags the list of a card's tags
     */
    public void setTags(java.util.List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * adds a new subtask to the list of subtasks
     * @param subtask
     */
    public void addSubtask(Subtask subtask){
        subtasks.add(subtask);
    }
}
