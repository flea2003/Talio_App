package server.database;

import commons.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
    /**
     * finds a subtask using its id
     * @param id the id of the subtask
     * @return the subtask or null
     */
    Optional<List<Subtask>> findAllById(long id);

}
