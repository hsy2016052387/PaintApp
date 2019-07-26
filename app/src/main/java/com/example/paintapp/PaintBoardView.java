package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import PaintKit.DrawInfo;
import PaintKit.PaintBoard;

public class PaintBoardView extends View {
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private float mLastX;
    private float mLastY;


    private Xfermode mXferModeClear;
    private Xfermode mXferModeDraw;

    private PaintBoard mPaintBoard;

    private boolean mCanEraser;
    private List<DrawInfo> mDrawInfoList;

    private PaintBoard.Mode mMode;

    private StatusChangeCallBack mStatusChangeCallBack;

    public PaintBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mPaintBoard = new PaintBoard();
        mMode = PaintBoard.Mode.DRAW;
        mDrawInfoList = mPaintBoard.getmDrawList();

        //创建画笔，设置抗锯齿标志、使位图进行有利的抖动的位掩码标志
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        //描边
        mPaint.setStyle(Paint.Style.STROKE);
        //滤波处理
        mPaint.setFilterBitmap(true);
        //圆弧
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //圆线帽
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //画笔粗细
        mPaint.setStrokeWidth(mPaintBoard.getPenSize());
        //画笔颜色
        mPaint.setColor(mPaintBoard.getPenColor());

        mXferModeDraw = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mXferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mPaint.setXfermode(mXferModeDraw);
    }

    private void initBitmap(){
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public PaintBoard.Mode getmMode() {
        return mMode;
    }

    public void setmMode(PaintBoard.Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            if (mMode == PaintBoard.Mode.DRAW) {
                mPaint.setXfermode(mXferModeDraw);
                //mPaint.setStrokeWidth(mDrawSize);
            } else {
                mPaint.setXfermode(mXferModeClear);
                //mPaint.setStrokeWidth(mEraserSize);
            }
        }
    }
    public void setPenSize(int size){
        mPaint.setStrokeWidth(size);
        mPaintBoard.setPenSize(size);
    }

    public int getPenSize(){
        return mPaintBoard.getPenSize();
    }

    public void setPenColor(int color){
        mPaint.setColor(color);
        mPaintBoard.setPenColor(color);
    }

    public void setmStatusChangeCallBack(StatusChangeCallBack mStatusChangeCallBack) {
        this.mStatusChangeCallBack = mStatusChangeCallBack;
    }

    public boolean canUndo(){
        return mDrawInfoList!=null && mDrawInfoList.size()>0;
    }

    private void saveDrawingPath(){
        if(mDrawInfoList==null)
            mDrawInfoList = new ArrayList<>();
        DrawInfo drawInfo = new DrawInfo(new Paint(mPaint),new Path(mPath));
        mDrawInfoList.add(drawInfo);
        mCanEraser = true;
        if(mStatusChangeCallBack!=null)
            mStatusChangeCallBack.undoStatusChanged();
    }

    private void reDraw(){
        if(mDrawInfoList!=null){
            mBitmap.eraseColor(Color.TRANSPARENT);
            for(DrawInfo drawInfo:mDrawInfoList){
                drawInfo.draw(mCanvas);
            }
            invalidate();
        }

    }

    public void undo(){
        int size = mDrawInfoList==null ? 0 : mDrawInfoList.size();
        if(size>0){
            mDrawInfoList.remove(size-1);
            if(size==1)
                mCanEraser =false;
            reDraw();
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoStatusChanged();
        }
    }

    public void clear(){
        if(mBitmap!=null){
            if(mDrawInfoList!=null)
                mDrawInfoList.clear();
            mCanEraser = false;
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoStatusChanged();
        }
    }

    public interface StatusChangeCallBack{
        void undoStatusChanged();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float y = event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                if (mPath == null) {
                    mPath = new Path();
                }
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                mPath.quadTo(mLastX, mLastY, (x + mLastX) / 2, (y + mLastY) / 2);
                if (mBitmap == null) {
                    initBitmap();
                }
                if (mMode == PaintBoard.Mode.ERASOR && !mCanEraser) {
                    break;
                }
                mCanvas.drawPath(mPath,mPaint);
                invalidate();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == PaintBoard.Mode.DRAW || mCanEraser) {
                    saveDrawingPath();
                }
                mPath.reset();
                break;
        }
        return true;
    }
}
