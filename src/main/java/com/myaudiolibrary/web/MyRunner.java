package com.myaudiolibrary.web;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    AlbumRepository albumRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("!!!!!!!!!!!!!!!!!!!! essai Runner !!!!!!!!!!!!!!!!!!!!!!!!!!");

        Iterable<Artist> artists = artistRepository.findAll();

        for (Artist artist : artists){
            Set<Album> albums = artist.getAlbums();
            System.out.println("**** Artist **** : " + artist.getName());

            for (Album album : albums){
                print("Album = " + album.getTitle());
            }
        }

    }

    public static void print(Object o){
        System.out.println(o);
    }
}
