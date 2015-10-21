package net.machina.photomanager.common;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.widget.ImageView;

/**
 * Created by machina on 15.10.2015.
 */
public class ImageThumbnail extends ImageView {
    private Bitmap mImage;
    private Point mCenterPoint;
    private int mWidth, mHeight;
    private Rect desiredRect = new Rect();
    private Canvas mCanvas;
    protected Context mContext;

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
        this.invalidate();
    }

    public Point getmCenterPoint() {
        return mCenterPoint;
    }

    public void setmCenterPoint(Point mCenterPoint) {
        this.mCenterPoint = mCenterPoint;
        this.invalidate();
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
        this.invalidate();
    }

    public ImageThumbnail(Context context, Bitmap image, Point center, int width, int height) {
        super(context);
        this.mContext = context;
        this.mImage = image;
        this.mCenterPoint = center;
        this.mWidth = width;
        this.mHeight = height;
        this.setImageBitmap(image);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            if (canvas == null) return;
            //this.mCanvas = canvas;
            super.onDraw(canvas);
            this.setTop(mCenterPoint.y - (mHeight / 2));
            this.setBottom(mCenterPoint.y + (mHeight / 2));
            this.setLeft(mCenterPoint.x - (mWidth / 2));
            this.setRight(mCenterPoint.x + (mWidth / 2));
            //this.draw(canvas);
        } catch(Exception ex) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this.mContext);
            builder .setTitle("Błąd")
                    .setMessage(ex.getMessage())
                    .setNeutralButton("OK", null)
                    .setCancelable(false)
                    .show();

        }
    }

}
