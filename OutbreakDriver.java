import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class OutbreakDriver extends JPanel implements ActionListener
{
	
	private Timer timer;
    private ArrayList<Person> people;
    private ArrayList<Double> dX_Array;
    private ArrayList<Double> dY_Array;
    private ArrayList<Integer> count;
    private ArrayList<Integer> infectedCounts;
    private ArrayList<Integer> recoveredCounts;
    private ArrayList<Integer> healthyCounts;
    private ArrayList<Integer> timeCounts;
    private ArrayList<Integer> timeCountsRecovered;
    private ArrayList<Integer> timeCountsHealthy;
    private int time;
    private int actualTime;
    private int infectedCount;
    private int recoveredCount;
    private int graph_y;
    private JLabel label;
    private JLabel label2;
    private Color fillColor;
    private Random random;
    private int Population;
	
	public OutbreakDriver(int givenPopulation)
	{
		Population = givenPopulation;
		people = new ArrayList<Person>();
		dX_Array = new ArrayList<Double>();
	    dY_Array = new ArrayList<Double>();
	    count = new ArrayList<Integer>();
	    infectedCounts = new ArrayList<Integer>();
	    recoveredCounts = new ArrayList<Integer>();
	    healthyCounts = new ArrayList<Integer>();
	    timeCounts = new ArrayList<Integer>();
	    timeCountsRecovered = new ArrayList<Integer>();
	    timeCountsHealthy = new ArrayList<Integer>();
	    graph_y = ((Population/2)+50);
	    infectedCounts.add(graph_y - 1);
	    recoveredCounts.add(graph_y);
	    healthyCounts.add(graph_y - (Population/3));
	    timeCounts.add(200);
	    timeCountsRecovered.add(200);
	    timeCountsHealthy.add(200);
	    time = 0;
	    actualTime = 0;
	    infectedCount = 1;
	    recoveredCount = 0;
	    label = new JLabel();
	    label.setBounds(30, 30, 150, 150);
	    this.add(label);
	    label2 = new JLabel();
	    label2.setBounds(250, 0, 200, 30);
	    this.add(label2);
	    fillColor = Color.BLUE;
		random = new Random();
		
		this.createTown(Population);
		
		this.setFocusable(true);
	    this.requestFocus();
	    timer = new Timer(1000/60, this);
	    timer.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		time++;
		
		if (time >= 3)
		{
			actualTime = actualTime + 1;
			time = 0;
		}
		
		for (int i = 0; i < people.size(); i++)
		{
			double cent_x = people.get(i).getCenterX();
			double cent_y = people.get(i).getCenterY();
			if (cent_x > 1250 || cent_x < 50)
			{
				dX_Array.set(i, -(dX_Array.get(i)));
			}
			if (cent_y > 650 || cent_y < 50)
			{
				dY_Array.set(i, -(dY_Array.get(i)));
			}
			
			double dx = dX_Array.get(i);
			double dy = dY_Array.get(i);
			people.get(i).move(dx, dy);
			
			if (people.get(i).isInfected())
			{
				count.set(i, count.get(i)+1);
			}
			
			if (count.get(i) == 559 && people.get(i).isInfected())
			{
				people.get(i).setFillColor(Color.MAGENTA);
				people.get(i).setRecovered();
				recoveredCount = recoveredCount + 1;
				timeCountsRecovered.add(200+actualTime);
				recoveredCounts.add(graph_y - (recoveredCount/3));
				if (infectedCount != 0)
				{
					infectedCount = infectedCount - 1;
					infectedCounts.add(graph_y-(infectedCount/3));
					timeCounts.add(200+actualTime);
				}
			}
			
			int healthy = (people.size() - infectedCount - recoveredCount);
			if (infectedCount != 0)
			{
				healthyCounts.add(graph_y - (healthy/3));
				timeCountsHealthy.add(200+actualTime);
			}
			label.setText(String.format("<html> <p><b><FONT COLOR=BLACK> Count </FONT></b></p> <br> <p> %-9s <FONT COLOR=PURPLE> %5d </FONT></p> <br> <p> %-9s <FONT COLOR=BLUE> %5d </FONT></p> <br> <p> %-9s <FONT COLOR=RED> %5d </FONT></p> </html>", "Recovered", recoveredCount, "Healthy", healthy, "Sick", infectedCount));
			
			for (int j = i+1; j < people.size(); j++)
			{
				double xDist = ((people.get(i).getCenterX()) - (people.get(j).getCenterX()));
				double yDist = ((people.get(i).getCenterY()) - (people.get(j).getCenterY()));
				double distBetweenPeople = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
				int idealDist = 20;
				
				if (distBetweenPeople - idealDist < 0)
				{
					doCollision(people.get(i), people.get(j), dX_Array.get(i), dY_Array.get(i), dX_Array.get(j), dY_Array.get(j), i, j);
					if (people.get(i).isInfected() && !people.get(j).isRecovered() && !people.get(j).isInfected())
					{
						people.get(j).setFillColor(Color.RED);
						people.get(j).setInfected();
						infectedCount = infectedCount + 1;
						infectedCounts.add(graph_y-(infectedCount/3));
						timeCounts.add(200+actualTime);
					}
					else if (people.get(j).isInfected() && !people.get(i).isRecovered() && !people.get(i).isInfected())
					{
						people.get(i).setFillColor(Color.RED);
						people.get(i).setInfected();
						infectedCount = infectedCount + 1;
						infectedCounts.add(graph_y-(infectedCount/3));
						timeCounts.add(200+actualTime);
					}
				}
			}
		}
		
		
		this.repaint();
	}
	
	@Override
    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        setOpaque(true);
        setBackground(Color.WHITE);
        label.setBounds(10, 0, 150, 150);
        this.add(label);
        label2.setText("<html> <p><b> Change over time </b></p> </html>");
        label2.setBounds(220, 20, 200, 30);
	    this.add(label2);
	    
	    g.setColor(Color.BLACK);
	    g.drawLine(200, (graph_y+3), 1270, (graph_y+3));
	    g.drawLine(200, (graph_y+3), 200, 15);
	    g.drawLine(200, 15, 1270, 15);
	    g.drawLine(1270, (graph_y+3), 1270, 15);
	    
	    for (int l = 1; l < healthyCounts.size() && l < timeCountsHealthy.size(); l++)
        {
        	g.setColor(Color.BLUE);
        	g.fillRect(timeCountsHealthy.get(l-1), healthyCounts.get(l-1), (timeCountsHealthy.get(l) - timeCountsHealthy.get(l-1)), ((graph_y+3) - healthyCounts.get(l-1)));
        	//g.drawLine(timeCountsHealthy.get(l-1), healthyCounts.get(l-1), timeCountsHealthy.get(l), healthyCounts.get(l));
        }
	    
	    for (int k = 1; k < recoveredCounts.size() && k < timeCountsRecovered.size(); k++)
        {
        	g.setColor(Color.MAGENTA);
        	g.fillRect(timeCountsRecovered.get(k-1), recoveredCounts.get(k-1), (timeCountsRecovered.get(k) - timeCountsRecovered.get(k-1)), ((graph_y+3) - recoveredCounts.get(k-1)));
        	//g.drawLine(timeCountsRecovered.get(k-1), recoveredCounts.get(k-1), timeCountsRecovered.get(k), recoveredCounts.get(k));
        }
        
        for (int j = 1; j < infectedCounts.size() && j < timeCounts.size(); j++)
        {
        	g.setColor(Color.RED);
        	g.fillRect(timeCounts.get(j-1), infectedCounts.get(j-1), (timeCounts.get(j) - timeCounts.get(j-1)), ((graph_y+3) - infectedCounts.get(j-1)));
        	//g.drawLine(timeCounts.get(j-1), infectedCounts.get(j-1), timeCounts.get(j), infectedCounts.get(j));
        }
        
        for (int i = 0; i < people.size(); i++)
        {
        	people.get(i).draw(g);
        }
    }
	
	public void createTown(int Population)
	{
		int i = Population;
		while(i > 0)
		{
			int x = random.nextInt(1170)+50;
    		int y = random.nextInt(550)+50;
    		int centerX = x + 10;
    		int centerY = y + 10;
					
			if (i != Population)
			{
				for (int k = 0; k < people.size(); k++)
				{
					centerX = x + 10;
		    		centerY = y + 10;
					double xDist = (centerX - (people.get(k).getCenterX()));
					double yDist = (centerY - (people.get(k).getCenterY()));
					double distBetweenPeople = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
					int idealDist = 20;
					
					if(distBetweenPeople - idealDist < 0)
					{
						x = random.nextInt(1170)+50;
			    		y = random.nextInt(550)+50;
			    		
			    		k = -1;
					}
				}
			}
			
			Person person = new Person(fillColor, x, y);
			people.add(person);
						
			int randomNumber = random.nextInt(2);
			double dX = 0;
			double dY = 0;
			
			if (randomNumber == 0)
			{
				dX = -1;
			}
			if (randomNumber == 1)
			{
				dX = 1;
			}
			
			randomNumber = random.nextInt(2);
			
			if (randomNumber == 0)
			{
				dY = 1;
			}
			if (randomNumber == 1)
			{
				dY = -1;
			}
			
			dX_Array.add(dX);
			dY_Array.add(dY);
			count.add(0);
			
			i--;
		}
		
		int randomNumber = random.nextInt(Population);
		Person infected = new Person(Color.RED, people.get(randomNumber).getX(), people.get(randomNumber).getY());
		infected.setInfected();
		people.set(randomNumber, infected);
	}
	
	public void doCollision(Person one, Person two, double dx_one, double dy_one, double dx_two, double dy_two, int i, int j)
	{
		double xVelocityDiff = dx_one - dx_two;
		double yVelocityDiff = dy_one - dy_two;
		
		double xDist = two.getCenterX() - one.getCenterX();
		double yDist = two.getCenterY() - one.getCenterY();
		
		if ((xVelocityDiff * xDist) + (yVelocityDiff * yDist) >= 0)
		{
			double angle = -Math.atan2(two.getCenterY() - one.getCenterY(), two.getCenterX() - one.getCenterX());
			int m_one = 1;
			int m_two = 1;
			
			double u_one_x = (dx_one * Math.cos(angle)) - (dy_one * Math.sin(angle));
			double u_one_y = (dx_one * Math.sin(angle)) + (dy_one * Math.cos(angle));
			double u_two_x = (dx_two * Math.cos(angle)) - (dy_two * Math.sin(angle));
			double u_two_y = (dx_two * Math.sin(angle)) + (dy_two * Math.cos(angle));
			
			double v_x_one_temp = ((u_one_x * (m_one - m_two))/(m_one + m_two)) + ((u_two_x * 2 * m_two)/(m_one + m_two));
			double v_x_two_temp = ((u_two_x * (m_one - m_two))/(m_one + m_two)) + ((u_one_x * 2 * m_two)/(m_one + m_two));
			double v_y_one_temp = u_one_y;
			double v_y_two_temp = u_two_y;
			
			double v_x_one_final = (v_x_one_temp * Math.cos(-angle)) - (v_y_one_temp * Math.sin(-angle));
			double v_x_two_final = (v_x_two_temp * Math.cos(-angle)) - (v_y_two_temp * Math.sin(-angle));
			double v_y_one_final = (v_x_one_temp * Math.sin(-angle)) + (v_y_one_temp * Math.cos(-angle));
			double v_y_two_final = (v_x_two_temp * Math.sin(-angle)) + (v_y_two_temp * Math.cos(-angle));
			
			dX_Array.set(i, v_x_one_final);
			dY_Array.set(i, v_y_one_final);
			dX_Array.set(j, v_x_two_final);
			dY_Array.set(j, v_y_two_final);
		}
	}

}
