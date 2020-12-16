package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS;

public class MainActivity extends AppCompatActivity {
    private final int CODE_FOR_WRITE_PERMISSION=1;
    private final int CODE_FOR_CAMERA_PERMISSION=2;
    private String[] Permissions=
            new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA};
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for(int i=0;i<2;i++){
            //申请权限
            if (checkSelfPermission(Permissions[i])!=PackageManager.PERMISSION_GRANTED) {
                requestPermissions(Permissions,CODE_FOR_WRITE_PERMISSION );
            }
        }
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<2;i++){
                    //容许你申请返回true，静止不在提示或者权限通过返回false,第一次没申请过返回false
                    if(shouldShowRequestPermissionRationale(Permissions[i])){
                        Toast.makeText(MainActivity.this,"没有"+Permissions[i]+"权限",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //通过requestCode来识别是否同一个请求
        Log.d("TAG", "onRequestPermissionsResult: "+permissions.length);
        if (requestCode == CODE_FOR_WRITE_PERMISSION){
            List<String> denied=new ArrayList<>();
            List<String> deniedAndNeverAskAgain =new ArrayList<>();
            boolean f=true;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                    f=false;
                    if(shouldShowRequestPermissionRationale(Permissions[i])){
                        denied.add(Permissions[i]);
                    }else{
                        deniedAndNeverAskAgain.add(Permissions[i]);
                    }
                }
            }
            if(f){
                ((Button)findViewById(R.id.button)).setText("lyn");
            }
            if (!denied.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setMessage("message")
                        .setPositiveButton("OK",
                                myrequestPermissions)
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }
            if(!deniedAndNeverAskAgain.isEmpty()){
                Toast.makeText(MainActivity.this,"有未授予的权限",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 1);
            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private DialogInterface.OnClickListener myrequestPermissions=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA},CODE_FOR_WRITE_PERMISSION );

        }
    };
}