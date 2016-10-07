package com.dekatruong.testdb.dto;

/**
 * Created by Deka on 06/10/2016.
 */

public class Feed {
    private long id; //To do: DB problem, thinking

    public String title;
    public String subtitle;

    public Feed(long id){
        this.id = id;
    }

    public Feed(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
