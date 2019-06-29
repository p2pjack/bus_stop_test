package com.dmuk.rellik.busstop.Data;

import static com.dmuk.rellik.busstop.InterfaceForReports.DatabaseContract.TableEntryReport.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import com.dmuk.rellik.busstop.R;
import com.dmuk.rellik.busstop.InterfaceForReports.DatabaseContract;
import com.dmuk.rellik.busstop.InterfaceForReports.DatabaseContract.TableEntryQuestions;
import com.dmuk.rellik.busstop.InterfaceForReports.DatabaseContract.TableEntryReport;
import com.dmuk.rellik.busstop.InterfaceForReports.DatabaseContract.TableEntryTPMchecks;
import com.dmuk.rellik.busstop.adapters.ViewQuestionsList;
import com.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/* SQLite adapter by Eaun Ballinger 2016 / modified in 2018 for
 DMUK bus stop viewer */

public class MySQLiteHelperAdapter extends SQLiteOpenHelper {

  private static final String QUESTIONS_CREATE_ENTRIES =
      "CREATE TABLE " + TableEntryQuestions.TABLE_NAME + " ("
          + TableEntryQuestions._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + TableEntryQuestions.COLUMN_NAME_QUESTION + " TEXT NOT NULL, "
          + TableEntryQuestions.COLUMN_NAME_LEVEL + " TEXT NOT NULL, "
          + TableEntryQuestions.COLUMN_NAME_DEFAULT + " TEXT NOT NULL)";

  private static final String REPORTS_CREATE_ENTRIES =
      "CREATE TABLE " + TABLE_NAME
          + " (" + TableEntryReport._ID + " INTEGER, "
          + TableEntryReport.COLUMN_NAME_SHIFT + " TEXT NOT NULL, "
          + TableEntryReport.COLUMN_NAME_ASSOCIATE + " TEXT NOT NULL, "
          + TableEntryReport.COLUMN_NAME_QUESTION + " TEXT NOT NULL, "
          + TableEntryReport.COLUMN_NAME_DEFAULT + " TEXT NOT NULL) ";

  private static final String TPM_CREATE_ENTRIES =
      "CREATE TABLE " + TableEntryTPMchecks.TABLE_NAME + " ("
          + TableEntryTPMchecks._ID + " INTEGER, "
          + TableEntryTPMchecks.COLUMN_NAME_QUESTION + " TEXT NOT NULL, "
          + TableEntryTPMchecks.COLUMN_NAME_LEVEL + " TEXT NOT NULL, "
          + TableEntryTPMchecks.COLUMN_NAME_DEFAULT + " TEXT NOT NULL)";

  private static final String QUESTIONS_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS " + TableEntryQuestions.TABLE_NAME;
  private static final String TPM_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS " + TableEntryTPMchecks.TABLE_NAME;
  private static final String REPORTS_DELETE_ENTRIES =
      "DROP TABLE IF EXISTS " + TABLE_NAME;
  private static final int DATABASE_VERSION = 1;
  private static final String DATABASE_NAME = "base.db";
  private static MySQLiteHelperAdapter mInstance;
  private SQLiteDatabase WritableDatabase;
  private Context mContext;
  private boolean mDatabaseCreated = false;


  public MySQLiteHelperAdapter(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    this.mContext = context;
    WritableDatabase = this.getWritableDatabase();
  }

  // Plug a leak use getInstance(this); to get state true / false
  public static synchronized MySQLiteHelperAdapter getInstance(Context ctx) {
    if (mInstance == null) {
      mInstance = new MySQLiteHelperAdapter(ctx.getApplicationContext());
    }
    return mInstance;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(QUESTIONS_CREATE_ENTRIES);
    db.execSQL(TPM_CREATE_ENTRIES);
    db.execSQL(REPORTS_CREATE_ENTRIES);
    Log.d("table set ?", " Done :)");
    mDatabaseCreated = true;
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // This database is only a cache for online data, so its upgrade policy is
    // to simply to discard the data and start over
    db.execSQL(QUESTIONS_DELETE_ENTRIES);
    db.execSQL(TPM_DELETE_ENTRIES);
    db.execSQL(REPORTS_DELETE_ENTRIES);
    onCreate(db);
  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }

