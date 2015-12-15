package net.machina.photomanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import net.machina.photomanager.common.CollageComponent;
import net.machina.photomanager.common.ImagingHelper;

import java.util.ArrayList;

public class CollageActivity extends AppCompatActivity {

    protected RelativeLayout layoutCollage;
    protected Point displaySize = new Point();
    protected int selectedId = 0;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage);

        layoutCollage = (RelativeLayout) findViewById(R.id.layoutCollage);

        Bundle extras = getIntent().getExtras();

        ArrayList<CollageComponent> components = (ArrayList<CollageComponent>) extras.getSerializable("components");
        if (components == null) {
            Toast.makeText(CollageActivity.this, "Nie przekazano listy komponentów!", Toast.LENGTH_SHORT).show();
            finish();
        }


        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(displaySize);

        try {
            for (int i = 0; i < components.size(); i++) {
                CollageComponent component = components.get(i);
                ImageView image = new ImageView(CollageActivity.this);
                image.setX(displaySize.x * component.getX());
                image.setY(displaySize.y * component.getY());
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (displaySize.x * component.getWidth()), (int) (displaySize.y * component.getHeight())));
                image.setBackgroundColor(0xff7f7f7f);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                image.setImageResource(R.mipmap.ic_camera);
                image.setId(10000 + i);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedId = v.getId();
                        Toast.makeText(CollageActivity.this, "Przekazane ID: " + selectedId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CollageActivity.this, CollageCameraActivity.class);
                        startActivityForResult(intent, 12345);
                    }
                });
                layoutCollage.addView(image);
            }
        } catch (Exception ex) {
            Toast.makeText(CollageActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(CollageActivity.this, "Wyszło - " + requestCode + " / " + resultCode + " / " + (data == null ? "nie ma danych" : "są dane"), Toast.LENGTH_SHORT).show();
        if (data != null) {
            ImageView selectedImage = (ImageView) CollageActivity.this.findViewById(selectedId);
            if (data.getByteArrayExtra("data") != null) {
                Bitmap bitmap = ImagingHelper.getScaledBitmap(ImagingHelper.getRotatedBitmapFromRaw(data.getByteArrayExtra("data"), 90), selectedImage.getWidth(), selectedImage.getHeight());
                selectedImage.setImageBitmap(bitmap);
            } else {
                Toast.makeText(CollageActivity.this, "Nie przekazano tablicy bajtów", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
