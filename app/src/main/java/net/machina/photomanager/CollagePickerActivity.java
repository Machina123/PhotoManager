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
        switch (v.getId()) {
            case R.id.btnCollage3:
                ArrayList<CollageComponent> components = new ArrayList<>(4);
                components.add(new CollageComponent(0, 0, 0.5f, 1f));
                components.add(new CollageComponent(0.5f, 0, 0.5f, 0.3333f));
                components.add(new CollageComponent(0.5f, 0.3333f, 0.5f, 0.3333f));
                components.add(new CollageComponent(0.5f, 0.6666f, 0.5f, 0.3333f));

                Intent collageStarter = new Intent(CollagePickerActivity.this, CollageActivity.class);
                collageStarter.putExtra("components", components);
                startActivity(collageStarter);

                break;
            default:
                Toast.makeText(CollagePickerActivity.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
        }
    }
}
