import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;

public class Person 
{
	private Color fillColor;
	private final int RADIUS = 10;
	private double my_x;
	private double my_y;
	private double centerX;
	private double centerY;
	private boolean infected = false;
	private boolean recovered = false;
	
	public Person(Color givenFillColor, double x, double y)
	{
		fillColor = givenFillColor;
		my_x = x;
		my_y = y;
		centerX = (my_x + RADIUS);
		centerY = (my_y + RADIUS);
	}
	
	public Person(double x, double y)
	{
		my_x = x;
		my_y = y;
	}
	
	public double getCenterX() 
	{
		return centerX;
	}

	public double getCenterY() 
	{
		return centerY;
	}
	
	public void setFillColor(Color c) 
	{ 
		fillColor = c;
	}    
	
	public Color getFillColor() 
	{ 
		return fillColor;
	}    
	
	public double getX() 
	{ 
		return this.my_x;
	}     
	
	public void setX(double x) 
	{ 
		this.my_x = x;
	}    
	
	public double getY() 
	{ 
		return this.my_y;
	}     
	
	public void setY(double y) 
	{ 
		this.my_y = y;
	}
	
	public void move(double dx, double dy)
	{
		my_x = my_x + dx;
		my_y = my_y + dy;
		
		centerX = (my_x + RADIUS);
		centerY = (my_y + RADIUS);
	}
	
	public boolean isInfected()
	{
		return this.infected;
	}
	
	public void setInfected()
	{
		infected = true;
	}
	
	public boolean isRecovered()
	{
		return this.recovered;
	}
	
	public void setRecovered()
	{
		recovered = true;
		infected = false;
	}
	
	public void draw(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D circle = new Ellipse2D.Double(this.my_x, this.my_y, this.RADIUS, this.RADIUS);
		g2.setPaint(this.fillColor);
	    g2.fill(circle);
	}
	
}
