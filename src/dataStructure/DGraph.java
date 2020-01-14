package dataStructure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gameData.Fruit;
import utils.Point3D;

public class DGraph implements graph, Serializable{

	public static class Edge implements edge_data, Serializable{

		int dest;
		int src;
		double weight;
		int tag;
		String info;

		boolean hasFruit;
		Fruit fruit;

		public Edge() {
			this.dest=-1;
			this.src=-1;
			this.weight=-1;
			this.info="free edge";
		}

		public Edge(int dest, int src, double weight) {
			this.dest=dest;
			this.src=src;
			this.weight=weight;
			this.info = "";
		}

		public int getDest() {
			return dest;
		}

		public void setDest(int dest) {
			this.dest = dest;
		}

		public int getSrc() {
			return src;
		}

		public void setSrc(int src) {
			this.src = src;
		}

		public double getWeight() {
			return weight;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public String getInfo() {
			return info;
		}

		public void setInfo(String info) {
			this.info = info;
		}

		@Override
		public int getTag() {
			return tag;
		}

		@Override
		public void setTag(int t) {
			this.tag = t;			
		}

		public void setHasFruit(boolean b) {
			this.hasFruit = b;
		}

		public boolean isFruity() {
			return this.hasFruit;
		}

		public void setFruit(Fruit f) {
			this.fruit = f;
		}
		
		public Fruit getFruit() {
			return this.fruit;
		}




	}

	public static class Node implements node_data, Comparable<node_data>, Serializable{

		private Point3D location;
		private int key;
		private int tag;
		private double weight;
		private String info;
		private Map<Integer, edge_data> adjList; //key is destination

		private final int X_GUI_OFFSET = 155;
		private final int Y_GUI_OFFSET = 25;

		private final int X_GUI_WINDOWRANGE = 580;
		private final int Y_GUI_WINDOWRANGE = 630;


		public Node() {
			adjList = new HashMap<Integer, edge_data>();
			this.key = -1;
			this.info = "free node";
			this.weight = -1;

		}


		public Node(int key) {
			adjList = new HashMap<Integer, edge_data>();
			this.key = key;	
			info = "";

			double rand = Math.random();
			double x = rand*(X_GUI_WINDOWRANGE)+X_GUI_OFFSET;
			rand = Math.random();
			double y = rand*(Y_GUI_WINDOWRANGE)+Y_GUI_OFFSET;			//numbers are specific for size of GUI

			Point3D p = new Point3D(x,y);

			this.location = p;
		}

		public Node(int key, double weight) {
			adjList = new HashMap<Integer, edge_data>();
			this.key = key;
			this.weight = weight;	
			info = "";

			double rand = Math.random();
			double x = rand*(X_GUI_WINDOWRANGE)+X_GUI_OFFSET;
			rand = Math.random();
			double y = rand*(Y_GUI_WINDOWRANGE)+Y_GUI_OFFSET;			//numbers are specific for size of GUI

			Point3D p = new Point3D(x,y);

			this.location = p;
		}
		public Node(int key, double weight, double x, double y) {
			adjList = new HashMap<Integer, edge_data>();
			this.key = key;
			this.weight = weight;	
			info = "";

			Point3D p = new Point3D(x+X_GUI_OFFSET, y+Y_GUI_OFFSET);		//Offset is for GUI
			this.location=p;

		}
		public Node(int key, double x, double y) {
			adjList = new HashMap<Integer, edge_data>();
			this.key = key;
			info = "";

			Point3D p = new Point3D(x+X_GUI_OFFSET, y+Y_GUI_OFFSET);
			this.location=p;

		}


		public edge_data getEdge(int keyDest) {
			return adjList.get(keyDest);
		}
		public void setEdge(int keyDest, Edge e) {
			adjList.put(keyDest, e);
		}
		public edge_data removeEdge(int keyDest) {
			edge_data e = adjList.remove(keyDest);
			return e;
		}
		public int removeAllEdges() {
			int toRemove = adjList.values().size();	
			return toRemove;

		}
		@Override
		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key=key;
		}



		@Override
		public Point3D getLocation() {
			return location;
		}

		@Override
		public void setLocation(Point3D p) {
			//Point3D offsetPoint = new Point3D(p.x()+X_GUI_OFFSET, p.y()+Y_GUI_OFFSET); //adjust to GUI offset
			Point3D offsetPoint = new Point3D(p.x(),p.y());
			this.location = offsetPoint;			
		}

		@Override
		public double getWeight() {
			return weight;
		}

		@Override
		public void setWeight(double w) {
			this.weight = w;

		}

		@Override
		public String getInfo() {
			return info;
		}

		@Override
		public void setInfo(String s) {
			this.info = s;
		}

		@Override
		public int getTag() {
			return tag;
		}

		@Override
		public void setTag(int t) {
			this.tag = t;

		}



		@Override
		public int compareTo(node_data o) {		//comparator for priority queue
			if(this.getWeight()>o.getWeight())
				return 1;
			else if(this.getWeight()<o.getWeight())
				return -1;
			else
				return 0;
		}

	}

	//map <src, map<dest, edge_data>>
	Map<Integer, node_data> graph; //key is src/key

	int vertixCount;
	int edgeCount;
	int modeCount;


	public DGraph() {
		graph = new HashMap<Integer, node_data>();
		vertixCount = 0;
		edgeCount = 0;
		modeCount = 0;
	}


	@Override
	public node_data getNode(int key) {
		node_data nD =graph.get(key);
		return nD;
	}

