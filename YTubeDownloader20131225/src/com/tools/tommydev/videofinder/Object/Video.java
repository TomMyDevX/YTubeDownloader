package com.tools.tommydev.videofinder.Object;

/**
 * Created by TomMy on 8/8/13.
 */
public class Video {
    public String ext = "";
    public String type = "";
    public String url = "";
    public String type_id="";
    public String error_type="";
    public Video(String ext, String type, String url, String type_id,String error_type) {
        this.ext = ext;
        this.type = type;
        this.url = url;
        this.type_id=type_id;
        this.error_type=error_type;
    }
}
