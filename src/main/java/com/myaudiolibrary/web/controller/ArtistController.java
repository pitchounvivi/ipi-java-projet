package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = "application/json"
    )
    @ResponseStatus(HttpStatus.OK)
    public Artist findById(
            @PathVariable(value = "id") Integer id
    ){
        Optional<Artist> unArtist = artistRepository.findById(id);

        if (!unArtist.isPresent()){
            throw new EntityNotFoundException("L'artist : " + id + "non trouvé");
        }

        return unArtist.get();
    }





}
