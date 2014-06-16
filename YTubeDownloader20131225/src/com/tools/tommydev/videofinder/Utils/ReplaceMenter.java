package com.tools.tommydev.videofinder.Utils;

/**
 * Created by TomMy on 9/23/13.
 */
public class ReplaceMenter {

        public static String Replace(String data){
            return  data.replaceAll("[^!\\w./\\s+]", "");
        }


}
