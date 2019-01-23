package memomaster.lunastratos.com.memomaster.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import memomaster.lunastratos.com.memomaster.Adapter.listviewAdapter;
import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.SqlManager.MySQLDatabaseHelper;
import memomaster.lunastratos.com.memomaster.VO.memoVO;
import memomaster.lunastratos.com.memomaster.view.NewMemoView;
import memomaster.lunastratos.com.memomaster.view.ReadAndWriteView;

public class HomeFragment extends Fragment {

    ListView listview_id;
    listviewAdapter adapter;
    Button memoBtn;

    private static final int READ_NOTE = 100;

    private SQLiteDatabase db;
    private MySQLDatabaseHelper helper;
    private static final String TABLE_NAME = "lunastratos_memomaster";
    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_layout, container, false);

        listview_id = v.findViewById(R.id.listview_id);

        //버튼연결
        memoBtn = v.findViewById(R.id.memoBtn);
        memoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewMemoView.class);
                intent.putExtra("newMemo", "1");
                startActivity(intent);
            }
        });

        //SQL helper connect
        helper = new MySQLDatabaseHelper(getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        db = helper.getWritableDatabase();


        //리스트 어뎁터
        adapter = new listviewAdapter(db);

        listview_id.setAdapter(adapter);
        listview_id.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ReadAndWriteView.class);
                memoVO item = (memoVO) adapter.getItem(position);
                intent.putExtra("number", item.getNumber());
                startActivityForResult(intent, READ_NOTE);


            }
        });


        return v;
    }

    /**
     * resume으로 별도 분리
     * 쉬다가 다시 시작하면 onResume실행
     * 시작할때도 생명주기에 의해서 onResume이 실행
     */
    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }


    //리스트 초기화
    public void refresh(){

        adapter.clear();// 초기화
        adapter.additems(); // 저장
        adapter.notifyDataSetChanged();

    }



}
