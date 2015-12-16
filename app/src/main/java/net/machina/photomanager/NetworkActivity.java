package net.machina.photomanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.machina.photomanager.common.Networking;
import net.machina.photomanager.common.PhotoListItem;
import net.machina.photomanager.common.ScreenHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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

public class NetworkActivity extends AppCompatActivity {

    public static final String API_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/GetPhotos.aspx";
    public static final String PHOTO_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/pictures/";
    public static final String THUMB_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/pictures/";

    public ArrayList<PhotoListItem> uploadedPhotos = new ArrayList<>();
    public JSONArray photoArray;

    public LinearLayout listPhotos;

    public Rect displaySize;

    public int photoIter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        listPhotos = (LinearLayout) findViewById(R.id.layoutNetworkPhotos);

        displaySize = ScreenHelper.getDisplaySize(NetworkActivity.this);

        if(!Networking.isAvailable(NetworkActivity.this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NetworkActivity.this);
            builder .setTitle("Błąd")
                    .setMessage("Brak połączenia z Internetem")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkActivity.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
            finish();
        }

        new GetPhotos().execute();
    }

    @SuppressWarnings("deprecation")
    public class GetPhotos extends AsyncTask<Void, Void, String> {

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
        protected void onPostExecute(String result) {
            try {
                JSONObject obj = new JSONObject(result);
                photoArray = obj.getJSONArray("photoArray");
                for(int i = 0; i < photoArray.length(); i++) {
                    JSONObject photo = photoArray.getJSONObject(i);
                    uploadedPhotos.add(new PhotoListItem(photo.getString("IMAGE_NAME"), photo.getString("IMAGE_SAVE_TIME")));
                }

                for(int j = 0; j < uploadedPhotos.size(); j++) {
                    new DownloadImage().execute(THUMB_LOCATION + uploadedPhotos.get(j).getImageName());
                }
            } catch (JSONException jsex) {
                jsex.printStackTrace();
            }
        }
    }

    public class DownloadImage extends AsyncTask<String, Void, String> {

        protected Drawable loadedImage;

        @Override
        protected void onPostExecute(String s) {
            final String largeUrl = s;
            ImageView image = new ImageView(NetworkActivity.this);
            image.setLayoutParams(new LinearLayout.LayoutParams(-1, displaySize.height() / 5));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageDrawable(loadedImage);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent big = new Intent(Intent.ACTION_VIEW, Uri.parse(largeUrl));
                    startActivity(big);
                }
            });
            listPhotos.addView(image);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                loadedImage = getImageFromServer(new URL(params[0]));
                return PHOTO_LOCATION + uploadedPhotos.get(photoIter++).getImageName();
            } catch (MalformedURLException muex) {
                muex.printStackTrace();
                return null;
            }
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
