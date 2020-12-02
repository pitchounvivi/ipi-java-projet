package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/artists")
public class DetailController {

    @Autowired
    private ArtistRepository artistRepository;

    //Affichage d'un artist
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET
    )
    public String getArtistById(
            final ModelMap model,
            @PathVariable Integer id
    ){
        Optional<Artist> artistOptional = artistRepository.findById(id);

        model.put("artist", artistOptional.get());
        return "detailArtist";
    }



    //Liste des artists
    @RequestMapping(
            params = {"page","size","sortProperty","sortDirection"},
            method = RequestMethod.GET)
    public String listDeTousLesArtists(
            final ModelMap model,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "matricule") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Page<Artist> pageArtists = artistRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));

        //gérer 404

        model.put("artists", pageArtists);

        //Affichage du numéro des éléments présents dans la page
        model.put("start", page * size +1);
        model.put("end", (page) * size + pageArtists.getNumberOfElements());

        //Les boutons et l'affichage de la page en cours
        model.put("pageNumber", page);//C'est pour afficher la page en cours
        model.put("previousPage", page-1);
        model.put("nextPage", page+1);

        return "listeArtists";
    }






}
