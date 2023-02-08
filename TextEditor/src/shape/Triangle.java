package shape;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;


public class Triangle extends Polygons {
	
	public Triangle() {}
	
	// Set properties of a triangle like its vertices positions and area
	public Triangle(Point p1, Point p2, Point p3) {
		this.setPosition(p1);
		this.prop.put("x1", p1.getX());
		this.prop.put("y1", p1.getY());
		this.prop.put("x2", p2.getX());
		this.prop.put("y2", p2.getY());
		this.prop.put("x3", p3.getX());
		this.prop.put("y3", p3.getY());
		double area = Math.abs((((p1.getX())*(p2.getY()-p3.getY()))+((p2.getX())*(p3.getY()-p1.getY()))+((p3.getX())*(p1.getY()-p2.getY())))/2.0);
		this.prop.put("area", area);
		this.setProperties(prop);
	}
	
	@Override
	public void draw(Graphics canvas) {
		((Graphics2D)canvas).setStroke(new BasicStroke(5f));
		int[] x = {getProperties().get("x1").intValue(), getProperties().get("x2").intValue(), getProperties().get("x3").intValue()};
		int[] y = {getProperties().get("y1").intValue(), getProperties().get("y2").intValue(), getProperties().get("y3").intValue()};
		((Graphics2D)canvas).setColor(getColor());
		
		// Draw triangle by passing two arrays of all Xs and Ys of its vertices
		((Graphics2D)canvas).drawPolygon(x, y, 3);
		((Graphics2D)canvas).setColor(getFillColor());
		
		// Fill triangle by passing same parameters
		((Graphics2D)canvas).fillPolygon(x, y, 3);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape cloned = new Triangle();
		cloned.setColor(this.getColor());
		cloned.setFillColor(this.getFillColor());
		cloned.setPosition(this.getPosition());
		cloned.setProperties(this.getProperties());
		return cloned;
	}
}
