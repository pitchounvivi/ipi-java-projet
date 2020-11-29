package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    //Recherche d'un ou des artistes dans la bdd par son nom
    //- pour utiliser avec le site web du prof utiliser => params = {"name","page","size","sortProperty","sortDirection"},
    //- pour utiliser avec seulement le http://localhost:5366/artists?name= => params = {"name"},
    @RequestMapping(
            params = {"name","page","size","sortProperty","sortDirection"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public Page<Artist> searchByName(
            @RequestParam(value = "name") String nameSearch,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Pageable pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> artistsByName = artistRepository.findByNameContainsIgnoreCase(nameSearch, pageRequest);
        return artistsByName;
    }


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
        if (page<0){
            throw new IllegalArgumentException("la page doit être positif ou null");//erreur 400
        }
        if (size<=0 || size>=50){
            throw new IllegalArgumentException("la taille doit être compris entre 0 et 50");//erreur 400
        }
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("Le paramètre sortDirection doit être ASC ou DESC");
        }

        Page<Artist> listPageArtists = artistRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));
        return listPageArtists;
    }


    //Création d'un artiste
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
            throw new EntityExistsException("L'artist " + artist.getName() + " existe déjà.");
        }

        return artistRepository.save(artist);
    }


    //Modification d'un artiste
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


    //Suppression d'un artiste
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArtist(
            @PathVariable (value = "id") Integer id
    ){
        if (!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste numéro " + id + " non trouvé");
        }

        artistRepository.deleteById(id);
    }

}
