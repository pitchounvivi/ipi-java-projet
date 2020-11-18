package com.myaudiolibrary.web.model;

import javax.persistence.*;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @JoinColumn(name = "AlbumId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AlbumId;

    @Column
    private String Title;

    @ManyToOne
    @JoinColumn(name = "ArtistId", nullable = false)
    private Artist artist;


    public Long getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(Long albumId) {
        AlbumId = albumId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
