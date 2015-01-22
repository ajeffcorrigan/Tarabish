package com.ajeffcorrigan.games.tarabish.menu;

import java.util.ArrayList;

import com.ajeffcorrigan.games.tarabish.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class InGameMenu {

	private NinePatch menubg;
	private Vector2 menu;
	private int width;
	private int height;
	private BitmapFont font;
	private ArrayList<MenuText> menutext = new ArrayList<MenuText>();
	private ArrayList<MenuEntity> menuentity = new ArrayList<MenuEntity>();
	
	/**
	 * Default constructor.
	 */
	public InGameMenu() {
	}
	
	public InGameMenu(int x, int y, int h, int w) {
		this.menu = new Vector2(x,y);
		this.width = w;
		this.height = h;
		this.menubg = new NinePatch(AssetManager.getTextureRegion("menubg"),8,8,8,8);
		this.font = new BitmapFont();
	}
	public Vector2 getMenu() {
		return menu;
	}
	public int getMenuX() {
		return (int)menu.x;
	}
	public int getMenuY() {
		return (int)menu.y;
	}

	public void setMenu(Vector2 menu) {
		this.menu = menu;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public void draw(SpriteBatch batch) {
		menubg.draw(batch, this.menu.x, this.menu.y, this.width, this.height);
		for(MenuText mt : menutext) {
			font.draw(batch, mt.getText(), mt.getLocationX(), mt.getLocationY());
		}
		for(MenuEntity me : menuentity) {
			batch.draw(me.getTr(), me.getLocationX(), me.getLocationY());
		}
	}
	public void addText(String s, int x, int y) {
		this.menutext.add(new MenuText(s,x,y));
	}
	public void addEntity(TextureRegion t, int x, int y) {
		this.menuentity.add(new MenuEntity(t,x,y));
	}
	public void addEntity(TextureRegion t, float x, float y) {
		this.menuentity.add(new MenuEntity(t,(int)x,(int)y));
	}
	
}
