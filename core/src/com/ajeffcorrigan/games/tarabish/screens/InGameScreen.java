package com.ajeffcorrigan.games.tarabish.screens;

import java.util.ArrayList;

import com.ajeffcorrigan.games.tarabish.AssetManager;
import com.ajeffcorrigan.games.tarabish.cards.Card;
import com.ajeffcorrigan.games.tarabish.cards.CardDeck;
import com.ajeffcorrigan.games.tarabish.cards.CardPile;
import com.ajeffcorrigan.games.tarabish.cards.CardPlayer;
import com.ajeffcorrigan.games.tarabish.menu.InGameMenu;
import com.ajeffcorrigan.games.tarabish.tarabish;
import com.ajeffcorrigan.games.tarabish.TarabishCPUPlayer;
import com.ajeffcorrigan.games.tarabish.TarabishGameRules;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class InGameScreen implements Screen {

	//MyGame object container.
	private tarabish game;
	//CardDeck object.
	private CardDeck cd;
	//CardPlayer object.
	private ArrayList<CardPlayer> players = new ArrayList<CardPlayer>(4);
	//CardPile for the trick.
	private CardPile trick = new CardPile(true,200,115,10,4);
	//Tarabish Game object.
	private TarabishGameRules tgr = new TarabishGameRules();
	//InGame menu object.
	private InGameMenu imenu;
	//Active bounds for detect valid screen touches.
	private ArrayList<Rectangle> activebounds = new ArrayList<Rectangle>();
	//Touch point coordinates.
	private Vector3 touchPoint = new Vector3();
	//Game state.
	private boolean gamePaused = false;
	//CPU Tarabish player AI.
	private TarabishCPUPlayer cai;
	//Player to call trump.
	private int ptrump;
	//Offset for human cards.
	private final int pOffset = 50;
	//Card & bounds to confirm.
	private Card confirmc = null;
	private Rectangle confirmb = null;
	private boolean anyMovement = false;
	
	public InGameScreen(tarabish g) {
		this.game = g;
		//Initialize a new deck.
		cd = new CardDeck(AssetManager.getTexture("cards"),92,128,4,9,AssetManager.getTexture("cardback"));
		imenu = new InGameMenu(50,100,Gdx.graphics.getHeight() - 150,Gdx.graphics.getWidth() - 100);
		cai = new TarabishCPUPlayer();
	}
	
	public void update(float deltaTime) {
	    if(!tgr.needTrump()) {
		    if(trick.cards.size() < 4) {
		    	//Gdx.app.log(this.getClass().getSimpleName(), "Trick size: " + trick.cards.size());
				//If it's not the players turn process CPU card play.
			    if(!players.get(0).isTurn()) {
			    	for(CardPlayer cp : players) {
			    		if(cp.isTurn()) { 
			    			Card playcard = cai.playCard(cp, tgr, trick);
			    			if(playcard == null) {
			    				Gdx.app.debug(this.getClass().getSimpleName(), "Error on card to be played!");
			    			} else {
				    			this.addToTrick(playcard, cp);
				    			if(trick.cards.size() != 4) {
			    					int tc = (cp.getPID() - 1) - 1;
			    					Gdx.app.log(this.getClass().getSimpleName(), "Next player: " + tc);
				    				cp.setTurn(false);
				    				players.get(tc).setTurn(true);
			    				}		    				
			    				Gdx.app.log(this.getClass().getSimpleName(), "Card added to trick. Suit:" + trick.cards.get(trick.getSize()).getSuit() + " Rank:" + trick.cards.get(trick.getSize()).getRank());
		    					Gdx.app.log(this.getClass().getSimpleName(), "Current player: " + (cp.getPID()-1));
			    			}
		    				break;
			    		}
			    	}
			    }
			}
		}
		//Process trick if it's 4 cards in size.
		if(trick.cards.size() == 4) {
	    	int w = tgr.checkTrick(this.trick);
	    	int[] handorder = {tgr.getLastwinner(),-1,-1,-1};
	    	for(int i = 1; i <= 3; i++) {
	    		if(handorder[i - 1] == 0) {
	    			handorder[i] = 3;
	    		} else {
	    			handorder[i] = handorder[i - 1] - 1;
	    		}
	    	}
	    	if(w != -1) {
				//TODO: Process winning cards.
				//TODO: Move cards from trick to pile.
	    		Gdx.app.log(this.getClass().getSimpleName(), "Winning card: " + w);
	    		Gdx.app.log(this.getClass().getSimpleName(), "Winning player: " + handorder[w]);
	    		game.dispose();
	    	}
	    }
		//If player touches screen, process it.
	    if (Gdx.input.justTouched()) {
	    	if(tgr.needTrump()) {
	    		for(int i = 0; i <= activebounds.size()-1; i++) {
	    			game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),0));
	    			if(activebounds.get(i).contains(touchPoint.x, touchPoint.y)) {
	    				Gdx.app.log(this.getClass().getSimpleName(), "ActiveBounds:"+i);
	    				if(i == 4) { 
	    					ptrump -= 1;
	    					if(ptrump == -1) { ptrump = 3; }
	    				} else { 
		    				tgr.setTrump(i);
		    				tgr.setPickedTrump(0);
		    				this.activebounds.clear();
		    				this.imenu = null;
		    				pickUpKitty();
		    				setInGameBounds();
		    				break;
	    				}
	    			}
	    		}
	    	} else {
	    		game.camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(),0));
    			for(int i = 0; i <= 8; i++) {
    				if(this.confirmc != null) {
    					confirmb = new Rectangle(this.confirmc.getCardX(), this.confirmc.getCardY(), cd.getTileWidth(), cd.getTileHeight());
    					if(confirmb.contains(touchPoint.x, touchPoint.y)) {
        					Gdx.app.log(this.getClass().getSimpleName(), "Touched " + i);
    						this.addToTrick(confirmc, players.get(0));
        				    Gdx.app.log(this.getClass().getSimpleName(), "Card added to trick. Suit:" + trick.cards.get(trick.getSize()).getSuit() + " Rank:" + trick.cards.get(trick.getSize()).getRank());
        				    Gdx.app.log(this.getClass().getSimpleName(), "Number of cards in hand: " + players.get(0).getCards().size());
        				    if(trick.cards.size() != 4) {
        				    	players.get(0).setTurn(false);
        				    	players.get(3).setTurn(true);
        				    } 
        				    confirmc = null;
        				    confirmb = null;
    						break;
    					} else {
    						confirmc = null;
    						confirmb = null;
    						break;
    					}
    				}
    				if(this.activebounds.get(i).contains(touchPoint.x, touchPoint.y)) {
    					if(this.confirmc == null) {	
    						this.confirmc = players.get(0).getCards().get(i);
    						break;
    					}
    				}
    			}
	    		if(activebounds.get(9).contains(touchPoint.x, touchPoint.y)) {
	    			this.gamePaused = true;
	    			Gdx.app.log(this.getClass().getSimpleName(), "Game paused! Create pause menu!");
	    		}
	    	}    	
	    } 
	}
	
	public void draw(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    game.camera.update();
	    game.batch.begin();
	    game.batch.draw(AssetManager.getTexture("felt"), 0, 0, game.camera.viewportWidth, game.camera.viewportHeight);
	
      	//Draw the player's cards.
      	for(CardPlayer cp : players) {
      		cp.drawPlayerPile(0, game.batch);
      		cp.drawPlayerHand(game.batch);
      	}
      	
		//If there is a to be confirmed card, bring entire card to top of cards.
      	if(this.confirmc != null) {
      		cd.drawCard(game.batch, confirmc);
      	}
      	
      	trick.drawPile(game.batch, cd);
      	
	    //Draw the other player shadows.
	    //game.batch.draw(AssetManager.getTextureRegion("pshadow"), -(AssetManager.getTextureRegion("pshadow").getRegionWidth()/2), Gdx.graphics.getHeight()/2);
      	//game.batch.draw(AssetManager.getTextureRegion("pshadow"), Gdx.graphics.getWidth()-(AssetManager.getTextureRegion("pshadow").getRegionWidth()/2), Gdx.graphics.getHeight()/2);
      	//game.batch.draw(AssetManager.getTextureRegion("pshadow"), Gdx.graphics.getWidth()/2-(AssetManager.getTextureRegion("pshadow").getRegionWidth()/2), Gdx.graphics.getHeight()-(AssetManager.getTextureRegion("pshadow").getRegionHeight()));
      	
      	//Is trump needed?
      	if(tgr.needTrump()) {
      		if(ptrump == 0) {
          		//Draw pick trump menu for player.
          		imenu.draw(game.batch);
      		} else {
      			int dec;
      			do {
      				Gdx.app.log(this.getClass().getSimpleName(), "Picking trump: " + ptrump);
      				dec = cai.pickTrump(players.get(ptrump),this.tgr,false);
      				if(dec == -1) {
      					Gdx.app.log(this.getClass().getSimpleName(), ptrump + " passed on picking.");
      					ptrump -= 1;
      					if(ptrump == -1) { ptrump = 3; }
      				} 
      				if(ptrump == tgr.getDealer() && dec == -1) {
      					dec = cai.pickTrump(players.get(ptrump),this.tgr,true);
      				}
      			} while(dec == -1 && ptrump != 0);
      			if(dec != -1) {
      				Gdx.app.log(this.getClass().getSimpleName(), "Trump CPU select: " + dec);
      				tgr.setTrump(dec);
      				tgr.setPickedTrump(ptrump);
      				pickUpKitty();
      				setInGameBounds();
      			}
      		}
	    } else {
	    	game.batch.draw(AssetManager.getTextureRegion("box"), (Gdx.graphics.getWidth()-AssetManager.getTextureRegion("box").getRegionWidth())-10, (Gdx.graphics.getHeight()-AssetManager.getTextureRegion("box").getRegionHeight())-10);
	    	game.batch.draw(AssetManager.getTextureRegion(tgr.getTrumpName().toLowerCase()), (Gdx.graphics.getWidth()-AssetManager.getTextureRegion("box").getRegionWidth())-10, (Gdx.graphics.getHeight()-AssetManager.getTextureRegion("box").getRegionHeight())-10);
	    	game.batch.draw(AssetManager.getTextureRegion("box"), 10, (Gdx.graphics.getHeight()-AssetManager.getTextureRegion("box").getRegionHeight())-10);
	    	game.batch.draw(AssetManager.getTextureRegion("pause"), 10, (Gdx.graphics.getHeight()-AssetManager.getTextureRegion("box").getRegionHeight())-10);	
	    }
      	
	    game.batch.end();
	    // If there is a to be confirmed card, highlight the card.
      	if(this.confirmc != null) {
      		ShapeRenderer sr = new ShapeRenderer();
      		sr.begin(ShapeType.Line);
      		sr.setColor(Color.BLUE);
      		sr.rect(this.confirmc.getCardX(), this.confirmc.getCardY(), cd.getTileWidth(), cd.getTileHeight());
      		sr.end();
      	}
	}
	
	
	@Override
	public void render(float delta) {		
		this.anyMovement = checkForMoves();
		if(this.anyMovement) {
			for(CardPlayer cp : players) {
				cp.update((int)delta);
			}	
			trick.update((int)delta);
		} else {
			update(delta);
		}
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		players.add(new CardPlayer(-50,-30,50,0,true,cd));
		players.add(new CardPlayer(Gdx.graphics.getWidth() + (cd.getTileHeight()/2) + 25,90,0,10,false,cd));
		players.add(new CardPlayer((Gdx.graphics.getWidth() / 2),Gdx.graphics.getHeight() + (cd.getTileHeight() / 2) + 25,10,0,false,cd));
		players.add(new CardPlayer(-(cd.getTileHeight()/2) - 25,Gdx.graphics.getHeight()-135,0,10,false,cd));

		int co = 0;
		for(CardPlayer player : players) {
			player.addPile(false);
			player.setCardOrientation(co);
			co += 90;
		}
		players.get(0).getPile(0).setPileXY((Gdx.graphics.getWidth()/2), -20);
		players.get(1).getPile(0).setPileXY(Gdx.graphics.getWidth() + (cd.getTileHeight()/2) - 10, Gdx.graphics.getHeight() / 2);
		players.get(2).getPile(0).setPileXY((Gdx.graphics.getWidth() / 2) - 10, Gdx.graphics.getHeight() + (cd.getTileHeight() / 2) - 10);
		players.get(3).getPile(0).setPileXY(-(cd.getTileHeight()/2) + 10, (Gdx.graphics.getHeight() / 2) + 50);

		
		players.get(0).getPile(0).setOffsetXY(5, 0);
		
		//Determine who is the dealer.
		int r = (int)(Math.random() * ((3) + 1));
		players.get(r).setDealer(true);
		tgr.setDealer(r);
		Gdx.app.log(this.getClass().getSimpleName(), "Dealer: " + tgr.getDealer());
		//Assign player to pick trump.
		ptrump = tgr.getDealer() - 1;
		if(ptrump == -1) { ptrump = 3; }
		tgr.setLastwinner(ptrump);
		players.get(ptrump).setTurn(true);
		this.intialDeal();
	}

	@Override
	public void hide() {
		this.cd = null;
		this.players = null;
	}

	@Override
	public void pause() {
		this.gamePaused = true;
	}

	@Override
	public void resume() {
		this.gamePaused = false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Shuffles deck and deals the the players.
	 */
	private void intialDeal() {
		//Shuffle deck.
		cd.shuffleCards();
		tgr.newDeal(cd, players);
		trumpMenu();
	}
	private void trumpMenu() {
		//Construct the trump menu.
		imenu.addText("Please select a trump suit!", imenu.getMenuX() + 20, (imenu.getMenuY() + imenu.getHeight()) - 20);
		Vector2 boxes = new Vector2(imenu.getMenuX() + 30, ((imenu.getMenuY() + imenu.getHeight()) - AssetManager.getTextureRegion("box").getRegionHeight()) - 60);
		imenu.addEntity(AssetManager.getTextureRegion("box"),boxes.x, boxes.y);
		imenu.addEntity(AssetManager.getTextureRegion("club"),boxes.x, boxes.y);
		this.activebounds.add(new Rectangle(boxes.x,boxes.y,AssetManager.getTextureRegion("box").getRegionWidth(),AssetManager.getTextureRegion("box").getRegionHeight()));
		boxes.x += AssetManager.getTextureRegion("box").getRegionWidth() + 30;
		imenu.addEntity(AssetManager.getTextureRegion("box"),boxes.x, boxes.y);
		imenu.addEntity(AssetManager.getTextureRegion("diamond"),boxes.x, boxes.y);
		this.activebounds.add(new Rectangle(boxes.x,boxes.y,AssetManager.getTextureRegion("box").getRegionWidth(),AssetManager.getTextureRegion("box").getRegionHeight()));
		boxes.x += AssetManager.getTextureRegion("box").getRegionWidth() + 30;
		imenu.addEntity(AssetManager.getTextureRegion("box"),boxes.x, boxes.y);
		imenu.addEntity(AssetManager.getTextureRegion("heart"),boxes.x, boxes.y);
		this.activebounds.add(new Rectangle(boxes.x,boxes.y,AssetManager.getTextureRegion("box").getRegionWidth(),AssetManager.getTextureRegion("box").getRegionHeight()));
		boxes.x += AssetManager.getTextureRegion("box").getRegionWidth() + 30;
		imenu.addEntity(AssetManager.getTextureRegion("box"),boxes.x, boxes.y);
		imenu.addEntity(AssetManager.getTextureRegion("spade"),boxes.x, boxes.y);
		this.activebounds.add(new Rectangle(boxes.x,boxes.y,AssetManager.getTextureRegion("box").getRegionWidth(),AssetManager.getTextureRegion("box").getRegionHeight()));
		imenu.addEntity(AssetManager.getTextureRegion("pass"),Gdx.graphics.getWidth() - 150, 120);
		this.activebounds.add(new Rectangle(Gdx.graphics.getWidth() - 190, 130,AssetManager.getTextureRegion("pass").getRegionWidth(),AssetManager.getTextureRegion("pass").getRegionHeight()));
	}
	/**
	 * Sets the touch hotspots for current in hand cards.
	 */
	private void setActiveCards() {
		for(int i = 0; i <= 9; i++) {
			if(i < players.get(0).getCards().size()) {
				Card c = players.get(0).getCards().get(i);
				this.activebounds.add(new Rectangle(c.getCardX(),c.getCardY(),this.pOffset,cd.getTileHeight()));
			} else {
				this.activebounds.add(new Rectangle(0,0,0,0));
			}	
		}
	}
	/**
	 * Pickup the kitty pile once trump selected.
	 */
	private void pickUpKitty() {
		for(CardPlayer player : players) {
			player.movePileToHand(0);
		}
	}
	/**
	 * Sets the hotspots for player.
	 */
	private void setInGameBounds() {
		if(this.gamePaused) {
			
		} else {
			this.activebounds.clear();
			setActiveCards();
			this.activebounds.add(new Rectangle(10,(Gdx.graphics.getHeight()-AssetManager.getTextureRegion("box").getRegionHeight())-10,AssetManager.getTextureRegion("box").getRegionWidth(),AssetManager.getTextureRegion("box").getRegionHeight()));
		}
	}
	/**
	 * Add a card to the trick pile.
	 */
	private void addToTrick(Card c, CardPlayer p) {
		//TODO: Add Card to trick remove from player.
		trick.addCard(c);
		c.setMoveXY(200,115);
		c.setSpin(true);
		p.removeCard(c);
	}
	/**
	 * Check players to see if there are any animation moves in progress.
	 * @return
	 */
	private boolean checkForMoves() {
		boolean mt = false;
		for(CardPlayer cp : players) {
			if(cp.isCardMoving()) {
				mt = true;
				break;
			}
		}
		if(trick.isCardMoving()) {
			mt = true;
		}
		return mt;
	}
}
