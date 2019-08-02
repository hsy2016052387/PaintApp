package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Retangel extends AbstractGraphics {
    public Retangel(){
        super();
        super.setType("Retangle");
    }
    @Override
    public void drawToCanvas(Canvas canvas, PointF p1, PointF p2, Paint paint) {
        float left = Math.min(p1.x,p2.x);
        float top = Math.min(p1.y,p2.y);
        float right = Math.max(p1.x,p2.x);
        float bottom = Math.max(p1.y,p2.y);
        canvas.drawRect(left,top,right,bottom,paint);
    }

    @Override
    public void saveToPath(Path path, PointF p1, PointF p2) {
        float left = Math.min(p1.x,p2.x);
        float top = Math.min(p1.y,p2.y);
        float right = Math.max(p1.x,p2.x);
        float bottom = Math.max(p1.y,p2.y);
        path.addRect(left,top,right,bottom,Path.Direction.CW);
    }

}
