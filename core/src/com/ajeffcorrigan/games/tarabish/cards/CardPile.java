package com.ajeffcorrigan.games.tarabish.cards;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CardPile {
	/** Owner of Card Pile. */
	private int ownerid;
	/** Starting axis location of pile. */
	private Vector2 pileXY = new Vector2();
	/** Pile direction; or zeros for stacked. */
	private Vector2 offsetXY = new Vector2(0,0);
	/** Should the pile be face up. */
	private boolean pileFaceUp = false;
	/** Cards in the pile. */
	public ArrayList<Card> cards = new ArrayList<Card>(52);
	/** Speed of card movement. */
	private float mspeed = 0.65f;
	/** Is the card in a movement status? */
	private boolean cardInMove = false;

	/**
	 * CardPile default constructor.
	 */
	public CardPile(){ }
	/**
	 * CardPile constructor.
	 * @param a boolean Is the cards to be face up?
	 */
	public CardPile(boolean a) {
		this.pileFaceUp = a;
	}
	/**
	 * CardPile constructor.
	 * @param x float X axis location of pile.
	 * @param y float Y axis location of pile.
	 */
	public CardPile(float x, float y) { this.pileXY.set(x, y); }
	/**
	 * CardPile constructor.
	 * @param a boolean Is the cards to be face up?
	 * @param x float X axis location of pile.
	 * @param y float Y axis location of pile.
	 */
	public CardPile(boolean a, float x, float y) {
		this.pileFaceUp = a;
		this.pileXY.set(x, y);
	}
	/**
	 * CardPile constructor.
	 * @param a boolean Is the cards to be face up?
	 * @param x float X axis location of pile.
	 * @param y float Y axis location of pile.
	 * @param i Owner ID number.
	 */
	public CardPile(boolean a, float x, float y, int i) {
		this.pileFaceUp = a;
		this.pileXY.set(x, y);
		this.ownerid = i;
	}	
	/**
	 * CardPile constructor.
	 * @param a Should pile be face up?
	 * @param x X axis location of pile.
	 * @param y Y axis location of pile.
	 * @param xo X offset value for pile.
	 * @param yo Y offset value for pile.
	 */
	public CardPile(boolean a, float x, float y, float xo, float yo) {
		this.pileFaceUp = a;
		this.pileXY.set(x, y);
		this.offsetXY.set(xo, yo);
	}
	/**
	 * CardPile constructor.
	 * @param a Should pile be face up?
	 * @param x X axis location of pile.
	 * @param y Y axis location of pile.
	 * @param xo X offset value for pile.
	 * @param yo Y offset value for pile.
	 * @param i Owner ID number.
	 */
	public CardPile(boolean a, float x, float y, float xo, float yo,int i) {
		this.pileFaceUp = a;
		this.pileXY.set(x, y);
		this.offsetXY.set(xo, yo);
		this.ownerid = i;
	}
	
	/**
	 * Add a card to the pile.
	 * @param c Card object to add to pile.
	 */
	public void addCard(Card c) {
		if(this.pileFaceUp != c.isCardFaceUp()) { c.flipCard(); }
		this.cards.add(c);
	}
	/**
	 * Add a card to the pile.
	 * @param c Card object to add to pile.
	 */
	public void addCard(Card c,boolean x) {
		if(x) {
			c.setCardXY(this.pileXY.x, this.pileXY.y);
		}
		this.addCard(c);
	}
	/**
	 * Add a card to the pile.
	 * @param c Card object to add to pile.
	 */
	public void addCard(Card c,boolean x,boolean z, float spin) {
		if(x) {
			c.setCardXY(this.pileXY.x, this.pileXY.y);
		}
		if(z) {
			this.offsetPile();
		}
		c.setOrientation(spin);
		this.addCard(c);
	}
	/**
	 * Remove a card from the pile.
	 * @param i Index number of the card to remove.
	 */
	public void removeCard(int i){ this.cards.remove(i); }
	/**
	 * Remove a card from pile.
	 * @param c Card object to remove.
	 */
	public void removeCard(Card c) {
		for(int i = 0; i < this.cards.size()-1; i++) {
			if(this.cards.get(i) == c) { 
				this.cards.remove(i);
				return;
			}
		}
	}
	
	/**
	 * Set the pile's x and y coordinates.
	 * @param x float X axis location.
	 * @param y float Y axis location.
	 */
	public void setPileXY(float x, float y) { this.pileXY.set(x, y); }
	/**
	 * Get the X/Y axis location of pile.
	 * @return Vector2f Pile location.
	 */
	public Vector2 getPileXY() { return this.pileXY; }
	
	/**
	 * Decides if the pile should be face up?
	 * @param a boolean Should cards be face up.
	 */
	public void setIsFaceUp(boolean a) { this.pileFaceUp = a; }
	/**
	 * Return value if cards are to be face up.
	 * @return boolean Should cards be face up.
	 */
	public boolean isFaceUp() { return this.isFaceUp(); }
	
	/**
	 * Set the offset x/y axis values.
	 * @param x float X axis offset.
	 * @param y float Y axis offset.
	 */
	public void setOffsetXY(float x, float y) { this.offsetXY.set(x, y); }
	/**
	 * Get the X/Y offset values.
	 * @return Vector2f Offset values.
	 */
	public Vector2 getOffsetXY() { return this.offsetXY; }
	
	/**
	 * Set the XY of hand cards w/ offset values.
	 */
	public void offsetPile() {
		for(int x = 0; x <= this.cards.size()-1; x++) {
			this.cards.get(x).setCardX(this.pileXY.x + (this.offsetXY.x * (x+1)));
			this.cards.get(x).setCardY(this.pileXY.y + (this.offsetXY.y * (x+1)));
		}
	}
	
	/**
	 * Draw the pile cards to the graphics object.
	 * @param g Graphics object.
	 */
	public void drawPile(SpriteBatch batch, CardDeck cd) {
		for(Card c : this.cards) {
			cd.drawCard(batch, c);
		}
	}
	/**
	 * Update the card state with in the pile.
	 * @param delta int timer.
	 */
	public void update(int delta) {
		for(Card c : cards) {
			if(c.updateMove()) {
				this.cardInMove = true;
				this.moveCard(delta, c);
			}
		}		
	}
	/**
	 * Gets the number of cards in the pile; starting at 0.
	 * @return int Number of cards.
	 */
	public int getSize() { return this.cards.size() - 1; }
	/**
	 * Draws the card to graphics batch.
	 * @param b
	 * @param c
	 */
	public void drawCard(SpriteBatch b, Card c, CardDeck cd) {
		cd.drawCard(b, c);
	}
	/**
	 * Gets the zero based integer of card in the pile.
	 * @param c
	 * @return
	 */
	public int getCardPlace(Card c) {
		for(int i = 0; i <= cards.size(); i++) {
			if(cards.get(i) == c) { return i; }
		}
		return -1;
	}
	/**
	 * Checks to see if a card is being moved / animation.
	 * @return Is card being moved?
	 */
	public boolean isCardMoving() { return this.cardInMove; }
	/**
	 * Recalculate the X,Y location for a card, based on their movement X,Y and delta.
	 */
	private void moveCard(int delta, Card c) {
		float dx = c.getCardX();
		float dy = c.getCardY();
		if(c.getMoveOnX()) {
			if(c.getMoveXY().x > c.getCardX()) { dx += mspeed * delta; } else { dx -= mspeed * delta; }
			c.setCardX(dx);
			if(inRange((int)dx,(int)c.getMoveXY().x)) { c.setMoveOnX(false); }
		}
		if(c.getMoveOnY()) {
			if(c.getMoveXY().y > c.getCardY()) { dy += mspeed * delta; } else { dy -= mspeed * delta; }
			c.setCardY(dy);
			if(inRange((int)dy,(int)c.getMoveXY().y)) { c.setMoveOnY(false); }
		}
		if(c.getSpin()) {
			float cd = c.getOrientation();
			cd += 10f;
			if(cd >= 360) { 
				cd = (cd - 360);
			}
			c.setOrientation(cd);
		}
		if(!c.updateMove()) { 
			this.cardInMove = false; 
			c.setSpin(false);
		}
	}
	
	private boolean inRange(int i, int j) {
		int k = 6;
		for(int x = 0; x < k; x++) {
			if(i+x == j) { return true; }
			if(i-x == j) { return true; }
		}
		return false;
	}	

	
}