	@Override
	public edge_data getEdge(int src, int dest) {
		node_data nD = graph.get(src);
		if(nD==null) {
			return null;
		}
		edge_data eD = ((Node) nD).getEdge(dest);
		return eD;		
	}

	@Override
	public void addNode(node_data n) throws RuntimeException{
		int id = n.getKey();
		if(id==-1)
			throw new RuntimeException("id is invalid");
		if(graph.containsKey(id)) {			//maybe throw exception
			return;
		}
		int key = n.getKey();
		graph.put(key, n);
		increaseVertexCount();
		increaseModeCount();
	}

	private void increaseModeCount() {
		modeCount++;
	}


	@Override
	public void connect(int src, int dest, double w) throws RuntimeException{
		if(this.getEdge(src, dest)!=null) {
			return;
		}
		node_data source = graph.get(src);
		//node_data destination =graph.get(dest);
		if(source==null || graph.get(dest)==null) {  //2nd condition raises runtime by a factor 2-3
			throw new RuntimeException("source/destination node does not exist");
		}
		Edge edge = new Edge(dest, src, w);
		((Node) source).setEdge(dest, edge);
		increaseEdgeCount();
		increaseModeCount();
	}

	@Override
	public Collection<node_data> getV() {
		Collection<node_data> nodeCol = graph.values();
		return nodeCol;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		node_data nD = graph.get(node_id);		
		Collection<edge_data> edgeCol = ((Node) nD).adjList.values();
		return edgeCol;
	}

	@Override
	public node_data removeNode(int key) {
		node_data nD = graph.remove(key);		
		if(nD==null) {
			return null;
		}
		int toRemove = ((Node) nD).removeAllEdges();
		Collection<node_data> nodes = this.getV();
		for(node_data n : nodes) {
			edge_data e = this.removeEdge(n.getKey(), key);

		}
		for(int i=0;i<toRemove;i++) {
			decreaseEdgeCount();
			increaseModeCount();
		}
		decreaseVertexCount();
		increaseModeCount();
		return nD;
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
		node_data nD = graph.get(src);
		edge_data eD = ((Node) nD).removeEdge(dest);
		if(eD!=null) {
			increaseModeCount();
			decreaseEdgeCount();
		}
		return eD;
	}


	@Override
	public int nodeSize() {
		return vertixCount;
	}

	private void increaseVertexCount() {
		vertixCount++;
	}
	private void increaseEdgeCount() {
		edgeCount++;
	}
	private void decreaseVertexCount() {
		vertixCount--;
	}
	private void decreaseEdgeCount() {
		edgeCount--;
	}

	@Override
	public int edgeSize() {
		return edgeCount;
	}

	@Override
	public int getMC() {
		return modeCount;
	}

	public String toString() {
		Collection<node_data> nodes = this.getV();
		for(node_data n : nodes) {
			int key = n.getKey();
			Collection<edge_data> edges = this.getE(key);
			System.out.print(key);
			for(edge_data e : edges) {
				System.out.print(" -> " + e.getDest());
			}
			System.out.println();
		}
		return null;
	}

	public Map<Integer, node_data> getGraph() {
		return this.graph;
	}

	/**
	 * add fruti to edge
	 * @param fruit
	 */
	public void fruity(Fruit fruit) {
		double epsilon = 0.001;
		double distance = 0, fruitDistance = 0;
		double x1, x2, y1, y2, xFruit, yFruit;


		Collection<node_data> nodes = this.getV();

		for(node_data node : nodes) {
			Collection<edge_data> edges = this.getE(node.getKey());
			for(edge_data edge : edges) {
				x1 = node.getLocation().x();
				x2 = this.getNode(edge.getDest()).getLocation().x();
				y1 = node.getLocation().y();
				y2 = this.getNode(edge.getDest()).getLocation().y();
				xFruit = fruit.getPos().x();
				yFruit = fruit.getPos().y();
				distance = Math.sqrt((Math.pow((x1-x2), 2)+Math.pow((y1-y2),2)));
				//System.out.println(distance);
				fruitDistance = Math.sqrt((Math.pow((x1-xFruit), 2)+Math.pow((y1-yFruit),2))) + Math.sqrt((Math.pow((xFruit-x2), 2)+Math.pow((yFruit-y2),2)));
				//System.out.println(fruitDistance);
				if(Math.abs(fruitDistance-distance)<epsilon) {
					((Edge)edge).setHasFruit(true);
					((Edge)edge).setFruit(fruit);
				}
			
			}
		}



	}

	/**
	 * init DGraph from JSON String
	 * @param jsonString
	 */
	public void init(String jsonString) {

		JSONObject obj;

		try {

			obj = new JSONObject(jsonString);
			JSONArray jsonEdges = obj.getJSONArray("Edges");
			JSONArray jsonNodes = obj.getJSONArray("Nodes");	

			for(int i=0; i<jsonNodes.length(); i++) {
				Object node = jsonNodes.get(i);
				node_data tempNode = new DGraph.Node(((JSONObject)node).getInt("id"));
				String posString = ((JSONObject)node).getString("pos");
				String posArray []= posString.split(",");
				double x = Double.parseDouble(posArray[0]);
				double y = Double.parseDouble(posArray[1]);
				double z = Double.parseDouble(posArray[2]);
				tempNode.setLocation(new Point3D(x,y,z));
				this.addNode(tempNode);

			}

			for(int i=0; i<jsonEdges.length(); i++) {
				Object edge = jsonEdges.get(i);
				int src = ((JSONObject)edge).getInt("src");
				int dest = ((JSONObject)edge).getInt("dest");
				double weight = ((JSONObject)edge).getDouble("w");

				this.connect(src, dest, weight);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
