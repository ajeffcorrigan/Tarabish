package com.ajeffcorrigan.games.tarabish;

import com.ajeffcorrigan.games.tarabish.screens.*;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class tarabish extends Game {
	
	private Texture bgImage, gameTex, cardb, playersh, tarabishtitle;
	//Game controls.
	public OrthographicCamera camera;
	public SpriteBatch batch;
	
	private TextureRegion txr, txr2;
	
	//Game size.
	public static int GAMEHEIGHT;
	public static int GAMEWIDTH; 
	
	/** Game state based IDs */
	private static final int INGAME = 0;
	private static final int PAUSED = 1;
	private static final int MAINMENU = 2;
	private static final int OPTIONS = 3;
	private static final int SPLASH = 4;
	
	//Have the assets been loaded?
	public static boolean assetsInit = false;
	
	//Different screens for the game.
	public SplashScreen splashScreen;
	public MainMenuScreen mainMenuScreen;
	public InGameScreen inGameScreen;

	@Override
	public void create() {
		//Set the SpriteBatch object.
		this.batch = new SpriteBatch();

		//Set the camera.
		this.camera = new OrthographicCamera();
		this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		GAMEHEIGHT = Gdx.graphics.getHeight();
		GAMEWIDTH = Gdx.graphics.getWidth();
		
		//Initialize the SplashScreen.
		this.splashScreen = new SplashScreen(this);

		//Show the splashScreen.
		this.setScreen(splashScreen);		
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
		// TODO Auto-generated method stub

	}
}
