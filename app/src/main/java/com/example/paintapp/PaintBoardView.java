package com.example.paintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import GeometryGraphics.AbstractGraphics;
import PaintKit.DrawInfo;
import PaintKit.Erasor;
import PaintKit.PaintBoard;
import PaintKit.Pen;
import PaintKit.Point;

import static android.content.ContentValues.TAG;


public class PaintBoardView extends View {
    private PointF mStartPoint,mEndPoint;
    private Paint mPaint,mOldPaint;
    private Path mPath;



    private Bitmap mBitmap;
    private Canvas mCanvas;

//    private float mLastX;
//    private float mLastY;

    private int mPaintBoardIndex;
    private int mSumPaintBoards;




    private Xfermode mXferModeClear;
    private Xfermode mXferModeDraw;

    private PaintBoard mPaintBoard;

    private boolean mDrawing;
    private boolean mCanEraser;



    private List<DrawInfo> mDrawInfoList;
    private List<DrawInfo> mRemoveInfoList;



    private List<PaintBoard> mPaintBoardList;

    private Mode mMode;
    private DrawInfo mDrawInfo;

    private StatusChangeCallBack mStatusChangeCallBack;
    private PreNextPageStatusChangeCallBack mPreNextPageStatusChangeCallBack;



    private AbstractGraphics mAbstractGraphics;

    public PaintBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public enum Mode{
        DRAW,
        ERASOR,
        Geometry
    }

    private void init(){
        mPaintBoardList = new ArrayList<>();
        mPaintBoard = new PaintBoard();

        mPaintBoardList.add(mPaintBoard);

        mPaintBoardIndex = 0;
        mSumPaintBoards = 1;



        mMode = Mode.DRAW;
        mDrawInfoList = mPaintBoard.getmDrawList();
        mRemoveInfoList = mPaintBoard.getmRemoveList();

        mStartPoint = new PointF();
        mEndPoint = new PointF();

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
        //初始化旧画笔
        mOldPaint = new Paint(mPaint);

        mXferModeDraw = null;
        mXferModeClear = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        //mPaint.setXfermode(mXferModeDraw);
        //mPaint.setXfermode(null);
    }

