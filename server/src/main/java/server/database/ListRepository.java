package server.database;

import commons.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ListRepository extends JpaRepository<List,Long> {
}
