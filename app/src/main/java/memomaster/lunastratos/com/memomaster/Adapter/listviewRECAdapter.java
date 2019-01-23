package memomaster.lunastratos.com.memomaster.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import memomaster.lunastratos.com.memomaster.VO.recVO;
import memomaster.lunastratos.com.memomaster.view.ItemRECView;

public class listviewRECAdapter extends BaseAdapter {

    ArrayList<recVO> alist = new ArrayList<>();

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

    public void addItem(recVO vo) {
        alist.add(vo);
    }

    public void clear() {
        alist.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemRECView im = new ItemRECView(parent.getContext());

        im.setName(alist.get(position).getFilename());

        return im;
    }
}
