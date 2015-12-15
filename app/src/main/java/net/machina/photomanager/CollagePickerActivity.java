package net.machina.photomanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.machina.photomanager.common.CollageComponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CollagePickerActivity extends AppCompatActivity implements View.OnClickListener {

    protected ImageView collage1, collage2, collage3, collage4, collage5, collage6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_picker);
        collage1 = (ImageView) findViewById(R.id.btnCollage1);
        collage2 = (ImageView) findViewById(R.id.btnCollage2);
        collage3 = (ImageView) findViewById(R.id.btnCollage3);
        collage4 = (ImageView) findViewById(R.id.btnCollage4);
        collage5 = (ImageView) findViewById(R.id.btnCollage5);
        collage6 = (ImageView) findViewById(R.id.btnCollage6);

        collage1.setOnClickListener(this);
        collage2.setOnClickListener(this);
        collage3.setOnClickListener(this);
        collage4.setOnClickListener(this);
        collage5.setOnClickListener(this);
        collage6.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onClick(View v) {
        ArrayList<CollageComponent> components = null;

        switch (v.getId()) {
            case R.id.btnCollage1:
                components = new ArrayList<>(2);
                components.add(new CollageComponent(0,0,0.5f, 1f));
                components.add(new CollageComponent(0.5f, 0, 0.5f, 1f));
                break;
            case R.id.btnCollage2:
                components = new ArrayList<>(3);
                components.add(new CollageComponent(0, 0, 1f, 0.33333f));
                components.add(new CollageComponent(0, 0.333333f, 1f, 0.33333f));
                components.add(new CollageComponent(0, 0.666667f, 1f, 0.33333f));
                break;
            case R.id.btnCollage3:
                components = new ArrayList<>(4);
                components.add(new CollageComponent(0, 0, 0.5f, 1f));
                components.add(new CollageComponent(0.5f, 0, 0.5f, 0.3333f));
                components.add(new CollageComponent(0.5f, 0.3333f, 0.5f, 0.3333f));
                components.add(new CollageComponent(0.5f, 0.6666f, 0.5f, 0.3333f));
                break;
            case R.id.btnCollage4:
                components = new ArrayList<>(5);
                components.add(new CollageComponent(0,0,0.5f,0.33333f));
                components.add(new CollageComponent(0.5f,0,0.5f,0.33333f));
                components.add(new CollageComponent(0,0.33333f,1f,0.33333f));
                components.add(new CollageComponent(0,0.66667f,0.5f,0.33333f));
                components.add(new CollageComponent(0.5f,0.66667f,0.5f,0.33333f));
                break;
            case R.id.btnCollage5:
                components = new ArrayList<>(6);
                components.add(new CollageComponent(0,0,0.5f, 0.33333f));
                components.add(new CollageComponent(0.5f,0,0.5f, 0.33333f));
                components.add(new CollageComponent(0,0.333333f,0.5f, 0.33333f));
                components.add(new CollageComponent(0.5f,0.333333f,0.5f, 0.33333f));
                components.add(new CollageComponent(0,0.666667f,0.5f, 0.33333f));
                components.add(new CollageComponent(0.5f,0.666667f,0.5f, 0.33333f));
                break;
            case R.id.btnCollage6:
                components = new ArrayList<>(8);
                components.add(new CollageComponent(0,0,0.5f, 0.25f));
                components.add(new CollageComponent(0.5f,0,0.5f, 0.25f));
                components.add(new CollageComponent(0,0.25f,0.5f, 0.25f));
                components.add(new CollageComponent(0.5f,0.25f,0.5f, 0.25f));
                components.add(new CollageComponent(0,0.5f,0.5f, 0.25f));
                components.add(new CollageComponent(0.5f,0.5f,0.5f, 0.25f));
                components.add(new CollageComponent(0,0.75f,0.5f, 0.25f));
                components.add(new CollageComponent(0.5f,0.75f,0.5f, 0.25f));
                break;
        }
        Intent collageStarter = new Intent(CollagePickerActivity.this, CollageActivity.class);
        collageStarter.putExtra("components", components);
        startActivity(collageStarter);
    }
}
