package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ListService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/lists")
public class ListController {
    @Autowired
    ListService listService;

    /**
     * gets a list by its id
     * @param id the list id
     * @return a response (bad request or ok)
     */
    @GetMapping("/{id}")
    public ResponseEntity<commons.List> getById(@PathVariable("id") long id) {
        commons.List list = listService.getListById(id);
        if (id < 0 || list == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * adds a list
     * @param list the list to be added
     * @return a response (bad request or ok)
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<commons.List> add(@RequestBody commons.List list) {
        if (list.name == null || list.name.strip().length() == 0) {
            return ResponseEntity.badRequest().build();
        }
        listService.saveList(list);
        return ResponseEntity.ok(list);
    }

    /**
     * deletes a list
     * @param id the id of the list to be deleted
     * @return a response (bad request or ok)
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        commons.List list = listService.getListById(id);
        if (id < 0 || list == null) {
            return ResponseEntity.badRequest().build();
        }
        listService.deleteList(Objects.requireNonNull(list));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * updates a list
     * @param list the list to be updated
     * @return a response (bad request or ok)
     */
    @PostMapping("/update")
    public ResponseEntity<commons.List> updateList(@RequestBody commons.List list){
        System.out.println(list.getBoard());
        listService.saveList(list);
        return ResponseEntity.ok(list);
    }

    /**
     * gets all the lists in the database in the right order
     * @return a list of the lists
     */
    @GetMapping(path = { "", "/" })
    public List<commons.List> getAll() {
        return listService.getAllLists();
    }
}