    private void initBitmap(){
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public Mode getmMode() {
        return mMode;
    }

    public void setmMode(Mode mode) {
        if (mode != mMode) {
            mMode = mode;
            if (mMode == Mode.ERASOR) {
                mPaint.setXfermode(mXferModeClear);

            } else{
                mPaint.setXfermode(mXferModeDraw);
            }
        }
    }
    public void setPenSize(int size){
        mPaint.setStrokeWidth(size);
        mPaintBoard.setPenSize(size);
    }

    public List<DrawInfo> getmDrawInfoList() {
        return mDrawInfoList;
    }

    public Bitmap getmBitmap() {
        if (mBitmap == null)
            initBitmap();
        return mBitmap;
    }

    public int getPenSize(){
        return mPaintBoard.getPenSize();
    }

    public void setPenColor(int color){
        mPaint.setColor(color);
        mPaintBoard.setPenColor(color);
    }

    public void setmAbstractGraphics(AbstractGraphics mAbstractGraphics) {
        this.mAbstractGraphics = mAbstractGraphics;
    }

    // ------------------------------------设置接口-----------------------------------
    public void setmStatusChangeCallBack(StatusChangeCallBack mStatusChangeCallBack) {
        this.mStatusChangeCallBack = mStatusChangeCallBack;
    }

    public void setmPreNextPageStatusChangeCallBack(PreNextPageStatusChangeCallBack mPreNextPageStatusChangeCallBack) {
        this.mPreNextPageStatusChangeCallBack = mPreNextPageStatusChangeCallBack;
    }

    // ------------------------------------设置接口-----------------------------------

    public int getmPaintBoardIndex() {
        return mPaintBoardIndex;
    }

    public int getmSumPaintBoards() {
        return mSumPaintBoards;
    }

    public List<PaintBoard> getmPaintBoardList() {
        return mPaintBoardList;
    }
    //判断能不能进行撤销操作
    public boolean canUndo(){
        return mDrawInfoList!=null && mDrawInfoList.size()>0;
    }

    //判断能不能进行反撤销操作
    public boolean canRedo(){
        return mRemoveInfoList!=null && mRemoveInfoList.size()>0;
    }

    //判断能不能按上一页
    public boolean canPrePage(){
        return mPaintBoardIndex>0;
    }

    //判断能不能按下一页
    public boolean canNext(){
        return mPaintBoardIndex < mSumPaintBoards - 1;
    }
    //保存笔迹到list中
    private void saveDrawingPath(DrawInfo drawInfo){
        if(mDrawInfoList==null)
            mDrawInfoList = new ArrayList<>();
        if(drawInfo!=null)
            mDrawInfoList.add(drawInfo);
        mCanEraser = true;

        if(mStatusChangeCallBack!=null)
            mStatusChangeCallBack.undoRedoStatusChanged();
    }

    //先把bitmap清空，再把储存在list中的笔迹重画一遍
    private void reDraw(){
        if(mDrawInfoList!=null){
            //清空bitmap
            if(mBitmap!=null)
                mBitmap.eraseColor(Color.TRANSPARENT);

            for(DrawInfo drawInfo:mDrawInfoList){
                rebuildPath(drawInfo);
            }
            invalidate();
        }

    }

    //利用DrawInfo中的信息把path重新画在bitMap上
    private void rebuildPath(DrawInfo drawInfo){
        float startX = drawInfo.getmPointList().get(0).x;
        float startY = drawInfo.getmPointList().get(0).y;

        mPath.moveTo(startX,startY);
        switch (drawInfo.getType()){
            case "Draw":
                mPaint.setXfermode(mXferModeDraw);
                mPaint.setColor(drawInfo.getmPen().getmColor());
                mPaint.setStrokeWidth(drawInfo.getmPen().getmSize());

                for(Point point: drawInfo.getmPointList()){
                    mPath.quadTo(startX, startY, (point.x + startX) / 2, (point.y + startY) / 2);
                    startX = point.x;
                    startY = point.y;
                }
                mCanvas.drawPath(mPath,mPaint);
                mPath.reset();
                break;
            case "Erasor":
                mPaint.setXfermode(mXferModeClear);
                mPaint.setColor(drawInfo.getmPen().getmColor());
                mPaint.setStrokeWidth(drawInfo.getmPen().getmSize());
                for(Point point: drawInfo.getmPointList()){
                    mPath.quadTo(startX, startY, (point.x + startX) / 2, (point.y + startY) / 2);
                    startX = point.x;
                    startY = point.y;
                }
                mCanvas.drawPath(mPath,mPaint);
                mPath.reset();
                break;
            //直线、矩形、圆形的DrawInfo中只有两个点
            case "StraightLine":
                mPaint.setXfermode(mXferModeDraw);
                mPaint.setColor(drawInfo.getmPen().getmColor());
                mPaint.setStrokeWidth(drawInfo.getmPen().getmSize());
                //画线
                mPath.lineTo(drawInfo.getmPointList().get(1).x,drawInfo.getmPointList().get(1).y);
                mCanvas.drawPath(mPath,mPaint);
                mPath.reset();
                break;
            case "Retangle":
                mPaint.setXfermode(mXferModeDraw);
                mPaint.setColor(drawInfo.getmPen().getmColor());
                mPaint.setStrokeWidth(drawInfo.getmPen().getmSize());
                //画矩形
                float left = Math.min(drawInfo.getmPointList().get(0).x,drawInfo.getmPointList().get(1).x);
                float top = Math.min(drawInfo.getmPointList().get(0).y,drawInfo.getmPointList().get(1).y);
                float right = Math.max(drawInfo.getmPointList().get(0).x,drawInfo.getmPointList().get(1).x);
                float bottom = Math.max(drawInfo.getmPointList().get(0).y,drawInfo.getmPointList().get(1).y);
                mPath.addRect(left,top,right,bottom,Path.Direction.CW);
                mCanvas.drawPath(mPath,mPaint);
                mPath.reset();
                break;
            case "Circle":
                mPaint.setXfermode(mXferModeDraw);
                mPaint.setColor(drawInfo.getmPen().getmColor());
                mPaint.setStrokeWidth(drawInfo.getmPen().getmSize());
                //画圆形
                float radius = (float) Math.sqrt(Math.pow((double)(drawInfo.getmPointList().get(0).x-drawInfo.getmPointList().get(1).x),2) + Math.pow((double)(drawInfo.getmPointList().get(0).y-drawInfo.getmPointList().get(1).y),2));
                mPath.addCircle((drawInfo.getmPointList().get(0).x+drawInfo.getmPointList().get(1).x)/2,(drawInfo.getmPointList().get(0).y+drawInfo.getmPointList().get(1).y)/2,radius,Path.Direction.CW);
                mCanvas.drawPath(mPath,mPaint);
                mPath.reset();
                break;
        }
    }

    //撤销操作：把mDrawInfoList的最后一个对象移到mRemoveInfoList中（mRemoveInfoList用于反撤销），重画剩下的笔迹
    public void undo(){
        int size = mDrawInfoList==null ? 0 : mDrawInfoList.size();
        if(size>0){
            DrawInfo drawInfo = mDrawInfoList.remove(size-1);
            if(mRemoveInfoList==null)
                mRemoveInfoList = new ArrayList<>();
            mRemoveInfoList.add(drawInfo);

            if(size==1)
                mCanEraser =false;
            //保存画笔信息
            mOldPaint.setStrokeWidth(mPaint.getStrokeWidth());
            mOldPaint.setColor(mPaint.getColor());
            mOldPaint.setXfermode(mPaint.getXfermode());
            reDraw();
            //恢复画笔信息
            mPaint.setStrokeWidth(mOldPaint.getStrokeWidth());
            mPaint.setColor(mOldPaint.getColor());
            mPaint.setXfermode(mOldPaint.getXfermode());
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoRedoStatusChanged();
        }
    }

    //反撤销：把mRemoveInfoList中的最后一条笔迹添加到mDrawInfoList，重画mDrawInfoList中的笔迹
    public void redo(){
        int size = mRemoveInfoList==null ? 0 : mRemoveInfoList.size();
        if(size>0){
            DrawInfo drawInfo = mRemoveInfoList.remove(size-1);
            mDrawInfoList.add(drawInfo);
            mCanEraser = true;
            //保存画笔信息
            mOldPaint.setStrokeWidth(mPaint.getStrokeWidth());
            mOldPaint.setColor(mPaint.getColor());
            mOldPaint.setXfermode(mPaint.getXfermode());
            reDraw();
            //恢复画笔信息
            mPaint.setStrokeWidth(mOldPaint.getStrokeWidth());
            mPaint.setColor(mOldPaint.getColor());
            mPaint.setXfermode(mOldPaint.getXfermode());
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoRedoStatusChanged();
        }
    }

    //清空操作：删除mDrawInfoList和mRemoveInfoList的笔迹，清空bitmap
    public void clear(){
        if(mBitmap!=null){
            if(mDrawInfoList!=null)
                mDrawInfoList.clear();

            if(mRemoveInfoList!=null)
                mRemoveInfoList.clear();

            mCanEraser = false;
            mBitmap.eraseColor(Color.TRANSPARENT);
            invalidate();
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoRedoStatusChanged();
        }
    }



    //设置PaintBoardView当前控制的PaintBoard（用于多页画图）
    private void setCurrentPaintBoard(PaintBoard paintBoard){
        //更换PaintBoard对象
        mPaintBoard = paintBoard;
        //更换PaintBoard的DrawInfoList
        mDrawInfoList = mPaintBoard.getmDrawList();
        //更换PaintBoard的RemoveInfoList
        mRemoveInfoList = mPaintBoard.getmRemoveList();
        //设置mPaintboard的画笔
        mPaintBoard.setPenSize((int)mPaint.getStrokeWidth());
        mPaintBoard.setPenColor(mPaint.getColor());
    }

    //增加新的PaintBoard，用于多页画图
    public void addNewPaintBoard(){

        mPaintBoardList.add(new PaintBoard());
        //总数+1
        mSumPaintBoards++;
        //index指向最后一个PaintBoard
        mPaintBoardIndex = mSumPaintBoards - 1;
        //设置PaintBoardView当前控制的PaintBoard为最后一个PaintBoard
        setCurrentPaintBoard(mPaintBoardList.get(mPaintBoardIndex));
        //清空Bitmap
        if(mBitmap!=null)
            mBitmap.eraseColor(Color.TRANSPARENT);
        //设置不能擦除
        mCanEraser = false;
        //invalidate();
        //更新撤销反撤销按钮的状态（是否可点击）
        if(mStatusChangeCallBack!=null)
            mStatusChangeCallBack.undoRedoStatusChanged();
        //更新前一页和下一页的按钮状态
        if(mPreNextPageStatusChangeCallBack!=null)
            mPreNextPageStatusChangeCallBack.preNextPageStatusChanged();

    }

    //切换到上一页（多页画图）
    public void prePage(){

        if(mPaintBoardIndex>0){

            //设置当前控制的PaintBoard为前一个
            setCurrentPaintBoard(mPaintBoardList.get(--mPaintBoardIndex));
            //更新撤销反撤销按钮的状态（是否可点击）
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoRedoStatusChanged();
            //更新前一页和下一页的按钮状态
            if(mPreNextPageStatusChangeCallBack!=null)
                mPreNextPageStatusChangeCallBack.preNextPageStatusChanged();
            //设置是否可擦除
            mCanEraser = canUndo();

            //保存画笔信息
            mOldPaint.setStrokeWidth(mPaint.getStrokeWidth());
            mOldPaint.setColor(mPaint.getColor());
            mOldPaint.setXfermode(mPaint.getXfermode());
            reDraw();
            //恢复画笔信息
            mPaint.setStrokeWidth(mOldPaint.getStrokeWidth());
            mPaint.setColor(mOldPaint.getColor());
            mPaint.setXfermode(mOldPaint.getXfermode());
        }
    }

    //切换到下一页（多页画图）
    public void nextPage(){
        if(mPaintBoardIndex < mSumPaintBoards-1){

            //设置当前控制的PaintBoard为下一个
            setCurrentPaintBoard(mPaintBoardList.get(++mPaintBoardIndex));
            //更新撤销反撤销按钮的状态（是否可点击）
            if(mStatusChangeCallBack!=null)
                mStatusChangeCallBack.undoRedoStatusChanged();
            //更新前一页和下一页的按钮状态
            if(mPreNextPageStatusChangeCallBack!=null)
                mPreNextPageStatusChangeCallBack.preNextPageStatusChanged();
            //设置是否可擦除
            mCanEraser = canUndo();
            //保存画笔信息
            mOldPaint.setStrokeWidth(mPaint.getStrokeWidth());
            mOldPaint.setColor(mPaint.getColor());
            mOldPaint.setXfermode(mPaint.getXfermode());
            reDraw();
            //恢复画笔信息
            mPaint.setStrokeWidth(mOldPaint.getStrokeWidth());
            mPaint.setColor(mOldPaint.getColor());
            mPaint.setXfermode(mOldPaint.getXfermode());
        }
    }

    /**
     * 切换View控制的List，用于反序列化
     * @param paintBoardList 接收反序列化xml文件得到的paintBoardList
     */
    public void switchPaintBoardList(List<PaintBoard> paintBoardList) {
        if (paintBoardList.size() < 1) {
            return;
        }

        mPaintBoardList = paintBoardList;
        mPaintBoardIndex = 0;
        mSumPaintBoards = paintBoardList.size();

        // 初始化显示画布所需的工具
        if (mPath == null)
            mPath = new Path();
        if (mBitmap == null) {
            initBitmap();
        }

        setCurrentPaintBoard(mPaintBoardList.get(mPaintBoardIndex));
        //更新撤销反撤销按钮的状态（是否可点击）
        if(mStatusChangeCallBack!=null)
            mStatusChangeCallBack.undoRedoStatusChanged();
        //更新前一页和下一页的按钮状态
        if(mPreNextPageStatusChangeCallBack!=null)
            mPreNextPageStatusChangeCallBack.preNextPageStatusChanged();
        //设置是否可擦除
        mCanEraser = canUndo();
        //保存画笔信息
        mOldPaint.setStrokeWidth(mPaint.getStrokeWidth());
        mOldPaint.setColor(mPaint.getColor());
        mOldPaint.setXfermode(mPaint.getXfermode());
        reDraw();
        //恢复画笔信息
        mPaint.setStrokeWidth(mOldPaint.getStrokeWidth());
        mPaint.setColor(mOldPaint.getColor());
        mPaint.setXfermode(mOldPaint.getXfermode());
    }



    //----------------------------------内部接口------------------------------------
    //用于设置撤销和反撤销按钮的状态
    public interface StatusChangeCallBack{
        void undoRedoStatusChanged();
    }

    public interface PreNextPageStatusChangeCallBack{
        void preNextPageStatusChanged();
    }
    //----------------------------------内部接口------------------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        if(mMode == Mode.Geometry && mDrawing && mAbstractGraphics!=null)
            mAbstractGraphics.drawToCanvas(canvas,mStartPoint,mEndPoint,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        final int action = event.getAction() & MotionEvent.ACTION_MASK;

        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "down");

                //开始书写笔迹
                mDrawing = true;
                mStartPoint.x = event.getX();
                mStartPoint.y = event.getY();
                if (mPath == null) {
                    mPath = new Path();
                }

                //书写或几何图形模式或可擦除模式
                if(mMode ==  Mode.DRAW || mMode == Mode.Geometry || mCanEraser){
                    switch (mMode){
                        case DRAW:
                            mDrawInfo = new DrawInfo(new Pen(mPaintBoard.getPenSize(),mPaintBoard.getPenColor()),"Draw");
                            break;
                        case ERASOR:
                            mDrawInfo = new DrawInfo(new Pen(mPaintBoard.getPenSize(),mPaintBoard.getPenColor()),"Erasor");
                            break;
                        case Geometry:
                            mDrawInfo = new DrawInfo(new Pen(mPaintBoard.getPenSize(),mPaintBoard.getPenColor()),mAbstractGraphics.getType());
                            break;
                    }
                    mDrawInfo.getmPointList().add(new Point(mStartPoint.x,mStartPoint.y));
                }

                mPath.moveTo(mStartPoint.x,mStartPoint.y);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "move");

