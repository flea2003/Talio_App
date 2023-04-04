package server.api;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.CardService;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    CardService cardService;

    /**
     * gets all the cards from a specific list
     * @param list_id the list where the cards are at
     * @return a list of the cards
     */
    public  List<Card> getAllFromList(long list_id){
        return cardService.getCardsByListId(list_id);
    }

    /**
     * gets all the cards in the database
     * @return a list of the cards
     */
    @GetMapping({"", "/"})
    public List<Card> getAll(){
        return cardService.getAllCards();
    }

    /**
     * gets a card by its id
     * @param id the id of the card
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById (@PathVariable long id){
        Card card = cardService.getCardById(id);
        if(id < 0 || card == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(card);
    }

    /**
     * deletes a card
     * @param id the id of the card to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> delete(@PathVariable long id){
        Card card = cardService.getCardById(id);
        if(id < 0 || card == null) {
            return ResponseEntity.badRequest().build();
        }

        cardService.deleteCard(card);
        return ResponseEntity.ok(card);
    }

    /**
     * adds a card
     * @param card the card to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path =  {"", "/"})
    public ResponseEntity<Card> add(@RequestBody Card card){
        if(card == null || isNullOrEmpty(card.name)){
            System.out.println("A CARD WAS UPDATED  feic");
            return ResponseEntity.badRequest().build();
        }
        else{
            cardService.saveCard(card);
            System.out.println("A CARD WAS UPDATED");
            return ResponseEntity.ok(card);
        }
    }

    /**
     * updates a card
     * @param card the card to be updated
     * @return a response (bad request or ok)
     */
    @PutMapping("/card")
    public ResponseEntity<Card> update(@RequestBody Card card){
        if (card == null || isNullOrEmpty(card.name) ) {
            return ResponseEntity.badRequest().build();
        }

        cardService.saveCard(card);
        return ResponseEntity.ok(card);
    }

    /**
     * checks if a string is null or empty
     * @param s the string to be checked
     * @return if the string is null or empty
     */
    static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
