package memomaster.lunastratos.com.memomaster.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import memomaster.lunastratos.com.memomaster.Fragments.HomeFragment;
import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.VO.memoVO;

public class ItemView extends LinearLayout {

    TextView item_title;
    TextView item_memo;
    Button memoDelete_Btn;

    Context context;
    SQLiteDatabase db;
    View.OnClickListener mClickListener;

    private static final String TABLE_NAME = "lunastratos_memomaster";

    public ItemView(Context context, SQLiteDatabase db, View.OnClickListener mClickListener) {
        super(context);
        this.context = context;
        this.db = db;
        this.mClickListener = mClickListener;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.itemview, this, true);

        item_title = findViewById(R.id.item_title);
        item_memo = findViewById(R.id.item_memo);
        memoDelete_Btn = findViewById(R.id.memoDelete_Btn);
        memoDelete_Btn.setOnClickListener(mClickListener);
        memoDelete_Btn.setFocusable(false);


    }

    public void setTitle(String str) {
        item_title.setText("제목: " + str);
    }

    public void setMemo(String str) {
        item_memo.setText("내용: " + str);
    }

    public void setTagBtnNumber(int num) {
        memoDelete_Btn.setTag(num);
    }



}