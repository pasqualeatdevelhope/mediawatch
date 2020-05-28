package it.pasqualecavallo.videowatch.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pasqualecavallo.videowatch.model.Season;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Integer>{

}
