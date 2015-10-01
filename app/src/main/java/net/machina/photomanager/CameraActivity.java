package net.machina.photomanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.machina.photomanager.common.CameraPreview;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("deprecation")
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

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
            final Camera thisCamera = camera;

            layoutCamSettings.setVisibility(View.GONE);
            btnShutter.setVisibility(View.GONE);
            btnAccept.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);

            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutCamSettings.setVisibility(View.VISIBLE);
                    btnShutter.setVisibility(View.VISIBLE);
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    thisCamera.startPreview();
                }
            });

            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String filename = dFormat.format(new Date());
                    File newPhoto = new File(path + "/" + filename + ".jpg");
                    try {
                        FileOutputStream fos = new FileOutputStream(newPhoto);
                        fos.write(thisData);
                        fos.close();
                        Toast.makeText(CameraActivity.this, "Zapisano zdjęcie!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(CameraActivity.this, "Nie można było zapisać zdjęcia", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    layoutCamSettings.setVisibility(View.VISIBLE);
                    btnShutter.setVisibility(View.VISIBLE);
                    btnAccept.setVisibility(View.GONE);
                    btnReject.setVisibility(View.GONE);
                    thisCamera.startPreview();
                }
            });
        }
    };

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (getIntent().getStringExtra("path") == null) return;
        path = getIntent().getStringExtra("path");
        btnShutter = (ImageView) findViewById(R.id.btnShutter);
        layoutCamSettings = (LinearLayout) findViewById(R.id.layoutCameraTop);
        btnAccept = (ImageView) findViewById(R.id.btnAccept);
        btnReject = (ImageView) findViewById(R.id.btnReject);
        btnExposure = (ImageView) findViewById(R.id.btnExposure);
        btnWhiteBalance = (ImageView) findViewById(R.id.btnWhiteBalance);
        btnResolution = (ImageView) findViewById(R.id.btnResolution);
        if (btnShutter != null) btnShutter.setOnClickListener(this);
        initCamera();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnShutter:
                if (camera != null) camera.takePicture(null, null, jpegCallback);
                break;
            case R.id.layoutCameraPreview:
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
            preview = new CameraPreview(CameraActivity.this, camera);
            cameraPrev = (FrameLayout) findViewById(R.id.layoutCameraPreview);
            if (cameraPrev != null) {
                cameraPrev.setOnClickListener(this);
                cameraPrev.addView(preview);
                setCameraDisplayOrientation(this, cameraId, camera);
                getCameraParameters();

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
        if(camera != null) {
            camParams = camera.getParameters();
            exposureLevels = new int[((camParams.getMaxExposureCompensation() * 2) + 1)];
            int j = 0;
            for(int i = camParams.getMinExposureCompensation(); i <= camParams.getMaxExposureCompensation(); i++) {
                exposureLevels[j++] = i;
            }

            whiteBalanceLevels = new String[camParams.getSupportedWhiteBalance().size()];
            photoSizeList = new Camera.Size[camParams.getSupportedPictureSizes().size()];

            camParams.getSupportedWhiteBalance().toArray(whiteBalanceLevels);
            camParams.getSupportedPictureSizes().toArray(photoSizeList);

            final String[] resStrings = new String[photoSizeList.length];
            for(int i = 0; i < photoSizeList.length; i++) {
                resStrings[i] = photoSizeList[i].width + " x " + photoSizeList[i].height;
            }

            btnResolution.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogRes = new AlertDialog.Builder(CameraActivity.this);
                    dialogRes   .setTitle("Rozmiar zdjęcia")
                                .setCancelable(true)
                                .setItems(resStrings, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        camParams.setPictureSize(photoSizeList[which].width, photoSizeList[which].height);
                                        camera.setParameters(camParams);
                                        Toast.makeText(CameraActivity.this, "Zmieniono rozdzielczość na " + resStrings[which], Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                }
            });

            btnWhiteBalance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogWB = new AlertDialog.Builder(CameraActivity.this);
                    dialogWB.setTitle("Balans bieli")
                            .setCancelable(true)
                            .setItems(whiteBalanceLevels, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    camParams.setWhiteBalance(whiteBalanceLevels[which]);
                                    Toast.makeText(CameraActivity.this, "Zmieniono balans bieli na " + whiteBalanceLevels[which], Toast.LENGTH_SHORT).show();
                                    camera.setParameters(camParams);
                                }
                            })
                            .show();
                }
            });

            final String[] expValues = new String[exposureLevels.length];
            for(int i = 0; i < exposureLevels.length; i++) {
                expValues[i] = exposureLevels[i] + "";
            }

            btnExposure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogExp = new AlertDialog.Builder(CameraActivity.this);
                    dialogExp   .setTitle("Wartość ekspozycji")
                                .setCancelable(true)
                                .setItems(expValues, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        camParams.setExposureCompensation(exposureLevels[which]);
                                        Toast.makeText(CameraActivity.this, "Zmieniono wartość ekspozycji na " + expValues[which], Toast.LENGTH_SHORT).show();
                                        camera.setParameters(camParams);
                                    }
                                })
                                .show();
                }
            });

        }
    }
}
