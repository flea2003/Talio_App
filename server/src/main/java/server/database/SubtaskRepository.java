package server.database;

import commons.Card;
import commons.Subtask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<Subtask, Long> {
}
