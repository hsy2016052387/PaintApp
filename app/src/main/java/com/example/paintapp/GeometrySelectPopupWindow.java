package com.example.paintapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
        import android.widget.PopupWindow;

        import GeometryGraphics.AbstractGraphicsFactory;
        import GeometryGraphics.CircleFactory;
        import GeometryGraphics.RetangleFactory;
        import GeometryGraphics.StraightLineFactory;

public class GeometrySelectPopupWindow implements View.OnClickListener {
    private ImageButton straightLine,retangle,circle;
    private PopupWindow mPopupWindow;
    private View mView;
    private PaintBoardView mPaintBoardView;

    private AbstractGraphicsFactory mAbstractGraphicsFactory;

    public GeometrySelectPopupWindow(Context context,PaintBoardView paintBoardView){
        //接收传递过来的paintBoardview
        mPaintBoardView = paintBoardView;

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

        //默认模式是直线
        straightLine.setSelected(true);
        //创建直线工厂
        mAbstractGraphicsFactory = new StraightLineFactory();
        //利用直线工厂创建直线类，赋值到mPaintBoardView的抽象图形中
        mPaintBoardView.setmAbstractGraphics(mAbstractGraphicsFactory.createConcreGraph());
    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.geometry_straight_line:
                if(!straightLine.isSelected()){
                    //选中该按钮
                    straightLine.setSelected(true);
                    retangle.setSelected(false);
                    circle.setSelected(false);
                    //创建直线工厂
                    mAbstractGraphicsFactory = new StraightLineFactory();
                    //利用直线工厂创建直线对象并赋值到mPaintBoardView的抽象图形中
                    mPaintBoardView.setmAbstractGraphics(mAbstractGraphicsFactory.createConcreGraph());
                }
                break;

            case R.id.geometry_retangle:
                if(!retangle.isSelected()){
                    //选中该按钮
                    retangle.setSelected(true);
                    straightLine.setSelected(false);
                    circle.setSelected(false);
                    //创建矩形工厂
                    mAbstractGraphicsFactory = new RetangleFactory();
                    //利用矩形工厂创建矩形对象并赋值到mPaintBoardView的抽象图形中
                    mPaintBoardView.setmAbstractGraphics(mAbstractGraphicsFactory.createConcreGraph());
                }
                break;

            case R.id.geometry_circle:
                if(!circle.isSelected()){
                    //选中该按钮
                    circle.setSelected(true);
                    straightLine.setSelected(false);
                    retangle.setSelected(false);
                    //创建圆形工厂
                    mAbstractGraphicsFactory = new CircleFactory();
                    //利用矩形工厂创建矩形对象并赋值到mPaintBoardView的抽象图形中
                    mPaintBoardView.setmAbstractGraphics(mAbstractGraphicsFactory.createConcreGraph());
                }
                break;
        }
    }
}
