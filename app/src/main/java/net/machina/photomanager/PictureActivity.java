package net.machina.photomanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import net.machina.photomanager.common.Constants;

import java.io.File;

public class PictureActivity extends AppCompatActivity {

    protected ListView listDirs;

    protected File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

    @Override
    protected void onStart() {
        File myDir = new File(startingDir.getPath() + "/" + Constants.APP_FOLDER + "/");
        if(!myDir.exists()){
            boolean success = myDir.mkdirs();
            if(success) {
                Toast.makeText(PictureActivity.this, "Stworzono katalog aplikacji", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PictureActivity.this, "Nie udało się stworzyć katalogu aplikacji", Toast.LENGTH_SHORT).show();
            }
        }
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listDirs = (ListView) findViewById(R.id.listFolders);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
