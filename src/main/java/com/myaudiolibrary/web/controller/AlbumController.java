package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;


    //Création d'un album
    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public Album createAlbum(
            @RequestBody Album album
    ){
        if (albumRepository.findByTitle(album.getTitle()) != null){
            throw new EntityExistsException("L'album : " + album.getTitle() + " existe déjà.");
        }

        //if(album.getTitle().isEmpty(){throw new EntityNotFoundException("L'album ne possède pas de titre");}


        return albumRepository.save(album);
    }


    //Suppression d'un album
    @RequestMapping(
            value = "/{albumId}",
            method = RequestMethod.DELETE
    )
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAlbum(
            @PathVariable("albumId") Integer albumId
    ){
        if (!albumRepository.existsById(albumId)){
            throw new EntityNotFoundException("L'album numéro " + albumId + " non trouvé");
        }

        albumRepository.deleteById(albumId);
    }

}
