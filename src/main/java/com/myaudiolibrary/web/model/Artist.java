package com.myaudiolibrary.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @Column(name = "ArtistId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer artistId;

    @Column(name = "Name")
    private String name;

    @OneToMany(mappedBy = "artist",fetch = FetchType.EAGER)
    @JsonIgnoreProperties("artist")
    private Set<Album> albums = new HashSet<>();


    public Integer getArtistId() {
        return artistId;
    }

    public void setArtistId(Integer artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
