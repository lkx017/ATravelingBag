package com.example.admin.tripapplication.cameraAction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.admin.tripapplication.R;
import com.example.admin.tripapplication.camera.CameraSourcePreview;
import com.example.admin.tripapplication.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

public final class FaceActivity extends AppCompatActivity {
    private static final String TAG = "FaceActivity";
    private static final int RC_HANDLE_GMS = 9001;
    private static final int Permission_CODE = 255;

    private CameraSource mCameraSource = null;
    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;
    private boolean mIsFrontFacing = true;

    LinearLayout ll1, ll2;
    ImageButton[] btns = new ImageButton[7];
    static Spinner sp1;
    static int select = 0;
    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called.");

        setContentView(R.layout.activity_face);

        mPreview = findViewById(R.id.preview);
        mGraphicOverlay = findViewById(R.id.faceOverlay);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        sp1 = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.locate, android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter);

        for (int i = 0; i < 7; i++) {
            int id = getResources().getIdentifier("btn" + (i + 1), "id", "com.example.admin.tripapplication");
            btns[i] = findViewById(id);
            btns[i].setOnClickListener(onClickListener);
        }

        if (savedInstanceState != null) {
            mIsFrontFacing = savedInstanceState.getBoolean("IsFrontFacing");
        }

        // Start using the camera if permission has been granted to this app,
        // otherwise ask for permission to use it.
        int r1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int r2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int r3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (r1 == PackageManager.PERMISSION_GRANTED && r2 == PackageManager.PERMISSION_GRANTED && r3 == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestPermission();
        }

        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(context);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            gpsOn();
        }
    }

    private void gpsOn() {
        Intent gps = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gps);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn1:
                    mIsFrontFacing = !mIsFrontFacing;
                    if (mCameraSource != null) {
                        mCameraSource.release();
                        mCameraSource = null;
                    }

                    createCameraSource();
                    startCameraSource();
                    break;
                case R.id.btn2:
                    mPreview.setDrawingCacheEnabled(true);
                    mCameraSource.takePicture(null, pictureCallback);
                    mPreview.setDrawingCacheEnabled(false);
                    break;
                case R.id.btn3:
                    if (ll1.getVisibility() == View.VISIBLE) {
                        ll1.setVisibility(View.GONE);
                    } else {
                        ll1.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.btn4:
                    Intent intent = new Intent(FaceActivity.this, LocationService.class);
                    startActivity(intent);
                    select = 0;
                    break;
                case R.id.btn5:
                    select = 1;
                    break;
                case R.id.btn6:
                    select = 2;
                    break;
                case R.id.btn7:
                    select = 3;
                    break;
            }
        }
    };

    CameraSource.PictureCallback pictureCallback = new CameraSource.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes) {
            mPreview.buildDrawingCache();
            Bitmap b2 = mPreview.getDrawingCache();

            Bitmap b1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            if(b1.getHeight()<b1.getWidth()) {
                Matrix matrix1 = new Matrix();
                if(mIsFrontFacing==true) {
                    matrix1.postRotate(-90);
                } else {
                    matrix1.postRotate(90);
                }
                b1 = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight(), matrix1, true);
            }

            if(mIsFrontFacing==true) {
                float[] mirrorY = {
                        -1, 0, 0,
                        0, 1, 0,
                        0, 0, 1
                };

                Matrix matrix2 = new Matrix();
                matrix2.setValues(mirrorY);
                b1 = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight(), matrix2, true);
            }


            Bitmap newb2 = Bitmap.createScaledBitmap(b2, b2.getWidth(), b2.getHeight() - 650, true);
            b1 = Bitmap.createScaledBitmap(b1, newb2.getWidth(), newb2.getHeight(), true);

            Bitmap b3 = Bitmap.createBitmap(b1.getWidth()-220, b1.getHeight()-10, b1.getConfig());
            Canvas canvas = new Canvas(b3);
            canvas.drawBitmap(b1, 0, 0, null);
            canvas.drawBitmap(newb2, -220, -10, null);

            // bitmap 이미지를 이용해 앨범에 저장
            String outUriStr = MediaStore.Images.Media.insertImage(getContentResolver(), b3, "Captured Image", "Captured Image using Camera.");

            if (outUriStr == null) {
                Log.d("tiger", "Image insert failed.");
                return;
            } else {
                Uri outUri = Uri.parse(outUriStr);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outUri));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called.");

        startCameraSource();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPreview.stop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("IsFrontFacing", mIsFrontFacing);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    // Handle camera permission requests
    // =================================

    private void requestPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, Permission_CODE);
            return;
        }

        final Activity thisActivity = this;
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions, Permission_CODE);
            }
        };
        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != Permission_CODE) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // We have permission to access the camera, so create the camera source.
            Log.d(TAG, "Camera permission granted - initializing camera source.");
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length + " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.disappointed_ok, listener)
                .show();
    }

    // Camera source
    // =============

    private void createCameraSource() {
        Log.d(TAG, "createCameraSource called.");

        Context context = getApplicationContext();
        FaceDetector detector = createFaceDetector(context);

        int facing = CameraSource.CAMERA_FACING_FRONT;
        if (!mIsFrontFacing) {
            facing = CameraSource.CAMERA_FACING_BACK;
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setFacing(facing)
                .setRequestedPreviewSize(1024, 768)
                .setRequestedFps(60.0f)
                .setAutoFocusEnabled(true)
                .build();
    }

    private void startCameraSource() {
        Log.d(TAG, "startCameraSource called.");

        // Make sure that the device has Google Play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    // Face detector
    // =============

    @NonNull
    private FaceDetector createFaceDetector(final Context context) {
        Log.d(TAG, "createFaceDetector called.");

        FaceDetector detector = new FaceDetector.Builder(context)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .setTrackingEnabled(true)
                .setMode(FaceDetector.FAST_MODE)
                .setProminentFaceOnly(mIsFrontFacing)
                .setMinFaceSize(mIsFrontFacing ? 0.35f : 0.15f)
                .build();

        MultiProcessor.Factory<Face> factory = new MultiProcessor.Factory<Face>() {
            @Override
            public Tracker<Face> create(Face face) {
                return new FaceTracker(mGraphicOverlay, context, mIsFrontFacing);
            }
        };

        Detector.Processor<Face> processor = new MultiProcessor.Builder<>(factory).build();
        detector.setProcessor(processor);

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");

            // Check the device's storage.  If there's little available storage, the native
            // face detection library will not be downloaded, and the app won't work,
            // so notify the user.
            IntentFilter lowStorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowStorageFilter) != null;

            if (hasLowStorage) {
                Log.w(TAG, getString(R.string.low_storage_error));
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.app_name)
                        .setMessage(R.string.low_storage_error)
                        .setPositiveButton(R.string.disappointed_ok, listener)
                        .show();
            }
        }
        return detector;
    }

}