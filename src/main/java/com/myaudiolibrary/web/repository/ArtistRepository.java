package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ArtistRepository extends JpaRepository<Artist, Integer> {

    Artist findByArtistId(Integer integer);

}