  // Get the list
  public List<ViewQuestionsList> getAllContacts(String TABLE) {

    List<ViewQuestionsList> LIST = new ArrayList<>();
    // Select All Query
    String selectQuery = "SELECT * FROM " + TABLE;
    Cursor cursor = WritableDatabase.rawQuery(selectQuery, null);

    // looping through all rows and adding to list
    if (cursor.moveToFirst()) {
      do {
        ViewQuestionsList mViewQuestionsList = new ViewQuestionsList();
        mViewQuestionsList.set_idList(cursor.getString(0));
        mViewQuestionsList.setQuestionList(cursor.getString(1));
        mViewQuestionsList.setLevelList(cursor.getString(2));
        mViewQuestionsList.setDefaultList(cursor.getString(3));

        // Adding contact to list
        LIST.add(mViewQuestionsList);
      } while (cursor.moveToNext());
      cursor.close();
    }
    // return list
    return LIST;
  }

  // Delete All
  public void DeleteAll(String TABLE) {
    new MySQLiteHelperAdapter.DeleteAll(TABLE).start();
  }

  // Import setup csv
  public void CSV() {
    new MySQLiteHelperAdapter.WriteCSVtoDatabase().start();
  }

  // ADD
  public void insert(String mID, String mSHIFT, String mASSOCIATE, String mQUESTION,
      String mANSWER) {
    new DatabaseInsert(mID, mSHIFT, mASSOCIATE, mQUESTION, mANSWER).start();
  }

  // UPDATE
  public void UPDATE_REPORT(String mID, String mSHIFT, String mASSOCIATE, String mQUESTION,
      String mANSWER) {
    new UpdateReport(mID, mSHIFT, mASSOCIATE, mQUESTION, mANSWER).start();
  }

  // Check if eatery exists and true / false
  public void DoesItExist(String mID, String mSHIFT, String mASSOCIATE, String mQUESTION,
      String mANSWER) {
    new CheckIfExists(mID, mSHIFT, mASSOCIATE, mQUESTION, mANSWER).start();
  }

