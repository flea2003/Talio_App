package server.api;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    CardRepository repo;

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * constructor
     * @param repo the card repository
     * @param messagingTemplate the messagingTemplate used to trigger the websocket
     */
    public CardController(CardRepository repo, SimpMessagingTemplate messagingTemplate) {
        this.repo = repo;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * gets all the cards from a specific list
     * @param list_id the list where the cards are at
     * @return a list of the cards
     */
    public  List<Card> getAllFromList(Long list_id){
        var optList = repo.findAllByListId(list_id);
        return optList.orElse(null);
    }

    /**
     * gets all the cards in the database
     * @return a list of the cards
     */
    @GetMapping({"", "/"})
    public List<Card> getAll(){
        return repo.findAll();
    }

    /**
     * gets a card by its id
     * @param id the id of the card
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Card> getById (@PathVariable Long id){
        if(id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * deletes a card
     * @param id the id of the card to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> delete(@PathVariable Long id){
        if(id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }

        Card card = repo.findById(id).get();
        repo.delete(card);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(card);
    }

    /**
     * adds a card
     * @param card the card to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path =  {"", "/"})
    public ResponseEntity<Card>add(@RequestBody Card card){
        System.out.println("CHECK");
        System.out.println(card);
        System.out.println("END CHECK");
        if(card == null || isNullOrEmpty(card.name)){
            return ResponseEntity.badRequest().build();
        }
        else{
            repo.save(card);
            messagingTemplate.convertAndSend("/topic/updates", true);
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

        repo.save(card);
        messagingTemplate.convertAndSend("/topic/updates", true);
        return ResponseEntity.ok(card);
    }

    /**
     * checks if a string is null or empty
     * @param s the string to be checked
     * @return if the string is null or empty
     */
    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
