package com.dmuk.rellik.busstop.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.dmuk.rellik.busstop.R;

public class KaizenActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_kaizen);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();

    Intent myIntent = new Intent(this, MainActivity.class);
    this.startActivity(myIntent);
    finish();
  }
}
