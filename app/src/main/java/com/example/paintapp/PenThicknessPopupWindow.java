package com.example.paintapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SeekBar;

public class PenThicknessPopupWindow {
    private SeekBar mSeekBar;
    private PopupWindow mPopupWindow;
    private View mView;
    private SeekBarCallBack mSeekBarCallBack;
    public PenThicknessPopupWindow(Context context){
        mView = LayoutInflater.from(context).inflate(R.layout.pen_thickness_popwindow,null);
        mPopupWindow = new PopupWindow(mView,1000,100,true);
        //设置popwindow的背景颜色
        //mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
        mSeekBar = (SeekBar)mView.findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean b) {
                if(mSeekBarCallBack!=null)
                    mSeekBarCallBack.seekBarProcessChanged(progess);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setSeekBarCallBack(SeekBarCallBack mSeekBarCallBack){
        this.mSeekBarCallBack = mSeekBarCallBack;
    }

    public SeekBarCallBack getSeeBarCallBack(){
        return mSeekBarCallBack;
    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }
    public interface SeekBarCallBack{
        void seekBarProcessChanged(int progess);
    }
}
