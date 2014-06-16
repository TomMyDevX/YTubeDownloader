package com.tools.tommydev.videofinder.Object;

/**
 * Created by TomMy on 8/8/13.
 */



public class Meta {
    public String num;
    public String type;
    public String ext;
    public String type_id;

    public Meta(String num, String ext, String type, String type_id) {
        this.num = num;
        this.ext = ext;
        this.type = type;
        this.type_id=type_id;
    }
}
