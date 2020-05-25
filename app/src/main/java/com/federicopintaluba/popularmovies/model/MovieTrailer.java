package com.federicopintaluba.popularmovies.model;

import android.os.Parcel;

public class MovieTrailer {

    private String id;
    private String key;
    private String name;
    private String type;

    public MovieTrailer() {
    }

    public MovieTrailer(String id, String key, String name, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.type = type;
    }

    protected MovieTrailer(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
