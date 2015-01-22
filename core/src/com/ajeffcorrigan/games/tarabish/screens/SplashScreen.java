package com.ajeffcorrigan.games.tarabish.screens;

import com.ajeffcorrigan.games.tarabish.AssetManager;
import com.ajeffcorrigan.games.tarabish.tarabish;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class SplashScreen implements Screen {
	//MyGame object container.
	private tarabish game;
	//Timer variable to show splash screen.
	private float timed = 0.0f;
	
	/**
	 * Constructor for spash screen.
	 * @param g Tarabish game container.
	 */
	public SplashScreen(tarabish g) {
		this.game = g;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(255, 255, 255, 0);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    game.batch.begin();
	    game.batch.draw(AssetManager.getTexture("logo"), (tarabish.GAMEWIDTH/2) - (AssetManager.getTexture("logo").getWidth()/2),(tarabish.GAMEHEIGHT/2) - (AssetManager.getTexture("logo").getHeight()/2));
	    game.batch.end();
	    if(timed > 10 && tarabish.assetsInit) {
			game.mainMenuScreen = new MainMenuScreen(game);
			game.setScreen(game.mainMenuScreen);
		}
	    timed += 3.2f * delta;
	    if(!tarabish.assetsInit) {
			AssetManager.loadTextureAs("felt", "greenfelt.jpg");
			AssetManager.loadTextureAs("cards", "classic_8x4x92x128.png");
			AssetManager.loadTextureAs("cardback", "cardback92128.jpg");
			AssetManager.loadTextureAs("bground2", "cb_tartan.jpg");
			AssetManager.loadTextureAs("gamesheet", "gamesheet.png");
			AssetManager.createTextureRegion("gtitle", "gamesheet", 0, 0, 350, 104);
			AssetManager.createTextureRegion("newgame", "gamesheet", 0, 104, 310, 59);
			AssetManager.createTextureRegion("rules", "gamesheet", 351, 9, 149, 55);
			AssetManager.createTextureRegion("pshadow", "gamesheet", 500, 0, 64, 64);
			AssetManager.createTextureRegion("diamond", "gamesheet", 0, 164, 52, 52);
			AssetManager.createTextureRegion("club", "gamesheet", 0, 214, 52, 52);
			AssetManager.createTextureRegion("heart", "gamesheet", 0, 268, 52, 52);
			AssetManager.createTextureRegion("spade", "gamesheet", 0, 320, 52, 52);
			AssetManager.createTextureRegion("menubg", "gamesheet", 0, 375, 64, 64);
			AssetManager.createTextureRegion("box", "gamesheet", 55, 163, 52, 52);
			AssetManager.createTextureRegion("check", "gamesheet", 55, 220, 52, 52);
			AssetManager.createTextureRegion("cancel", "gamesheet", 55, 270, 52, 52);
			AssetManager.createTextureRegion("pause", "gamesheet", 55, 319, 52, 52);
			AssetManager.createTextureRegion("pass", "gamesheet", 354, 72, 72, 30);
			tarabish.assetsInit = true;
	    }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		AssetManager.loadTextureAs("logo", "logo.png");
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {

	}
}
