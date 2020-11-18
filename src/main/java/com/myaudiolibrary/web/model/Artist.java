package com.myaudiolibrary.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "ArtistId", nullable = false)
    private Long ArtistId;

    @Column
    private String Name;

    @OneToMany(mappedBy = "artist")
    @JsonIgnoreProperties("artist")
    private Set<Album> albums;

    public Long getArtistId() {
        return ArtistId;
    }

    public void setArtistId(Long artistId) {
        ArtistId = artistId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }
}
