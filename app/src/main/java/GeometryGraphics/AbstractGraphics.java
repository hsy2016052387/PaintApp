package GeometryGraphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

public abstract class AbstractGraphics {
    public abstract void draw(Canvas canvas, PointF p1, PointF p2, Paint paint);
    public abstract void saveToPath(Path path,PointF p1,PointF p2);
}
