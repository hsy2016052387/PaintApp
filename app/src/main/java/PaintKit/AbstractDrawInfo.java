package PaintKit;

import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDrawInfo {
    private AbstractPen mPen;
    private List<Point> mPointList;
    private String type;

    public AbstractDrawInfo() {}

    public AbstractDrawInfo(AbstractPen mPen) {
        this.mPen = mPen;
        mPointList = new ArrayList<>();
    }

    public abstract void rebuildPath(Path path);

    public void setmPen(AbstractPen mPen) {
        this.mPen = mPen;
    }

    public void setmPointList(List<Point> mPointList) {
        this.mPointList = mPointList;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AbstractPen getmPen() {
        return mPen;
    }

    public List<Point> getmPointList(){
        return mPointList;
    }

    public String getType() {
        return type;
    }
}
