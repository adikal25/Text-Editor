package shape;

public interface Shape{

    public void setPosition(java.awt.Point position);
    public java.awt.Point getPosition();

    // update shape specific properties (e.g., radius)
    public void setProperties(java.util.Map<String, Double> properties);
    public java.util.Map<String, Double> getProperties();

    public void setColor(java.awt.Color color);
    public java.awt.Color getColor();

    public void setFillColor(java.awt.Color color);
    public java.awt.Color getFillColor();

    public void draw(java.awt.Graphics canvas); // redraw the shape on the canvas

    public Object clone() throws CloneNotSupportedException; // create a deep clone of the shape
}