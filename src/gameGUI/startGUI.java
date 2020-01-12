package gameGUI;

import java.util.List;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameData.Fruit;

public class startGUI {

	public static void main(String[] args) {
	
		game_service game = Game_Server.getServer(2);
	
		
		
		
		
		DGraph dgraph = new DGraph();
		dgraph.init(game.getGraph());
		
		List<String> fruits = game.getFruits();
		
		for(String fruit : fruits) {
			Fruit f = new Fruit(fruit);
			dgraph.fruity(f);
		}
		
		GUI gui = new GUI(dgraph);
		gui.setVisible(true);
	}

}
