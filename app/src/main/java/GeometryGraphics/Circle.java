package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Circle extends AbstractGraphics {
    @Override
    public void draw(Canvas canvas, PointF p1, PointF p2, Paint paint) {
        //float radius = Math.sqrt(Math.pow((double)(p1.x-p2.x),2))
    }

    @Override
    public void saveToPath(Path path, PointF p1, PointF p2) {

    }
}
