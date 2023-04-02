package commons;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Subtask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id; // the task's id

    private String name; // the task's title

    private String description; // the task's description

    private int numberInTheCard; // which task in the card

    /**
     * to link the subtask to a specific card
     */

    @ManyToOne
    @JoinColumn(name = "card_id")
    @JsonBackReference
    private Card card;

    public Integer completed;

    public Subtask() {}

    /**
     * A subtask's constructor
     * @param id the task's id long
     * @param name the task's string name
     * @param description its string description
     * @param numberInTheCard its order in the card
     * @param card the card it is linked to
     */
    public Subtask(long id, String name, String description, int numberInTheCard, Card card, int completed) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.numberInTheCard = numberInTheCard;
        this.card = card;
        this.completed = 0;
    }

    public Subtask(String name, String description, int numberInTheCard, Card card, int completed) {
        this.name = name;
        this.numberInTheCard = numberInTheCard;
        this.card = card;
        this.description = description;
        this.completed = 0;
    }

    /**
     * a getter for the id
     * @return the long id
     */
    public long getId() {
        return id;
    }

    /**
     * a getter for name
     * @return the string name
     */
    public String getName() {
        return name;
    }

    /**
     * a getter for description
     * @return the string description
     */
    public String getDescription() {
        return description;
    }

    /**
     * a getter for the position number
     * @return the int numberInTheCard
     */
    public int getNumberInTheCard() {
        return numberInTheCard;
    }

    /**
     * a getter for card
     * @return the Card of its origin
     */
    public Card getCard() {
        return card;
    }

    /**
     * a setter for id
     * @param id the task's id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * a setter for name
     * @param name the string name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * a setter for description
     * @param description the string of its description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * a setter for numberInTheCard
     * @param numberInTheCard its position in the card
     */
    public void setNumberInTheCard(int numberInTheCard) {
        this.numberInTheCard = numberInTheCard;
    }

    /**
     * a setter for card
     * @param card the parent card
     */
    public void setCard(Card card) {
        this.card = card;
    }

    /**
     * compares an object to the subtask
     * @param obj the object compared to
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * the hashcode created from the task's attributes
     * @return a hashcode
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * to string representation of a Subtask
     * @return the string of the object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

    public void switchState(){
        completed = 1 - completed;
    }

    public int isCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }
}
