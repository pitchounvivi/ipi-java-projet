package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping("/artists")
public class DetailController {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    //Affichage d'un artiste
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET
    )
    public String getArtistById(
            final ModelMap model,
            @PathVariable Integer id
    ){
        Optional<Artist> artistOptional = artistRepository.findById(id);

        if(artistOptional.isEmpty()){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé !");
        }

        model.put("artist", artistOptional.get());
        return "detailArtist";
    }


    //Liste des artistes recherché par leur nom
    @RequestMapping(
            params = "name",
            method = RequestMethod.GET)
    public String searchByName(
            final ModelMap model,
            @RequestParam(value = "name") String nameSearch,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Pageable pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> artistsByName = artistRepository.findByNameContainsIgnoreCase(nameSearch, pageRequest);

        //gérer 404
        if (artistsByName.isEmpty()){
            throw new EntityNotFoundException("L'artiste de nom " + nameSearch + " n'existe pas !");
        }

        model.put("artists", artistsByName);

        //Affichage du numéro des éléments présents dans la page
        model.put("start", page * size +1);
        model.put("end", (page) * size + artistsByName.getNumberOfElements());

        //Les boutons et l'affichage de la page en cours
        model.put("pageNumber", page +1);//C'est pour afficher la page en cours
        //->(plus 1 pour ne pas afficher 0, mais moi ça me perturbe d'avoir 0 dans l'url et d'avoir la page affichée indiquant 1)

        model.put("previousPage", page-1);
        model.put("nextPage", page+1);

        //Les paramètres pour l'affichage correct dans le cas d'une recherche par nom avec une grosse liste
        model.put("size", size);
        model.put("sortProperty", sortProperty);
        model.put("sortDirection", sortDirection);


        return "listeArtists";
    }


    //Liste des artistes
    @RequestMapping(
            params = {"page","size","sortProperty","sortDirection"},
            method = RequestMethod.GET)
    public String listDeTousLesArtists(
            final ModelMap model,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ){
        Page<Artist> pageArtists = artistRepository.findAll(PageRequest.of(page, size,
                Sort.Direction.fromString(sortDirection), sortProperty));

        //gestion des erreurs
        if (page<0){
            throw new IllegalArgumentException("la page doit être positif ou null");//erreur 400
        }
        if (page>pageArtists.getTotalPages()){
            throw new IllegalArgumentException("le numéro de la page doit être inférieur");//erreur 400
        }
        if (size<=0 || size>=50){
            throw new IllegalArgumentException("la taille doit être compris entre 0 et 50");//erreur 400
        }
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)){
            throw new IllegalArgumentException("Le paramètre sortDirection doit être ASC ou DESC");
        }
        if (!sortProperty.equalsIgnoreCase("name")){
            throw new IllegalArgumentException("Le propriété demandée n'est pas la bonne");//erreur 400
        }


        model.put("artists", pageArtists);

        //Affichage du numéro des éléments présents dans la page
        model.put("start", page * size +1);
        model.put("end", (page) * size + pageArtists.getNumberOfElements());

        //Les boutons et l'affichage de la page en cours
        model.put("pageNumber", page +1);//C'est pour afficher la page en cours (plus 1 pour pas afficher 0)
        model.put("previousPage", page-1);
        model.put("nextPage", page+1);

        //Les paramètres pour l'affichage correct dans le cas d'une recherche par nom avec une grosse liste
        model.put("size", size);
        model.put("sortProperty", sortProperty);
        model.put("sortDirection", sortDirection);

        return "listeArtists";
    }




    /////////////////////////Zone de gestion : création, modification, suppression
    //Accéder au formulaire pour créer un artiste
    @RequestMapping(
            value = "/new",
            method = RequestMethod.GET
    )
    public String newArtist(
            final ModelMap model
    ){
        model.put("artist", new Artist());
        return ("detailArtist");
    }


    //Création et mise à jour d'un nouvel artiste
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView createOrSaveArtist(
            Artist artist
    ){
        artist = artistRepository.save(artist);
        return new RedirectView("/artists/" + artist.getId());
    }


    //Suppression d'un artist
    @RequestMapping(
            value = "/{id}/delete",
            method = RequestMethod.GET
    )
    public RedirectView deleteArtist(
            @PathVariable Integer id
    ){
        artistRepository.deleteById(id);
        return new RedirectView("/artists?page=0&size=10&sortProperty=name&sortDirection=ASC");
    }


    //Création d'un nouvel album
    @RequestMapping(
            value = "/{id}/albums",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView newAlbum(
            Album album,
            @PathVariable Integer id
    ){
        Album albumToArtist = new Album();
        albumToArtist.setTitle(album.getTitle());
        albumToArtist.setArtist(artistRepository.getOne(id));
        albumRepository.save(albumToArtist);
        return new RedirectView("/artists/" + id);
    }


    //Suppression d'un album
    @RequestMapping(
            value = "/{id}/albums/{idAlbum}/delete",
            method = RequestMethod.GET
    )
    public RedirectView deleteAlbum(
            @PathVariable Integer id,
            @PathVariable Integer idAlbum
    ){
        albumRepository.deleteById(idAlbum);
        return new RedirectView("/artists/" + id);
    }


    //Modifier d'un album
    @RequestMapping(
            value = "/{id}/albums/{idAlbum}/post",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public RedirectView modifyAlbum(
            Album album,
            @PathVariable Integer id,
            @PathVariable Integer idAlbum
    ){
        Optional<Album> albumToModify = albumRepository.findById(idAlbum);
        albumToModify.get().setTitle(album.getTitle());
        albumRepository.save(albumToModify.get());
        return new RedirectView("/artists/" + id);
    }

}