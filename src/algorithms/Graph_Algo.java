package algorithms;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import dataStructure.DGraph;
import dataStructure.DGraph.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;

/**
 * This empty class represents the set of graph-theory algorithms which should
 * be implemented as part of Ex2 - Do edit this class.
 * 
 * @author
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable {

	private graph dGraph;
	
	public Graph_Algo() {
		dGraph = new DGraph();
	}
	
	public Graph_Algo(graph g) {
		this.dGraph=g;
	}

	@Override
	public void init(graph g) {
		dGraph = g;
	}

	@Override
	public void init(String file_name) {
		ObjectInputStream objectinputstream = null;
		try {
			FileInputStream streamIn = new FileInputStream(file_name);
			objectinputstream = new ObjectInputStream(streamIn);
			this.dGraph = (graph) objectinputstream.readObject();
			objectinputstream.close();
		} catch (Exception e) {
			throw new RuntimeException("file not found");
		}
	}

	@Override
	public void save(String file_name) {
		try {
			FileOutputStream fout = new FileOutputStream(file_name);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(this.dGraph);
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public graph getGraph() {
		return this.dGraph;
	}

	/**
	 * Do DFS once starting at node v, transpose the graph and do another DFS on the
	 * transposed graph starting at the same node v
	 */
	@Override
	public boolean isConnected() {
		if(this.dGraph.nodeSize()==0||this.dGraph.nodeSize()==1)
			return true;
		boolean connected = this.DFSiterative();
		Graph_Algo trans = new Graph_Algo();
		if (connected) {
			trans.init(this.getTranspose());
			
			connected = trans.DFSiterative();
		}
		return connected;

	}

	/**
	 * modified dijkstra algorithm to suit the implementation of the graph this
	 * function modiefies the tag of each node, tag is the key of the node from
	 * where the shortest path was found
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		node_data start = this.dGraph.getNode(src);

		start.setWeight(0); // set weight of starting node to 0
		start.setInfo("not visited");
		Collection<node_data> nodes = this.dGraph.getV();
		PriorityQueue<node_data> nodeQ = new PriorityQueue<node_data>();

		for (node_data n : nodes) { // initate weights(distance) of nodes with "infinity"
			if (n.getKey() != src) { // initiate tags to -1
				n.setWeight(Double.POSITIVE_INFINITY);
				n.setTag(-1);
				n.setInfo("not visited"); // random info
			}
			// nodeQ.add((Node)n);
		}
		nodeQ.add(start);

		while (!nodeQ.isEmpty()) {
			node_data f = nodeQ.remove();
			if (f.getInfo().equals("visited")) {
				continue;
			}
			f.setInfo("visited");

			
			Collection<edge_data> edges = this.dGraph.getE(f.getKey());
			for (edge_data e : edges) {
				node_data neighbor = this.dGraph.getNode(e.getDest());
				if ((neighbor.getWeight() > (f.getWeight() + e.getWeight()))) { // remove and reinsert into priority-
					neighbor.setWeight(f.getWeight() + e.getWeight());
					neighbor.setTag(f.getKey());
					nodeQ.add(neighbor);
				}
			}
		}

		return this.dGraph.getNode(dest).getWeight();
	}

	/**
	 * run the previous function iterate on nodes starting at destination and add
	 * each node we past on the way to the list
	 */
	@Override
	public List<node_data> shortestPath(int src, int dest) {
		LinkedList<node_data> path = new LinkedList<node_data>();
		double dist = this.shortestPathDist(src, dest);
		if (dist == Double.POSITIVE_INFINITY) {
			return null;
		}
		node_data currNode = this.dGraph.getNode(dest);
		while (currNode.getKey() != src) {
			path.addFirst(currNode);
			currNode = this.dGraph.getNode(currNode.getTag());
		}
		path.addFirst(currNode);
		return path;
	}

	@Override
	public List<node_data> TSP(List<Integer> targets) {

		if (targets.isEmpty()) { // target list is empty
			return null;
		}
		Iterator<Integer> iter = targets.iterator();
		LinkedList<node_data> path = new LinkedList<node_data>();

		int src = iter.next();

		if (!iter.hasNext()) { // there is only 1 target
			path.add(this.dGraph.getNode(src));
			return path;
		}

		node_data last = null;
		
		List<Integer> alreadyPast = new LinkedList<Integer>();  //for nodes past on the way to other nodes

		while (iter.hasNext()) {
			int dest = iter.next();
			if (alreadyPast.contains(dest)) {		//skip nodes already reached
				continue;
			}
			List<node_data> tempPath = this.shortestPath(src, dest);
			
			if(tempPath==null) {			//if there is no path return null
				return null;
			}

			path.addAll(tempPath);
			last = path.removeLast();

			Iterator<Integer> iterInner = targets.iterator();


			while(iterInner.hasNext()) {   		//"tag" nodes that have been past on path that they are 
				int check = iterInner.next();
				if(tempPath.contains(this.dGraph.getNode(check))) { 
					alreadyPast.add(check);
				}
			}

			src = dest;
		}

		path.add(last);

		return path;
	}

	@Override
	public graph copy() {
		Collection<node_data> nodes = dGraph.getV();
		DGraph graphCopy = new DGraph();
		for (node_data n : nodes) {
			node_data nD = new DGraph.Node(n.getKey(), n.getWeight());
			graphCopy.addNode(nD);
		}
		for (node_data n : nodes) {
			Collection<edge_data> edges = dGraph.getE(n.getKey());
			for (edge_data e : edges) {
				graphCopy.connect(e.getSrc(), e.getDest(), e.getWeight());
			}
		}
		return graphCopy;
	}
	/**
	 * iterative "DFS" for big inputs
	 * @return
	 */
	private boolean DFSiterative() {
		
		boolean connected = true;
		
		Collection<node_data> nodes = this.dGraph.getV();
		for (node_data n : nodes) {
			n.setTag(0); //set to not visited
		}
		
		Iterator<node_data> iter = nodes.iterator();
		node_data start = iter.next(); // get first item (any node to start dfs from)
				
		Stack<node_data> nodeStack = new Stack<node_data>();
		
		nodeStack.push(start);
		
		while(!nodeStack.isEmpty()) {
			node_data currentNode = nodeStack.pop();			
			 if(currentNode.getTag() == 0) 
             {  
				 currentNode.setTag(1);
             } 
			 Collection<edge_data> edges = this.dGraph.getE(currentNode.getKey());
			 for(edge_data e : edges) {		//iterate on all adjacent nodes
				 node_data adjacentNode = this.dGraph.getNode(e.getDest());
				 if(adjacentNode.getTag()!=1) { //if adjacent node has been visited
					 nodeStack.push(adjacentNode);
				 }
			 }
		}
		
		for(node_data n : nodes) {  //check if there are nodes that have not been visited
			if(n.getTag()==0) {
				connected = false;
				return connected;
			}
		}
		
		return connected;
		
	}

	/**
	 * modified DFS algorithm. just checks if every node is reachable from starting
	 * node
	 * method is recursive and therefore does not work for really big input (>100,000 nodes)
	 * @return true if connected
	 */
	private boolean DFS() {
		boolean connected = true;

		Collection<node_data> nodes = this.dGraph.getV();
		for (node_data n : nodes) {
			n.setTag(0);
		}
		Iterator<node_data> iter = nodes.iterator();
		node_data start = iter.next(); // get first item
		DFSUtil(start);
		for (node_data n : nodes) {
			if (n.getTag() == 0) {
				connected = false;
				return connected;
			}
		}
		return connected;
	}

	/**
	 * recursive help function for DFS traversal
	 * 
	 * @param start
	 */
	private void DFSUtil(node_data start) {

		Collection<edge_data> edges = this.dGraph.getE(start.getKey());
		for (edge_data e : edges) {
			node_data visit = this.dGraph.getNode(e.getDest());
			if (visit.getTag() == 0) { // if there has not been a visit yet
				visit.setTag(1);
				DFSUtil(visit);
			}
		}
	}

	/**
	 * method to get transpose of given graph. the transpose is a deep copy, doesnt
	 * modify original graph
	 * 
	 * @return transposed graph
	 */
	private graph getTranspose() {
		ArrayList<edge_data> toRemove = new ArrayList<edge_data>();
		ArrayList<edge_data> toConnect = new ArrayList<edge_data>();
		Collection<node_data> nodes = this.dGraph.getV();
		graph transpose = this.copy();
		for (node_data n : nodes) {
			Collection<edge_data> edges = this.dGraph.getE(n.getKey());
			for (edge_data e : edges) {
				toRemove.add(e);
				edge_data eCon = new DGraph.Edge(e.getDest(), e.getSrc(), e.getWeight());
				toConnect.add(eCon);
			}
		}
		Iterator<edge_data> edgeRm = toRemove.iterator();
		while (edgeRm.hasNext()) {
			edge_data eD = edgeRm.next();
			transpose.removeEdge(eD.getSrc(), eD.getDest());
		}
		Iterator<edge_data> edgeCn = toConnect.iterator();
		while (edgeCn.hasNext()) {
			edge_data eD = edgeCn.next();
			transpose.connect(eD.getDest(), eD.getSrc(), eD.getWeight());
		}

		return transpose;
	}

	/**
	 * simple toString function doesnt return a string
	 */
	public String toString() {
		this.dGraph.toString();
		return "";
	}

	/*
	 * private boolean iterDFS() {
	 * 
	 * }
	 */
}
