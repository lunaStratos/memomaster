package memomaster.lunastratos.com.memomaster.FileManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 백업망 구축
 */

//implementation('com.googlecode.json-simple:json-simple:1.1.1') {
//        exclude group: 'junit', module: 'junit'
//    }

public class FileManager {

    private SQLiteDatabase db;
    private static final String TABLE_NAME = "lunastratos_memomaster";

    public FileManager(SQLiteDatabase db) {
        this.db = db;
    }

    private String filename = "memomaster.dat";

    public boolean backup() {

        String sql = "SELECT * FROM " + TABLE_NAME + "";
        Cursor c = db.rawQuery(sql, null);
        if(c.getCount() == 0){
            return false;
        }
        JSONArray arrayBackup = new JSONArray();


        JSONObject jsonBackupResult = new JSONObject();
        //SQL에서 읽기
        while (c.moveToNext()) {
            JSONObject jsonBackup = new JSONObject();
            int number = c.getInt(0);
            String title = c.getString(1);
            String memo = c.getString(2);

            try {
                jsonBackup.put("number", number);
                jsonBackup.put("title", title);
                jsonBackup.put("memo", memo);
                arrayBackup.add(jsonBackup);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

        }

        jsonBackupResult.put("any", arrayBackup);
        Log.i("number", "" + arrayBackup);


        String dir = fileDir() + filename;
        try {
            FileWriter fw = new FileWriter(dir);
            fw.write(jsonBackupResult.toJSONString());
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }


        return true;
    }

    public boolean restore() {
        FileReader fr = null;
        try {
            String dir = fileDir() + filename;
            fr = new FileReader(dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        JSONParser parser = new JSONParser();
        JSONObject items = null;
        JSONArray arr = new JSONArray();

        try {
            items = (JSONObject) parser.parse(fr);
            arr = (JSONArray) items.get("any");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        String sql = "DROP TABLE " + TABLE_NAME;
        db.execSQL(sql);

        sql = "create table if not exists " + TABLE_NAME + "(number integer primary key autoincrement, title text, memo text);";
        db.execSQL(sql);

        String values = "";
        if(arr.size() == 0){
            return false;
        }

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = new JSONObject();
            obj = (JSONObject) arr.get(i);
            long number = (long) obj.get("number");
            String title = (String) obj.get("title");
            String memo = (String) obj.get("memo");
            values += "(" + number + ", '" + title + "', '" + memo + "')";
            if (i != (arr.size() - 1)) {
                values += ",";
            }
        }
        sql = "INSERT INTO " + TABLE_NAME + "(number,title, memo) VALUES " +
                values;
        db.execSQL(sql);

        return true;
    }

    //이메일 백업위치 반환
    public String emailBackup() {
        String dir = fileDir() + filename;
        return dir;
    }

    //파일위치 : Download
    public String fileDir() {
        String dir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
        }

        return dir;
    }


}
