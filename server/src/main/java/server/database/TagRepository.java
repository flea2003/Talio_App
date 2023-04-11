package server.database;

import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * finds tags by their cards' id
     * @param cardValue the card id
     * @return a list of tags
     */
    java.util.List<Tag> findByCardsTagsId(long cardValue);

    /**
     * finds all tags by their id
     * @param key the tag's key
     * @return a tag
     */
    Optional<Tag> findAllById(long key);

    /**
     * finds all tags by their board's id
     * @param id the board's id
     * @return a list of tags
     */
    Optional<List<Tag>> findAllByBoardId(Long id);
}
