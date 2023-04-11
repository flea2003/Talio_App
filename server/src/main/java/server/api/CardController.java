package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.CardService;
import server.services.Pair;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

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
     * gets a card by its id
     * @param id the id of the card
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById (@PathVariable long id){

        Card card = null;
        try {
            card = cardService.getCardById(id);
        }
        catch (Exception e){
            ResponseEntity.badRequest().build();
        }

        if(id < 0 || card == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(card);
    }

    private Map<Pair<Object, Card>, Consumer<Card>> listeners = new ConcurrentHashMap<>();

    /**
     * Long Polling Method
     * @return res
     */
    @PostMapping("/longPoll")
    public DeferredResult<ResponseEntity<Card>> getUpdates(@RequestBody Card card){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Card>>(3000L, noContent);

        var key = new Object();
        System.out.println(listeners.size());
        listeners.put(new Pair<>(key, card), q -> {
            res.setResult(ResponseEntity.ok(q));
        });
        res.onCompletion(() -> {
            listeners.remove(new Pair<>(key, card));
        });
        return res;
    }

    public void activateListeners(Card card){
        listeners.forEach((k, l) -> {
            if(k.getB().id == card.id) {
                  l.accept(card);
            }
        });
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
        activateListeners(card);
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
            return ResponseEntity.badRequest().build();
        }
        else{
            cardService.saveCard(card);
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
        activateListeners(card);
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

    /**
     * gets all the cards in the database in the right order
     * @return a list of the cards
     */
    @GetMapping({"", "/"})
    public List<Card> getAll(){
        return cardService.getAllCards();
    }
}
