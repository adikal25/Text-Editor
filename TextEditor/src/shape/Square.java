package shape;

import java.awt.Point;

public class Square extends Polygons {
	
	public Square() {
		
	}
	
	public Square(Point topLeft, double side) {
		this.setPosition(topLeft);
		this.prop.put("width", side);
		this.prop.put("height", side);
		setProperties(prop);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape cloned = new Square();
		cloned.setColor(this.getColor());
		cloned.setFillColor(this.getFillColor());
		cloned.setPosition(this.getPosition());
		cloned.setProperties(this.getProperties());
		return cloned;
	}
}
