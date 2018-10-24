package demo.face.comi.io.camerademogoogle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

import demo.face.comi.io.camerademogoogle.activity.CameraActivity;
import demo.face.comi.io.camerademogoogle.camera.CameraUtils;
/**
 * Description：
 * Author: star
 * Email: guimingxing@163.com
 * Date: 2018-10-24 9:33
 */

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=this;
    }

    /**
     * 先检测是否有拍照权限
     * @param view
     */
    public void show(View view){
        Acp.getInstance(mContext).request(new AcpOptions.Builder()
                        .setPermissions(Manifest.permission.CAMERA)
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        Intent intent=new Intent(mContext, CameraActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(mContext, permissions.toString() + "权限拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
