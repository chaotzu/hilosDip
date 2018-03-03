package org.netzd.hilosdip;

/**
 * Created by Alumno12 on 24/02/18.
 */

public class Video {
    private String id=null;
    private String title=null;
    private String year=null;
    private String type=null;
    private String poster=null;

    public Video() {
    }

    public Video(String id, String title, String year, String type, String poster) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.type = type;
        this.poster = poster;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}


