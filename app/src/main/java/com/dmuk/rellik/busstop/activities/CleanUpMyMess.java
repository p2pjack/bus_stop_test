package com.dmuk.rellik.busstop.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.dmuk.rellik.busstop.Data.MySQLiteHelperAdapter;
import com.dmuk.rellik.busstop.InterfaceForReports.DataForReports;
import com.dmuk.rellik.busstop.R;
import com.dmuk.rellik.busstop.Utills.Utilities;
import com.dmuk.rellik.busstop.adapters.QuestionsAdapterView;
import com.dmuk.rellik.busstop.camera.CameraFragment;
import com.dmuk.rellik.busstop.email.EmailActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CleanUpMyMess extends AppCompatActivity implements DataForReports,
    OnClickListener {


  private static final String IMG_CAPTURE = "capture";

  public QuestionsAdapterView adapter;
  boolean isFABOpen;
  private StringBuilder forTheReport = new StringBuilder();
  private ArrayList<String> failedForReportList = new ArrayList<>();
  private MySQLiteHelperAdapter db;
  private int counta;
  private int countb;
  private String SHIFT = null;
  private String spin_val;
  private String passTheSubject;
  private String passTheBody;
  private RadioButton radioButtonRed, radioButtonBlue, radioButtonYellow, radioButtonDays;
  private TextInputEditText mEditText;
  private FloatingActionButton actionButton, sendActionButton, cameraActionButton;
  private Spinner location_spinner;
  private String[] fillMySpinner = {"PR Hall", "A-block", "B-block", "C-block",
      "Lower mezz", "Upper mezz"};
  private RecyclerView recyclerView;
  private RadioGroup radioGroup;
  private SharedPreferences imgCount;
  private FrameLayout myLayout;

  // TODO : Clean up and make standard !!!!
  /* By Eaun Ballinger 2018 */


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_clean_up_my_mess);
    db = MySQLiteHelperAdapter.getInstance(this);

    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
    StrictMode.setVmPolicy(builder.build());

    // Shift Buttons
    radioGroup = findViewById(R.id.radioGroupShifts);
    radioButtonRed = findViewById(R.id.radioButtonRed);
    radioButtonBlue = findViewById(R.id.radioButtonBlue);
    radioButtonYellow = findViewById(R.id.radioButtonYellow);
    radioButtonDays = findViewById(R.id.radioButtonDays);
    // Get Associate name
    mEditText = findViewById(R.id.editTextAssociate);
    // SPINNER
    location_spinner = findViewById(R.id.spinner_location);
    location_spinner.setPrompt("Location");
    // FAB
    actionButton = findViewById(R.id.myFAButton);
    sendActionButton = findViewById(R.id.myFAButtonSend);
    cameraActionButton = findViewById(R.id.myFAButtonCamera);
    // On click FAB
    actionButton.setOnClickListener(this);
    sendActionButton.setOnClickListener(this);
    cameraActionButton.setOnClickListener(this);
    myLayout = findViewById(R.id.container);
    // Hide FAB
    sendActionButton.hide();
    cameraActionButton.hide();
    actionButton.show();
    radioShiftButton();
    editTextAssociate();

    // Recycler View
    recyclerView = findViewById(R.id.rv);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    String mTABLE = "fiveS";
    adapter = new QuestionsAdapterView(this, db.getAllContacts(mTABLE));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    spinClick();
    cleanUp();
    //hideMyFab();
  }

  private void spinClick() {
    // On click spinner
    location_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        spin_val = fillMySpinner[position];
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        spin_val = null;
      }
    });
    ArrayAdapter<String> spin_adapter = new ArrayAdapter<>(CleanUpMyMess.this,
        android.R.layout
            .simple_spinner_item, fillMySpinner);
    location_spinner.setAdapter(spin_adapter);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    db.ClearMyTable();
  }



  private void createEmailContents() {
    Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat =
        new SimpleDateFormat(" dd/MM/YYYY  @ hh:mm:a", Locale.getDefault());
    String mGetDateAndTime = mSimpleDateFormat.format(mCalendar.getTime());
    passTheSubject = "5S Checks Report " + " " + spin_val + " " + mGetDateAndTime;
    forTheReport.append("Checks done on : ")
        .append(SHIFT)
        .append(" Shift \n");
    forTheReport.append("Checks done by : ")
        .append(mEditText
            .getText()
            .toString())
        .append("\n");
    forTheReport.append("Time & Date of checks : ")
        .append(mGetDateAndTime)
        .append("\n");
    forTheReport.append("Location Checked : ")
        .append(spin_val)
        .append("\n");
    forTheReport.append("\n\n\n");
    if (failedForReportList.size() == 0) {
      forTheReport
          .append("All checks have been passed \n");
    } else {
      for (int loopTheReport = 0; loopTheReport < failedForReportList.size(); loopTheReport++) {
        forTheReport
            .append(failedForReportList
                .get(loopTheReport));
        forTheReport
            .append("\n");
      }
    }
    forTheReport
        .append("\n\n\n");
    forTheReport
        .append(" CSV file has been generated and attached \n\n");
    if (Utilities.countTheFiles() == 1){
      forTheReport
          .append(" ")
          .append(Utilities.countTheFiles()
              .toString()).append(" Picture has been taken and attached  \n");
    }else if (Utilities.countTheFiles() > 1){
      forTheReport
          .append(" ")
          .append(Utilities.countTheFiles()
              .toString())
          .append(" Pictures have been taken and attached  \n");
    }else {
      forTheReport
          .append(" No pictures taken \n");
    }
    forTheReport
        .append(" Report created by the bus stop viewer app \n By E.Ballinger ");
    passTheBody = forTheReport.toString();
    Long l = Utilities.countTheFiles();
    Log.d("File Count is : ", String.valueOf(l));
    cleanUp();
  }

  private void cleanUp() {
    forTheReport.setLength(0);
    failedForReportList.clear();
    mEditText.setText("");
    radioGroup.clearCheck();
    imgCount = getSharedPreferences(IMG_CAPTURE, 0);
    SharedPreferences.Editor mMEditor = imgCount.edit();
    mMEditor.putInt(IMG_CAPTURE, 1);
    mMEditor.apply();
  }

  // Show and hide Send FAB
  private void showFABMenu() {
    isFABOpen = true;
    actionButton.animate()
        .translationY(-getResources()
            .getDimension(R.dimen.standard_55));
    sendActionButton.animate()
        .translationY(-getResources()
            .getDimension(R.dimen.standard_105));
    sendActionButton.show();
    cameraActionButton.animate()
        .translationY(-getResources()
            .getDimension(R.dimen.standard_155));
    cameraActionButton.show();
  }

  private void closeFABMenu() {
    isFABOpen = false;
    actionButton.animate()
        .translationY(0);
    sendActionButton.animate()
        .translationY(0);
    sendActionButton.hide();
    cameraActionButton.animate()
        .translationY(0);
    cameraActionButton.hide();
  }

  // Get the clicks
  @Override
  public void onClick(View v) {
    int id = v.getId();
    switch (id) {
      case R.id.myFAButton:
        if (!isFABOpen) {
          sendActionButton.show();
          cameraActionButton.show();
          showFABMenu();

        } else {
          closeFABMenu();
        }

        break;
      case R.id.myFAButtonSend:
        checkEverythingOff();
        closeFABMenu();
        break;
      case R.id.myFAButtonCamera:
        closeFABMenu();
        checkIfChanged();
       getSharedState();
        break;
    }
  }

  private void moveToEmail() {
    Intent mIntent = new Intent(this, EmailActivity.class);
    mIntent.putExtra("sub", passTheSubject);
    mIntent.putExtra("body", passTheBody);
    startActivity(mIntent);
    finish();
  }

  // TODO : Count the images
  private void getCamera() {
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container, CameraFragment.newInstance())
        .commit();
  }

  private void getSharedState() {
    imgCount = getSharedPreferences(IMG_CAPTURE, 0);
    int mImgIntA = imgCount.getInt(IMG_CAPTURE, 0);
    if (Utilities.countTheFiles() < 5) {
      getCamera();
    } else if (Utilities.countTheFiles() == 5) {
      Toast.makeText(this, "Five Images captured", Toast.LENGTH_SHORT).show();
    }
  }
  // Is it ok ?
  private void checkEverythingOff() {
    if (Utilities.getMyCountAtTheTable("'report'")
        != Utilities.getMyCountAtTheTable("'fiveS'")) {
      Toast.makeText(this,
          "Have all the questions been answered ??",
          Toast.LENGTH_SHORT).show();
    } else {
      db.exportDB();
      db.ClearMyTable();
      sendActionButton.hide();
      closeFABMenu();
      createEmailContents();
      moveToEmail();
    }
    if (Utilities.getMyCountAtTheTable("'report'")
        > Utilities.getMyCountAtTheTable("'fiveS'")) {
      db.ClearMyTable();
      Toast.makeText(this,
          "Over sized table ",
          Toast.LENGTH_SHORT).show();
    }
  }
  // TODO: 10/08/2018 Make it work
  private void hideMyFab() {

    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
          if (actionButton.getVisibility() == View.INVISIBLE) {
            actionButton.show();
          }
        }
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0 && actionButton.getVisibility() == View.VISIBLE) {
          actionButton.hide();
          closeFABMenu(); }
       else if (dy < 0 && actionButton.getVisibility() != View.VISIBLE) {
          actionButton.show();
        }
      }
    });
  }

  // Get a name
  private void editTextAssociate() {
    try {
      mEditText.setOnKeyListener((v, keyCode, event) -> {
        // If the event is a key-down event on the "enter" button
        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
            (keyCode == KeyEvent.KEYCODE_ENTER)) {
          Utilities.hideKeyboard(this);
          return true;
        }
        return false;
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // What shift
  public void radioShiftButton() {
    radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
      View radioButton = radioGroup.findViewById(checkedId);
      int index = radioGroup.indexOfChild(radioButton);
      // Add logic here or not
      try {
        switch (index) {
          case 0:
            SHIFT = radioButtonDays.getText().toString();
            break;
          case 1:
            SHIFT = radioButtonBlue.getText().toString();
            break;
          case 2:
            SHIFT = radioButtonYellow.getText().toString();
            break;
          case 3:
            SHIFT = radioButtonRed.getText().toString();
            break;
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  private void checkIfChanged(){

    myLayout.addOnLayoutChangeListener(
        (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
          if (left < 1 ){
            actionButton.hide();
          }else {
            actionButton.show();
          }
        });
  }



  @Override
  public void onBackPressed() {
    Intent mIntent = new Intent(CleanUpMyMess.this, MainActivity.class);
    startActivity(mIntent);
    finish();
  }

  @Override
  public void IdataForTheReport(int position, String CheckedMessage, String CheckedLevel,
      Boolean IsItChecked, String theDefault) {
    String NewId = String.valueOf(position);
    String answer = IsItChecked.toString();
    String mNewAssociate = mEditText.getText().toString();
    /*
    Fill and check the array
     */
    if (!theDefault.matches(IsItChecked.toString())) {
      try {
        failedForReportList.add(CheckedMessage + " "
            + ":  Has failed the checks");
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      if (failedForReportList.contains(CheckedMessage + " "
          + ":  Has failed the checks")) {
        failedForReportList.remove(CheckedMessage + " "
            + ":  Has failed the checks");
      }
    }
    Log.d("Failed = :", failedForReportList.toString());

    if (SHIFT == null | mNewAssociate.equals("") | spin_val == null) {

      if (TextUtils.isEmpty(mNewAssociate)) {
        mEditText.setError("No Associate ? ");
      }
      Utilities.alertSomeoneDialog(this,"Shift or Associate Missing");
    } else {

      try {
        db.DoesItExist(NewId, SHIFT, mNewAssociate, CheckedMessage, answer);
      } catch (Exception e) {

        e.printStackTrace();
        Log.d("Update Report :", String.valueOf(e));
      }

    }

    Log.d("Has it been clicked : ", NewId + " " + SHIFT + " " + mNewAssociate + " "
        + CheckedMessage + " " + CheckedLevel + " " + IsItChecked);

  }
  @Override
  public void ideleteUnclickedForReport(int ID) {
    db.DeleteUncheckedFromReport(ID);
  }
}
