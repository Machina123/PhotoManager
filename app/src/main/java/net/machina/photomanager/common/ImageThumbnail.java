package net.machina.photomanager.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

/**
 * Created by Machina on 21.10.2015.
 */
public class ImageThumbnail extends View {

    protected static int STROKE_COLOR = 0xffffffff;

    protected Bitmap mImage, mRotatedImage;
    protected Point mCenterPoint;
    protected Paint mPaint;
    protected Matrix matrix;

    public ImageThumbnail(Context context, Bitmap image, Point center) {
        super(context);
        this.mImage = image;
        this.mCenterPoint = center;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(STROKE_COLOR);
        mPaint.setStrokeWidth(3.0f);
        matrix = new Matrix();
        matrix.postRotate(90);
        this.mRotatedImage = Bitmap.createBitmap(mImage, 0, 0, mImage.getWidth(), mImage.getHeight(), matrix, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, (mImage.getWidth() / 2) + 2.0f, mPaint);
        canvas.drawBitmap(mRotatedImage, mCenterPoint.x - (mImage.getWidth() / 2), mCenterPoint.y - (mImage.getHeight() / 2), mPaint);
        super.onDraw(canvas);
    }


    public void setPosition(Point newPosition) {
        this.mCenterPoint = newPosition;
        this.invalidate();
    }

}
