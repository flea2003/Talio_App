package server.database;

import commons.Board;
import commons.Card;
import commons.Subtask;
import commons.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    java.util.List<Tag> findByCardsTagsValue(long cardValue);
    Optional<Tag> findAllById(long key);

    Optional<List<Tag>> findAllByBoardId(Long id);
}
