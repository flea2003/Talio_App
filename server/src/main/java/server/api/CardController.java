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
        return optList.orElse(null);
    }

    @GetMapping({"", "/"})
    public List<Card> getAll(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getById (@PathVariable long id){
        if(id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.findById(id).get());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Card> delete(@PathVariable long id){
        if(id < 0 || !repo.existsById(id))
            return ResponseEntity.badRequest().build();

        Card card = repo.findById(id).get();
        repo.delete(card);
        return ResponseEntity.ok(card);
    }

    @PostMapping(path =  {"", "/"})
    public ResponseEntity<Card>add(@RequestBody Card card){
        if(card == null || isNullOrEmpty(card.name)){
            return ResponseEntity.badRequest().build();
        }
        else{
            repo.save(card);
            return ResponseEntity.ok(card);
        }
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
