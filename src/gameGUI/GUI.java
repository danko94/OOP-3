package gameGUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.DGraph.Edge;
import gameData.Fruit;
import gameData.Robot;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

public class GUI extends JFrame{

	private DGraph dgraph;

	private game_service game;

	private final int X_RANGE = 600;
	private final int Y_RANGE = 600;

	private final int OFFSET = 50;

	private BufferedImage banana, apple, robot;

	private int numOfRobots;

	private static int robotCount;

	private JButton move;
	private JButton button2;

	private JPanel pane;
	
	private JPanel root;
	
	private JTextArea points;

	Thread moveGame;

	boolean painted;


	public GUI(int gameID) {
		root = new JPanel();
		setSize(X_RANGE,Y_RANGE);
		setTitle("Game GUI");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		

		robotCount = 0;
		painted = false;

		moveGame = new Thread() {
			public void run() {
				long start = System.currentTimeMillis();
				while(true) {
					if(System.currentTimeMillis()-start>100) {
						try {
							game.move();
						} catch (Exception e) {
							System.out.println("exc");
						}
						start = System.currentTimeMillis();
						repaint();
					}				
				}
			}
		};


		pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.LINE_AXIS));

		move = new JButton("move");
		button2 = new JButton("button2");

		pane.add(move);
		pane.add(Box.createHorizontalGlue());
		pane.add(button2);

		this.add(pane, BorderLayout.SOUTH);
		game = Game_Server.getServer(gameID);

		dgraph = new DGraph();
		dgraph.init(game.getGraph());

		String gameString = game.toString();

		numOfRobots = -1;

		List<String> fruits = game.getFruits();

		for(String fruit : fruits) {
			Fruit f = new Fruit(fruit);
			dgraph.fruity(f);
		}

		try {
			JSONObject obj = new JSONObject(gameString.substring("{\"GameServer\":".length(),gameString.length()-1));
			numOfRobots = obj.getInt("robots");
		} catch (JSONException e) {
			e.printStackTrace();
		}


		try {

			File file = new File("C:\\Users\\Daniel Korotine\\eclipse-workspace\\Ex3_v2\\data\\banana.png");
			banana = ImageIO.read(file);
			File file2 = new File("C:\\Users\\Daniel Korotine\\eclipse-workspace\\Ex3_v2\\data\\robot.png");
			robot = ImageIO.read(file2);

		} catch (Exception e) {
			e.printStackTrace();
		}


		//////////Button action listeners///////////////

		move.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String input = JOptionPane.showInputDialog("Enter robot ID/node");
				int ID = -1;
				boolean flag = true;

				try {
					ID = Integer.parseInt(input);
					if(ID<0||ID>=numOfRobots) {
						throw new RuntimeException();
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(rootPane, "invalid input", "error", JOptionPane.WARNING_MESSAGE);
					flag=false;
				}
				if(flag) {
					String input2 = JOptionPane.showInputDialog("Enter destination Node");
					int dest=-1;

					try {
						dest = Integer.parseInt(input2);
						if(dest<0||dest>dgraph.nodeSize())
							throw new RuntimeException();
						game.chooseNextEdge(ID, dest);
						System.out.println("check");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(rootPane, "invalid input", "error", JOptionPane.WARNING_MESSAGE);
					}
				}

			}
		});


	}
	
	

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2 = (Graphics2D)g;

		//for scaling purposes
		Collection<node_data> nodes = dgraph.getV();

		double minX = Scaling.getMinX(nodes);
		double maxX = Scaling.getMaxX(nodes);
		double minY = Scaling.getMinY(nodes);
		double maxY = Scaling.getMaxY(nodes);


		if(!painted) {

			

			///////paint edges/////////////

			for(node_data node : nodes) {

				Collection<edge_data> edges = dgraph.getE(node.getKey());
				for(edge_data edge : edges ) {

					Point3D p = node.getLocation();
					node_data dest = dgraph.getNode(edge.getDest());
					Point3D pDest = dest.getLocation();
					g.setColor(Color.cyan);


					double scaledX = Scaling.scale(p.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
					double scaledY = Scaling.scale(p.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);

					double scaledXDest = Scaling.scale(pDest.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
					double scaledYDest = Scaling.scale(pDest.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);

					g2.setStroke(new BasicStroke(2));
					g2.drawLine((int)scaledX+5, (int)scaledY+5, (int)scaledXDest+5, (int)scaledYDest+5);



					/////////print weight on edges//////////////

					g.setColor(Color.DARK_GRAY);



					double weight = edge.getWeight();

					weight = Math.round(weight * 100.0) / 100.0;			//show only 2 decimal places

					double midX = ((scaledX+scaledXDest)/2);
					double midY = ((scaledY+scaledYDest)/2);

					double quarterX = ((midX+scaledXDest)/2);
					double quarterY = ((midY+scaledYDest)/2);



					//g.drawString(""+weight, (int)quarterX+7, (int)quarterY+7);			//put weights on edges
					
					

					if(((Edge)edge).isFruity()){
						double scaledXFruit = Scaling.scale(((Edge)edge).getFruit().getPos().x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
						double scaledYFruit = Scaling.scale(((Edge)edge).getFruit().getPos().y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);
						//g.drawImage(banana, (int)scaledXFruit,(int)scaledYFruit, this);
					}




				}
			}

			///////paint nodes///////

			for(node_data node : nodes) {				



				Point3D p = node.getLocation();


				g.setColor(Color.red);
				double scaledX = Scaling.scale(p.x(),minX,maxX,0+OFFSET,(double)X_RANGE-OFFSET);
				double scaledY = Scaling.scale(p.y(),minY,maxY,0+OFFSET,(double)Y_RANGE-OFFSET);
				g.fillOval((int)scaledX, (int)scaledY, 10, 10);
				g.drawString(""+node.getKey(), (int)scaledX, (int)scaledY-5);

			}
			
		
			
		}
		
		List<String> fruits = game.getFruits();


		for(String f : fruits) {
			Fruit fr = new Fruit(f);
			dgraph.fruity(fr);
			Point3D fruitLocation= fr.getPos();
			double fruitX = fruitLocation.x();
			double fruitY = fruitLocation.y();
			fruitX = Scaling.scale(fruitX, minX, maxX, OFFSET, X_RANGE-OFFSET);
			fruitY = Scaling.scale(fruitY, minY, maxY, OFFSET, Y_RANGE-OFFSET);
			g.drawImage(banana, (int)fruitX,(int)fruitY, this);

			
		}
		List<String> robots = game.getRobots();
		System.out.println(robots.size());


		for(String r : robots) {
			//System.out.println("hi");
			System.out.println(r);
			Robot rob = new Robot(r);
			Point3D robotLocation= rob.getPos();
			double robotX = robotLocation.x();
			double robotY = robotLocation.y();
			robotX = Scaling.scale(robotX, minX, maxX, OFFSET, X_RANGE-OFFSET);
			robotY = Scaling.scale(robotY, minY, maxY, OFFSET, Y_RANGE-OFFSET);
			g.drawImage(robot, (int)robotX-30,(int)robotY-20, this);
		}

		////////add robots for manual control//////

		for(;robotCount<numOfRobots;robotCount++) {
			String robotLocation = JOptionPane.showInputDialog("Enter robot location: ");
			int src;
			try {
				src = Integer.parseInt(robotLocation);

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "error, try again");
				robotCount--;
				continue;
			}

			Point3D nodeLocation = dgraph.getNode(src).getLocation();

			double nodeX = nodeLocation.x();
			double nodeY = nodeLocation.y();

			nodeX = Scaling.scale(nodeX, minX, maxX, OFFSET, X_RANGE-OFFSET);
			nodeY = Scaling.scale(nodeY, minY, maxY, OFFSET, Y_RANGE-OFFSET);

			g.drawImage(robot, (int)nodeX-30,(int)nodeY-20, this);

			game.addRobot(src);



		}
		if(robotCount==numOfRobots&&!moveGame.isAlive()) {
			game.startGame();
			moveGame.start();
		}


	}











	/////////////Private methods////////////////7


}
