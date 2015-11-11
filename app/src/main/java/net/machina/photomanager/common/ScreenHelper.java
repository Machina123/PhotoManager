package net.machina.photomanager.common;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.WindowManager;

/**
 * Created by machi on 29.10.2015.
 */
public class ScreenHelper {

    public static Rect getDisplaySize(Context context) {
        Rect returned = new Rect();

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRectSize(returned);

        return returned;
    }

    public static Point getScreenCenter(Context context) {
        Point outPoint = new Point();

        Rect size = getDisplaySize(context);
        outPoint.set(size.centerX(), size.centerY());
        return outPoint;
    }

}
