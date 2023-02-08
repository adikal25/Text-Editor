package shape;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class EllipticalShapes extends GeometricShapes {
	
	public EllipticalShapes () {
		this.prop.put("width", 0.0);
		this.prop.put("height", 0.0);
		setColor(clr);
		setFillColor(fillClr);
		setProperties(prop);
	}
	
	@Override
	public void draw(Graphics canvas) {
		((Graphics2D)canvas).setStroke(new BasicStroke(5f));
		((Graphics2D)canvas).setColor(getColor());
		
		// Drawing elliptical shapes by passing to it its position and its width and height
		((Graphics2D)canvas).drawOval((int)getPosition().getX(), (int)getPosition().getY(), 
		getProperties().get("width").intValue(), getProperties().get("height").intValue());
		((Graphics2D)canvas).setColor(getFillColor());
		
		// Filling elliptical shapes by passing the same parameters
		((Graphics2D)canvas).fillOval((int)getPosition().getX(), (int)getPosition().getY(), 
		getProperties().get("width").intValue(), getProperties().get("height").intValue());
	}
}
