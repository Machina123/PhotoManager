package net.machina.photomanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;

public class HelloActivity extends AppCompatActivity implements View.OnClickListener{

    protected RelativeLayout btnCamera;
    protected RelativeLayout btnGallery;
    protected RelativeLayout btnCollage;
    protected RelativeLayout btnNetwork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        btnCamera = (RelativeLayout) findViewById(R.id.btnCamera);
        btnGallery = (RelativeLayout) findViewById(R.id.btnGallery);
        btnCollage = (RelativeLayout) findViewById(R.id.btnCollage);
        btnNetwork = (RelativeLayout) findViewById(R.id.btnNetwork);

        if (btnNetwork != null && btnCollage != null && btnGallery != null && btnCamera != null) createListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public void createListeners() {
        btnCamera.setOnClickListener(this);
        btnCollage.setOnClickListener(this);
        btnNetwork.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle extras = new Bundle();

        switch(v.getId()) {
            case R.id.btnCamera:
                extras.putInt("activityMethod", 1);
                intent = new Intent(HelloActivity.this, GallerySelectorActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            case R.id.btnGallery:
                extras.putInt("activityMethod", 0);
                intent = new Intent(HelloActivity.this, GallerySelectorActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
                break;
            default:
        }
    }
}
