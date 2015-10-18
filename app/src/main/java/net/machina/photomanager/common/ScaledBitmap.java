package net.machina.photomanager.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by machina on 15.10.2015.
 */
public class ScaledBitmap {

    public static Bitmap makeFromRaw(byte[] _rawArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
        return scaledBitmap;
    }

    public static Bitmap makeFromRaw(byte[] _rawArray, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return scaledBitmap;
    }

}
