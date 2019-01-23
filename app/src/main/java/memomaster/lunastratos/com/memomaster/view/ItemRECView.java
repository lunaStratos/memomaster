package memomaster.lunastratos.com.memomaster.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import memomaster.lunastratos.com.memomaster.R;

public class ItemRECView extends LinearLayout {

    TextView rec_filename;
    Context context;

    public ItemRECView(Context context) {
        super(context);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.itemrecview, this,true);

        rec_filename = findViewById(R.id.rec_filename);

    }

    public void setName(String str) {
        rec_filename.setText(" "+ str);
    }

}
