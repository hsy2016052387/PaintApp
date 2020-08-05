package PaintKit;

import android.graphics.Color;

public abstract class AbstractPen {
    public int getmSize() {
        return mSize;
    }

    public void setmSize(int mSize) {
        this.mSize = mSize;
    }



    public int getmColor() {
        return mColor;
    }

    public void setmColor(int mColor) {
        this.mColor = mColor;
    }

    private int mSize = 5;
    private int mColor = Color.BLACK;

}
