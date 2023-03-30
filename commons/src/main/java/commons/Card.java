package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
     * constructor
     * @param description the description of the card
     * @param name the name of the card
     * @param list the list the card is in
     * @param numberInTheList the number of the card in the list
     */
    public Card(String description, String name, List list, int numberInTheList) {
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
}