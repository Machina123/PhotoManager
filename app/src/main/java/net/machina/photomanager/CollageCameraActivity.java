package net.machina.photomanager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.machina.photomanager.common.CameraPreview;
import net.machina.photomanager.common.Circle;
import net.machina.photomanager.common.ImageThumbnail;
import net.machina.photomanager.common.ImagingHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("deprecation")
public class CollageCameraActivity extends AppCompatActivity implements View.OnClickListener {

    protected static int cameraRotation;
    public String path;
    protected Camera camera;
    protected int cameraId;
    protected CameraPreview preview;
    protected FrameLayout cameraPrev;
    protected ImageView btnShutter;
    protected ImageView btnAccept, btnReject;
    protected ImageView btnExposure, btnWhiteBalance, btnResolution;
    protected LinearLayout layoutCamSettings;
    protected int[] exposureLevels;
    protected String[] whiteBalanceLevels;
    protected Camera.Size[] photoSizeList;

    protected Camera.Parameters camParams;

    protected Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            final byte[] thisData = data;

            btnAccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            btnShutter.setVisibility(View.GONE);

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    byte[] data = new byte[0];
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("data", data);
                    setResult(12345, returnIntent);
                    CollageCameraActivity.this.finish();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    Bitmap data = BitmapFactory.decodeByteArray(thisData, 0, thisData.length);
                    Bitmap dataSmall = ImagingHelper.getScaledBitmap(data, data.getWidth() / 4, data.getHeight() / 4);
                    byte[] returned = ImagingHelper.getRawFromBitmap(dataSmall);
                    returnIntent.putExtra("data", returned);
                    setResult(12345, returnIntent);
                    Toast.makeText(CollageCameraActivity.this, "Wyjście i zapisanie danych", Toast.LENGTH_SHORT).show();
                    CollageCameraActivity.this.finish();
                }
            });
        }
    };

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        cameraRotation = result;
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_camera);
        btnShutter = (ImageView) findViewById(R.id.btnShutter2);
        layoutCamSettings = (LinearLayout) findViewById(R.id.layoutCameraTop2);
        btnAccept = (ImageView) findViewById(R.id.btnAccept2);
        btnReject = (ImageView) findViewById(R.id.btnReject2);
        btnExposure = (ImageView) findViewById(R.id.btnExposure2);
        btnWhiteBalance = (ImageView) findViewById(R.id.btnWhiteBalance2);
        btnResolution = (ImageView) findViewById(R.id.btnResolution2);
        if (btnShutter != null) btnShutter.setOnClickListener(this);
        initCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShutter2:

                if (camera != null) camera.takePicture(null, null, jpegCallback);

                break;
            case R.id.layoutCameraPreview2:
                if (camera != null) camera.autoFocus(null);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    protected void initCamera() {
        boolean isCameraAvailable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!isCameraAvailable) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Uwaga")
                    .setMessage("Brak kamery w telefonie lub kamera niedostępna!")
                    .setCancelable(false)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            cameraId = getCameraId();
            if (cameraId >= 0) camera = Camera.open(cameraId);
            preview = new CameraPreview(CollageCameraActivity.this, camera);
            cameraPrev = (FrameLayout) findViewById(R.id.layoutCameraPreview2);
            if (cameraPrev != null) {
                cameraPrev.setOnClickListener(this);
                cameraPrev.addView(preview);
                setCameraDisplayOrientation(this, cameraId, camera);
                getCameraParameters();
                camera.startPreview();
            }
        }
    }

    protected int getCameraId() {
        int cameraCount = Camera.getNumberOfCameras();
        int returned = 0;

        for (int i = 0; i < cameraCount; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                returned = i;
            }
        }

        return returned;
    }

    protected void getCameraParameters() {
        if (camera != null) {
            camParams = camera.getParameters();
            exposureLevels = new int[((camParams.getMaxExposureCompensation() * 2) + 1)];
            int j = 0;
            for (int i = camParams.getMinExposureCompensation(); i <= camParams.getMaxExposureCompensation(); i++) {
                exposureLevels[j++] = i;
            }

            whiteBalanceLevels = new String[camParams.getSupportedWhiteBalance().size()];
            photoSizeList = new Camera.Size[camParams.getSupportedPictureSizes().size()];

            camParams.getSupportedWhiteBalance().toArray(whiteBalanceLevels);
            camParams.getSupportedPictureSizes().toArray(photoSizeList);
            camParams.setPictureSize(photoSizeList[0].width, photoSizeList[0].height);
            camera.setParameters(camParams);

            final String[] resStrings = new String[photoSizeList.length];
            for (int i = 0; i < photoSizeList.length; i++) {
                resStrings[i] = photoSizeList[i].width + " x " + photoSizeList[i].height;
            }

            btnResolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogRes = new AlertDialog.Builder(CollageCameraActivity.this);
                    dialogRes.setTitle("Rozmiar zdjęcia")
                            .setCancelable(true)
                            .setItems(resStrings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    camParams.setPictureSize(photoSizeList[which].width, photoSizeList[which].height);
                                    camera.setParameters(camParams);
                                    Toast.makeText(CollageCameraActivity.this, "Zmieniono rozdzielczość na " + resStrings[which], Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }
            });

            btnWhiteBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogWB = new AlertDialog.Builder(CollageCameraActivity.this);
                    dialogWB.setTitle("Balans bieli")
                            .setCancelable(true)
                            .setItems(whiteBalanceLevels, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    camParams.setWhiteBalance(whiteBalanceLevels[which]);
                                    Toast.makeText(CollageCameraActivity.this, "Zmieniono balans bieli na " + whiteBalanceLevels[which], Toast.LENGTH_SHORT).show();
                                    camera.setParameters(camParams);
                                }
                            })
                            .show();
                }
            });

            final String[] expValues = new String[exposureLevels.length];
            for (int i = 0; i < exposureLevels.length; i++) {
                expValues[i] = exposureLevels[i] + "";
            }

            btnExposure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogExp = new AlertDialog.Builder(CollageCameraActivity.this);
                    dialogExp.setTitle("Wartość ekspozycji")
                            .setCancelable(true)
                            .setItems(expValues, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    camParams.setExposureCompensation(exposureLevels[which]);
                                    Toast.makeText(CollageCameraActivity.this, "Zmieniono wartość ekspozycji na " + expValues[which], Toast.LENGTH_SHORT).show();
                                    camera.setParameters(camParams);
                                }
                            })
                            .show();
                }
            });

        }
    }
}
