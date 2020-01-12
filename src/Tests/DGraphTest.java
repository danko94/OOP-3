package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.Graph_Algo;
import algorithms.graph_algorithms;
import dataStructure.DGraph;
import dataStructure.DGraph.Node;
import dataStructure.edge_data;
import dataStructure.node_data;

class DGraphTest {

	DGraph dG;

	@BeforeEach
	public void createGraph() {
		dG = new DGraph();
		DGraph.Node nG = new DGraph.Node(1, 2.5);
		DGraph.Node nG1 = new DGraph.Node(2, 7);
		DGraph.Node nG2 = new DGraph.Node(3, 14);

		dG.addNode(nG2);
		dG.addNode(nG1);
		dG.addNode(nG);
		dG.connect(1, 2, 3);
		dG.connect(3, 1, 2);
		dG.connect(2, 3, 3);

	}

	@Test
	void getNodetest() {

		node_data nD = dG.getNode(2);
		assertEquals(7, nD.getWeight());

	}

	@Test
	void getEdgetest() {
		edge_data eD = dG.getEdge(1, 2);
		assertEquals(3, eD.getWeight());
	}

	@Test
	void addNodetest() {
		DGraph.Node nG = new DGraph.Node(4, 1);
		int before = dG.nodeSize();
		dG.addNode(nG);
		int after = dG.nodeSize();
		assertEquals(before+1, after);
	}

	@Test
	void connecttest() {
		DGraph.Node nG = new DGraph.Node(4, 1);
		dG.addNode(nG);
		int before = dG.edgeSize();
		dG.connect(4, 1, 1);

		assertEquals(before+1, dG.edgeSize());
	}
	@Test
	void removeNodetest() {
		assertNull(dG.removeNode(420));

		int before = dG.edgeSize();
		dG.removeNode(3);
		assertEquals(before-2, dG.edgeSize());		
	}
	@Test
	void collectiontest() {
		DGraph.Node node = new DGraph.Node(4,2);
		dG.addNode(node);
		dG.connect(1, 4, 2);

		Collection<node_data> nodes = dG.getV();
		assertEquals(dG.nodeSize(), nodes.size());

		Collection<edge_data> edges = dG.getE(1);
		assertEquals(2, edges.size());
	}

	@Test
	void modeCounttest() {
		int before = dG.getMC();
		dG.addNode(new Node(4));
		assertNotEquals(before, dG.getMC());
		before = dG.getMC();
		dG.connect(4, 1, 2);
		assertNotEquals(before, dG.getMC());
		before = dG.getMC();
		dG.connect(4, 3, 2);
		assertNotEquals(before, dG.getMC());
		before = dG.getMC();
		dG.removeEdge(4, 1);
		assertNotEquals(before, dG.getMC());
		before = dG.getMC();
		dG.removeEdge(4, 3);
		assertNotEquals(before, dG.getMC());
		before = dG.getMC();
		dG.removeNode(4);
		assertNotEquals(before, dG.getMC());		
	}

	@Test
	void compareNodetest() {
		Node node1 = (Node)dG.getNode(1);
		node_data node2 = dG.getNode(2);

		node1.setWeight(5);
		node2.setWeight(2);

		int actual = node1.compareTo(node2);

		assertEquals(1, actual);

	}

	@Test
	void create10MilEdges() {
		DGraph dG = new DGraph();

		long start = System.currentTimeMillis();
		for(int i=1; i<=1000000;i++) {
			node_data n = new DGraph.Node(i);
			dG.addNode(n);
		}
		for(int i=1; i<=1000000; i++) {
			for(int j=1; j<=10;j++) {
				int randDest=(int)(Math.random() * (1000000-1) +1);
				double randWeight= (Math.random() * (20) +2);
				try {
					dG.connect(i, randDest, randWeight);

				} catch (Exception e) {
					continue;
				}
			}
		}

		long finish = System.currentTimeMillis();
		double timeLapsed = (finish-start)/1000.0;
		System.out.println(timeLapsed);		//delete later

		if(timeLapsed>10) {
			assertTrue(false);
		}
		else {
			assertTrue(true);
		}
		graph_algorithms algo = new Graph_Algo(dG);

		long start2 = System.currentTimeMillis();
		boolean connected = algo.isConnected();
		System.out.println(connected);			//delete later
		long finish2 = System.currentTimeMillis();

		double timeLapsed2 = (finish2-start2)/1000.0;
		System.out.println(timeLapsed2);		//delete later

		if(timeLapsed2>15) {
			assertTrue(false);
		}
		else {
			assertTrue(true);
		}
		
		long start3 = System.currentTimeMillis();
		int randSrc = (int)(Math.random()*1000000-1)+1;
		int randDst = (int)(Math.random()*1000000-1)+1;
		List<node_data> path = algo.shortestPath(randSrc, randDst);
		double shortest = algo.shortestPathDist(randSrc, randDst);
		
		long finish3 = System.currentTimeMillis();
		double timeLapsed3 = (finish3-start3)/1000.0;
		System.out.println(timeLapsed3);		//delete
		System.out.println("shortest path from " + randSrc + " to " + randDst + " is: " + shortest);//delete

		for(node_data n : path) {
			System.out.print(n.getKey() + " -> ");
		}

	}
}
