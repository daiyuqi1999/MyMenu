package SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBAdapter {
    private static final String DB_NAME = "user.db";
    private static final String DB_TABLE = "db_user";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_SEXY = "sexy";
    public static final String KEY_Isused = "isused";
    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;

    public DBAdapter(Context _context) {
        context = _context;
    }

    /** Close the database */
    public void close() {
        if (db != null){
            db.close();
            db = null;
        }
    }

    /** Open the database */
    public void open() throws SQLiteException {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }


    public long insert(Student student) {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_NAME, student.getName());
        newValues.put(KEY_PWD, student.getPwd());
        newValues.put(KEY_SEXY, student.getSexy());
        if(student.getIsused()==1) {
            newValues.put(KEY_Isused, 1);
            Log.d("c","插入数据未是");
        }
        else
            newValues.put(KEY_Isused, 0);

        return db.insert(DB_TABLE, null, newValues);
    }


    public Student[] queryAllData() {
        Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_PWD, KEY_SEXY,KEY_Isused},
                null, null, null, null, null);
        return ConvertToPeople(results);
    }

    public Student[] queryOneData(long id) {
        Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_PWD, KEY_SEXY,KEY_Isused},
                KEY_ID + "=" + id, null, null, null, null);
        return ConvertToPeople(results);
    }

    private Student[] ConvertToPeople(Cursor cursor){
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()){
            return null;
        }
        Student[] peoples = new Student[resultCounts];
        for (int i = 0 ; i<resultCounts; i++){
            peoples[i] = new Student();
            peoples[i].setID(cursor.getInt(0));
            peoples[i].setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            peoples[i].setPwd(cursor.getString(cursor.getColumnIndex(KEY_PWD)));
            peoples[i].setSexy(cursor.getString(cursor.getColumnIndex(KEY_SEXY)));
            peoples[i].setIsused(cursor.getInt(cursor.getColumnIndex(KEY_Isused)));

            cursor.moveToNext();
        }
        return peoples;
    }

    public long deleteAllData() {
        return db.delete(DB_TABLE, null, null);
    }

    public long deleteOneData(long id) {
        return db.delete(DB_TABLE,  KEY_ID + "=" + id, null);
    }

    public long updateOneData(long id , Student people){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_NAME, people.getName());
        updateValues.put(KEY_PWD, people.getPwd());
        updateValues.put(KEY_SEXY, people.getSexy());
        updateValues.put(KEY_Isused, people.getIsused());
        return db.update(DB_TABLE, updateValues,  KEY_ID + "=" + id, null);
    }

    /** 静态Helper类，用于建立、更新和打开数据库*/
    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE = "create table " +
                DB_TABLE + " (" + KEY_ID + " integer primary key autoincrement, " +
                KEY_NAME+ " text not null, " + KEY_PWD+ " text not null," + KEY_SEXY +
                " text not null, "+KEY_Isused+" integer );";

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            _db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
            _db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(_db);
        }
    }
}