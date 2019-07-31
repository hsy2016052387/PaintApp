package GeometryGraphics;

public class RetangleFactory extends AbstractGraphicsFactory {
    @Override
    public AbstractGraphics createConcreGraph() {
        return new Retangel();
    }
}
