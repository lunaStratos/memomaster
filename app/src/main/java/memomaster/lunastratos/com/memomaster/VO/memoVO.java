package memomaster.lunastratos.com.memomaster.VO;

import android.view.View;

public class memoVO {

    private String title;
    private String memo;
    private int number;

    public memoVO(int number, String title, String memo) {
        this.title = title;
        this.memo = memo;
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
