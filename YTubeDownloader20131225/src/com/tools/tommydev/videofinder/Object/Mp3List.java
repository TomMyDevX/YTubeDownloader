package com.tools.tommydev.videofinder.Object;

/**
 * Created by TomMy on 8/5/13.
 */
public class Mp3List {
    String id;
    String title;
    String time;
    String thumbnail;
    int duration;
    String description;
    String filepath;
    String small_thumbnail;

    public String getSmall_thumbnail() {
        return small_thumbnail;
    }

    public void setSmall_thumbnail(String small_thumbnail) {
        this.small_thumbnail = small_thumbnail;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
