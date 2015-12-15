package net.machina.photomanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.machina.photomanager.common.ScreenHelper;

import java.io.File;

public class PhotoPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView btnBack, btnEdit, btnDelete, imgPreview;
    protected String filePath, parentPath;
    protected Bitmap image;

    protected float scale = 1.0f;
    protected Matrix matrix = new Matrix();
    protected ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        Point screenCenter = ScreenHelper.getScreenCenter(PhotoPreviewActivity.this);

        imgPreview = (ImageView) findViewById(R.id.imgPreviewPhoto);

        imgPreview.setPivotX(screenCenter.x);
        imgPreview.setPivotY(screenCenter.y);

        scaleGestureDetector = new ScaleGestureDetector(PhotoPreviewActivity.this, new ScaleListener());

        btnBack = (ImageView) findViewById(R.id.btnPreviewBack);
        btnEdit = (ImageView) findViewById(R.id.btnPreviewEdit);
        btnDelete = (ImageView) findViewById(R.id.btnPreviewDelete);

        btnBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnEdit.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            filePath = extras.getString("path", "");
            if(filePath.length() < 1) finish();
            parentPath = extras.getString("parent", "");

            image = BitmapFactory.decodeFile(filePath);
            imgPreview.setImageBitmap(image);

        } else finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnPreviewBack:
                finish();
                break;
            case R.id.btnPreviewEdit:

                Intent intent = new Intent(PhotoPreviewActivity.this, PhotoEditActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("parent", parentPath);
                startActivity(intent);

                break;
            case R.id.btnPreviewDelete:
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
                builder .setTitle("Pytanie")
                        .setMessage("Czy na pewno chcesz usunąć to zdjęcie?")
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File myPicture = new File(filePath);
                                if (myPicture.delete()) {
                                    Toast.makeText(PhotoPreviewActivity.this, "Zdjęcie usunięto", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(PhotoPreviewActivity.this, "Nie udało się usunąć zdjęcia", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Nie",null)
                        .setCancelable(true)
                        .show();
                break;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 6.0f));

            imgPreview.setScaleX(scale);
            imgPreview.setScaleY(scale);
            imgPreview.setImageMatrix(matrix);
            return true;
        }
    }
}
