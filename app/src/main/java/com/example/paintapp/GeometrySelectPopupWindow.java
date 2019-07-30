package com.example.paintapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;

public class GeometrySelectPopupWindow implements View.OnClickListener {
    private ImageButton straightLine,retangle,circle;
    private PopupWindow mPopupWindow;
    private View mView;

    public GeometrySelectPopupWindow(Context context){
        //关联mview布局
        mView = LayoutInflater.from(context).inflate(R.layout.geometric_graphics_selected_popupwindow,null);
        //创建popupwindow，宽为屏幕宽度，高100
        mPopupWindow = new PopupWindow(mView,new ScreenUtil(context).getScreenSize(ScreenUtil.WIDTH),100,true);
        //设置popwindow的背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaa008577));

        straightLine = mView.findViewById(R.id.geometry_straight_line);
        retangle = mView.findViewById(R.id.geometry_retangle);
        circle = mView.findViewById(R.id.geometry_circle);

        straightLine.setOnClickListener(this);
        retangle.setOnClickListener(this);
        circle.setOnClickListener(this);

        straightLine.setSelected(true);

    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.geometry_straight_line:
                break;

            case R.id.geometry_retangle:
                break;

            case R.id.geometry_circle:
                break;
        }
    }
}
