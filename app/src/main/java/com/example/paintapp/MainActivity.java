package com.example.paintapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import PaintKit.PaintBoard;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private PenThicknessPopupWindow mPenThicknessPopupWindow;
    private PenColorPopupWindow mPenColorPopupWindow;
    private SwitchPagePopupWindow mSwitchPagePopupWindow;
    private GeometrySelectPopupWindow mGeometrySelectPopupWindow;

    private PaintBoardView mPaintBoardView;
    private View mPenColorView;
    private View mPenView;
    private View mLineView;
    private View mErasorView;
    private View mGeometryView;
    private View mUndoView;
    private View mRedoView;
    private View mClearView;
    private View mAddView;



    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPaintBoardView = (PaintBoardView)findViewById(R.id.paint_board_view);
        mPenColorView = (TextView)findViewById(R.id.pen_color);
        mPenView = (ImageView)findViewById(R.id.pen);
        mLineView = (ImageView)findViewById(R.id.lineThickness);
        mErasorView = (ImageView)findViewById(R.id.erasor);
        mGeometryView = (ImageView)findViewById(R.id.geometry);
        mUndoView = (ImageView)findViewById(R.id.undo);
        mRedoView = (ImageView)findViewById(R.id.redo);
        mClearView = (ImageView)findViewById(R.id.clear);
        mAddView = (ImageView)findViewById(R.id.add);


        mPenThicknessPopupWindow = new PenThicknessPopupWindow(MainActivity.this,mPaintBoardView.getPenSize());
        mPenColorPopupWindow = new PenColorPopupWindow(MainActivity.this);
        mSwitchPagePopupWindow = new SwitchPagePopupWindow(MainActivity.this,mPaintBoardView);
        mGeometrySelectPopupWindow = new GeometrySelectPopupWindow(MainActivity.this,mPaintBoardView);

        mPenColorView.setOnClickListener(this);
        mPenView.setOnClickListener(this);
        mLineView.setOnClickListener(this);
        mErasorView.setOnClickListener(this);
        mGeometryView.setOnClickListener(this);
        mUndoView.setOnClickListener(this);
        mRedoView.setOnClickListener(this);
        mClearView.setOnClickListener(this);
        mAddView.setOnClickListener(this);

        mPenColorPopupWindow.setmChooseColorCallback(new PenColorPopupWindow.ChooseColorCallback() {
            @Override
            public void onChooseColor(int color) {
                mPenColorView.setBackgroundColor(color);
                mPaintBoardView.setPenColor(color);
            }
        });

        mPenThicknessPopupWindow.setSeekBarCallBack(new PenThicknessPopupWindow.SeekBarCallBack() {
            @Override
            public void seekBarProcessChanged(int progess) {
                mPaintBoardView.setPenSize(progess);
            }
        });

        mPaintBoardView.setmStatusChangeCallBack(new PaintBoardView.StatusChangeCallBack() {
            @Override
            public void undoRedoStatusChanged() {
                mUndoView.setEnabled(mPaintBoardView.canUndo());
                mRedoView.setEnabled(mPaintBoardView.canRedo());
            }
        });


        mPenColorView.setBackground(new ColorDrawable(ContextCompat.getColor(MainActivity.this,R.color.penColorBlack)));

        mPenView.setSelected(true);
        mUndoView.setEnabled(false);
        mRedoView.setEnabled(false);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pen_color:
                mPenColorPopupWindow.showPopupWindow(mPenColorView);
                break;

            case R.id.pen:
                if(!mPenView.isSelected()){
                    mPenView.setSelected(true);
                    mGeometryView.setSelected(false);
                    mErasorView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoard.Mode.DRAW);
                }
                break;

            case R.id.lineThickness:
                mPenThicknessPopupWindow.showPopupWindow(mLineView);
                break;

            case R.id.erasor:
                if(!mErasorView.isSelected()){
                    mErasorView.setSelected(true);
                    mGeometryView.setSelected(false);
                    mPenView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoard.Mode.ERASOR);
                }
                break;

            case R.id.geometry:
                if(!mGeometryView.isSelected()){
                    mGeometryView.setSelected(true);
                    mErasorView.setSelected(false);
                    mPenView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoard.Mode.Geometry);
                }
                mGeometrySelectPopupWindow.showPopupWindow(mGeometryView);
                break;

            case R.id.undo:
                mPaintBoardView.undo();
                break;

            case R.id.redo:
                mPaintBoardView.redo();
                break;

            case R.id.clear:
                mPaintBoardView.clear();
                break;

            case R.id.add:
                mSwitchPagePopupWindow.showPopupWindow(mAddView);
                break;
        }
    }
}
