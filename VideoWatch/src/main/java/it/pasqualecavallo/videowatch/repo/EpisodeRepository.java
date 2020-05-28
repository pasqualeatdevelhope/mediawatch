package it.pasqualecavallo.videowatch.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.pasqualecavallo.videowatch.model.Episode;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Integer>{

}