  // Count the how many is at the table
  public int getCount(String Table) {
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + Table, null);
    cursor.moveToFirst();
    int count = cursor.getInt(0);
    cursor.close();
    return count;
  }

  public void exportDB() {
    String SAVE = "My Bus Stop/export report";
    File directory;
    directory = new File(Environment.getExternalStorageDirectory(), SAVE);
    File exportDir = new File(directory, "CVS EXPORT");
    if (!exportDir.exists()) {
      exportDir.mkdirs();
    }

    File file = new File(exportDir, "report.csv");
    try {
      file.createNewFile();
      CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
      Cursor curCSV = WritableDatabase.rawQuery("SELECT * FROM report", null);
      csvWrite.writeNext(curCSV.getColumnNames());
      while (curCSV.moveToNext()) {
        //Which column you want to export
        String arrStr[] = {curCSV.getString(0), curCSV.getString(1),
            curCSV.getString(2), curCSV.getString(3),
            curCSV.getString(4)};
        csvWrite.writeNext(arrStr);
      }
      csvWrite.close();
      curCSV.close();
    } catch (Exception sqlEx) {
      Log.e("Database ", sqlEx.getMessage(), sqlEx);
    }
  }

  public void ClearMyTable() {

    WritableDatabase.delete(TABLE_NAME, null, null);
  }

  public void DeleteUncheckedFromReport(int id) {
    new DeleteTheUnclicked(id).start();
  }

  private class DeleteAll extends Thread {

    String table;

    DeleteAll(String delete) {
      this.table = delete;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      //  super.run();
      WritableDatabase.delete(table, null, null);
      WritableDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = ?"
          , new String[]{table});
      WritableDatabase.execSQL("vacuum");
    }
  }

  private class WriteCSVtoDatabase extends Thread {

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      // TODO : Changed for demo rename pr-hall-questions to five_s_questions
      if (mDatabaseCreated) {
        InputStream inStream = mContext.getResources()
            .openRawResource(R.raw.five_s_questions);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line;
        WritableDatabase.beginTransaction();

        try {
          while ((line = buffer.readLine()) != null) {
            String[] columns = line.split(",");
            if (columns.length != 3) {
              Log.d(DatabaseContract.GlobalValues.TAG_DATABASE
                  + " Questions", "Skipping Bad CSV Row" + columns[0]);
              continue;
            }
            ContentValues cv = new ContentValues(3);
            cv.put(TableEntryQuestions.COLUMN_NAME_QUESTION, columns[0].trim());
            cv.put(TableEntryQuestions.COLUMN_NAME_LEVEL, columns[1].trim());
            cv.put(TableEntryQuestions.COLUMN_NAME_DEFAULT, columns[2].trim());
            WritableDatabase.insert(TableEntryQuestions.TABLE_NAME,
                null, cv);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
        WritableDatabase.setTransactionSuccessful();
        WritableDatabase.endTransaction();
        mDatabaseCreated = false;
      }
    }
  }

  private class DatabaseInsert extends Thread {

    String ID;
    String SHIFT;
    String ASSOCIATE;
    String QUESTION;
    String ANSWER;

    DatabaseInsert(String id, String shift, String associate, String question,
        String questionAnswer) {
      this.ID = id;
      this.SHIFT = shift;
      this.ASSOCIATE = associate;
      this.QUESTION = question;
      this.ANSWER = questionAnswer;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      String[] args = {ID, SHIFT, ASSOCIATE, QUESTION, ANSWER};

      WritableDatabase.execSQL(
          "INSERT OR REPLACE INTO report('_id','shift','associate','questions','default') VALUES(?,?,?,?,?)",
          args);
    }
  }

  private class UpdateReport extends Thread {

    String ID;
    String SHIFT;
    String ASSOCIATE;
    String QUESTION;
    String ANSWER;

    UpdateReport(String id, String shift, String associate, String question,
        String questionAnswer) {
      this.ID = id;
      this.SHIFT = shift;
      this.ASSOCIATE = associate;
      this.QUESTION = question;
      this.ANSWER = questionAnswer;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      String[] args = {ID};
      ContentValues row = new ContentValues();
      row.put(TableEntryReport.COLUMN_NAME_SHIFT, SHIFT);
      row.put(TableEntryReport.COLUMN_NAME_ASSOCIATE, ASSOCIATE);
      row.put(TableEntryReport.COLUMN_NAME_QUESTION, QUESTION);
      row.put(TableEntryReport.COLUMN_NAME_DEFAULT, ANSWER);
      WritableDatabase.update(TableEntryReport.TABLE_NAME, row, "_id = ?", args);
    }
  }

  private class CheckIfExists extends Thread {

    String ID;
    String SHIFT;
    String ASSOCIATE;
    String QUESTION;
    String ANSWER;

    CheckIfExists(String id, String shift, String associate, String question,
        String questionAnswer) {
      this.ID = id;
      this.SHIFT = shift;
      this.ASSOCIATE = associate;
      this.QUESTION = question;
      this.ANSWER = questionAnswer;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      String[] args = {ID};
      String[] argss = {ID, SHIFT, ASSOCIATE, QUESTION, ANSWER};
      Cursor mCursor = WritableDatabase.rawQuery(
          "SELECT * FROM report WHERE  _rowid_ = ?", args);

      if (mCursor == null) {
        ContentValues row = new ContentValues();
        row.put(TableEntryReport.COLUMN_NAME_SHIFT, SHIFT);
        row.put(TableEntryReport.COLUMN_NAME_ASSOCIATE, ASSOCIATE);
        row.put(TableEntryReport.COLUMN_NAME_QUESTION, QUESTION);
        row.put(TableEntryReport.COLUMN_NAME_DEFAULT, ANSWER);
        WritableDatabase.update(TableEntryReport.TABLE_NAME, row, "_id = ?", args);
      } else {
        WritableDatabase.execSQL(
            "INSERT OR REPLACE INTO report('_id','shift','associate','questions','default')"
                + " VALUES(?,?,?,?,?)", argss);
      }

    }
  }

  private class DeleteTheUnclicked extends Thread {

    int Id;

    DeleteTheUnclicked(int deleteId) {
      this.Id = deleteId;
    }

    @Override
    public void run() {
      Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
      String whereClause = "_id=?";
      String[] whereArgs = new String[]{String.valueOf(Id)};
      WritableDatabase.delete(TableEntryReport.TABLE_NAME, whereClause, whereArgs);
    }
  }


}

