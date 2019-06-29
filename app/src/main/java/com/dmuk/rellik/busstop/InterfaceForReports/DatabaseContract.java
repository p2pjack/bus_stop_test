package com.dmuk.rellik.busstop.InterfaceForReports;

import android.provider.BaseColumns;

public class DatabaseContract {


  private DatabaseContract() {
  }

  /* Inner class that defines the table contents */
  public static class TableEntryQuestions implements BaseColumns {

    public static final String TABLE_NAME = "fiveS";
    public static final String COLUMN_NAME_QUESTION = "'questions'";
    public static final String COLUMN_NAME_LEVEL = "'level'";
    public static final String COLUMN_NAME_DEFAULT = "'default'";

  }

  public static class TableEntryReport implements BaseColumns {

    public static final String TABLE_NAME = "report";
    public static final String COLUMN_NAME_QUESTION = "'questions'";
    public static final String COLUMN_NAME_ASSOCIATE = "'associate'";
    public static final String COLUMN_NAME_SHIFT = "'shift'";
    public static final String COLUMN_NAME_DEFAULT = "'default'";

  }

  public static class TableEnteryBarriers implements BaseColumns {

  }


  public static class TableEntryTPMchecks implements BaseColumns {

    public static final String TABLE_NAME = "tpm";
    public static final String COLUMN_NAME_QUESTION = "'questions'";
    public static final String COLUMN_NAME_LEVEL = "'level'";
    public static final String COLUMN_NAME_DEFAULT = "'default'";

  }


  public static final class GlobalValues {

    public static final String TAG_DATABASE = "DB_Adapter";
  }

}
