package com.dmuk.rellik.busstop.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.panorama.Panorama;

/* By Eaun Ballinger 2018 */

public class PanoramaImgCheck extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

  private final static String TAG = PanoramaImgCheck.class.getSimpleName();
  private GoogleApiClient mClient;
  private Intent viewerIntent;
  private String ScanResult;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    mClient = new GoogleApiClient.Builder(this, this, this)
        .addApi(Panorama.API)
        .build();

    Bundle bundle = getIntent().getExtras();
    assert bundle != null;
    ScanResult = bundle.getString("message");

    Log.d("Scan Result is : ", ScanResult);

    mClient.connect();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {

    Uri uri = Uri.parse(ScanResult);
    Panorama.PanoramaApi.loadPanoramaInfo(mClient, uri).setResultCallback(
        result -> {

          if (result.getStatus().isSuccess()) {
            viewerIntent = result.getViewerIntent();
            if (viewerIntent != null) {
              startActivity(viewerIntent);
            }
          } else {
            Log.d("scan fail : ", result.toString());
          }
        });
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause called pan");

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy called pan");

  }

  @Override
  public void onBackPressed() {
    Intent mIntent = new Intent(PanoramaImgCheck.this, MainActivity.class);
    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(mIntent);
    finish();
  }

}
