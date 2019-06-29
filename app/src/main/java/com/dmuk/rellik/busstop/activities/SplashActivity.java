package com.dmuk.rellik.busstop.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.dmuk.rellik.busstop.Data.MySQLiteHelperAdapter;
import com.dmuk.rellik.busstop.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.wangyuwei.particleview.ParticleView;

/*
  Created by Eaun-Ballinger on 11/07/2018.
  Splash screen to setup background files and permissions
  Create the database / import CSV files on setup
  No modification of this code is allowed without permission

 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SplashActivity extends AppCompatActivity {

  private final static String TAG = MainActivity.class.getSimpleName();
  private ParticleView mPv1;
  private MySQLiteHelperAdapter db;
  private String[] directory = {"My Bus Stop", "My Bus Stop/img", "My Bus Stop/video",
      "My Bus Stop/pdf", "My Bus Stop/questions", "My Bus Stop/export report", "My Bus Stop/saved"
      + " img"};
  private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
      Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,
      Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE,
      Manifest.permission.ACCESS_WIFI_STATE};
  private Context mContext;
  private SharedPreferences mSettings;
  private boolean firstRun;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    db = MySQLiteHelperAdapter.getInstance(this);
    mSettings = getSharedPreferences("prefs", 0);
    GetMyPermission();
    ani1();
    StartAnimations();

    if (!firstRun)//if running for first time
    //Splash will load for first time
    {
      db.CSV();
      MyDirectory();
      SetTheFlag();
      ani2();
    } else {
      MoveToMain();
    }

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    //finish();
  }

  private void SetTheFlag() {
    boolean firstRun = mSettings.getBoolean("firstRun", false);
  }


  private void MyDirectory() {
    for (String Directory : directory) {
      File f = new File(Environment.getExternalStorageDirectory(), Directory);

      if (!f.exists()) {
        Log.d(TAG, "Folder doesn't exist, creating it...");
        boolean rv = f.mkdir();
        Log.d(TAG, "Folder creation " + (rv ? "success" : "failed"));
      } else {
        Log.d(TAG, "Folder already exists.");
      }

    }


  }


  private void GetMyPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (arePermissionsEnabled()) {
        //                   permissions granted, continue flow normally
        Log.d("permissions :", Arrays.toString(permissions));
      } else {
        requestMultiplePermissions();
      }
    }

  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  private boolean arePermissionsEnabled() {
    for (String permission : permissions) {
      if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    Log.d("Permission granted :", Arrays.toString(permissions));
    return true;
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  private void requestMultiplePermissions() {
    List<String> remainingPermissions = new ArrayList<>();
    for (String permission : permissions) {
      if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        remainingPermissions.add(permission);
      }
    }
    requestPermissions(remainingPermissions.toArray(new String[remainingPermissions.size()]),
        101);
  }


  @TargetApi(Build.VERSION_CODES.M)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == 101) {
      for (int i = 0; i < grantResults.length; i++) {
        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
          if (shouldShowRequestPermissionRationale(permissions[i])) {
            new AlertDialog.Builder(this)
                .setMessage("Your error message here")
                .setPositiveButton("Allow",
                    (dialog, which) -> requestMultiplePermissions())
                .setNegativeButton("Cancel",
                    (dialog, which) -> dialog.dismiss())
                .create()
                .show();
          }
          return;
        }
      }
      //all is good, continue flow
    }
  }


  /**
   * Make intent to move to MainActivity
   */

  private void ani1() {
    mPv1 = findViewById(R.id.pv1);
    mPv1.postDelayed(() -> mPv1.startAnim(), 800);
  }

  private void ani2() {
    new Handler().postDelayed(this::MoveToMain, 4500);
  }

  private void MoveToMain() {
 /*   Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
    finish();*/
    startActivity(new Intent(SplashActivity.this, MainActivity.class));
    finish();


  }

  private void StartAnimations() {
    Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
    anim.reset();
    RelativeLayout l = findViewById(R.id.lin_lay);
    l.clearAnimation();
    l.startAnimation(anim);

    anim = AnimationUtils.loadAnimation(this, R.anim.translate);
    anim.reset();
    ImageView iv = findViewById(R.id.splash);
    iv.clearAnimation();
    iv.startAnimation(anim);

    anim = AnimationUtils.loadAnimation(this, R.anim.push_up_out);
    anim.reset();
    TextView ivv = findViewById(R.id.MyName);
    ivv.clearAnimation();
    ivv.startAnimation(anim);
  }
}
