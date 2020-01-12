package gameGUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import dataStructure.DGraph;
import dataStructure.DGraph.Edge;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

public class GUI extends JFrame{

	DGraph dgraph;

	private final int X_RANGE = 600;
	private final int Y_RANGE = 600;

	private final int OFFSET = 50;
	
	BufferedImage image;


	public GUI(DGraph dgraph) {
		setSize(X_RANGE,Y_RANGE);
		setTitle("Game GUI");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		this.dgraph = dgraph;


		try {
			
			File file = new File("C:\\Users\\Daniel Korotine\\eclipse-workspace\\Ex3_v2\\data\\banana.png");
			image = ImageIO.read(file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D)g;

		Collection<node_data> nodes = dgraph.getV();

		double minX = getMinX(nodes);
		double maxX = getMaxX(nodes);
		double minY = getMinY(nodes);
		double maxY = getMaxY(nodes);

		for(node_data node : nodes) {

			Collection<edge_data> edges = dgraph.getE(node.getKey());
			for(edge_data edge : edges ) {

				Point3D p = node.getLocation();
				node_data dest = dgraph.getNode(edge.getDest());
				Point3D pDest = dest.getLocation();
				g.setColor(Color.cyan);


				double scaledX = scale(p.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
				double scaledY = scale(p.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);

				double scaledXDest = scale(pDest.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
				double scaledYDest = scale(pDest.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);

				g2.setStroke(new BasicStroke(2));
				g2.drawLine((int)scaledX+5, (int)scaledY+5, (int)scaledXDest+5, (int)scaledYDest+5);

				g.setColor(Color.DARK_GRAY);



				double weight = edge.getWeight();

				weight = Math.round(weight * 100.0) / 100.0;			//show only 2 decimal places

				double midX = ((scaledX+scaledXDest)/2);
				double midY = ((scaledY+scaledYDest)/2);
				
				

				//g.drawString(""+weight, (int)midX+7, (int)midY+7);
				
				
				if(((Edge)edge).isFruity()){
					double scaledXFruit = scale(((Edge)edge).getFruit().getPos().x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
					double scaledYFruit = scale(((Edge)edge).getFruit().getPos().y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);
					System.out.println(scaledXFruit + " " + scaledYFruit );
					g.drawImage(image, (int)scaledXFruit,(int)scaledYFruit, this);
				}


			}
		}



		for(node_data node : nodes) {				//paint nodes on top of edges



			Point3D p = node.getLocation();


			g.setColor(Color.red);
			double scaledX = scale(p.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
			double scaledY = scale(p.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);
			g.fillOval((int)scaledX, (int)scaledY, 10, 10);
			g.drawString(""+node.getKey(), (int)scaledX, (int)scaledY-5);

		}
		
		
		
		






	}
	/**
	 * 
	 * @param data denote some data to be scaled
	 * @param r_min the minimum of the range of your data
	 * @param r_max the maximum of the range of your data
	 * @param t_min the minimum of the range of your desired target scaling
	 * @param t_max the maximum of the range of your desired target scaling
	 * @return
	 */
	private double scale(double data, double r_min, double r_max, 
			double t_min, double t_max)
	{

		double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
		return res;
	}

	private double getMinX(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().x();
			if(temp<min)
				min=temp;
		}
		return min;
	}
	private double getMinY(Collection<node_data> nodes) {
		double min = Double.MAX_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().y();
			if(temp<min)
				min=temp;
		}
		return min;
	}
	private double getMaxX(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().x();
			if(temp>max)
				max=temp;
		}
		return max;
	}
	private double getMaxY(Collection<node_data> nodes) {
		double max = Double.MIN_VALUE;

		for(node_data node : nodes) {
			double temp = node.getLocation().y();
			if(temp>max)
				max=temp;
		}
		return max;
	}

}
