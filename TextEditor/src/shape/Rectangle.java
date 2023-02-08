package shape;

import java.awt.Point;

public class Rectangle extends Polygons {
	
	public Rectangle() {
		
	}
	
	public Rectangle(Point topLeft, double width, double height) {
		this.setPosition(topLeft);
		this.prop.put("width", width);
		this.prop.put("height", height);
		setProperties(prop);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape cloned = new Rectangle();
		cloned.setColor(this.getColor());
		cloned.setFillColor(this.getFillColor());
		cloned.setPosition(this.getPosition());
		cloned.setProperties(this.getProperties());
		return cloned;
	}
}
