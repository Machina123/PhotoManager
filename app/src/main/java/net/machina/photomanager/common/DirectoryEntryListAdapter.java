package net.machina.photomanager.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.machina.photomanager.R;

import java.util.ArrayList;


/**
 * Created by Machina on 17.09.2015.
 */
public class DirectoryEntryListAdapter extends ArrayAdapter<DirectoryEntry> {

    private ArrayList<DirectoryEntry> objects;
    private Context thisContext;

    public DirectoryEntryListAdapter(Context context, int resource, ArrayList<DirectoryEntry> items) {
        super(context, resource, items);
        this.thisContext = context;
        this.objects = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null) {
            LayoutInflater inflater = (LayoutInflater)thisContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.layout_direntry, null);
        }

        DirectoryEntry item = objects.get(position);

        if(item != null) {
            TextView label = (TextView) v.findViewById(R.id.textLabel);
            ImageView icon = (ImageView) v.findViewById(R.id.imgIcon);

            label.setText(item.getName());
            icon.setImageResource(item.getIconResId());
        }

        return v;
    }
}
