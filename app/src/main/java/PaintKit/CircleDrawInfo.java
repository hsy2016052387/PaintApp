package PaintKit;

import android.graphics.Path;

public class CircleDrawInfo extends AbstractDrawInfo {
    public CircleDrawInfo() {
        super();
        super.setType("CircleDrawInfo");
    }

    public CircleDrawInfo(AbstractPen pen) {
        super(pen);
        super.setType("CircleDrawInfo");
    }

    /**
     * 利用点重新绘制出path
     * @param path 要绘制的path
     */
    @Override
    public void rebuildPath(Path path) {
        float startX = super.getmPointList().get(0).x;
        float startY = super.getmPointList().get(0).y;
        path.moveTo(startX,startY);

        float diameter = (float) Math.sqrt(Math.pow((double)(super.getmPointList().get(0).x-super.getmPointList().get(1).x),2) + Math.pow((double)(super.getmPointList().get(0).y-super.getmPointList().get(1).y),2));
        path.addCircle((super.getmPointList().get(0).x+super.getmPointList().get(1).x)/2,(super.getmPointList().get(0).y+super.getmPointList().get(1).y)/2,diameter/2,Path.Direction.CW);
    }
}
