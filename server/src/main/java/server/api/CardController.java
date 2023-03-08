package server.api;

import commons.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.CardRepository;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    CardRepository repo;

    public  List<Card> getAllFromList(long list_id){
        var optList = repo.findAllByList_id(list_id);
        if(optList.isEmpty())
            return null;
        else return optList.get();
    }

    @GetMapping({"", "/"})
    public List getAll(){
        return repo.findAll();
    }

    @GetMapping("/card/{id}")
    public ResponseEntity<Card> getById (@PathVariable long id){
        if(id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @DeleteMapping("/card/{id}")
    public ResponseEntity<Card> delete(@PathVariable long id){
        if(id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        Card card = repo.findById(id).get();
        repo.delete(card);
        return ResponseEntity.ok(card);
    }

    @PostMapping("/card")
    public ResponseEntity<Card> add(@RequestBody Card card){
        if (card == null || isNullOrEmpty(card.name) )
           return ResponseEntity.badRequest().build();

        repo.save(card);
        return ResponseEntity.ok(card);
    }

    @PutMapping("/card")
    public ResponseEntity<Card> update(@RequestBody Card card){
        if (card == null || isNullOrEmpty(card.name) )
            return ResponseEntity.badRequest().build();

        repo.save(card);

        return ResponseEntity.ok(card);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
