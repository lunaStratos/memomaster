package memomaster.lunastratos.com.memomaster.VO;

public class recVO {

    String filename;
    String dir;

    public recVO(String filename, String dir) {
        this.filename = filename;
        this.dir = dir;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
