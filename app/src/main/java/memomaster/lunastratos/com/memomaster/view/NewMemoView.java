package memomaster.lunastratos.com.memomaster.view;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.SqlManager.MySQLDatabaseHelper;

public class NewMemoView extends AppCompatActivity {

    EditText new_memo;
    EditText new_title;
    private SQLiteDatabase db;
    private MySQLDatabaseHelper helper;
    private static final String TABLE_NAME = "lunastratos_memomaster";

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);

        new_memo = findViewById(R.id.insert_memo);
        new_title = findViewById(R.id.insert_title);

        //SQL helper connect
        helper = new MySQLDatabaseHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();

        autoSave();

    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    //뒤로가기 버튼 누를시
    @Override
    public void onBackPressed() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        String title = pref.getString("title", "");
        String memo = pref.getString("memo", "");

        if (!title.replaceAll(" ", "").equals("") && !memo.replaceAll(" ", "").equals("")) {
            newMemo(title, memo);
            //마지막 종료
            Toast.makeText(getApplicationContext(), "새로운 메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
        }
        super.onBackPressed();
    }


    //임시저장
    public void tempSave() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("title", new_title.getText().toString());
        editor.putString("memo", new_memo.getText().toString());
        editor.commit();
    }

    //임시저장
    public void refresh() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("title", "");
        editor.putString("memo", "");
        editor.commit();
    }


    public void newMemo(String title, String memo) {
        String sql = "insert into " + TABLE_NAME + "(number, title, memo) VALUES" + "(NULL, '" + title + "', '" + memo + "' )" + ";";
        db.execSQL(sql);
        finish();

    }


    public void autoSave() {
        new_memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tempSave();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
    }
}
