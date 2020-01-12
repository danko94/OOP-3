package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import algorithms.Graph_Algo;
import dataStructure.*;
import dataStructure.DGraph.Node;
import utils.Point3D;

public class DrawingWindow extends JFrame implements MouseListener {

	JPanel mainPanel;

	Graph_Algo graph;

	JMenu menuFile;
	JMenu menuAlgo;
	JMenuBar menubar;

	JButton save;
	JButton shortestPath;
	JButton shortestPathDist;
	JButton tsp;
	JButton isConnected;

	JButton removeNode;
	JButton removeEdge;
	JButton addEdge;

	JButton exit;

	JMenuItem load;


	private final int XDIMENSION = 150;
	private final int YDIMENSION = 80;
	private final int HEIGHT = 750;
	private final int WIDTH = 750;



	public DrawingWindow(Graph_Algo graph) {
		this.graph = graph;

		

		menubar = new JMenuBar();
		menuFile = new JMenu("file");

		load = new JMenuItem("load graph from file");

		menuFile.add(load);

		mainPanel = new JPanel();
		setContentPane(mainPanel);

		mainPanel.setLayout(new BorderLayout(4,4));

		GridBagLayout gridLayout = new GridBagLayout();
		JPanel graphs = new JPanel(gridLayout);
		graphs.setBorder(new TitledBorder("Graph"));
		mainPanel.add(graphs);

		setSize(HEIGHT,WIDTH);
		setLocationRelativeTo(null);
		setTitle("Draw Graph");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		graphs.addMouseListener(this);
		menubar.add(menuFile);

		graphs.setBackground(Color.LIGHT_GRAY);







		//BUTTONS init

		shortestPathDist = new JButton("shortest path distance");
		tsp = new JButton("TSP");
		save = new JButton("save");
		shortestPath = new JButton("shortest path");
		isConnected = new JButton("strongly connected");

		removeEdge = new JButton("remove Edge");
		removeNode = new JButton("remove Node");
		addEdge = new JButton("add Edge");

		exit = new JButton("QUIT");

		//MENU LISTENERS

		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean flag=true;
				graph dgraph = graph.getGraph();


				int save = JOptionPane.showConfirmDialog(mainPanel, "Do you want to save your graph?", "Unsaved data", JOptionPane.YES_NO_CANCEL_OPTION);
				if(save == JOptionPane.YES_OPTION) {					
					String graphFilename = "";

					graphFilename = JOptionPane.showInputDialog("enter filename");

					try {
						graph.save(graphFilename);

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(mainPanel, "Error occured", "ERROR",JOptionPane.ERROR_MESSAGE);
					}					
				}
				else if(save == JOptionPane.CANCEL_OPTION) {
					flag = false;
				}

				if(flag) {
					String graphFilename = JOptionPane.showInputDialog("enter filename");


					try {
						Graph_Algo graphLoad = new Graph_Algo();
						graphLoad.init(graphFilename);
						dgraph = graphLoad.copy();

						repaint();



					} catch (Exception e2) {
						JOptionPane.showMessageDialog(mainPanel, "File not found", "ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		//BUTTONS LISTENERS

		save.addActionListener(new ActionListener() {   			//save file


			@Override
			public void actionPerformed(ActionEvent e) {
				String graphFilename;
				boolean flag = true;

				graphFilename = JOptionPane.showInputDialog("enter filename");

				if(graphFilename==null||graphFilename.contentEquals("")) { //if input was canceled or is empty
					flag = false;					
				}

				if(flag) {
					try {
						graph.save(graphFilename);

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(mainPanel, "Error occured", "ERROR",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		shortestPathDist.addActionListener(new ActionListener() {   //compute shortest dist



			@Override
			public void actionPerformed(ActionEvent e) {

				int src;
				int dest;
				graph dgraph = graph.getGraph();
				String s = JOptionPane.showInputDialog(mainPanel, "Enter source vertex ID");
				String d = JOptionPane.showInputDialog(mainPanel, "Enter destination vertex ID");
				node_data source = null;
				node_data destination = null;

				double dist;

				try {    //check if input is integer and that node with given id exist
					src = Integer.parseInt(s);
					source = dgraph.getNode(src);
					dest = Integer.parseInt(d);
					destination = dgraph.getNode(dest);
					if(destination==null||source==null)
						throw new RuntimeException();

					dist = graph.shortestPathDist(src, dest);

					JOptionPane.showMessageDialog(mainPanel, "Shortest distance between " + src + " and " + dest + " is: " + dist);

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(mainPanel, "given input is not valid", "not valid input",JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		shortestPath.addActionListener(new ActionListener() {



			@Override
			public void actionPerformed(ActionEvent e) {			

				int src=-1;
				int dest=-1;
				graph dgraph = graph.getGraph();

				String s = JOptionPane.showInputDialog(mainPanel, "Enter source vertex ID");
				String d = JOptionPane.showInputDialog(mainPanel, "Enter destination vertex ID");
				node_data source = null;
				node_data destination = null;

				double dist;
				boolean flag = true;

				try {    //check if input is integer and that node with given id exist
					src = Integer.parseInt(s);
					source = dgraph.getNode(src);
					dest = Integer.parseInt(d);
					destination = dgraph.getNode(dest);
					if(destination==null||source==null)
						throw new RuntimeException();
				} catch (Exception e2) {
					flag = false;
					JOptionPane.showMessageDialog(mainPanel, "given input is not valid", "not valid input",JOptionPane.WARNING_MESSAGE);
				}

				if(flag) {
					LinkedList<node_data> path = (LinkedList)graph.shortestPath(src, dest);

					if(path!=null) {
						Iterator<node_data> iter = path.iterator();

						node_data start = iter.next();

						while(iter.hasNext()) {
							//start.setInfo("on the path");
							node_data finish = iter.next();
							edge_data toTag = dgraph.getEdge(start.getKey(), finish.getKey());
							toTag.setInfo("on the path");
							start = finish;
						}

						repaint();

					}
					else {
						JOptionPane.showMessageDialog(mainPanel, "no path found", "no path",JOptionPane.WARNING_MESSAGE);
					}
				}

			}
		});



		tsp.addActionListener(new ActionListener() {					//Does WORK


			@Override
			public void actionPerformed(ActionEvent e) {
				int amount = 0;
				LinkedList<Integer> mustPass = new LinkedList<Integer>();
				boolean flag = true;
				graph dgraph = graph.getGraph();
				String input = "";
				input = JOptionPane.showInputDialog("enter amount of vertices to pass through");
				try {
					amount = Integer.parseInt(input);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(mainPanel, "invalid input", "error",JOptionPane.WARNING_MESSAGE);
					flag = false;
				}
				if(flag) {
					for(int i=0; i<amount;i++) {
						String inputNodeID = JOptionPane.showInputDialog("Enter ID of Node no: " + i);
						int toInsert = -1;
						try {
							toInsert = Integer.parseInt(inputNodeID);
							node_data n =dgraph.getNode(toInsert);
							if(n==null) {
								throw new RuntimeException();
							}

						} catch (Exception e2) {
							JOptionPane.showMessageDialog(mainPanel, "invalid input", "error",JOptionPane.WARNING_MESSAGE);
							flag=false;
							break;
						}
						mustPass.add(toInsert);
					}
				}
				if(flag) {
					LinkedList<node_data> path = (LinkedList)graph.TSP(mustPass);
					if(path==null) {
						JOptionPane.showMessageDialog(mainPanel, "no path found", "no path",JOptionPane.WARNING_MESSAGE);
					}
					else {
						Iterator<node_data> iter = path.iterator();

						node_data start = iter.next();

						while(iter.hasNext()) {
							//start.setInfo("on the path");
							node_data finish = iter.next();
							edge_data toTag = dgraph.getEdge(start.getKey(), finish.getKey());
							toTag.setInfo("on the path");
							start = finish;
						}
						repaint();
					}
				}

			}

		});

		exit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

		addEdge.addActionListener(new ActionListener() { 			//stop request



			@Override
			public void actionPerformed(ActionEvent e) {


				int src=-1, dest=-1;
				boolean flag = true;
				double weight=-1;

				graph dgraph = graph.getGraph();
				String srcString = JOptionPane.showInputDialog("Enter source node ID");
				String destString = JOptionPane.showInputDialog("Enter destination node ID");
				String weightString =  JOptionPane.showInputDialog("Enter weight of edge");

				try {
					src = Integer.parseInt(srcString);
					dest = Integer.parseInt(destString);
					weight = Double.parseDouble(weightString);
					if(weight<=0) {													//dijkstra only works with positive weight
						throw new RuntimeException();
					}

				} catch (Exception e2) {
					flag = false;
					JOptionPane.showMessageDialog(mainPanel, "invalid input", "error",JOptionPane.WARNING_MESSAGE);
				}

				if(flag) {
					try {
						dgraph.connect(src, dest, weight);
						repaint();

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(mainPanel, "src/destination node does not exist", "error",JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});

		removeEdge.addActionListener(new ActionListener() {			//stop request


			@Override
			public void actionPerformed(ActionEvent e) {
				int dest=-1,src=-1;

				boolean flag = true;

				graph dgraph = graph.getGraph();

				String srcString = JOptionPane.showInputDialog("Enter source node ID");
				String destString = JOptionPane.showInputDialog("Enter destination node ID");

				try {
					src = Integer.parseInt(srcString);
					dest = Integer.parseInt(destString);


				} catch (Exception e2) {
					flag = false;
					JOptionPane.showMessageDialog(mainPanel, "invalid input", "error",JOptionPane.WARNING_MESSAGE);
				}

				if(flag) {
					if(null==dgraph.removeEdge(src, dest)){
						JOptionPane.showMessageDialog(mainPanel, "edge does not exist", "error",JOptionPane.WARNING_MESSAGE);
					}
					repaint();

				}

			}
		});

		removeNode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int id = -1;

				boolean flag = true;

				graph dgraph = graph.getGraph();

				String idString = JOptionPane.showInputDialog("Enter source ID to remove");

				try {
					id = Integer.parseInt(idString);
					if(null==dgraph.removeNode(id)) {
						throw new RuntimeException();
					}

				} catch (Exception e2) {
					flag = false;
					JOptionPane.showMessageDialog(mainPanel, "edge does not exist", "error",JOptionPane.WARNING_MESSAGE);
				}

				if(flag) {
					repaint();
					//JOptionPane.showMessageDialog(mainPanel, "Specified node deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
				}


			}
		});

		isConnected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				boolean connected = graph.isConnected();
				if(connected) {

					JOptionPane.showMessageDialog(mainPanel,  "The graph is strongly connected");

				}
				else {
					JOptionPane.showMessageDialog(mainPanel,  "The graph is not strongly connected");
				}
			}
		});

		//BUTTONS config and add to panel
		save.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));
		tsp.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));
		addEdge.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));
		shortestPath.setPreferredSize(new Dimension(XDIMENSION, YDIMENSION));
		removeNode.setPreferredSize(new Dimension(XDIMENSION, YDIMENSION));
		isConnected.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));
		shortestPathDist.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));
		exit.setPreferredSize(new Dimension(XDIMENSION,YDIMENSION));

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BorderLayout());
		JPanel subSubPanelTop = new JPanel();
		JPanel subSubPanelBot = new JPanel();
		JPanel subSubPanelMid = new JPanel();

		subSubPanelTop.setLayout(new BorderLayout());
		subSubPanelBot.setLayout(new BorderLayout());
		subSubPanelMid.setLayout(new BorderLayout());

		topPanel.add(shortestPathDist);

		subSubPanelTop.add(exit,BorderLayout.NORTH);
		subSubPanelTop.add(tsp,BorderLayout.CENTER);
		subSubPanelTop.add(save,BorderLayout.SOUTH);

		subSubPanelBot.add(shortestPath, BorderLayout.NORTH);
		subSubPanelBot.add(shortestPathDist, BorderLayout.CENTER);
		subSubPanelBot.add(isConnected, BorderLayout.SOUTH);

		subSubPanelMid.add(addEdge, BorderLayout.NORTH);
		subSubPanelMid.add(removeEdge);
		subSubPanelMid.add(removeNode, BorderLayout.SOUTH);

		subPanel.add(subSubPanelTop, BorderLayout.NORTH);
		subPanel.add(subSubPanelBot, BorderLayout.SOUTH);
		subPanel.add(subSubPanelMid);



		mainPanel.add(subPanel, BorderLayout.WEST);

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		DGraph dgraph = (DGraph)this.graph.getGraph();
		Collection<node_data> nodes = dgraph.getV();




		Graphics2D g2 = (Graphics2D)g;


		for(node_data node : nodes) {
			Point3D p = node.getLocation();

			Collection<edge_data> edges = dgraph.getE(node.getKey());
			for(edge_data edge : edges ) {

				if(edge.getInfo().equals("on the path")) {
					edge.setInfo("");
					node_data dest = dgraph.getNode(edge.getDest());
					Point3D pDest = dest.getLocation();
					g.setColor(Color.green);


					g2.setStroke(new BasicStroke(4));
					g2.drawLine(p.ix()+5, p.iy()+5, pDest.ix()+5, pDest.iy()+5);

					g.setColor(Color.BLACK);
					double midX = ((p.x()+pDest.x())/2);
					double midY = ((p.y()+pDest.y())/2);

					double quarterX = ((midX+pDest.x())/2);
					double quarterY = ((midY+pDest.y())/2);

					int eighthX = (int)((quarterX+pDest.x())/2);
					int eighthY = (int)((quarterY+pDest.y())/2);

					double weight = edge.getWeight();
					
					weight = Math.round(weight * 100.0) / 100.0;    //show only 2 decimal places

					g.drawString(""+weight, (int)midX+7,(int)midY+7);		
					g.setColor(Color.YELLOW);

					g.fillOval(eighthX+3, eighthY+3, 5, 5);		//add point an eighth away of destination node indicating direction
					continue;
				}

				node_data dest = dgraph.getNode(edge.getDest());
				Point3D pDest = dest.getLocation();
				g.setColor(Color.cyan);


				g2.setStroke(new BasicStroke(2));
				g2.drawLine(p.ix()+5, p.iy()+5, pDest.ix()+5, pDest.iy()+5);

				g.setColor(Color.DARK_GRAY);

				double midX = ((p.x()+pDest.x())/2);
				double midY = ((p.y()+pDest.y())/2);

				double quarterX = ((midX+pDest.x())/2);
				double quarterY = ((midY+pDest.y())/2);

				int eighthX = (int)((quarterX+pDest.x())/2);
				int eighthY = (int)((quarterY+pDest.y())/2);
				
				double weight = edge.getWeight();
				
				weight = Math.round(weight * 100.0) / 100.0;			//show only 2 decimal places

				g.drawString(""+weight, (int)midX+7, (int)midY+7);
				g.setColor(Color.YELLOW);

				g.fillOval(eighthX+3, eighthY+3, 5, 5);			//add point an eighth away of destination node indicating direction




			}
		}
		for(node_data node : nodes) {				//paint nodes on top of edges



			Point3D p = node.getLocation();


			g.setColor(Color.red);
			g.fillOval(p.ix(), p.iy(), 10, 10);
			g.drawString(""+node.getKey(), p.ix(), p.iy()-5);

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {

		DGraph.Node newNode ;

		int x = e.getX();
		int y = e.getY();

		Point3D point = new Point3D(x,y);
		graph dgraph = graph.getGraph();


		int id = dgraph.nodeSize()+1;
		
		while(dgraph.getNode(id)!=null) {
			id++;
		}

		int input = JOptionPane.showConfirmDialog(mainPanel, "Create new node?", "New Node", JOptionPane.YES_NO_OPTION);
		if(input == JOptionPane.YES_OPTION) {
			try {				
				newNode = new DGraph.Node(id, x, y);
				dgraph.addNode(newNode);
				repaint();

			} catch (Exception e2) {
				JOptionPane.showMessageDialog(mainPanel, "invalid input", "error",JOptionPane.WARNING_MESSAGE);
			}
		}




	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
