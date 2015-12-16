package net.machina.photomanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import net.machina.photomanager.common.Constants;
import net.machina.photomanager.common.ImagingHelper;
import net.machina.photomanager.common.Networking;
import net.machina.photomanager.common.PhotoListItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class HelloActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String API_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/GetPhotos.aspx";
    public static final String PHOTO_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/thumbnails/";

    public Snackbar snackbar;
    protected RelativeLayout btnCamera;
    protected RelativeLayout btnGallery;
    protected RelativeLayout btnCollage;
    protected RelativeLayout btnNetwork;
    protected ImageView btnNext, btnPrev;
    protected ProgressBar progress;
    public ArrayList<PhotoListItem> uploadedPhotos = new ArrayList<>();
    public JSONArray photoArray;
    public ImageView imgPreview;

    public int photoIter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        
        imgPreview = (ImageView) findViewById(R.id.imageBegin);
        
        btnCamera = (RelativeLayout) findViewById(R.id.btnCamera);
        btnGallery = (RelativeLayout) findViewById(R.id.btnGallery);
        btnCollage = (RelativeLayout) findViewById(R.id.btnCollage);
        btnNetwork = (RelativeLayout) findViewById(R.id.btnNetwork);

        btnPrev = (ImageView) findViewById(R.id.btnHelloPrevImage);
        btnNext = (ImageView) findViewById(R.id.btnHelloNextImage);
        progress = (ProgressBar) findViewById(R.id.prgSpinner);

        if (btnNetwork != null && btnCollage != null && btnGallery != null && btnCamera != null) createListeners();
        if (!Networking.isAvailable(HelloActivity.this)) {
            snackbar = Snackbar.make(getWindow().getDecorView().getRootView(),
                    R.string.error_no_intetnet,
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getImageCount();
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
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    public void getImageCount() {
        new GetPhotos().execute();
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
            case R.id.btnCollage:
                intent = new Intent(HelloActivity.this, CollagePickerActivity.class);
                startActivity(intent);
                break;
            case R.id.btnNetwork:
                intent = new Intent(HelloActivity.this, NetworkActivity.class);
                startActivity(intent);
                break;
            case R.id.btnHelloPrevImage:
                Log.d(Constants.LOGGER_TAG, "Poprzednie");
                if((photoIter - 1) < 0) return;
                new DownloadPhoto().execute(PHOTO_LOCATION + uploadedPhotos.get(--photoIter).getImageName());
                break;
            case R.id.btnHelloNextImage:
                Log.d(Constants.LOGGER_TAG, "Następne");
                if((photoIter + 1) >= uploadedPhotos.size()) return;
                new DownloadPhoto().execute(PHOTO_LOCATION + uploadedPhotos.get(++photoIter).getImageName());
                break;
            default:
        }
    }

    @SuppressWarnings("deprecation")
    public class GetPhotos extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(HelloActivity.this);

        @Override
        protected String doInBackground(Void... params) {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(API_LOCATION);
            HttpResponse response = null;
            try {
                response = client.execute(httpPost);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String responseGet;
                String resp = "";
                while ((responseGet = reader.readLine()) != null) {
                    resp += responseGet;
                }
                return resp;
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String result) {
            progress.setVisibility(View.GONE);
            try {
                JSONObject obj = new JSONObject(result);
                photoArray = obj.getJSONArray("photoArray");
                for(int i = 0; i < photoArray.length(); i++) {
                    JSONObject photo = photoArray.getJSONObject(i);
                    uploadedPhotos.add(new PhotoListItem(photo.getString("IMAGE_NAME"), photo.getString("IMAGE_SAVE_TIME")));
                }
                
                if(uploadedPhotos.size() > 0) {
                    new DownloadPhoto().execute(PHOTO_LOCATION + uploadedPhotos.get(0).getImageName());
                }

            } catch (JSONException jsex) {
                jsex.printStackTrace();
            }
        }
    }

    public class DownloadPhoto extends AsyncTask<String, Void, Void> {

        protected Drawable loadedImage;

        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progress.setVisibility(View.GONE);
            imgPreview.setImageDrawable(loadedImage);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                loadedImage = getImageFromServer(new URL(params[0]));
            }catch (MalformedURLException muex) {
                muex.printStackTrace();
            }

            return null;
        }
    }

    public static Drawable getImageFromServer(URL url) {
        try {
            InputStream inputStream = (InputStream) url.getContent();
            return Drawable.createFromStream(inputStream, "interwebz");
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return null;
        }
    }
}
