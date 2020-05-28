package it.pasqualecavallo.videowatch.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.pasqualecavallo.videowatch.model.Serie;


@Repository
public interface SerieRepository extends JpaRepository<Serie, Integer>{

	@Query("Select s from Serie s where s.name like %:name%")
	public List<Serie> findByNameContaining(String name);
	
}
