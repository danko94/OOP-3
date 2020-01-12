package gameData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Point3D;

public class Fruit {
	
	private int type;
	private Point3D pos;
	private double value;
	
	/**
	 * empty constructor
	 */
	public Fruit() {}
	
	/**
	 * Fruit constructor from JSON String
	 * @param jsonString 
	 */
	public Fruit(String jsonString) {
		String sub =jsonString.substring(9, jsonString.length()-1);
		
		JSONObject obj;
		String posString;
		double x,y,z,value;
		
		try {
			obj = new JSONObject(sub);
			this.value = obj.getDouble("value");
			this.type = obj.getInt("type");
			posString = obj.getString("pos");
			
			String posArray [] = posString.split(",");
			
			x = Double.parseDouble(posArray[0]);
			y = Double.parseDouble(posArray[1]);	
			z = Double.parseDouble(posArray[2]);
			
			this.pos = new Point3D(x,y,z);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Point3D getPos() {
		return pos;
	}

	public void setPos(Point3D pos) {
		this.pos = pos;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
