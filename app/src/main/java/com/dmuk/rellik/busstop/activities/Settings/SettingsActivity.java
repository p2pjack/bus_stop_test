package com.dmuk.rellik.busstop.activities.Settings;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.dmuk.rellik.busstop.R;

public class SettingsActivity extends PreferenceActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
  }
}
