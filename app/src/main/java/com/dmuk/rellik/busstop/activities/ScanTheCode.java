package com.dmuk.rellik.busstop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.dmuk.rellik.busstop.R;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/* By Eaun Ballinger 2018 */

public class ScanTheCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

  private final static String TAG = ScanTheCode.class.getSimpleName();
  public String myResult;
  private ZXingScannerView scannerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan_the_code);
    //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    scannerView = new ZXingScannerView(this);
    setContentView(scannerView);

  }

  @Override
  public void handleResult(Result result) {
    myResult = result.getText();
    Log.d("QRCodeScanner", result.getText());
    Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

    Intent intent = new Intent(ScanTheCode.this, PanoramaImgCheck.class);
    intent.putExtra("message", myResult);
    startActivity(intent);

  }

  @Override
  public void onResume() {
    super.onResume();

    if (scannerView == null) {
      scannerView = new ZXingScannerView(this);
      setContentView(scannerView);
    }
    scannerView.setResultHandler(this);
    scannerView.startCamera();
  }


  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause called scan");

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    scannerView.stopCamera();
    Log.d(TAG, "onDestroy called scan");
    Intent mIntent = new Intent(ScanTheCode.this, MainActivity.class);
    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(mIntent);
    finish();

  }

  @Override
  public void onBackPressed() {
    Intent mIntent = new Intent(ScanTheCode.this, MainActivity.class);
    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(mIntent);
    finish();
  }

}
