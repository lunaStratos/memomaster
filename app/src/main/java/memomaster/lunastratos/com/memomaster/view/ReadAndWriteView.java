package memomaster.lunastratos.com.memomaster.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.SqlManager.MySQLDatabaseHelper;

public class ReadAndWriteView extends AppCompatActivity {

    EditText insert_memo;
    EditText insert_title;

    private SQLiteDatabase db;
    private MySQLDatabaseHelper helper;
    private static final String TABLE_NAME = "lunastratos_memomaster";
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;

    private int memoNumber = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);

        //findViewById
        insert_memo = findViewById(R.id.insert_memo);
        insert_title = findViewById(R.id.insert_title);

        //SQL helper connect
        helper = new MySQLDatabaseHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();

        // 선택된 noteNumber 가져오기
        memoNumber = (int) getIntent().getExtras().get("number"); //alist.get.number
        readMemo(memoNumber); //read it

        //커서 마지막
        insert_memo.setSelection(insert_memo.getText().length());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String title = insert_title.getText().toString();
        String memo = insert_memo.getText().toString();

        if(!title.replaceAll(" ", "").equals("") && !memo.replaceAll(" ", "").equals("")){
            saveMemo(title, memo);
        }else{
            deleteMemo();
            Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();

        }

        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
        finish();
    }

    public void readMemo(int noteNumber){

        String sql = "select * from " + TABLE_NAME + " where number = " + noteNumber;
        Cursor c = db.rawQuery(sql, null); // 반환, db.execSQL(); 반환안함
        String title = "";
        String text = "";


        while (c.moveToNext()) {
            int id = c.getInt(0); //number
            title = c.getString(1); //title
            text = c.getString(2); //memo
        }

        insert_title.setText(title);
        insert_memo.setText(text);

    }


    public void saveMemo(String title, String memo) {
        String sql = "update " + TABLE_NAME + " set title='" + title + "', memo='" + memo +"' WHERE number = " + memoNumber;
        db.execSQL(sql);
    }

    public void deleteMemo(){
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE number =" + memoNumber;
        db.execSQL(sql);

    }
}
