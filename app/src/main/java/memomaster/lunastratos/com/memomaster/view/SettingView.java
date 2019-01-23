package memomaster.lunastratos.com.memomaster.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import memomaster.lunastratos.com.memomaster.FileManager.FileManager;
import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.SqlManager.MySQLDatabaseHelper;

public class SettingView extends AppCompatActivity implements View.OnClickListener {

    Button backupBtn;
    Button restoreBtn;
    Button textSettingBtn;
    Button emailBackupBtn;


    private SQLiteDatabase db;
    private MySQLDatabaseHelper helper;
    private static final String TABLE_NAME = "lunastratos_memomaster";
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        //findViewById
        backupBtn = findViewById(R.id.backupBtn);
        restoreBtn = findViewById(R.id.restoreBtn);
        textSettingBtn = findViewById(R.id.textSettingBtn);
        emailBackupBtn = findViewById(R.id.emailBackupBtn);

        backupBtn.setOnClickListener(this);
        restoreBtn.setOnClickListener(this);
        textSettingBtn.setOnClickListener(this);
        emailBackupBtn.setOnClickListener(this);

        //SQL helper connect
        helper = new MySQLDatabaseHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();


    }

    @Override
    public void onClick(View v) {
        FileManager fm = new FileManager(db);

        switch (v.getId()) {
            case R.id.backupBtn:

                boolean backupResult = fm.backup();
                if(backupResult){
                    Toast.makeText(getApplicationContext(), "백업이 완료되었습니다.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "백업할 데이터가 없습니다.", Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.restoreBtn:

                boolean restoreResult = fm.restore();
                if(restoreResult){
                    Toast.makeText(getApplicationContext(), "복구가 완료되었습니다.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "복구가 실패하였습니다.", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.textSettingBtn:


                break;
            case R.id.emailBackupBtn:
                /**
                 * 파일을 만들고 나서
                 * 이메일 보내기
                 */

                boolean backupEmailResult = fm.backup();

                if(backupEmailResult){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    File file = new File(fm.emailBackup());
                    String[] address = {""}; //주소를 넣어두면 미리 주소가 들어가 있다.

                    Uri uri = FileProvider.getUriForFile(this, "memomaster.lunastratos.com.memomaster.fileprovider", file);
                    intent.putExtra(Intent.EXTRA_EMAIL, address);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "제목");
                    intent.putExtra(Intent.EXTRA_TEXT, "보낼 내용");
                    intent.putExtra(Intent.EXTRA_STREAM,uri); //파일 첨부
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "오류가 있어 실패하였습니다.", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }
}
