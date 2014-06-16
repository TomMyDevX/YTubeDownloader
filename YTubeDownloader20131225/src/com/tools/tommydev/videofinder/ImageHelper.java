package com.tools.tommydev.videofinder;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by TomMy on 9/5/13.
 */
public class ImageHelper {
    @SuppressWarnings("deprecation")
	public static Drawable resize(Drawable image,int width) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, width, false);
        return new BitmapDrawable(bitmapResized);
    }
}
