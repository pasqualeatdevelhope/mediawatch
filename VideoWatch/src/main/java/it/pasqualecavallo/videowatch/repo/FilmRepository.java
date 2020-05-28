package it.pasqualecavallo.videowatch.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.pasqualecavallo.videowatch.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer>{

	@Query("Select f from Film f where f.title like %:title%")
	public List<Film> findByNameContaining(String title);

}
