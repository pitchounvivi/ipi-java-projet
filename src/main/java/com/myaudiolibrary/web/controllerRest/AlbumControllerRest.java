package com.myaudiolibrary.web.controllerRest;

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
public class AlbumControllerRest {

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

        //Album dont le nom existe déjà, toutefois il faut rafraichir la page pour voir que l'enregistrement n'a pas été pris en compte (le problème serait réglé avec une redirection Thymeleaf)
        //Cette vérification évite le duplicage d'album en base
        if (albumRepository.findByTitle(album.getTitle()) != null){
            throw new EntityExistsException("L'album " + album.getTitle() + " existe déjà. Enregistrement non pris en compte. Rafraichissez la page.");
        }

        //Vérifie la présence d'un titre
        //Mais même problème que précédemment, il faut rafraichir la page
        if(album.getTitle() == null){
            throw new IllegalArgumentException("L'Album ne possède pas de titre. Enregistrement non pris en compte. Rafraichissez la page.");
        }

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
