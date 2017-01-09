package almaty.kbtu.game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by RIA on 27.05.2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_SCORE = "players";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PLAYER = "player_name";
    public static final String COLUMN_SCORE = "player_score";

    private static final String DATABASE_NAME = "scores.db";
    private static final int DATABASE_VERSION = 1;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SCORE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_PLAYER
            + " text not null, " + COLUMN_SCORE + " integer);";
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }
}
