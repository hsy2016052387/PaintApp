package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public class StraightLine extends AbstractGraphics {
    public StraightLine(){
        super();
        super.setType("StraightLine");
    }
    @Override
    public void drawToCanvas(Canvas canvas, PointF p1, PointF p2, Paint paint) {
        canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
    }

    @Override
    public void saveToPath(Path path, PointF p1, PointF p2) {
        path.lineTo(p2.x,p2.y);
    }
}
