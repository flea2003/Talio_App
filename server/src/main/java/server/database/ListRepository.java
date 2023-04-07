package server.database;

import commons.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ListRepository extends JpaRepository<List,Long> {
    /**
     * finds a list using its board id
     * @param boardId the id of the board
     * @return the list or null
     */
    Optional<java.util.List<List>> findAllByBoardId(long boardId);
}
