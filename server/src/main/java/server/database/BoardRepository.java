package server.database;

import commons.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    /**
     * finds a board using its key
     * @param key the key of the board
     * @return the board or null
     */
    Optional<Board> findBoardByKey(String key);

}