package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class Retangel extends AbstractGraphics {
    @Override
    public void draw(Canvas canvas, PointF p1, PointF p2, Paint paint) {
        canvas.drawRect(p1.x,p1.y,p2.x,p2.y,paint);
    }

    @Override
    public void saveToPath(Path path, PointF p1, PointF p2) {
        path.addRect(p1.x,p1.y,p2.x,p2.y,Path.Direction.CW);
    }

}
