package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Circle extends AbstractGraphics {
    public Circle(){
        super();
        super.setType("Circle");
    }
    @Override
    public void drawToCanvas(Canvas canvas, PointF p1, PointF p2, Paint paint) {
        float radius = (float) Math.sqrt(Math.pow((double)(p1.x-p2.x),2) + Math.pow((double)(p1.y-p2.y),2));
        canvas.drawCircle((p1.x+p2.x)/2,(p1.y+p2.y)/2,radius,paint);
    }

    @Override
    public void saveToPath(Path path, PointF p1, PointF p2) {
        float radius = (float) Math.sqrt(Math.pow((double)(p1.x-p2.x),2) + Math.pow((double)(p1.y-p2.y),2));
        path.addCircle((p1.x+p2.x)/2,(p1.y+p2.y)/2,radius,Path.Direction.CW);
    }
}