                mEndPoint.x = event.getX();
                mEndPoint.y = event.getY();

                //mPath.reset();
                //mPath.addRect(left,top,right,bottom,Path.Direction.CW);
                if (mBitmap == null) {
                    initBitmap();
                }
                if (mMode == Mode.ERASOR && !mCanEraser) {
                    break;
                }

                //画笔或橡皮擦模式时才在move的过程中获取点
                if(mDrawInfo!=null && mMode != Mode.Geometry){
                    mDrawInfo.getmPointList().add(new Point(mEndPoint.x,mEndPoint.y));
                }


                //如果是画笔和橡皮擦模式，直接画到bitmap上
                if(mMode!= Mode.Geometry){
                    //这里终点设为两点的中心点的目的在于使绘制的曲线更平滑，如果终点直接设置为x,y，效果和lineto是一样的,实际是折线效果
                    mPath.quadTo(mStartPoint.x, mStartPoint.y, (mEndPoint.x + mStartPoint.x) / 2, (mEndPoint.y + mStartPoint.y) / 2);
                    mCanvas.drawPath(mPath,mPaint);
                    mStartPoint.x = event.getX();
                    mStartPoint.y = event.getY();
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "up");

                if(mMode == Mode.Geometry && mAbstractGraphics!=null){
                    mAbstractGraphics.saveToPath(mPath,mStartPoint,mEndPoint);
                    mCanvas.drawPath(mPath,mPaint);
                }

                //up的时候获取最后一个点并保存路径
                if(mDrawInfo!=null){
                    mDrawInfo.getmPointList().add(new Point(mEndPoint.x,mEndPoint.y));
                    saveDrawingPath(mDrawInfo);
                }


                //结束书写
                mDrawInfo = null;
                mDrawing = false;
                mPath.reset();
                break;
        }
        return true;
    }
}
