package PaintKit;

import android.graphics.Path;

public class RetangleDrawInfo extends AbstractDrawInfo{
    public RetangleDrawInfo() {
        super();
        super.setType("RetangleDrawInfo");
    }

    public RetangleDrawInfo(AbstractPen pen) {
        super(pen);
        super.setType("RetangleDrawInfo");
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

        float left = Math.min(super.getmPointList().get(0).x,super.getmPointList().get(1).x);
        float top = Math.min(super.getmPointList().get(0).y,super.getmPointList().get(1).y);
        float right = Math.max(super.getmPointList().get(0).x,super.getmPointList().get(1).x);
        float bottom = Math.max(super.getmPointList().get(0).y,super.getmPointList().get(1).y);
        path.addRect(left,top,right,bottom,Path.Direction.CW);
    }
}
