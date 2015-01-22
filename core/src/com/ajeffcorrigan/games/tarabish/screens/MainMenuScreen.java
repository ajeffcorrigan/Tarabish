package com.ajeffcorrigan.games.tarabish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.ajeffcorrigan.games.tarabish.AssetManager;
import com.ajeffcorrigan.games.tarabish.tarabish;

public class MainMenuScreen implements Screen {

	//MyGame object container.
	private tarabish game;
	
	private Rectangle newBounds, rulesBounds;
	
	private Vector3 touchPoint;
	
	public <Tarabish> MainMenuScreen(tarabish g) {
		this.game = g;
		//Dispose and destroy splashScreen.
		if(game.splashScreen != null) { 
			game.splashScreen.dispose();
			this.game.splashScreen = null;
		}
		newBounds = new Rectangle(95,132,AssetManager.getTextureRegion("newgame").getRegionWidth(),AssetManager.getTextureRegion("newgame").getRegionHeight());
		rulesBounds = new Rectangle(165,45,AssetManager.getTextureRegion("rules").getRegionWidth(),AssetManager.getTextureRegion("rules").getRegionHeight());
		touchPoint = new Vector3();
	}
	
	public void draw(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    game.camera.update();
	    if(tarabish.assetsInit) {
		    game.batch.begin();
		    game.batch.draw(AssetManager.getTexture("bground2"), 0, 0, game.camera.viewportWidth, game.camera.viewportHeight);
		    game.batch.draw(AssetManager.getTextureRegion("gtitle"), 65, 215);
		    game.batch.draw(AssetManager.getTextureRegion("newgame"), 95, 132);
		    game.batch.draw(AssetManager.getTextureRegion("rules"), 165, 45);
			game.batch.end();
	    }
	}
	
	public void update(float deltaTime) {
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) { Gdx.app.exit(); }
		if (Gdx.input.justTouched()) {
			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),0));
			if(newBounds.contains(touchPoint.x, touchPoint.y)) {
				game.inGameScreen = new InGameScreen(game);
				game.setScreen(game.inGameScreen);
			}
			if(rulesBounds.contains(touchPoint.x, touchPoint.y)) {
				Gdx.app.log("Notice","Rules touched.");
			}
		}
	}
	
	
	@Override
	public void render(float delta) {
		this.update(delta);
		this.draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		Gdx.app.log("Notice", "hide() called on mainMenuScreen.");
	}

	@Override
	public void pause() {
		Gdx.app.log("Notice", "pause() called on mainMenuScreen.");

	}

	@Override
	public void resume() {
		Gdx.app.log("Notice", "resume() called on mainMenuScreen.");
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
