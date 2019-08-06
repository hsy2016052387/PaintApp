package com.example.paintapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PenColorPopupWindow implements View.OnClickListener {
    private TextView blackView,redView,orangeView,yellowView,greenView,blueView,purposView;
    private PopupWindow mPopupWindow;
    private View mView;



    private ChooseColorCallback mChooseColorCallback;
    public PenColorPopupWindow(Context context){
        //关联mView布局
        mView = LayoutInflater.from(context).inflate(R.layout.pen_color_popupwindow,null);
        //创建popupwindow，宽为屏幕宽度，高100
        mPopupWindow = new PopupWindow(mView,new ScreenUtil(context).getScreenSize(ScreenUtil.WIDTH),150);
        //设置popwindow的背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaa008577));
        // 设置相应外部事件
        mPopupWindow.setOutsideTouchable(true);

        blackView = (TextView)mView.findViewById(R.id.color_black);
        redView = (TextView)mView.findViewById(R.id.color_red);
        orangeView = (TextView)mView.findViewById(R.id.color_orange);
        yellowView = (TextView)mView.findViewById(R.id.color_yellow);
        greenView = (TextView)mView.findViewById(R.id.color_green);
        blueView = (TextView)mView.findViewById(R.id.color_blue);
        purposView = (TextView)mView.findViewById(R.id.color_purpos);

        blackView.setOnClickListener(this);
        redView.setOnClickListener(this);
        orangeView.setOnClickListener(this);
        yellowView.setOnClickListener(this);
        greenView.setOnClickListener(this);
        blueView.setOnClickListener(this);
        purposView.setOnClickListener(this);
    }

    public ChooseColorCallback getmChooseColorCallback() {
        return mChooseColorCallback;
    }

    public void setmChooseColorCallback(ChooseColorCallback mChooseColorCallback) {
        this.mChooseColorCallback = mChooseColorCallback;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.color_black:
                mChooseColorCallback.onChooseColor(0xff000000);
                break;
            case R.id.color_red:
                mChooseColorCallback.onChooseColor(0xffff4949);
                break;
            case R.id.color_orange:
                mChooseColorCallback.onChooseColor(0xffff9800);
                break;
            case R.id.color_yellow:
                mChooseColorCallback.onChooseColor(0xffffff41);
                break;
            case R.id.color_green:
                mChooseColorCallback.onChooseColor(0xff17D517);
                break;
            case R.id.color_blue:
                mChooseColorCallback.onChooseColor(0xff3b3bff);
                break;
            case R.id.color_purpos:
                mChooseColorCallback.onChooseColor(0xff9027ec);
                break;
        }
    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }

    public interface ChooseColorCallback{
        void onChooseColor(int color);
    }
}
