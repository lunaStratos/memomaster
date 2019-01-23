package memomaster.lunastratos.com.memomaster.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import memomaster.lunastratos.com.memomaster.R;
import memomaster.lunastratos.com.memomaster.VO.memoVO;

public class listviewAdapter extends BaseAdapter {

    ArrayList<memoVO> alist = new ArrayList<>();
    SQLiteDatabase db;
    Button btn;

    private static final String TABLE_NAME = "lunastratos_memomaster";

    public listviewAdapter(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int position) {
        return alist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(memoVO vo) {
        alist.add(vo);
    }

    public void clear() {
        alist.clear();
    }

    Context context;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

         context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemview, parent, false);
        }

        //방식1
//        ItemView im = new ItemView(parent.getContext(), db, mClickListener);
//        im.setMemo(alist.get(position).getMemo());
//        im.setTitle(alist.get(position).getTitle());
//        im.setTagBtnNumber(alist.get(position).getNumber());

        TextView insert_memo = convertView.findViewById(R.id.item_memo);
        TextView insert_title = convertView.findViewById(R.id.item_title);
         btn = convertView.findViewById(R.id.memoDelete_Btn);

        insert_memo.setText(alist.get(position).getMemo());
        insert_title.setText(alist.get(position).getTitle());
        btn.setTag(alist.get(position).getNumber());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShow();
            }
        });
        btn.setFocusable(false);

//        return im;
        return convertView;
    }

    void dialogShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("메모삭제");
        builder.setMessage("이 메모를 삭제하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        int positions = (int) btn.getTag();
                        String sql = "DELETE FROM " + TABLE_NAME + " WHERE number = " + positions;
                        db.execSQL(sql);
                        //갱신
                        alist.clear();
                        additems();
                        notifyDataSetChanged();
                        Toast.makeText(context,"메모를 삭제했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    /**
     * Sql연결해서 제목 날짜만 가져오기
     */
    public void additems() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);

        while (c.moveToNext()) {
            int number = c.getInt(0);
            String title = c.getString(1);
            String memo = c.getString(2);
            addItem(new memoVO(number, title, memo));

        }

    }



}
