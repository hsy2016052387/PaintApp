package GeometryGraphics;

public class StraightLineFactory extends AbstractGraphicsFactory {
    @Override
    public AbstractGraphics createConcreGraph() {
        return new StraightLine();
    }
}
