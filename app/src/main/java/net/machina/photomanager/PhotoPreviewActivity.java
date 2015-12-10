package net.machina.photomanager;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import net.machina.photomanager.common.ImageTransform;
import net.machina.photomanager.common.ImagingHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoPreviewActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    public static final String API_LOCATION = "http://4ib2.spec.pl.hostingasp.pl/Ciepiela_Patryk/PhotoManager/Save.aspx";

    protected FlipStatus flipStatus = FlipStatus.NO_FLIP;
    protected ImageView btnEditText, imgPreview, btnFlipPic, btnRotatePic, btnContrastPic, btnColorEffectsPic, btnSharePic;
    protected LinearLayout layoutSliders;
    protected SeekBar seekBrightness, seekContrast, seekSaturation;
    protected Bitmap sourceBitmap, effectedBitmap, sendableBitmap;
    protected ColorMatrix selectedTransform;
    protected String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent().getExtras() != null) {
            filePath = getIntent().getStringExtra("parent");
        } else {
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        layoutSliders = (LinearLayout) findViewById(R.id.layoutSliders);

        btnEditText = (ImageView) findViewById(R.id.btnEditFont);
        btnEditText.setOnClickListener(this);

        btnFlipPic = (ImageView) findViewById(R.id.btnFlipPic);
        btnFlipPic.setOnClickListener(this);

        btnRotatePic = (ImageView) findViewById(R.id.btnRotatePic);
        btnRotatePic.setOnClickListener(this);

        btnContrastPic = (ImageView) findViewById(R.id.btnContrastPic);
        btnContrastPic.setOnClickListener(this);

        btnColorEffectsPic = (ImageView) findViewById(R.id.btnColorEffectsPic);
        btnColorEffectsPic.setOnClickListener(this);

        btnSharePic = (ImageView) findViewById(R.id.btnSharePic);
        btnSharePic.setOnClickListener(this);

        imgPreview = (ImageView) findViewById(R.id.imgFullscreen);

        seekBrightness = (SeekBar) findViewById(R.id.seekBrightness);
        seekContrast = (SeekBar) findViewById(R.id.seekContrast);
        seekSaturation = (SeekBar) findViewById(R.id.seekSaturation);

        seekBrightness.setOnSeekBarChangeListener(this);
        seekContrast.setOnSeekBarChangeListener(this);
        seekSaturation.setOnSeekBarChangeListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String photoPath = extras.getString("path");
            Bitmap photo = BitmapFactory.decodeFile(photoPath);
            imgPreview.setImageBitmap(photo);
            this.sourceBitmap = photo;
            this.effectedBitmap = photo;
            this.selectedTransform = new ColorMatrix(ImageTransform.initial);
            // photo.recycle();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditFont:
                //Toast.makeText(PhotoPreviewActivity.this, "przechodzenie do activity", Toast.LENGTH_SHORT).show();
                Intent launchIntent = new Intent(PhotoPreviewActivity.this, TextEditActivity.class);
                startActivityForResult(launchIntent, 10000);
                break;
            case R.id.btnFlipPic:
                flipPicture();
                break;
            case R.id.btnRotatePic:
                rotatePicture();
                break;
            case R.id.btnContrastPic:
                toggleSliders();
                break;
            case R.id.btnColorEffectsPic:
                String[] effects = {"oryginalny", "tylko czerwony", "tylko zielony", "tylko niebieski", "negatyw", "sepia", "odcienie szarości"};
                AlertDialog.Builder effectsDialog = new AlertDialog.Builder(PhotoPreviewActivity.this);
                effectsDialog.setTitle("Wybierz efekt")
                        .setItems(effects, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ColorMatrix transformation;
                                switch (which) {
                                    case 1:
                                        transformation = new ColorMatrix(ImageTransform.red);
                                        break;
                                    case 2:
                                        transformation = new ColorMatrix(ImageTransform.green);
                                        break;
                                    case 3:
                                        transformation = new ColorMatrix(ImageTransform.blue);
                                        break;
                                    case 4:
                                        transformation = new ColorMatrix(ImageTransform.negative);
                                        break;
                                    case 5:
                                        transformation = new ColorMatrix();
                                        transformation.setSaturation(0);
                                        ColorMatrix cm_sepia = new ColorMatrix();
                                        cm_sepia.setScale(1, 1, 0.8f, 1);
                                        transformation.postConcat(cm_sepia);
                                        break;
                                    case 6:
                                        transformation = new ColorMatrix();
                                        transformation.setSaturation(0);
                                        break;
                                    default:
                                        transformation = new ColorMatrix(ImageTransform.initial);
                                }
                                setImageColorTransformation(transformation);
                            }
                        })
                        .setCancelable(true)
                        .show();
                break;
            case R.id.btnSharePic:
                CharSequence[] items = {"Zapisz w galerii", "Udostępnij innej aplikacji", "Prześlij na serwer"};
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
                builder.setTitle("Udostępnij zdjęcie")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        savePhoto();
                                        break;
                                    case 1:
                                        sharePhoto();
                                        break;
                                    case 2:
                                        uploadPhoto();
                                        break;
                                    default:
                                        return;
                                }
                            }
                        })
                        .setCancelable(true)
                        .show();
                break;
            default:
                return;
        }
    }

    public void setImageColorTransformation(ColorMatrix cm) {
        imgPreview.setImageBitmap(ImageTransform.applyColorMatrix(effectedBitmap, cm));
        this.selectedTransform = cm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 10000:
                switch (resultCode) {
                    case 0:
                        Toast.makeText(PhotoPreviewActivity.this, "Otrzymano odpowiedź", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(PhotoPreviewActivity.this, "Porażka :(", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void flipPicture() {
        switch (flipStatus) {
            case NO_FLIP:
                flipPicture(-1.0f, 1.0f);
                flipStatus = FlipStatus.VERTICAL;
                break;
            case VERTICAL:
                flipPicture(1.0f, -1.0f);
                flipStatus = FlipStatus.BOTH;
                break;
            case HORIZONTAL:
                flipPicture(1.0f, -1.0f);
                flipStatus = FlipStatus.NO_FLIP;
                break;
            case BOTH:
                flipPicture(-1.0f, 1.0f);
                flipStatus = FlipStatus.HORIZONTAL;
                break;
            default:
                return;
        }
    }

    public void flipPicture(float x, float y) {
        Matrix transformMatrix = new Matrix();
        transformMatrix.postScale(x, y);
        Bitmap source = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
        Bitmap sourceCopy = sourceBitmap;
        sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
        imgPreview.setImageBitmap(translated);
    }

    public void rotatePicture() {
        Matrix transformMatrix = new Matrix();
        transformMatrix.postRotate(90);
        Bitmap source = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        Bitmap translated = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), transformMatrix, false);
        Bitmap sourceCopy = sourceBitmap;
        sourceBitmap = Bitmap.createBitmap(sourceCopy, 0, 0, sourceCopy.getWidth(), sourceCopy.getHeight(), transformMatrix, false);
        imgPreview.setImageBitmap(translated);
    }

    public void toggleSliders() {
        switch (layoutSliders.getVisibility()) {
            case View.VISIBLE:
                layoutSliders.setVisibility(View.GONE);
                break;
            case View.GONE:
                layoutSliders.setVisibility(View.VISIBLE);
                break;
            default:
                return;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBrightness:
            case R.id.seekContrast:
            case R.id.seekSaturation:
                float contrast = 1 + (seekContrast.getProgress() / 0x3f);
                float brightness = (-256 + (seekBrightness.getProgress() * 2.0f));
                float saturation = seekSaturation.getProgress() / 25.5f;
                effectedBitmap = ImageTransform.setContrastBrightness(sourceBitmap, brightness, contrast, saturation);
                imgPreview.setImageBitmap(ImageTransform.applyColorMatrix(effectedBitmap, selectedTransform));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void uploadPhoto() {
        sendableBitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        new NetworkTask().execute();
    }

    public void sharePhoto() {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String filename = dFormat.format(new Date());
        sendableBitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        File rootDir = new File(filePath);
        File newPhoto = new File(rootDir.getParent() + "/PhotoManager-" + filename + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(newPhoto);
            fos.write(ImagingHelper.getRawFromBitmap(sendableBitmap));
            fos.close();
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + newPhoto.getAbsolutePath()));
            startActivity(Intent.createChooser(shareIntent, "Udostępnij plik"));

        } catch (Exception e) {
            //Toast.makeText(PhotoPreviewActivity.this, "Nie można było zapisać zdjęcia", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
            builder.setTitle("Błąd")
                    .setMessage("Nie można było udostępnić pliku:\n" + e.getMessage())
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .show();
        } finally {
            sendableBitmap.recycle();
        }

    }

    public void savePhoto() {
        SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        String filename = dFormat.format(new Date());
        sendableBitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        File newPhoto = new File(filePath + "/" + filename + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(newPhoto);
            fos.write(ImagingHelper.getRawFromBitmap(sendableBitmap));
            fos.close();
            Toast.makeText(PhotoPreviewActivity.this, "Zapisano zdjęcie!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(PhotoPreviewActivity.this, "Nie można było zapisać zdjęcia", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            sendableBitmap.recycle();
        }

    }

    protected enum FlipStatus {
        NO_FLIP,
        VERTICAL,
        HORIZONTAL,
        BOTH;

        @Override
        public String toString() {
            switch (this) {
                case NO_FLIP:
                    return "No flip";
                case VERTICAL:
                    return "Vertical";
                case HORIZONTAL:
                    return "Horizontal";
                case BOTH:
                    return "Both directions";
                default:
                    throw new IllegalArgumentException("Błędny parametr!");
            }
        }
    }

    @SuppressWarnings("deprecation")
    public class NetworkTask extends AsyncTask<Void, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(PhotoPreviewActivity.this);

        @Override
        protected String doInBackground(Void... params) {
            final Bitmap fileToSend = sendableBitmap;
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(API_LOCATION);
            httpPost.setEntity(new ByteArrayEntity(ImagingHelper.getRawFromBitmap(fileToSend)));
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
            } finally {
                fileToSend.recycle();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Oczekiwanie na odpowiedź serwera");
            dialog.show();
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoPreviewActivity.this);
            builder.setTitle("Odpowiedź z serwera")
                    .setMessage(result)
                    .setCancelable(false)
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

}
