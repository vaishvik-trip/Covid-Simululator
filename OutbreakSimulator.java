import javax.swing.*;
import java.util.*;
import javax.swing.JPanel;

public class OutbreakSimulator extends JFrame
{
	
	private JPanel outbreak;
	private static int population = 0; 
	
	public OutbreakSimulator(int givenPopulation)
	{
		population = givenPopulation;
		outbreak = new OutbreakDriver(population);
		this.getContentPane().add(outbreak);
	}

	public static void main(String[] args) 
	{
		try (Scanner in = new Scanner(System.in);) {
			boolean done = false;
			while (!done) {
				System.out.println("\nThe population of the town should be between 100 and 700.\n");
				System.out.print("Please enter the population of the town: ");
				if (in.hasNextInt()) {
					population = Math.abs(in.nextInt());
					if (population >= 100 && population <= 700) {
						done = true;
					} else {
						System.out.println("\nThe population should be between 100 and 700. Try again.\n");
					}
				} else {
					System.out.println("Please enter a valid integer as the population.");
					System.out.println();
				}
			}

			OutbreakSimulator frame = new OutbreakSimulator(population);
			frame.setSize(1300, 700);
			frame.setTitle("Outbreak Simulation");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("\nSomething went wrong!\n");
		}
	}

}
