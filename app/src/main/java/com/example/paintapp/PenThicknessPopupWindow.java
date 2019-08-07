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
    public PenThicknessPopupWindow(Context context,int seekBarDefaultProcess) {
        // 关联mView布局
        mView = LayoutInflater.from(context).inflate(R.layout.pen_thickness_popwindow,null);
        // 创建popupwindow，宽为屏幕宽度，高100
        mPopupWindow = new PopupWindow(mView,new ScreenUtil(context).getScreenSize(ScreenUtil.WIDTH),100);
        // 设置popwindow的背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaa008577));
        // 设置相应外部事件
        mPopupWindow.setOutsideTouchable(true);

        mSeekBar = (SeekBar)mView.findViewById(R.id.seekBar);
        // 设置seekbar默认值
        mSeekBar.setProgress(seekBarDefaultProcess);
        // 设置seekbar progress改变监听事件
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean b) {
                if (mSeekBarCallBack != null)
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

    public void setSeekBarCallBack(SeekBarCallBack mSeekBarCallBack) {
        this.mSeekBarCallBack = mSeekBarCallBack;
    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }

    public interface SeekBarCallBack {
        void seekBarProcessChanged(int progess);
    }
}
