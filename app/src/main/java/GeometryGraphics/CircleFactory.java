package GeometryGraphics;

public class CircleFactory extends AbstractGraphicsFactory {
    @Override
    public AbstractGraphics createConcreGraph() {
        return new Circle();
    }
}
