package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

@Controller
@RequestMapping("/artists")
public class DetailController {

    @Autowired
    private ArtistRepository artistRepository;

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


}
