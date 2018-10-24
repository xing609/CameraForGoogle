package demo.face.comi.io.camerademogoogle.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.IOException;

import demo.face.comi.io.camerademogoogle.R;
import demo.face.comi.io.camerademogoogle.bean.CameraFacing;
import demo.face.comi.io.camerademogoogle.camera.CameraManager;
import demo.face.comi.io.camerademogoogle.camera.CameraUtils;
import demo.face.comi.io.camerademogoogle.view.CameraSurfaceView;

/**
 * Description：调手机前后置摄像头
 * Author: star
 * Email: guimingxing@163.com
 * Date: 2018-10-24 9:32
 */

public class CameraActivity extends Activity {
    private final static String TAG="CameraActivity";
    private Button btnChange;
    private FrameLayout camera_preview;
    private CameraSurfaceView cameraSurfaceView;
    private CameraManager mCameraManager;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mContext=this;
        camera_preview = (FrameLayout) findViewById(R.id.camera_preview);
        btnChange=(Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnChange.getText().equals("前置")){
                    btnChange.setText("后置");
                    openCamera(CameraFacing.FRONT);//需要在子线程中操作
                }else {
                    btnChange.setText("前置");
                    openCamera(CameraFacing.BACK);//需要在子线程中操作
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume...");
        openCamera(CameraFacing.BACK);//需要在子线程中操作
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onPause...");
        cameraSurfaceView.onPause();
    }

    /**
     * 初始化相机，主要是打开相机，并设置相机的相关参数，并将holder设置为相机的展示平台
     */
    private void openCamera(CameraFacing cameraFacing) {
        if(CameraUtils.checkCameraHardware(mContext)){
            mCameraManager = new CameraManager(this);
            if (mCameraManager.isOpen()) {
                Log.w(TAG, "surfaceCreated: 相机已经被打开了");
                return;
            }
            try {
                mCameraManager.openCamera(cameraFacing);
            } catch (IOException e) {
                e.printStackTrace();
            }
            relayout();
            cameraSurfaceView.onResume();
        }else{
            Toast.makeText(mContext,"该手机不支持摄像头！",Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 设置界面展示大小
     */
    private void relayout() {
        // Create our Preview view and set it as the content of our activity.
        cameraSurfaceView = new CameraSurfaceView(this,mCameraManager);
        Point previewSizeOnScreen = mCameraManager.getConfigurationManager().getPreviewSizeOnScreen();//相机预览尺寸
        Point screentPoint=mCameraManager.getConfigurationManager().getScreenResolution();//自己展示相机预览控件所能设置最大值
        Point point = CameraUtils.calculateViewSize(previewSizeOnScreen, screentPoint);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(point.x,point.y);
        layoutParams.gravity= Gravity.CENTER;
        cameraSurfaceView.setLayoutParams(layoutParams);
        camera_preview.addView(cameraSurfaceView);
    }
}
