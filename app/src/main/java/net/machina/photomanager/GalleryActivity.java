package net.machina.photomanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.ImagingHelper;
import net.machina.photomanager.common.ScreenHelper;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    protected String displayPath;
    protected LinearLayout layoutPhotos;
    protected ArrayList<String> files = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        layoutPhotos = (LinearLayout) findViewById(R.id.layoutListPhotos);

        if (getIntent().getExtras() != null) {
            //textView.setText("Przekazana ścieżka: \n" + getIntent().getStringExtra("path") + "\nActivity: GalleryActivity");
            displayPath = getIntent().getStringExtra("path");
            Log.d(Constants.LOGGER_TAG, displayPath);
            File myDir = new File(displayPath + "/");

            if (myDir != null) {

                for (File file : myDir.listFiles()) {
                    files.add(file.getAbsolutePath());
                    Log.d(Constants.LOGGER_TAG, "Dodawanie: " + file.getAbsolutePath());
                }
            }

            Rect displaySize = ScreenHelper.getDisplaySize(GalleryActivity.this);

            for (int i = 0; i < files.size(); i++) {
                final int index = i;
                Bitmap fullSizeBitmap = BitmapFactory.decodeFile(files.get(i));
                ImageView imagePrev = new ImageView(GalleryActivity.this);
                imagePrev.setLayoutParams(new LinearLayout.LayoutParams(displaySize.width(), displaySize.height() / 5));
                imagePrev.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imagePrev.setImageBitmap(ImagingHelper.getScaledBitmap(fullSizeBitmap, fullSizeBitmap.getWidth() / 2, fullSizeBitmap.getHeight() / 2));
                imagePrev.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent launchIntent = new Intent(GalleryActivity.this, PhotoPreviewActivity.class);
                        launchIntent.putExtra("path", files.get(index));
                        startActivity(launchIntent);
                    }
                });
                layoutPhotos.addView(imagePrev);
                fullSizeBitmap.recycle();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
