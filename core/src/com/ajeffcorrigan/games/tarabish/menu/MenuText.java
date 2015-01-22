package com.ajeffcorrigan.games.tarabish.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MenuText {

	private String text;
	private Vector2 location;
	private Color tcolor;
	
	/**
	 * Default constructor.
	 */
	public MenuText() {
		
	}
	public MenuText(String s, int x, int y) {
		this.text = s;
		this.location = new Vector2(x,y);
		this.tcolor = new Color(Color.BLACK);
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
	public Color getTcolor() {
		return tcolor;
	}
	public void setTcolor(Color tcolor) {
		this.tcolor = tcolor;
	}
	public String getText() {
		return text;
	}
}
