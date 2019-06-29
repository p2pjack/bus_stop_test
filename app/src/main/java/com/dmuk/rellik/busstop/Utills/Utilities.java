package com.dmuk.rellik.busstop.Utills;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.dmuk.rellik.busstop.ApplicationContextProvider;
import com.dmuk.rellik.busstop.Data.MySQLiteHelperAdapter;
import com.dmuk.rellik.busstop.InterfaceForReports.GenerateTheReportBuilder;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Utilities {


  // Empty constructor
  public Utilities() {
  }

  // Count the files
  public static Long countTheFiles() {
    File dir = new File(Environment.getExternalStorageDirectory(),
        "/My Bus Stop/saved img/");
    long totalNumFiles;
    totalNumFiles = dir.listFiles().length;
    return totalNumFiles;
  }

  // Delete files after report
  public static boolean delete(File dir) {
    if (dir != null && dir.isDirectory()) {
      String[] children = dir.list();
      for (String aChildren : children) {
        boolean success = delete(new File(dir, aChildren));
        if (!success) {
          return false;
        }
      }
    }
    assert dir != null;
    return (dir.getName().contains(".JPG")) && dir.delete();
  }

  // Hide the keyboard
  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    View f = activity.getCurrentFocus();
    if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass())) {
      if (imm != null) {
        imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
      }
    } else {
      activity.getWindow()
          .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
  }


  // Whats not filled in ??
  public static void alertSomeoneDialog(Activity activity, String alertMessage) {
    try {
      AlertDialog.Builder builder1 =
          new AlertDialog.Builder(activity);
      builder1.setMessage(alertMessage);
      builder1.setCancelable(true);
      builder1.setPositiveButton(
          "OK",
          (dialog, id) -> dialog.cancel());
      AlertDialog alert11 = builder1.create();
      alert11.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Generic error dialog with a close button.
   */
  public static void showDialog(String title, String message, Context context) {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
        context);
    alertDialogBuilder.setTitle(title);
    alertDialogBuilder
        .setMessage(message)
        .setCancelable(false)
        .setPositiveButton("Close", (dialog, id) -> dialog.cancel());
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
  }


  // Get the the count
  public static int getMyCountAtTheTable(String TableToCheck) {
    MySQLiteHelperAdapter db = MySQLiteHelperAdapter
        .getInstance(ApplicationContextProvider.getContext());
    int counta;
    counta = db.getCount(TableToCheck);
    return counta;
  }

  public static void createEmailContents(String SHIFT, String spin_val, String associate, String
      passTheSubject, String passTheBody) {
    String subject = passTheSubject;
    String body = passTheBody;
    String shift = SHIFT;
    String location = spin_val;

    StringBuilder forTheReport = new StringBuilder();
    ArrayList<String> failedForReportList = new ArrayList<>();
    Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat =
        new SimpleDateFormat(" dd/MM/YYYY  @ hh:mm:a", Locale.getDefault());
    String mGetDateAndTime = mSimpleDateFormat.format(mCalendar.getTime());
    subject = "5S Checks Report " + " " + location + " " + mGetDateAndTime;
    forTheReport.append("Checks done on : ")
        .append(shift)
        .append(" Shift");
    forTheReport.append("\n");
    forTheReport.append("Checks done by : ")
        .append(associate)
        .append("\n");
    forTheReport.append("Time & Date of checks : ")
        .append(mGetDateAndTime)
        .append("\n");
    forTheReport.append("Location Checked : ")
        .append(spin_val)
        .append("\n");
    forTheReport.append("\n")
        .append("\n")
        .append("\n");
    if (failedForReportList.size() == 0) {
      forTheReport
          .append("All checks have been passed")
          .append("\n");
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
        .append("\n")
        .append("\n")
        .append("\n");
    forTheReport
        .append(" CSV file has been generated and attached ")
        .append("\n")
        .append("\n");
    if (Utilities.countTheFiles() == 1) {
      forTheReport
          .append(" ")
          .append(Utilities.countTheFiles()
              .toString()).append(" Picture has been taken and attached")
          .append("\n");
    } else if (Utilities.countTheFiles() > 1) {
      forTheReport
          .append(" ")
          .append(Utilities.countTheFiles()
              .toString())
          .append(" Pictures have been taken and attached")
          .append("\n");
    } else {
      forTheReport
          .append(" No pictures taken ")
          .append("\n");
    }
    forTheReport
        .append(" Report created by the bus stop viewer app \n By E.Ballinger ");
    body = forTheReport.toString();
    Long l = Utilities.countTheFiles();
    Log.d("File Count is : ", String.valueOf(l));

    // forTheReport.setLength(0);
    // failedForReportList.clear();

  }

  public class EmailContentsBuilder implements GenerateTheReportBuilder {

    private String subject,body,shift,location;

    StringBuilder forTheReport = new StringBuilder();
    ArrayList<String> failedForReportList = new ArrayList<>();
    Calendar mCalendar = Calendar.getInstance();
    SimpleDateFormat mSimpleDateFormat =
        new SimpleDateFormat(" dd/MM/YYYY  @ hh:mm:a", Locale.getDefault());

    @Override
    public void getValuesForReportToBuild(String SHIFT, String spin_val, String associate,
        String passTheSubject, String passTheBody) {
      subject = passTheSubject;
      body = passTheBody;
      shift = SHIFT;
      location = spin_val;
    }


  }
}
