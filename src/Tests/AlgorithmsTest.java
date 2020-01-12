package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import algorithms.*;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlgorithmsTest {

	graph_algorithms algo;
	graph g;

	@BeforeEach
	public void setBeforeEach() {
		g = new DGraph();

		node_data n1 = new DGraph.Node();
		node_data n2 = new DGraph.Node();
		node_data n3 = new DGraph.Node();

		((DGraph.Node)n1).setKey(1);
		((DGraph.Node)n2).setKey(2);
		((DGraph.Node)n3).setKey(3);		


		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);


		g.connect(1, 2, 2);
		g.connect(2, 3, 1);
		g.connect(1, 3, 6);

		algo = new Graph_Algo();
		algo.init(g);
	}


	@Test
	void isConnectedtest() {
		assertFalse(algo.isConnected());
		g.connect(3, 1, 2);
		assertTrue(algo.isConnected());
	}

	@Test
	void shortestPathDisttest() {
		assertNotEquals(6, algo.shortestPathDist(1, 3));
		assertEquals(3, algo.shortestPathDist(1, 3));
	}

	@Test
	void shortestPathtest() {
		node_data n1 = new DGraph.Node();
		node_data n2 = new DGraph.Node();
		node_data n3 = new DGraph.Node();

		((DGraph.Node)n1).setKey(4);
		((DGraph.Node)n2).setKey(5);
		((DGraph.Node)n3).setKey(6);		


		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);

		g.connect(2, 4, 4);
		g.connect(4, 6, 1);
		g.connect(4, 5, 3);
		g.connect(5, 6, 2);

		g.connect(3, 6, 10);

		List<node_data> path = algo.shortestPath(1, 6);

		assertTrue(path.contains(n1));
		assertFalse(path.contains(n2));


	}

	@Test
	void TSPtest() {

		node_data n1 = new DGraph.Node();
		node_data n2 = new DGraph.Node();
		node_data n3 = new DGraph.Node();

		((DGraph.Node)n1).setKey(4);
		((DGraph.Node)n2).setKey(5);
		((DGraph.Node)n3).setKey(6);		


		g.addNode(n1);
		g.addNode(n2);
		g.addNode(n3);

		g.connect(2, 4, 4);
		g.connect(4, 6, 1);
		g.connect(4, 5, 3);
		g.connect(5, 6, 2);

		g.connect(3, 6, 10);

		ArrayList<Integer> targets = new ArrayList<Integer>();
		targets.add(1);
		targets.add(5);
		targets.add(6);

		List<node_data> path = algo.TSP(targets);

		assertTrue(path.contains(n2));

		targets.add(4);				
		List<node_data> path2 = algo.TSP(targets);		//4 is already on the path

		assertTrue(path.equals(path2));
		targets.add(3);
		List<node_data> path3 = algo.TSP(targets);		//no way back from 6 to 3

		assertNull(path3);

		ArrayList<Integer> targets2 = new ArrayList<Integer>();
		targets2.add(1);
		targets2.add(3);
		targets2.add(6);

		List<node_data> path4 = algo.TSP(targets2);		//there is a way from 3 to 6 though

		assertNotNull(path4);


	}

	@Test
	void copytest() {
		DGraph dG = new DGraph();
		Graph_Algo newGraph = new Graph_Algo();
		dG.addNode(new DGraph.Node(1));
		dG.addNode(new DGraph.Node(2));
		dG.addNode(new DGraph.Node(3));
		dG.addNode(new DGraph.Node(4));
		dG.addNode(new DGraph.Node(5));
		dG.connect(1,2,12);
		dG.connect(1,3,11);
		dG.connect(1,4,3);
		dG.connect(2,5,1);
		dG.connect(3,2,3);
		dG.connect(3,5,2);
		dG.connect(4,1,2);
		dG.connect(4,2,5);
		dG.connect(4,3,5);
		dG.connect(4,5,4);
		dG.connect(5,2,2);
		dG.connect(5,3,1);

		newGraph.init(dG); // Initialize newGraph using dG
		graph dGraph = newGraph.copy();
		assertEquals(12, dGraph.edgeSize());
		dG.connect(1,5,6);
		assertNotEquals(13, dGraph.edgeSize()); // Deep copy has been made

	}
}
