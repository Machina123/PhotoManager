package net.machina.photomanager;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import net.machina.photomanager.common.Networking;

public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
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
        }
    }


    public class GetImagesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }
    }
}
