package com.example.paintapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import FileIO.BitmapControler;
import FileIO.FilePicker;
import FileIO.XmlManager;

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

    private XmlManager mXMLManager;
    private BitmapControler mBitmapControler;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_CHOOSEFILE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

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
        switch (view.getId()) {
            case R.id.pen_color:
                mPenColorPopupWindow.showPopupWindow(mPenColorView);
                break;

            case R.id.pen:
                if (!mPenView.isSelected()) {
                    mPenView.setSelected(true);
                    mGeometryView.setSelected(false);
                    mErasorView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoardView.Mode.DRAW);
                }
                break;

            case R.id.lineThickness:
                mPenThicknessPopupWindow.showPopupWindow(mLineView);
                break;

            case R.id.erasor:
                if (!mErasorView.isSelected()) {
                    mErasorView.setSelected(true);
                    mGeometryView.setSelected(false);
                    mPenView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoardView.Mode.ERASOR);
                }
                break;

            case R.id.geometry:
                if (!mGeometryView.isSelected()) {
                    mGeometryView.setSelected(true);
                    mErasorView.setSelected(false);
                    mPenView.setSelected(false);
                    mPaintBoardView.setmMode(PaintBoardView.Mode.Geometry);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu_item,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 右上角菜单item的点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.saveAsXML:
                if (mPaintBoardView.getmSumPaintBoards() == 1 && mPaintBoardView.getmDrawInfoList().size() < 1) {
                    Toast.makeText(MainActivity.this,"当前没有笔迹需要保存",Toast.LENGTH_SHORT).show();
                    break;
                }
                savaAsXmlItemClick();
                break;

            case R.id.openXML:
                openXmlItemClick();
                break;

            case R.id.saveAsPhoto:
                saveAsPhotoItemClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data) {//选择文件返回
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==RESULT_OK){
            switch(requestCode){
                  case REQUEST_CHOOSEFILE:
                      Uri uri=data.getData();
                      String chooseFilePath= FilePicker.getInstance(this).getChooseFileResultPath(uri);
                      Log.i("==>","选择文件返回："+chooseFilePath);
                      //sendFileMessage(chooseFilePath);
                      if (mXMLManager == null)
                          mXMLManager = new XmlManager(MainActivity.this);
                      mPaintBoardView.switchPaintBoardList(mXMLManager.pullXmlToPaintBoardList(chooseFilePath));
                      break;
            }
        }
    }

    /**
     * 右上角菜单saveAsXml的点击事件
     */
    private void savaAsXmlItemClick() {
        int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            Log.i("==>","没有权限");
            // 申请权限
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        Log.i("==>","点击保存为XML");
        if (mXMLManager == null)
            mXMLManager = new XmlManager(MainActivity.this);

        final EditText editText = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("用以下名字保存xml文件")
                .setView(editText)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mXMLManager.savaToXMl(mPaintBoardView.getmPaintBoardList(),editText.getText().toString());
                    }
                })
                .show();
    }

    /**
     * 右上角菜单openXml的点击事件
     */
    private void openXmlItemClick() {
        //选择文件【调用系统的文件管理】
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType(“image/*”);//选择图片
        //intent.setType(“audio/*”); //选择音频
        //intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
        //intent.setType(“video/*;image/*”);//同时选择视频和图片
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CHOOSEFILE);
    }

    /**
     * 右上角菜单saveAsPhoto的点击事件
     */
    private void saveAsPhotoItemClick() {
        if (mBitmapControler == null)
            mBitmapControler = new BitmapControler(MainActivity.this);
        Time time  =  new Time();
        time.setToNow();
        String str_time = time.format("%Y-%m-%d--%H:%M:%S");
        Log.i("MainActivity",str_time);

        Bitmap backgroundBitmap  = mBitmapControler.getBitMapByColor(mPaintBoardView.getWidth(),mPaintBoardView.getHeight(),Color.WHITE);
        Bitmap forebackgroundBitmap = mPaintBoardView.getmBitmap();
        Bitmap finalSaveBitmap = mBitmapControler.combineBitmap(backgroundBitmap,forebackgroundBitmap);
        mBitmapControler.savaBitmapToPhoto(finalSaveBitmap,str_time);
    }




}
