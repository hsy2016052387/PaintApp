package PaintKit;

import android.graphics.Path;

public class ErasorDrawInfo extends AbstractDrawInfo {

    public ErasorDrawInfo() {
        super();
        super.setType("ErasorDrawInfo");
    }

    public ErasorDrawInfo(AbstractPen pen) {
        super(pen);
        super.setType("ErasorDrawInfo");
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
        for(Point point: super.getmPointList()){
            path.quadTo(startX, startY, (point.x + startX) / 2, (point.y + startY) / 2);
            startX = point.x;
            startY = point.y;
        }
    }
}
