package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * finds a list using its id
     * @param id the id of the list
     * @return the list or null
     */
    Optional<List<Card>> findAllByListId(Long id);

}
