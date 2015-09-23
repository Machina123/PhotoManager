package net.machina.photomanager;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.DirectoryEntry;
import net.machina.photomanager.common.DirectoryEntryListAdapter;

import java.io.File;
import java.util.ArrayList;

public class PictureActivity extends AppCompatActivity {

    protected static File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    protected ListView listDirs;
    protected ArrayList<DirectoryEntry> directories = new ArrayList<>();
    protected File myDirectory = new File(startingDir.getPath() + "/" + Constants.APP_FOLDER + "/");

    @Override
    protected void onStart() {
        if (!myDirectory.exists()) {
            boolean success = myDirectory.mkdirs();
            if(success) {
                Toast.makeText(PictureActivity.this, "Stworzono katalog aplikacji", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PictureActivity.this, "Nie udało się stworzyć katalogu aplikacji", Toast.LENGTH_SHORT).show();
            }
        }

        if (!generateStartingDirs()) {
            Toast.makeText(this, "Nie udało się utworzyć katalogów", Toast.LENGTH_SHORT).show();
        }

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        listDirs = (ListView) findViewById(R.id.listFolders);

        directories.clear();
        if (myDirectory != null) {
            for (File file : myDirectory.listFiles()) {
                directories.add(new DirectoryEntry(file.isDirectory() ? R.mipmap.ic_directory : R.mipmap.ic_imagefile, file.getName(), file.getAbsolutePath()));
            }

            final DirectoryEntryListAdapter adapter = new DirectoryEntryListAdapter(PictureActivity.this, R.layout.layout_direntry, directories);
            listDirs.setAdapter(adapter);
        }
    }

    public boolean generateStartingDirs() {
        String[] startDirs = {"miejsca", "osoby", "przedmioty"};

        boolean isSuccess = true;

        for (String dir : startDirs) {
            File getDir = new File(startingDir.getPath() + "/" + Constants.APP_FOLDER + "/" + dir + "/");
            if (!getDir.exists()) {
                isSuccess = getDir.mkdirs();
            }
        }

        return isSuccess;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
