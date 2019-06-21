package cn.edu.zucc.bigapp.run.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RunDatabaseHelper extends SQLiteOpenHelper {

  public static final String CREATE_RUN = "create table Run("
      + "id integer primary key autoincrement, "
      + "date text, "
      + "distance text, "
      + "time text)";

  private Context mContext;

  public RunDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
    mContext = context;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_RUN);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("drop table if exists Run");
    onCreate(sqLiteDatabase);
  }
}
