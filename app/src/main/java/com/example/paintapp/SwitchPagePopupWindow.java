package com.example.paintapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import PaintKit.PaintBoard;

public class SwitchPagePopupWindow implements View.OnClickListener {
    private ImageButton mAddPage,mPreviewPage,mNextPage;
    private TextView mTextView;
    private PaintBoardView mPaintBoardView;
    private PopupWindow mPopupWindow;
    private View mView;



    public SwitchPagePopupWindow(Context context,PaintBoardView paintBoardView){
        //接收传递过来的PaintBoardView
        mPaintBoardView = paintBoardView;

        //关联mView布局
        mView = LayoutInflater.from(context).inflate(R.layout.addpage_popupwindow,null);
        ////创建popupwindow，宽为屏幕宽度，高100
        mPopupWindow = new PopupWindow(mView,500,100);
        //设置popwindow的背景颜色
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0xaa008577));
        // 设置相应外部事件
        mPopupWindow.setOutsideTouchable(true);

        mAddPage = (ImageButton)mView.findViewById(R.id.add_page);
        mPreviewPage = (ImageButton)mView.findViewById(R.id.preview_page);
        mNextPage = (ImageButton)mView.findViewById(R.id.next_page);
        mTextView =(TextView)mView.findViewById(R.id.this_page) ;

        mAddPage.setOnClickListener(this);
        mPreviewPage.setOnClickListener(this);
        mNextPage.setOnClickListener(this);

        mPreviewPage.setEnabled(false);
        mNextPage.setEnabled(false);

        paintBoardView.setmPreNextPageStatusChangeCallBack(new PaintBoardView.PreNextPageStatusChangeCallBack() {
            @Override
            public void preNextPageStatusChanged() {
                mPreviewPage.setEnabled(mPaintBoardView.canPrePage());
                mNextPage.setEnabled(mPaintBoardView.canNext());
                mTextView.setText(mPaintBoardView.getmPaintBoardIndex()+1 +"/"+mPaintBoardView.getmSumPaintBoards());
            }
        });
    }

    public void showPopupWindow(View view){
        mPopupWindow.showAsDropDown(view);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_page:
                mPaintBoardView.addNewPaintBoard();
                break;

            case R.id.preview_page:
                mPaintBoardView.prePage();
                break;

            case R.id.next_page:
                mPaintBoardView.nextPage();
                break;
        }
    }
}
