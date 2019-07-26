package PaintKit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawInfo {
    private Paint mPaint;
    private Path mPath;

    public DrawInfo(Paint paint,Path path){
        mPaint = paint;
        mPath = path;
    }

    public void draw(Canvas canvas){
        canvas.drawPath(mPath,mPaint);
    }
}
