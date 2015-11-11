package net.machina.photomanager.common;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import net.machina.photomanager.GalleryActivity;

import java.util.ArrayList;

/**
 * Created by machi on 29.10.2015.
 */
public class ImagePreviewAdapter extends BaseAdapter {

    protected Context mContext;
    protected String[] mPathList;

    public ImagePreviewAdapter(Context cont, String[] pathList) {
        super();
        this.mContext = cont;
        this.mPathList = pathList;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView image;

        if (convertView != null) {

            image = new ImageView(this.mContext);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setLayoutParams(new GridView.LayoutParams(180, 180));
            Log.d(Constants.LOGGER_TAG, "Dodawanie podgladu dla " + this.mPathList[position]);

        } else {
            image = (ImageView) convertView;
        }

        image.setImageBitmap(BitmapFactory.decodeFile(this.mPathList[position]));
        return image;
    }
}
