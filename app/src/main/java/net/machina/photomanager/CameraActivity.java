package net.machina.photomanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        //TODO: implementacja kamery
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
