package PaintKit;

import android.graphics.Path;

public class StraightLineDrawInfo extends AbstractDrawInfo {
    public StraightLineDrawInfo() {
        super();
        super.setType("StraightLineDrawInfo");
    }

    public StraightLineDrawInfo(AbstractPen pen) {
        super(pen);
        super.setType("StraightLineDrawInfo");
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
        path.lineTo(super.getmPointList().get(1).x,super.getmPointList().get(1).y);
    }
}
