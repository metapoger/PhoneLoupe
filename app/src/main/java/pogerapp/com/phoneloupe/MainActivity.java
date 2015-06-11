package pogerapp.com.phoneloupe;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;



public class MainActivity extends Activity {

    CameraPreview mPreview;
    int maxZoom;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 0);
        setContentView(R.layout.activity_main);
        if(checkCameraHardware(getApplicationContext())){
            final Camera mCamera = getCameraInstance();
            mCamera.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            maxZoom = mCamera.getParameters().getMaxZoom();
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPreview.focus();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "asdfadsfasdf", Toast.LENGTH_SHORT).show();
            finish();
        }
        SeekBar seekBar = (SeekBar)findViewById(R.id.zoom_seek_bar);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mPreview != null) {
                    mPreview.zoom(i * maxZoom / 100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mPreview != null) {
                    mPreview.focus();
                }
            }
        });

        Button flashLightButton = (Button)findViewById(R.id.flashlight);
        flashLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreview.setFlashLight(!mPreview.isFlashlight);
            }
        });

    }


    @SuppressWarnings("deprecation")
    public static Camera getCameraInstance(){
        Camera camera = null;

        try {
            camera = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return camera; // returns null if camera is unavailable
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
