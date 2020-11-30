package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    //Vérification de la présence du nom
    Artist findByName(String name);

    //Recherche paginée d'artistes par le nom sans prise en compte des majuscules
    Page<Artist> findByNameContainsIgnoreCase(String name, Pageable pageable);

}
