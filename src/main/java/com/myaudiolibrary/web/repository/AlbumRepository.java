package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

    //Vérification de la présence du nom
    Album findByTitle(String title);

}
