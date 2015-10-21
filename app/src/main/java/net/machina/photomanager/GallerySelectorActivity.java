package net.machina.photomanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.DirectoryEntry;
import net.machina.photomanager.common.DirectoryEntryListAdapter;

import java.io.File;
import java.util.ArrayList;

public class GallerySelectorActivity extends AppCompatActivity {

    protected static File startingDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    protected ListView listDirs;
    protected ArrayList<DirectoryEntry> directories = new ArrayList<>();
    protected File myDirectory = new File(startingDir.getPath() + "/" + Constants.APP_FOLDER + "/");
    protected ActivityMethod method;
    protected LinearLayout btnAddLibrary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        btnAddLibrary = (LinearLayout) findViewById(R.id.btnNewGallery);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            switch (extras.getInt("activityMethod", -1)) {
                case -1:
                    finish();
                    break;
                case 0:     // wyświetlenie galerii
                    method = ActivityMethod.ViewGallery;
                    break;
                case 1:     // zrobienie zdjęcia do galerii
                    method = ActivityMethod.TakePhoto;
                    break;
                default:
                    finish();
                    return;
            }
        } else finish();

        listDirs = (ListView) findViewById(R.id.listFolders);

        if (!myDirectory.exists()) {
            boolean success = myDirectory.mkdirs();
            if (success) {
                Toast.makeText(GallerySelectorActivity.this, "Stworzono katalog aplikacji", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(GallerySelectorActivity.this, "Nie udało się stworzyć katalogu aplikacji", Toast.LENGTH_SHORT).show();
            }
        }

        if (!generateStartingDirs()) {
            Toast.makeText(this, "Nie udało się utworzyć katalogów", Toast.LENGTH_SHORT).show();
        }

        loadDirectories();

        btnAddLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogNewGallery = new AlertDialog.Builder(GallerySelectorActivity.this);
                final EditText txtNewGallery = new EditText(GallerySelectorActivity.this);
                dialogNewGallery.setTitle("Nazwa nowej galerii?")
                        .setView(txtNewGallery)
                        .setPositiveButton("Stwórz", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String dir = txtNewGallery.getText().toString();
                                File getDir = new File(startingDir.getPath() + "/" + Constants.APP_FOLDER + "/" + dir + "/");
                                if (!getDir.exists()) {
                                    getDir.mkdirs();
                                    loadDirectories();
                                } else {
                                    Toast.makeText(GallerySelectorActivity.this, "Taki katalog już istnieje!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Anuluj", null)
                        .show();
            }
        });
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

    public void loadDirectories() {
        directories.clear();
        if (myDirectory != null) {
            for (File file : myDirectory.listFiles()) {
                if (file.isDirectory())
                    directories.add(new DirectoryEntry(R.mipmap.ic_directory, file.getName(), file.getAbsolutePath()));
            }

            final DirectoryEntryListAdapter adapter = new DirectoryEntryListAdapter(GallerySelectorActivity.this, R.layout.layout_direntry, directories);
            listDirs.setAdapter(adapter);
            listDirs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle extras = new Bundle();
                    extras.putString("path", directories.get(position).getPath());
                    Intent intent;
                    switch (method) {
                        case TakePhoto:
                            intent = new Intent(GallerySelectorActivity.this, CameraActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                            break;
                        case ViewGallery:
                            intent = new Intent(GallerySelectorActivity.this, GalleryActivity.class);
                            intent.putExtras(extras);
                            startActivity(intent);
                            break;
                    }
                }
            });
        }
    }

    protected enum ActivityMethod {
        ViewGallery,
        TakePhoto
    }
}
