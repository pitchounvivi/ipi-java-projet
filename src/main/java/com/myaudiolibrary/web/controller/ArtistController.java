package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(value = "/artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;


    //Recherche d'un artiste par son id
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
            throw new EntityNotFoundException("L'artist avec l'id : " + id + " non trouvé.");
        }

        return unArtist.get();
    }


    //Recherche d'un artist par son nom





    //Pagination liste de tous les artistes
    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"page","size","sortProperty","sortDirection"}
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<Artist> listPageArtists(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Page<Artist> listPageArtists = artistRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));
        return listPageArtists;
    }


    //Création d'un artist
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public Artist createArtist(
            @RequestBody Artist artist
    ){
        if (artistRepository.findByName(artist.getName()) != null){
            throw new EntityExistsException("L'artist " + artist.getName() + " n'est pas.");
        }

        return artistRepository.save(artist);
    }


    //Modification d'un artist
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Artist modifyArtist(
            @PathVariable (value = "id") Integer id,
            @RequestBody Artist artist
    ){
        if (!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste numéro " + id + " non trouvé");
        }

        return artistRepository.save(artist);
    }


}
