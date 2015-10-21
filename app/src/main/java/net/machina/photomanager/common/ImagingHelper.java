package net.machina.photomanager.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;

/**
 * Created by machina on 15.10.2015.
 */
public class ImagingHelper {

    public static Bitmap makeScaledBitmapFromRaw(byte[] _rawArray) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 10, bitmap.getHeight() / 10, false);
        return scaledBitmap;
    }

    public static Bitmap makeScaledBitmapFromRaw(byte[] _rawArray, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return scaledBitmap;
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }

    public static Bitmap getRotatedBitmapFromRaw(byte[] _rawArray, float rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static byte[] getRawRotatedFromRaw(byte[] _rawArray, float rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap bitmap = BitmapFactory.decodeByteArray(_rawArray, 0, _rawArray.length);
        Bitmap rotBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        rotBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    public static Bitmap getRotated(Bitmap bitmap, float rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static byte[] getRawFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }

}
