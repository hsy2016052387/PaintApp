package FileIO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class BitmapControler {
    private Context context;
    private Bitmap backgroundBitmap,finalBitmap;


    public BitmapControler(Context context){
        this.context = context;
    }

    /**
     * @param width 宽度
     * @param height 高度
     * @param color 背景颜色
     * @return 创建一个指定背景颜色的bitmap
     */
    public Bitmap getBitMapByColor(int width,int height,int color) {
        if (backgroundBitmap == null) {
            int[] colors = new int[width * height];
            for (int i = 0; i < colors.length; i++) {
                colors[i] = color;
            }
            backgroundBitmap = Bitmap.createBitmap(colors,width,height,Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888,true);
        }else {
            backgroundBitmap.eraseColor(color);
        }
        return backgroundBitmap;
    }

    /**
     * 把bitmap保存为图片
     * @param bitmap 当前的bitmap
     * @param fileName 要保存的文件名
     */
    public void savaBitmapToPhoto(Bitmap bitmap,String fileName) {
        String imagePath =  Environment.getExternalStorageDirectory().toString() + "/BitmapPhoto" + fileName + ".png";
        File file = new File(imagePath);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public Bitmap combineBitmap(Bitmap backgroundBitmap,Bitmap foregroundBitmap) {
        if (finalBitmap == null) {
            finalBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),backgroundBitmap.getHeight(),Bitmap.Config.ARGB_8888);
        }else {
            finalBitmap.eraseColor(Color.TRANSPARENT);
        }
        Canvas canvas = new Canvas(finalBitmap);
        // 画背景图
        canvas.drawBitmap(backgroundBitmap,0,0,null);
        // 画前景图
        canvas.drawBitmap(foregroundBitmap,0,0,null);
        canvas.save();
        canvas.restore();
        return finalBitmap;
    }
}
