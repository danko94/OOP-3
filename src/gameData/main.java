package gameData;

import java.util.Collection;
import java.util.List;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;

public class main {

	public static void main(String[] args) {
		game_service game = Game_Server.getServer(2);
		
		List<String> fruits = game.getFruits();
		
		for(String fruit : fruits) {
			System.out.println(fruit);
			Fruit f = new Fruit(fruit);
			System.out.println(f.getValue());
		}
		
		game.addRobot(0);
		List<String> robots = game.getRobots();
		
		
		for(String robot : robots) {
			System.out.println(robot);
			Robot r = new Robot(robot);
			System.out.println(r.getSpeed());
		}
		
		DGraph g = new DGraph();
		
		g.init(game.getGraph());
		
		Collection<node_data> nodes = g.getV();
		
		for(node_data n :  nodes) {
			System.out.println(n.getLocation().x());
		}
	}

}
