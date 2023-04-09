package server.database;

import commons.Card;
import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {
    /**
     * finds a card using its list id
     * @param id the id of the list
     * @return the card or null
     */
    Optional<List<Card>> findAllByListId(Long id);
}
