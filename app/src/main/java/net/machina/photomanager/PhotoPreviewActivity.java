package net.machina.photomanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoPreviewActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView btnEditText, imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        btnEditText = (ImageView) findViewById(R.id.btnEditFont);
        btnEditText.setOnClickListener(this);
        imgPreview = (ImageView) findViewById(R.id.imgFullscreen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String photoPath = extras.getString("path");
            Bitmap photo = BitmapFactory.decodeFile(photoPath);
            imgPreview.setImageBitmap(photo);
            // photo.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditFont:
                //Toast.makeText(PhotoPreviewActivity.this, "przechodzenie do activity", Toast.LENGTH_SHORT).show();
                Intent launchIntent = new Intent(PhotoPreviewActivity.this, TextEditActivity.class);
                startActivityForResult(launchIntent, 10000);
                break;
            default:
                return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10000:
                switch (resultCode) {
                    case 0:
                        Toast.makeText(PhotoPreviewActivity.this, "Otrzymano odpowiedź", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(PhotoPreviewActivity.this, "Porażka :(", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

}
