package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    /**
     * link the card to its subtasks
     */
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL)
    @JsonManagedReference
    public java.util.List<Subtask> subtasks;

    public String description;

    public String name;
    public int numberInTheList;

    @ManyToOne
    @JoinColumn(name = "list_id")
    @JsonBackReference
    private List list;

    public Card(ArrayList<Subtask> subtasks, String description, String name, List list, int numberInTheList) {
        this.subtasks = subtasks;
        this.description = description;
        this.name = name;
        this.list = list;
        this.numberInTheList = numberInTheList;
    }

    public List getList() {
        return list;
    }

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

    @SuppressWarnings("unused")
    public Card() {}

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

    public void setNumberInTheList(int numberInTheList) {
        this.numberInTheList = numberInTheList;
    }

    public int getNumberInTheList() {
        return numberInTheList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
}