package net.machina.photomanager.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by machina on 08.10.2015.
 */
public class Circle extends View {

    protected Context mContext;
    protected float desiredCircleRadius = 0.0f;
    protected Rect displaySize = new Rect();

    public Circle(Context context) {
        super(context);
        this.mContext = context;

        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getRectSize(displaySize);

        if (displaySize.height() > displaySize.width())
            desiredCircleRadius = displaySize.width() * 0.4f;
        else desiredCircleRadius = displaySize.height() * 0.4f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        paint.setColor(Color.argb(255, 255, 255, 255));
        canvas.drawCircle(displaySize.centerX(), displaySize.centerY(), desiredCircleRadius, paint);


    }

    public Point getPointOnCircle(double angleRadians) {
        Point returned = new Point();

        int calculatedX = (int) (displaySize.centerX() + (desiredCircleRadius * Math.cos(angleRadians)));
        int calculatedY = (int) (displaySize.centerY() + (desiredCircleRadius * Math.sin(angleRadians)));
        returned.set(calculatedX, calculatedY);

        return returned;
    }

}