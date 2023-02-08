package shape;

public interface DrawingEngine {

    /* redraw all shapes on the canvas */
    public void refresh(java.awt.Graphics canvas);

    public void addShape(Shape shape);
    public void removeShape(Shape shape);
    public void updateShape(Shape oldShape, Shape newShape);

    /* return the created shapes objects */
    public Shape[] getShapes();
    
    /* return the classes (types) of supported shapes already exist and the
     * ones that can be dynamically loaded at runtime */
    public java.util.List<Class<? extends Shape>> getSupportedShapes();
  

   
}