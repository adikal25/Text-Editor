package shape;


import java.awt.Canvas;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Sketchpad {
	
	 public JFrame frame;
	
	public Sketchpad() {
           frame = new JFrame();
           initialize();
	}

	// Initializing drawing engine, default color and fill color and other booleans to help in drawing
	DrawingEngine engine = new Logic();
	private int action = -1;
	boolean secondClick = false, thirdClick = false, moveAttempt = false, resizeAttempt = false;
	Point firstPoint, secondPoint;
	Color clr = Color.BLACK, fillClr = Color.WHITE;
	Shape toMove = null;
	Class<Shape> importedClass;
	
	/**
	 * Initialize the contents of the frame.
	 */
	private Shape contains(Point selected) {
		Shape[] shapes = engine.getShapes();
		for (int i = shapes.length -1 ; i>= 0 ; i--) {
			// If the sum of distance from the selected point to first end of line & from the
			// selected point to the second end equals the length of the line (with error)
			if (shapes[i].getClass().getSimpleName().equals("Triangle")) {
				if(Math.abs((((selected.getX())*(shapes[i].getProperties().get("y2")-shapes[i].getProperties().get("y3")))+((shapes[i].getProperties().get("x2"))*(shapes[i].getProperties().get("y3")-selected.getY()))+((shapes[i].getProperties().get("x3"))*(selected.getY()-shapes[i].getProperties().get("y2"))))/2.0) + 
				 Math.abs((((shapes[i].getProperties().get("x1"))*(selected.getY()-shapes[i].getProperties().get("y3")))+((selected.getX())*(shapes[i].getProperties().get("y3")-shapes[i].getProperties().get("y1")))+((shapes[i].getProperties().get("x3"))*(shapes[i].getProperties().get("y1")-selected.getY())))/2.0) +
				 Math.abs((((shapes[i].getProperties().get("x1"))*(shapes[i].getProperties().get("y2")-selected.getY()))+((shapes[i].getProperties().get("x2"))*(selected.getY()-shapes[i].getProperties().get("y1")))+((selected.getX())*(shapes[i].getProperties().get("y1")-shapes[i].getProperties().get("y2"))))/2.0) <= shapes[i].getProperties().get("area") * 1.001
				 && Math.abs((((selected.getX())*(shapes[i].getProperties().get("y2")-shapes[i].getProperties().get("y3")))+((shapes[i].getProperties().get("x2"))*(shapes[i].getProperties().get("y3")-selected.getY()))+((shapes[i].getProperties().get("x3"))*(selected.getY()-shapes[i].getProperties().get("y2"))))/2.0) + 
				 Math.abs((((shapes[i].getProperties().get("x1"))*(selected.getY()-shapes[i].getProperties().get("y3")))+((selected.getX())*(shapes[i].getProperties().get("y3")-shapes[i].getProperties().get("y1")))+((shapes[i].getProperties().get("x3"))*(shapes[i].getProperties().get("y1")-selected.getY())))/2.0) +
				 Math.abs((((shapes[i].getProperties().get("x1"))*(shapes[i].getProperties().get("y2")-selected.getY()))+((shapes[i].getProperties().get("x2"))*(selected.getY()-shapes[i].getProperties().get("y1")))+((selected.getX())*(shapes[i].getProperties().get("y1")-shapes[i].getProperties().get("y2"))))/2.0) >= shapes[i].getProperties().get("area") * 0.998){
					return shapes[i];
				}
			}
			else if (shapes[i].getClass().getSimpleName().equals("Circle")) {
				if (Point.distance(selected.getX(), selected.getY(), shapes[i].getPosition().getX() + shapes[i].getProperties().get("width")/2, shapes[i].getPosition().getY() + shapes[i].getProperties().get("height")/2) <= shapes[i].getProperties().get("width") / 2) {
					return shapes[i];

				}
			}
			// If satisfying the inequality (x-h)^2/a^2 + (y-k)^2/b^2 <= 1
			// where (h, k) is the center of ellipse and a is major axis and b is minor axis
			else if (shapes[i].getClass().getSimpleName().equals("Ellipse")) {
				double x = selected.getX(), y = selected.getY();
				double h = shapes[i].getPosition().getX() + shapes[i].getProperties().get("width")/2;
				double k = shapes[i].getPosition().getY() + shapes[i].getProperties().get("height")/2;
				double a = shapes[i].getProperties().get("width")/2, b = shapes[i].getProperties().get("height")/2;
				if ((Math.pow(x - h, 2) / Math.pow(a, 2)) + (Math.pow(y - k, 2) / Math.pow(b, 2)) <= 1) {
					return shapes[i];
				}
			}
			else if (shapes[i].getClass().getSimpleName().equals("Rectangle")) {
				if (Math.abs(selected.getX() - (shapes[i].getPosition().getX() + shapes[i].getProperties().get("width")/2)) <= shapes[i].getProperties().get("width")/2 
				&& Math.abs(selected.getY() - (shapes[i].getPosition().getY() + shapes[i].getProperties().get("height")/2)) <= shapes[i].getProperties().get("height")/2) {
					return shapes[i];

				}
			}
			else if (shapes[i].getClass().getSimpleName().equals("Square")) {
				if (Math.abs(selected.getX() - (shapes[i].getPosition().getX() + shapes[i].getProperties().get("width")/2)) <= shapes[i].getProperties().get("width")/2 
						&& Math.abs(selected.getY() - (shapes[i].getPosition().getY() + shapes[i].getProperties().get("height")/2)) <= shapes[i].getProperties().get("height")/2) {
					return shapes[i];
				}
			}
		}
		return null;
	}
	
	private void initialize() {
		
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
                frame.setVisible(true);;
		Canvas canvas = new Canvas();
		canvas.setLocation(1, 2);
		canvas.setSize(700, 700);
		
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				
				// Circle action
				 if (action == 1) {
					if (!secondClick) {
						firstPoint = e.getPoint();
						secondClick = true;
					}
					else {
						Point boundary = e.getPoint();
						double radius = Point.distance(firstPoint.getX(), firstPoint.getY(), boundary.getX(), boundary.getY());
						Point topLeft = new Point();
						topLeft.setLocation(firstPoint.getX() - radius, firstPoint.getY() - radius);
						Shape circle = new Circle(topLeft, radius*2);
						circle.setColor(Color.BLACK);
						circle.setFillColor(Color.WHITE);
						engine.addShape(circle);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
                                                firstPoint = e.getPoint();
                                                secondClick=true;
						resizeAttempt = true;
						if (contains(e.getPoint()) != null) {
						firstPoint = e.getPoint();
						resizeAttempt = true;
						moveAttempt = false;
					}
					}
				}
				// Ellipse action
				else if (action == 2) {
					if (!secondClick) {
						firstPoint = e.getPoint();
						secondClick = true;
					}
					else if (!thirdClick) {
						secondPoint = e.getPoint();
						thirdClick = true;
					}
					else {
						double halfWidth = Math.abs(firstPoint.getX() - secondPoint.getX());
						double halfHeight = Math.abs(firstPoint.getY() - e.getPoint().getY());
						Point topLeft = new Point();
						topLeft.setLocation(firstPoint.getX() - halfWidth, firstPoint.getY() - halfHeight);
						Shape ellipse = new Ellipse(topLeft, 2*halfWidth, 2*halfHeight);
						ellipse.setColor(Color.BLACK);
						ellipse.setFillColor(Color.WHITE);
						engine.addShape(ellipse);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						secondClick = false;
						thirdClick = false;
					}
				}
				// Triangle action
				else if (action == 3) {
					if (!secondClick) {
						firstPoint = e.getPoint();
						secondClick = true;
					}
					else if (!thirdClick) {
						secondPoint = e.getPoint();
						thirdClick = true;
					}
					else {
						Shape triangle = new Triangle(firstPoint, secondPoint, e.getPoint());
						triangle.setColor(Color.BLACK);
						triangle.setFillColor(Color.WHITE);
						engine.addShape(triangle);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						secondClick = false;
						thirdClick = false;
					}
				}
				// Rectangle action
				else if (action == 4) {
					if (!secondClick) {
						firstPoint = e.getPoint();
						secondClick = true;
					}
					else {
						Point topLeft = new Point();
						double minX = Math.min(firstPoint.getX(), e.getX());
						double minY = Math.min(firstPoint.getY(), e.getY());
						topLeft.setLocation(minX, minY);
						double width = Math.abs(firstPoint.getX() - e.getX());
						double height = Math.abs(firstPoint.getY() - e.getY());
						Shape rectangle = new Rectangle(topLeft, width, height);
						rectangle.setColor(Color.BLACK);
						rectangle.setFillColor(Color.WHITE);
						engine.addShape(rectangle);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						secondClick = false;
					}
				}
				// Square action
				else if (action == 5) {
					if (!secondClick) {
						firstPoint = e.getPoint();
						secondClick = true;
					}
					else {
						Point topLeft = new Point();
						double minX = Math.min(firstPoint.getX(), e.getX());
						double minY = Math.min(firstPoint.getY(), e.getY());
						topLeft.setLocation(minX, minY);
						// Diagonal * cos(45 degrees) = side of square
						double side = Point.distance(firstPoint.getX(), firstPoint.getY(), e.getX(), e.getY()) * Math.cos(Math.toRadians(45));
						Shape square = new Square(topLeft, side);
						square.setColor(Color.BLACK);
						square.setFillColor(Color.WHITE);
						engine.addShape(square);
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
						secondClick = false;
					}
				}
				
				
				// Resize action
				else if (action == 6) {
					if (contains(e.getPoint()) != null) {
						firstPoint = e.getPoint();
						resizeAttempt = true;
						moveAttempt = false;
					}
				}
                                 //clear all shapes
                                 else if (action == 7) {
                                      
				canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                                 DrawingEngine ll = new Logic();
                                   engine=ll;
                                   System.out.println("Clear All the Shape");
				}
                                 
				
			}
					
			@Override
			public void mouseReleased(MouseEvent e) {
				if (moveAttempt) {
					toMove = contains(firstPoint);
					if (toMove != null) {
						if (toMove.getClass().getSimpleName().equals("Triangle")) {
							double x1,y1,x2,y2,x3,y3;
							if (firstPoint.getX() <= e.getX()) {
								x1 = toMove.getProperties().get("x1") + ( e.getX() - firstPoint.getX() );
								x2 = toMove.getProperties().get("x2") + ( e.getX() - firstPoint.getX() );
								x3 = toMove.getProperties().get("x3") + ( e.getX() - firstPoint.getX() );
							}
							else {
								x1 = toMove.getProperties().get("x1") - ( firstPoint.getX() - e.getX() );
								x2 = toMove.getProperties().get("x2") - ( firstPoint.getX() - e.getX() );
								x3 = toMove.getProperties().get("x3") - ( firstPoint.getX() - e.getX() );
							}
							if (firstPoint.getY() <= e.getY()) {
								y1 = toMove.getProperties().get("y1") + ( e.getY() - firstPoint.getY() );
								y2 = toMove.getProperties().get("y2") + ( e.getY() - firstPoint.getY() );
								y3 = toMove.getProperties().get("y3") + ( e.getY() - firstPoint.getY() );
							}
							else {
								y1 = toMove.getProperties().get("y1") - ( firstPoint.getY() - e.getY() );
								y2 = toMove.getProperties().get("y2") - ( firstPoint.getY() - e.getY() );
								y3 = toMove.getProperties().get("y3") - ( firstPoint.getY() - e.getY() );
							}
							Point first = new Point();
							Point second = new Point();
							Point third = new Point();
							first.setLocation(x1,y1);
							second.setLocation(x2,y2);
							third.setLocation(x3,y3);
							Shape moved = new Triangle(first,second,third);
							moved.setColor(toMove.getColor());
							moved.setFillColor(toMove.getFillColor());
							engine.updateShape(toMove, moved);
						}
						else {
							double x1,y1;
							if (firstPoint.getX() <= e.getX()) {
								x1 = toMove.getPosition().getX() + ( e.getX() - firstPoint.getX() );
							}
							else {
								x1 = toMove.getPosition().getX() - ( firstPoint.getX() - e.getX() );
							}
							if (firstPoint.getY() <= e.getY()) {
								y1 = toMove.getPosition().getY() + ( e.getY() - firstPoint.getY() );
							}
							else {
								y1 = toMove.getPosition().getY() - ( firstPoint.getY() - e.getY() );
							}
							Point newPosition = new Point();
							newPosition.setLocation(x1,y1);
							try {
								Shape moved = (Shape) toMove.clone();
								moved.setPosition(newPosition);
								moved.setColor(toMove.getColor());
								moved.setFillColor(toMove.getFillColor());
								engine.updateShape(toMove, moved);
							} catch (CloneNotSupportedException e1) {
								e1.printStackTrace();
							}
						}
						toMove = null;
						canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
						engine.refresh(canvas.getGraphics());
					}
					moveAttempt = false;
				}
				else if (resizeAttempt) {
					Shape toResize = contains(firstPoint);
					if (toResize.getClass().getSimpleName().equals("Rectangle")) {
						double x = toResize.getPosition().getX(), y = toResize.getPosition().getY();
						// Four corners of the rectangle
						int whichCorner = 1;
						Point topLeft = new Point();
						topLeft.setLocation(x, y);
						Point topRight = new Point();
						topRight.setLocation(x + toResize.getProperties().get("width"), y);
						Point bottomLeft = new Point();
						bottomLeft.setLocation(x, y + toResize.getProperties().get("height"));
						Point bottomRight = new Point();
						bottomRight.setLocation(x + toResize.getProperties().get("width"), y + toResize.getProperties().get("height"));
						// Get the near corner to the mouse position & the far one
						Point nearCorner = new Point();
						Point farCorner = new Point();
						double min = Point.distance(topLeft.getX(), topLeft.getY(), e.getX(), e.getY());
						nearCorner.setLocation(topLeft);
						farCorner.setLocation(bottomRight);
						if (min > Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY())) {
							whichCorner = 2;
							min = Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(topRight);
							farCorner.setLocation(bottomLeft);
						}
						if (min > Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY())) {
							whichCorner = 3;
							min = Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomRight);
							farCorner.setLocation(topLeft);
						}
						if (min > Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY())) {
							whichCorner = 4;
							min = Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomLeft);
							farCorner.setLocation(topRight);
						}
						double newWidth = Math.abs(e.getX() - farCorner.getX());
						double newHeight = Math.abs(e.getY() - farCorner.getY());
						Point newTopLeft = new Point();
						if (whichCorner == 1) {
							newTopLeft.setLocation(e.getPoint());
						}
						else if (whichCorner == 2) {
							newTopLeft.setLocation(topLeft.getX(), e.getY());
						}
						else if (whichCorner == 3) {
							newTopLeft.setLocation(topLeft);
						}
						else if (whichCorner == 4) {
							newTopLeft.setLocation(e.getX(), e.getY() - newHeight);
						}
						nearCorner.setLocation(e.getX(), e.getY());
						Shape rectangle = new Rectangle(newTopLeft, newWidth, newHeight);
						rectangle.setColor(toResize.getColor());
						rectangle.setFillColor(toResize.getFillColor());
						engine.updateShape(toResize, rectangle);
					}
					else if (toResize.getClass().getSimpleName().equals("Square")) {
						double x = toResize.getPosition().getX(), y = toResize.getPosition().getY();
						double sideLength = toResize.getProperties().get("width");
						// Four corners of the square
						int whichCorner = 1;
						Point topLeft = new Point();
						topLeft.setLocation(x, y);
						Point topRight = new Point();
						topRight.setLocation(x + sideLength, y);
						Point bottomLeft = new Point();
						bottomLeft.setLocation(x, y + sideLength);
						Point bottomRight = new Point();
						bottomRight.setLocation(x + sideLength, y + sideLength);
						// Get the near corner to the mouse position
						Point nearCorner = new Point();
						Point farCorner = new Point();
						double min = Point.distance(topLeft.getX(), topLeft.getY(), e.getX(), e.getY());
						nearCorner.setLocation(topLeft);
						farCorner.setLocation(bottomRight);
						if (min > Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY())) {
							whichCorner = 2;
							min = Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(topRight);
							farCorner.setLocation(bottomLeft);
						}
						if (min > Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY())) {
							whichCorner = 3;
							min = Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomRight);
							farCorner.setLocation(topLeft);
						}
						if (min > Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY())) {
							whichCorner = 4;
							min = Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomLeft);
							farCorner.setLocation(topRight);
						}
						double newSideLength = Point.distance(farCorner.getX(), farCorner.getY(), e.getX(), e.getY()) * Math.cos(Math.toRadians(45));
						Point newTopLeft = new Point();
						if (whichCorner == 1) {
							newTopLeft.setLocation(e.getPoint());
						}
						else if (whichCorner == 2) {
							newTopLeft.setLocation(topLeft.getX(), e.getY());
						}
						else if (whichCorner == 3) {
							newTopLeft.setLocation(topLeft);
						}
						else if (whichCorner == 4) {
							newTopLeft.setLocation(e.getX(), e.getY() - newSideLength);
						}
						nearCorner.setLocation(e.getX(), e.getY());
						Shape square = new Square(newTopLeft, newSideLength);
						square.setColor(toResize.getColor());
						square.setFillColor(toResize.getFillColor());
						engine.updateShape(toResize, square);
					}
					else if (toResize.getClass().getSimpleName().equals("Circle")) {
						double radius = toResize.getProperties().get("width") / 2;
						Point center = new Point();
						center.setLocation(toResize.getPosition().getX() + radius, toResize.getPosition().getY() + radius);
						double newRadius = Point.distance(center.getX(), center.getY(), e.getX(), e.getY());
						Point newTopLeft = new Point();
						newTopLeft.setLocation(center.getX() - newRadius, center.getY() - newRadius);
						Shape newCircle = new Circle(newTopLeft, newRadius*2);
						newCircle.setColor(toResize.getColor());
						newCircle.setFillColor(toResize.getFillColor());
						engine.updateShape(toResize, newCircle);
					}
					else if (toResize.getClass().getSimpleName().equals("Ellipse")) {
						double x = toResize.getPosition().getX(), y = toResize.getPosition().getY();
						// Four corners of the ellipse
						int whichCorner = 1;
						Point topLeft = new Point();
						topLeft.setLocation(x, y);
						Point topRight = new Point();
						topRight.setLocation(x + toResize.getProperties().get("width"), y);
						Point bottomLeft = new Point();
						bottomLeft.setLocation(x, y + toResize.getProperties().get("height"));
						Point bottomRight = new Point();
						bottomRight.setLocation(x + toResize.getProperties().get("width"), y + toResize.getProperties().get("height"));
						// Get the near corner to the mouse position & the far one
						Point nearCorner = new Point();
						Point farCorner = new Point();
						double min = Point.distance(topLeft.getX(), topLeft.getY(), e.getX(), e.getY());
						nearCorner.setLocation(topLeft);
						farCorner.setLocation(bottomRight);
						if (min > Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY())) {
							whichCorner = 2;
							min = Point.distance(topRight.getX(), topRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(topRight);
							farCorner.setLocation(bottomLeft);
						}
						if (min > Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY())) {
							whichCorner = 3;
							min = Point.distance(bottomRight.getX(), bottomRight.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomRight);
							farCorner.setLocation(topLeft);
						}
						if (min > Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY())) {
							whichCorner = 4;
							min = Point.distance(bottomLeft.getX(), bottomLeft.getY(), e.getX(), e.getY());
							nearCorner.setLocation(bottomLeft);
							farCorner.setLocation(topRight);
						}
						double newWidth = Math.abs(e.getX() - farCorner.getX());
						double newHeight = Math.abs(e.getY() - farCorner.getY());
						Point newTopLeft = new Point();
						if (whichCorner == 1) {
							newTopLeft.setLocation(e.getPoint());
						}
						else if (whichCorner == 2) {
							newTopLeft.setLocation(topLeft.getX(), e.getY());
						}
						else if (whichCorner == 3) {
							newTopLeft.setLocation(topLeft);
						}
						else if (whichCorner == 4) {
							newTopLeft.setLocation(e.getX(), e.getY() - newHeight);
						}
						nearCorner.setLocation(e.getX(), e.getY());
						Shape ellipse = new Ellipse(newTopLeft, newWidth, newHeight);
						ellipse.setColor(toResize.getColor());
						ellipse.setFillColor(toResize.getFillColor());
						engine.updateShape(toResize, ellipse);
					}
                                        else if (toResize.getClass().getSimpleName().equals("Triangle")) {
						// Three vertices of triangle
						Point vertex_1 = new Point();
						vertex_1.setLocation(toResize.getProperties().get("x1"), toResize.getProperties().get("y1"));
						Point vertex_2 = new Point();
						vertex_2.setLocation(toResize.getProperties().get("x2"), toResize.getProperties().get("y2"));
						Point vertex_3 = new Point();
						vertex_3.setLocation(toResize.getProperties().get("x3"), toResize.getProperties().get("y3"));
						double length_1 = Point.distance(vertex_1.getX(), vertex_1.getY(), e.getX(), e.getY());
						double length_2 = Point.distance(vertex_2.getX(), vertex_2.getY(), e.getX(), e.getY());
						double length_3 = Point.distance(vertex_3.getX(), vertex_3.getY(), e.getX(), e.getY());
						Shape triangle;
						// Determining the nearest vertex to the mouse position
						if (length_1 < length_2 && length_1 < length_3) {
							triangle = new Triangle(e.getPoint(), vertex_2, vertex_3);
						}
						else if (length_2 < length_1 && length_2 < length_3) {
							triangle = new Triangle(e.getPoint(), vertex_1, vertex_3);
						}
						else {
							triangle = new Triangle(e.getPoint(), vertex_1, vertex_2);
						}
						triangle.setColor(toResize.getColor());
						triangle.setFillColor(toResize.getFillColor());
						engine.updateShape(toResize, triangle);
					}
					canvas.getGraphics().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
					resizeAttempt = false;
					engine.refresh(canvas.getGraphics());
				}
			}
		});
		
		
		
		JButton btnCircle = new JButton("Circle");
		btnCircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondClick = false;
				action = 1;
			}
		});
		btnCircle.setBounds(10, 10, 112, 23);
		frame.getContentPane().add(btnCircle);
		
		JButton btnEllipse = new JButton("Ellipse");
		btnEllipse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondClick = false;
				thirdClick = false;
				action = 2;
			}
		});
		btnEllipse.setBounds(100, 10, 112, 23);
		frame.getContentPane().add(btnEllipse);
		
		JButton btnTriangle = new JButton("Triangle");
		btnTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondClick = false;
				thirdClick = false;
				action = 3;
			}
		});
		btnTriangle.setBounds(200, 10, 112, 23);
		frame.getContentPane().add(btnTriangle);
		
		JButton btnRect = new JButton("Rectangle");
		btnRect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondClick = false;
				action = 4;
			}
		});
		btnRect.setBounds(300, 10, 112, 23);
		frame.getContentPane().add(btnRect);
		
		JButton btnSqre = new JButton("Square");
		btnSqre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondClick = false;
				action = 5;
			}
		});
		btnSqre.setBounds(400, 10, 112, 23);
		frame.getContentPane().add(btnSqre);
		
		// Action buttons
		JButton btnRmv = new JButton("Clear");
		btnRmv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = 7;
			}
		});
		btnRmv.setBounds(600, 10, 112, 23);
		frame.getContentPane().add(btnRmv);
		
	
		JButton btnResize = new JButton("Resize");
		btnResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				action = 6;
			}
		});
		btnResize.setBounds(500, 10, 112, 23);
		frame.getContentPane().add(btnResize);
                frame.getContentPane().add(canvas);
		
	}
}
