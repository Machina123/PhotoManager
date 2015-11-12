package net.machina.photomanager.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageTransform {

    public static final float[] initial = {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0
    };

    public static final float[] red = {
            2, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 1, 0
    };

    public static final float[] green = {
            0, 0, 0, 0, 0,
            0, 2, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 0, 1, 0
    };

    public static final float[] blue = {
            0, 0, 0, 0, 0,
            0, 0, 0, 0, 0,
            0, 0, 2, 0, 0,
            0, 0, 0, 1, 0
    };

    public static final float[] negative = {
            -1, 0, 0, 0, 0,
            0, -1, 0, 0, 0,
            0, 0, -1, 0, 0,
            0, 0, 0, 1, 0
    };

    public static Bitmap applyColorMatrix(Bitmap src, ColorMatrix matrix) {
        Bitmap dest = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));
        canvas.drawBitmap(src, 0, 0, paint);
        return dest;
    }

    /**
     * Zmiana jasności i kontrastu obrazka
     *
     * @param bt Jasność (-255..255)
     * @param ct Kontrast (0..10)
     */
    public static Bitmap setContrastBrightness(Bitmap src, float bt, float ct) {

        final float[] contrastBrightness = {
                ct, 0, 0, 0, bt,
                0, ct, 0, 0, bt,
                0, 0, ct, 0, bt,
                0, 0, 0, 1, 0
        };

        ColorMatrix cbMatrix = new ColorMatrix(contrastBrightness);
        Bitmap ret = applyColorMatrix(src, cbMatrix);
        return ret;
    }
}
