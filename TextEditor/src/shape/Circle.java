package shape;

import java.awt.Point;


public class Circle extends EllipticalShapes {

	public Circle() {}
	
	public Circle (Point topLeft, double diameter) {
		this.setPosition(topLeft);
		this.prop.put("width", diameter);
		this.prop.put("height", diameter);
		setProperties(prop);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape cloned = new Circle();
		cloned.setColor(this.getColor());
		cloned.setFillColor(this.getFillColor());
		cloned.setPosition(this.getPosition());
		cloned.setProperties(this.getProperties());
		return cloned;
	}
}
