package PaintKit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class DrawInfo {

//    private Paint mPaint;
//    private Path mPath;
//
//    public DrawInfo(Paint paint,Path path){
//        mPaint = paint;
//        mPath = path;
//    }
//
//    public void draw(Canvas canvas){
//        canvas.drawPath(mPath,mPaint);
//    }

    private AbstractPen mPen;
    private List<Point> mPointList;
    private String type;

    public DrawInfo(AbstractPen pen,String type){
        mPen = pen;
        mPointList = new ArrayList<>();
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
