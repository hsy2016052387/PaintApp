package PaintKit;

public class DrawInfoSimpleFactory {
    public static AbstractDrawInfo createConcreDrawInfo(String type) {
        AbstractDrawInfo abstractDrawInfo = null;
        switch (type) {
            case "PenDrawInfo":
                abstractDrawInfo =  new PenDrawInfo();
                break;
            case "ErasorDrawInfo":
                abstractDrawInfo = new ErasorDrawInfo();
                break;
            case "StraightLineDrawInfo":
                abstractDrawInfo = new StraightLineDrawInfo();
                break;
            case "RetangleDrawInfo":
                abstractDrawInfo = new RetangleDrawInfo();
                break;
            case "CircleDrawInfo":
                abstractDrawInfo = new CircleDrawInfo();
                break;
        }
        return abstractDrawInfo;
    }

    public static AbstractDrawInfo createConcreDrawInfoWithPen(String type,Pen pen) {
        AbstractDrawInfo abstractDrawInfo = null;
        switch (type) {
            case "PenDrawInfo":
                abstractDrawInfo =  new PenDrawInfo(pen);
                break;
            case "ErasorDrawInfo":
                abstractDrawInfo = new ErasorDrawInfo(pen);
                break;
            case "StraightLineDrawInfo":
                abstractDrawInfo = new StraightLineDrawInfo(pen);
                break;
            case "RetangleDrawInfo":
                abstractDrawInfo = new RetangleDrawInfo(pen);
                break;
            case "CircleDrawInfo":
                abstractDrawInfo = new CircleDrawInfo(pen);
                break;
        }
        return abstractDrawInfo;
    }
}
