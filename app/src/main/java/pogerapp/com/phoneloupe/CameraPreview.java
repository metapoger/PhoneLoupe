package pogerapp.com.phoneloupe;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by antonurivskiy on 19/05/15.
 */
@SuppressWarnings("ALL")
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera;
	Context context;
	public boolean isFront;
	public boolean isFlashlight = false;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		this.context = context;
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Camera.Parameters params = mCamera.getParameters();

		if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
			params.set("orientation", "portrait");
			mCamera.setDisplayOrientation(90);
			mCamera.autoFocus(null);
		}
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			Log.d("camera preview", "Error setting camera preview: " + e.getMessage());
		}
	}

	public void zoom(int zoomLevel) {
		Camera.Parameters parameters = mCamera.getParameters();
		int maxZoom = parameters.getMaxZoom();
		if (parameters.isZoomSupported()) {

			if (zoomLevel >= 0 && zoomLevel < maxZoom) {
				try {
					parameters.setZoom(zoomLevel);
					mCamera.setParameters(parameters);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				Log.e("log", "Camera failed to zoom: ");
			}
		}

	}

	public void focus() {
		mCamera.autoFocus(null);
	}

	public void setFlashLight(boolean isFlashlight){
		this.isFlashlight = isFlashlight;
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			if(isFlashlight) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			}else{
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			}
			mCamera.setParameters(parameters);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
		}

		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();

		} catch (Exception e) {
			Log.d("camera preview", "Error starting camera preview: " + e.getMessage());
		}
	}
}