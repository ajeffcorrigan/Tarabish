package com.ajeffcorrigan.games.tarabish.menu;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class MenuEntity {

	private Vector2 location;
	private TextureRegion tr;
	
	public MenuEntity(TextureRegion t, int x, int y) {
		this.location = new Vector2(x,y);
		this.tr = t;
	}

	public Vector2 getLocation() {
		return location;
	}
	public int getLocationX() {
		return (int)location.x;
	}
	public int getLocationY() {
		return (int)location.y;
	}
	public void setLocation(Vector2 location) {
		this.location = location;
	}
	public TextureRegion getTr() {
		return tr;
	}	
}
