package gameGUI;

import java.util.List;

import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import gameData.Fruit;

public class startGUI {

	public static void main(String[] args) {
	
		
		int gameID = 2;

		
		
		
		GUI gui = new GUI(gameID);
		gui.setVisible(true);
	}

}
