package app.kyjsuptec.kjingenieros.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import app.kyjsuptec.kjingenieros.R;
import app.kyjsuptec.kjingenieros.helpers.ShowCamera;

public class CameraActivity extends Activity {

    private Camera cameraObject;
    private ShowCamera showCamera;
    private Bitmap bitmap;
    private Intent intent = new Intent();
    private Button buttonFotoTomada;
    private Button buttonTomarFoto;
    private Button buttonFotoOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ActionBar actionBar = getActionBar();
        setTitle("Tomar foto");
        actionBar.setDisplayHomeAsUpEnabled(true);

        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);

        buttonFotoTomada = (Button) findViewById(R.id.buttonFotoTomada);
        buttonTomarFoto = (Button) findViewById(R.id.buttonTomarFoto);
        buttonFotoOK = (Button) findViewById(R.id.buttonFotoOK);

    }

    public static Camera isCameraAvailiable() {
        Camera object = null;
        try {
            object = Camera.open();
        } catch (Exception e) {
            Log.e("Cam - Exc", e.toString());
        }
        return object;
    }

    private PictureCallback capturedIt = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                Toast.makeText(getApplicationContext(), "not taken",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Foto tomada",
                        Toast.LENGTH_SHORT).show();

                intent.putExtra("pic", data);
                //intent.putExtra("pic", "Juan");

                setResult(RESULT_OK, intent);

            }
            buttonFotoTomada.setVisibility(View.VISIBLE);
            buttonFotoOK.setVisibility(View.VISIBLE);
            buttonTomarFoto.setVisibility(View.INVISIBLE);
        }

    };

    public void snapIt(View view) throws InterruptedException {
        cameraObject.takePicture(null, null, capturedIt);

    }

    public void snapOther(View view) {
        cameraObject.startPreview();
        buttonFotoTomada.setVisibility(View.INVISIBLE);
        buttonFotoOK.setVisibility(View.INVISIBLE);
        buttonTomarFoto.setVisibility(View.VISIBLE);

    }

    public void sendPhoto(View view) {
        cameraObject.release();
        finish();

    }

    @Override
    public void onBackPressed() {
        setResult(4, null);
        cameraObject.release();
        super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(4, null);
                cameraObject.release();
                finish();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

}
