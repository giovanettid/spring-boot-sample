package com.giovanetti;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonneRepository extends CrudRepository<Personne,Long> {

    List<Personne> findPersonneByNom(String nom);

}
