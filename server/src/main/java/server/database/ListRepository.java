package server.database;

import commons.Card;
import commons.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListRepository extends JpaRepository<List,Long> {
}
