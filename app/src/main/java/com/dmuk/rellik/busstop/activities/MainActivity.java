package com.dmuk.rellik.busstop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.dmuk.rellik.busstop.R;

/* By Eaun Ballinger 2018 */

public class MainActivity extends AppCompatActivity {


  private static final int TIME_INTERVAL = 500;
  SharedPreferences settings;
  private Button busStop, Cleanup,Kaizen;
  private long mBackPressed;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

    settings = getSharedPreferences("prefs", 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putBoolean("firstRun", true);
    editor.apply();

    busStop = findViewById(R.id.button_bus_top);
    Cleanup = findViewById(R.id.button_5s_check);
    Kaizen = findViewById(R.id.button_tpm_checks);

    GetMyClick();
  }

  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  public boolean onOptionsItemSelected(MenuItem item) {

    //respond to menu item selection

    Toast.makeText(this,"You clicked me !!",Toast.LENGTH_SHORT).show();

    return super.onOptionsItemSelected(item);
  }

  private void GetMyClick() {

    busStop.setOnClickListener(v -> {
      Intent myIntent = new Intent(MainActivity.this, ScanTheCode.class);
      MainActivity.this.startActivity(myIntent);
      finish();
    });

    Cleanup.setOnClickListener(v -> {
      Intent myIntent = new Intent(MainActivity.this, CleanUpMyMess.class);
      MainActivity.this.startActivity(myIntent);
      finish();
    });

    Kaizen.setOnClickListener(v -> {
      Intent myIntent = new Intent(MainActivity.this, KaizenActivity.class);
      MainActivity.this.startActivity(myIntent);
      finish();
    });
  }

  @Override
  public void onBackPressed() {

/*    new AlertDialog.Builder(this)
        .setTitle("Really Exit?")
        .setMessage("Are you sure you want to exit?")
        .setNegativeButton(android.R.string.no, null)
        .setPositiveButton(android.R.string.yes,
            (arg0, arg1) -> MainActivity.super.onBackPressed())
        .create()
        .show();*/
  }
}
