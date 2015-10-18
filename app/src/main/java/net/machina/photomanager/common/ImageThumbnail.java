package net.machina.photomanager.common;

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

    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public Point getmCenterPoint() {
        return mCenterPoint;
    }

    public void setmCenterPoint(Point mCenterPoint) {
        this.mCenterPoint = mCenterPoint;
    }

    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public ImageThumbnail(Context context, Bitmap image, Point center, int width, int height) {
        super(context);
        this.mImage = image;
        this.mCenterPoint = center;
        this.mWidth = width;
        this.mHeight = height;
        this.setImageBitmap(image);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas == null) return;
        this.mCanvas = canvas;
        super.onDraw(mCanvas);
        this.setX(mCenterPoint.x - (mWidth / 2));
        this.setY(mCenterPoint.y - (mHeight / 2));
        this.draw(canvas);
    }

    public void redraw() {
        this.setTop(mCenterPoint.y - (mHeight / 2));
        this.setBottom(mCenterPoint.y + (mHeight / 2));
        this.setLeft(mCenterPoint.x - (mWidth / 2));
        this.setRight(mCenterPoint.x + (mWidth / 2));
        this.draw(mCanvas);
    }

}
