package lk.prasad.crop.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prasad on 5/7/16.
 */
public class SqliteController extends SQLiteOpenHelper {
    public SqliteController(Context context) {
        super(context, "aaa", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS language (language int) ;");
		sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS profile (id varchar, name varchar,tel varchar,province varchar,email varchar, address varchar, provinceid int) ;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void insert(ContentValues contentValues, String tableName){
        SQLiteDatabase database = this.getWritableDatabase();

        database.insert(tableName, null, contentValues);
        database.close();

    }

    public void update(ContentValues contentValues, String id){
        SQLiteDatabase database = this.getWritableDatabase();

        database.update("profile", contentValues, "id = ?", new String[]{id});
        database.close();

    }

    public List<String> getProfileDataFromDb(String id){
        List<String> results = new ArrayList<String>();
        SQLiteDatabase database = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM profile WHERE id=?";
        Cursor c = database.rawQuery(selectQuery, new String[] { id });
        if (c.moveToFirst()) {
            results.add(0,  c.getString(c.getColumnIndex("id")));
            results.add(1, c.getString(c.getColumnIndex("name")));
            results.add(2, c.getString(c.getColumnIndex("tel")));
            results.add(3, c.getString(c.getColumnIndex("province")));
            results.add(4, c.getString(c.getColumnIndex("email")));;
            results.add(5, c.getString(c.getColumnIndex("provinceid")));
            results.add(6, c.getString(c.getColumnIndex("address")));

        } while(c.moveToNext());

        c.close();
        database.close();
        return results;
    }

    public boolean checkForFirstTime() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM language", null);

        if (c.getCount() > 0) {
            return true;
        }

        c.close();
        database.close();
        return false;
    }

    public int getLanguage() {
        int result = 0;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database
                .rawQuery("SELECT * FROM language", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(0);
            }
        }
        cursor.close();
        database.close();
        return result;
    }

    public String getPhoneIdFromDb(){
        String id = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database
                .rawQuery("SELECT * FROM profile", null);
        if(cursor.moveToFirst()){
            do{
                //assing values
                id = cursor.getString(0);
                //Do something Here with values

            }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return id;
    }

}
