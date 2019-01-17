package com.example.intelligentlamp.classes;


import java.io.Serializable;

public class Music implements Serializable {
//    private String name;
//
//    private int imageId;
//
//    private int time;
//
//    private long id;
//
//    public Music(String name, int imageId) {
//        this.name = name;
//        this.imageId = imageId;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setImageId(int imageId) {
//        this.imageId = imageId;
//    }
//
//    public int getImageId() {
//        return imageId;
//    }

    private long id;
    private long albumId;
    private String title;
    private String artist;
    private long duration;
    private long size;
    private String url;
    private String album;
    private int isMusic;
    private boolean isFavorite = false;

    public void setId(long id){
        this.id = id;
    }

    public long getId(){return this.id;}

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){return this.title;}

    public void setArtist(String artist){
        this.artist = artist;
    }

    public String getArtist(){return this.artist;}

    public void setDuration(long duration){this.duration = duration;}

    public long getDuration(){return this.duration;}

    public void setSize(long size){this.size = size;}

    public long getSize(){return this.size;}

    public void setUrl(String url){this.url = url;}

    public String getUrl(){return this.url;}

    public void setAlbum(String album){this.album = album;}

    public String getAlbum(){return this.album;}

    public void setAlbumId(long albumId){this.albumId = albumId;}

    public long getAlbumId(){return this.albumId;}

    public void setFavorite(boolean favorite){this.isFavorite =favorite;}

    public boolean getFavorite(){return isFavorite;}
}
