package PaintKit;

import android.graphics.Color;

public abstract class AbstractPaper {
    public int getmWidth() {
        return mWidth;
    }

    public void setmWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getmBackgroudColor() {
        return mBackgroudColor;
    }

    public void setmBackgroudColor(int mBackgroudColor) {
        this.mBackgroudColor = mBackgroudColor;
    }



    public int getmHeight() {
        return mHeight;
    }

    public void setmHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    private int mWidth = 1000;
    private int mHeight = 1000;
    private int mBackgroudColor = Color.WHITE;
}
