package com.example.paintapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import PaintKit.PaintBoard;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout mLinearLayout;
    private ViewTreeObserver mViewTreeObserver;
    private ImageView mImage;
    private PaintBoard mPaintBoard;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Xfermode mDrawMode;
    private Xfermode mErasorMode;
    private PenThicknessPopupWindow mPenThicknessPopupWindow;

    private View mPenView;
    private View mLineView;
    private View mErasorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        mImage = (ImageView)findViewById(R.id.image);
        mPenView = (ImageView)findViewById(R.id.pen);
        mLineView = (ImageView)findViewById(R.id.lineThickness);
        mErasorView = (ImageView)findViewById(R.id.erasor);
        mPaintBoard = new PaintBoard();
        mDrawMode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
        mErasorMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPenThicknessPopupWindow = new PenThicknessPopupWindow(MainActivity.this);

        mPenView.setOnClickListener(this);
        mLineView.setOnClickListener(this);
        mErasorView.setOnClickListener(this);
        mPenThicknessPopupWindow.setSeekBarCallBack(new PenThicknessPopupWindow.SeekBarCallBack() {
            @Override
            public void seekBarProcessChanged(int progess) {
                mPaintBoard.setPenSize(progess);
                mPaint.setStrokeWidth(mPaintBoard.getPenSize());
            }
        });

        mPenView.setSelected(true);

        mViewTreeObserver = mLinearLayout.getViewTreeObserver();
        mViewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mLinearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                // 创建一张空白图片,宽和高与imageview保持一致
                mBitmap = Bitmap.createBitmap(mLinearLayout.getWidth(),mLinearLayout.getHeight() , Bitmap.Config.ARGB_8888);
                // 创建一张画布
                mCanvas = new Canvas(mBitmap);
                // 画布背景为灰色
                mCanvas.drawColor(mPaintBoard.getPaperBackgroundColor());
                // 创建画笔
                mPaint = new Paint();
                // 设置防止锯齿
                mPaint.setAntiAlias(true);
                //设置画笔画空心的图案
                //mPaint.setStyle(Paint.Style.STROKE);
                // 画笔颜色
                mPaint.setColor(mPaintBoard.getPenColor());
                // 宽度5个像素
                mPaint.setStrokeWidth(mPaintBoard.getPenSize());
                // 先将白色色背景画上
                mCanvas.drawBitmap(mBitmap, new Matrix(), mPaint);
                mImage.setImageBitmap(mBitmap);

            }
        });

        mImage.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int)motionEvent.getX();
                        startY = (int)motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int)motionEvent.getX();
                        int endY = (int)motionEvent.getY();
                        mCanvas.drawLine(startX,startY,endX,endY,mPaint);

                        //更新起点坐标
                        startX = (int)motionEvent.getX();
                        startY = (int)motionEvent.getY();
                        mImage.setImageBitmap(mBitmap);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pen:
                if(!mPenView.isSelected()){
                    mPenView.setSelected(true);
                    mErasorView.setSelected(false);
                    mPaint.setXfermode(mDrawMode);
                    mPaintBoard.setmMode(PaintBoard.Mode.DRAW);
                }
                break;
            case R.id.lineThickness:
                mPenThicknessPopupWindow.showPopupWindow(mLineView);
                break;
            case R.id.erasor:
                if(!mErasorView.isSelected()){
                    mErasorView.setSelected(true);
                    mPenView.setSelected(false);
                    mPaint.setXfermode(mErasorMode);
                    mPaintBoard.setmMode(PaintBoard.Mode.ERASOR);
                }
                break;

        }
    }
}
