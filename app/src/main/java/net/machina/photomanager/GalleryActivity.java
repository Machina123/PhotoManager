package net.machina.photomanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.ImagingHelper;
import net.machina.photomanager.common.ScreenHelper;

import java.io.File;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int ID_CONST = 0x3fff0000;

    protected String displayPath;
    protected LinearLayout layoutPhotos;
    protected ArrayList<String> files = new ArrayList<>();
    protected int elemHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        layoutPhotos = (LinearLayout) findViewById(R.id.layoutPhotoList);

        if (getIntent().getExtras() != null) {
            displayPath = getIntent().getStringExtra("path");
            Log.d(Constants.LOGGER_TAG, displayPath);
            reloadPictures();
        } else {
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onClick(View v) {
        final int picId = v.getId() - ID_CONST;
        Intent launchIntent = new Intent(GalleryActivity.this, PhotoPreviewActivity.class);
        launchIntent.putExtra("path", files.get(picId));
        launchIntent.putExtra("parent", displayPath);
        startActivity(launchIntent);
    }

    public void reloadPictures() {

        layoutPhotos.removeAllViews();
        files.clear();

        File myDir = new File(displayPath + "/");

        final Rect displaySize = ScreenHelper.getDisplaySize(GalleryActivity.this);
        elemHeight = (int) (displaySize.height() * 0.25f);

        LinearLayout.LayoutParams prmsSmall = new LinearLayout.LayoutParams(-1, -1, 2);
        prmsSmall.setMargins(4,4,4,4);
        LinearLayout.LayoutParams prmsLarge = new LinearLayout.LayoutParams(-1, -1, 1);
        prmsLarge.setMargins(4, 4, 4, 4);

        if (myDir != null) {

            EvenState rowState = EvenState.ODD;

            for (File file : myDir.listFiles()) {
                files.add(file.getAbsolutePath());
                Log.d(Constants.LOGGER_TAG, "Dodawanie: " + file.getAbsolutePath());
            }

            for (int rows = 0; rows < (files.size() /2) + 1 ; rows++) {

                if((2*rows) >= files.size()) return;

                LinearLayout layout = new LinearLayout(GalleryActivity.this);
                layout.setLayoutParams(new LinearLayout.LayoutParams(-1, elemHeight));
                ImageView iv1 = new ImageView(GalleryActivity.this);

                switch (rowState) {
                    case ODD:
                        iv1.setLayoutParams(prmsSmall);
                        break;
                    case EVEN:
                        iv1.setLayoutParams(prmsLarge);
                        break;
                }

                Bitmap b1 = BitmapFactory.decodeFile(files.get(2 * rows));
                iv1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                iv1.setImageBitmap(ImagingHelper.getScaledBitmap(b1, b1.getWidth() / 3, b1.getHeight() / 3));
                iv1.setId(ID_CONST + (2 * rows));
                iv1.setOnClickListener(this);
                layout.addView(iv1);

                if (2 * rows + 1 < files.size()) {
                    ImageView iv2 = new ImageView(GalleryActivity.this);
                    switch (rowState) {
                        case ODD:
                            iv2.setLayoutParams(prmsLarge);
                            break;
                        case EVEN:
                            iv2.setLayoutParams(prmsSmall);
                            break;
                    }
                    Bitmap b2 = BitmapFactory.decodeFile(files.get((2 * rows) + 1));
                    iv2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    iv2.setImageBitmap(ImagingHelper.getScaledBitmap(b2, b2.getWidth() / 3, b2.getHeight() / 3));
                    iv2.setId(ID_CONST + (2 * rows) + 1);
                    iv2.setOnClickListener(this);
                    layout.addView(iv2);
                }

                rowState = (rowState == EvenState.ODD ? EvenState.EVEN : EvenState.ODD);
                layoutPhotos.addView(layout);
            }
        }

    }

    public enum EvenState {
        ODD,
        EVEN
    }
}
