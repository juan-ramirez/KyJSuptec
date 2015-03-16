package app.kyjsuptec.kjingenieros;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holdMe;
    private Camera theCamera;

    public ShowCamera(Context context, Camera camera) {
        super(context);
        theCamera = camera;
        holdMe = getHolder();
        holdMe.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            theCamera.setPreviewDisplay(holder);
            theCamera.setDisplayOrientation(90);
            theCamera.startPreview();
        } catch (IOException e) {
            Log.e("IO - Exc", e.toString());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

}