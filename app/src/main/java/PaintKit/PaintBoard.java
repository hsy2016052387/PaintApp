package PaintKit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PaintBoard implements Serializable{

    private AbstractPen mPen;
    private AbstractPaper mPaper;
    private Erasor mErasor;



    //private Mode mMode = Mode.DRAW;




    private List<DrawInfo> mDrawList;
    private List<DrawInfo> mRemoveList;





    //初始化
    public PaintBoard(){
        //画笔默认大小为5，默认颜色为黑色
        mPen = new Pen();
        //画纸，默认大小为1000*1000，背景颜色为白色
        mPaper = new Paper();
        //橡皮擦默认大小为5
        mErasor = new Erasor();

        mDrawList = new ArrayList<>();
        mRemoveList = new ArrayList<>();

    }


    public void setPenSize(int size){
        mPen.setmSize(size);
    }

    public int getPenSize(){
        return mPen.getmSize();
    }

    public void setPenColor(int color){
        mPen.setmColor(color);
    }

    public int getPenColor(){
        return mPen.getmColor();
    }

    public void setPaperWith(int with){
        mPaper.setmWidth(with);
    }

    public void setPaperHeight(int height){
        mPaper.setmHeight(height);
    }

    public void setPaperBackgroundColor(int color){
        mPaper.setmBackgroudColor(color);
    }

    public int getPaperWith(){
        return mPaper.getmWidth();
    }

    public int getPaperHeight(){
        return mPaper.getmHeight();
    }

    public int getPaperBackgroundColor(){
        return mPaper.getmBackgroudColor();
    }

    public void setErasorSize(int size){
        mErasor.setmSize(size);
    }

    public int getErasorSize(){
        return mErasor.getmSize();
    }

    public List<DrawInfo> getmDrawList() {
        return mDrawList;
    }

    public List<DrawInfo> getmRemoveList(){
        return mRemoveList;
    }

}
