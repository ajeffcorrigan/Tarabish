package com.ajeffcorrigan.games.tarabish.cards;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CardPlayer {
	
	/** Player's ID */
	private int pid;
	/** Next ID for player. */
	private static int nextid = 1;
		
	/** Hand of cards held by player. */
	private ArrayList<Card> hand;
	/** Player's pile of cards, can be face down or up. */
	private ArrayList<CardPile> pile = new ArrayList<CardPile>();
	
	/** Is player the dealer? */
	private boolean isDealer = false;
	/** Is player human? */
	private boolean isHuman = false;
	/** Is it player's turn? */
	private boolean isTurn = false;
	
	/** Start XY location for cards. */
	private Vector2 handXY = new Vector2();
	/** Offset for cards in hand. */
	private Vector2 offsetXY = new Vector2(0,0);
	/** Base offset values for cards in hand. */
	private Vector2 baseOffsetXY = new Vector2(0,0);
	/** Speed of card movement. */
	private float mspeed = 0.65f;
	/** Is the card in a movement status? */
	private boolean cardInMove = false;
	/** Player's card orientation */
	private float cardOrientation = 0f;
	/** CardDeck object. */
	private static CardDeck cd;
	
	/**
	 * CardPlayer default constructor.
	 */
	public CardPlayer() {
		this.hand = new ArrayList<Card>();
		this.pid = nextid;
		nextid++;
	}
	/**
	 * CardPlayer constructor.
	 * @param nac
	 */
	public CardPlayer(float x, float y, float xo, float yo, CardDeck cd) {
		this.hand = new ArrayList<Card>();
		this.handXY.set(x, y);
		this.baseOffsetXY.set(xo, yo);
		this.pid = nextid;
		this.cd = cd;
		nextid++;
	}
	/**
	 * CardPlayer constructor.
	 * @param nac
	 */
	public CardPlayer(float x, float y, float xo, float yo, boolean h, CardDeck cd) {
		this.hand = new ArrayList<Card>();
		this.handXY.set(x, y);
		this.baseOffsetXY.set(xo, yo);
		this.isHuman = h;
		this.pid = nextid;
		this.cd = cd;
		nextid++;
	}
	/**
	 * CardPlayer constructor.
	 * @param nac
	 */
	public CardPlayer(boolean h, CardDeck cd) {
		this.hand = new ArrayList<Card>();
		this.isHuman = h;
		this.pid = nextid;
		this.cd = cd;
		nextid++;
	}
	
	/**
	 * @return the isDealer
	 */
	public boolean isDealer() {
		return isDealer;
	}
	/**
	 * @param isDealer the isDealer to set
	 */
	public void setDealer(boolean isDealer) {
		this.isDealer = isDealer;
	}
	
	/**
	 * Add a card to players hand.
	 * @param c
	 */
	public void addCard(Card c) {
		if(this.isHuman) { 
			if(!c.isCardFaceUp()) { c.flipCard(); }
			this.hand.add(c);
			this.offsetHand();
		} else {
			if(c.isCardFaceUp()) { c.flipCard(); }
			this.hand.add(c);
			this.offsetHand();
		}
	}
	
	/**
	 * Gets the current size of cards hands.
	 * @return Number of cards in hand.
	 */
	public int sizeofHand() { return this.hand.size() - 1; }
	/**
	 * Returns ArrayList of cards in hand.
	 * @return Hand of cards.
	 */
	public ArrayList<Card> getCards() { return this.hand; }
	/**
	 * Remove a card from the player's hand.
	 * @param c Card to remove from hand.
	 */
	public void removeCard(Card c) {
		for(int i = 0; i < this.hand.size(); i++) {
			if(this.hand.get(i) == c) {
				this.hand.remove(i);
				this.offsetHand();
				return;
			}
		}
	}
	/**
	 * Set the human flag to true for human based players.
	 */
	public void setHuman() { this.isHuman = true; }
	/**
	 * Checks value of isHuman attribute.
	 * @return boolean Is player a human controlled player?
	 */
	public boolean isHuman() { return this.isHuman; }
	/**
	 * Set the turn attribute.
	 * @param b Is it this player's turn?
	 */
	public void setTurn(boolean b) {this.isTurn = b; }
	/**
	 * Returns value of turn attribute.
	 * @return boolean Is it this player's turn?
	 */
	public boolean isTurn() { return this.isTurn; }
	/**
	 * Set the starting x and y axis coordinates.
	 */
	public void setHandXY(float x, float y) { this.handXY.set(x, y); }
	/**
	 * Set the offset x and y axis coordinates.
	 */
	public Vector2 getHandXY() { return this.handXY; }
	public Vector2 getOffsetXY() { return this.offsetXY; }
	public void setOffsetXY(float x, float y) { this.offsetXY.set(x, y); }
	/**
	 * Sets the base offset values.
	 * @param x
	 * @param y
	 */
	public void setBaseOffset(float x, float y) { 
		this.baseOffsetXY.set(x, y);
	}
	/**
	 * Get the base offset values.
	 * @return
	 */
	public Vector2 getBaseOffset() { return this.baseOffsetXY; }
	/**
	 * Set the XY of hand cards w/ offset values.
	 */
	private void offsetHand() {
		for(int x = 0; x <= this.hand.size()-1; x++) {
			if(this.baseOffsetXY.x != 0) {
				this.hand.get(x).setCardX(this.handXY.x + (this.baseOffsetXY.x * (x+1)));
			} else {
				this.hand.get(x).setCardX(this.handXY.x);
			}
			if(this.baseOffsetXY.y != 0) {
				this.hand.get(x).setCardY(this.handXY.y + (this.baseOffsetXY.y * (x+1)));
			} else {
				this.hand.get(x).setCardY(this.handXY.y);
			}
		}
	}
	/**
	 * Set the XY of hand cards w/ offset values.
	 */
	private void offsetPile(CardPile p) {
		for(int x = 0; x <= p.cards.size()-1; x++) {
			p.cards.get(x).setCardX(p.getPileXY().x + (p.getOffsetXY().x * (x+1)));
			p.cards.get(x).setCardY(p.getPileXY().y + (p.getOffsetXY().y * (x+1)));
		}
	}
	/**
	 * Create a new pile of cards for this player.
	 * @param face Should the cards be face up?
	 * @param x X axis location for pile.
	 * @param y Y axis location for pile.
	 */
	public void addPile(boolean face, float x, float y) {
		this.pile.add(new CardPile(face,x,y,this.baseOffsetXY.x,this.baseOffsetXY.y,this.pid));
	}
	/**
	 * Create a new pile of cards for this player.
	 * @param face Should the cards be face up?
	 */
	public void addPile(boolean face) {
		this.pile.add(new CardPile(face,this.handXY.x,this.handXY.y,this.baseOffsetXY.x,this.baseOffsetXY.y,this.pid));
	}
	/**
	 * Add a card to the player's pile.
	 * @param i Which pile does card need to be added to.
	 * @param c Card object to add.
	 */
	public void addCardToPile(int i, Card c) {
		this.pile.get(i).addCard(c);
		offsetPile(this.pile.get(i));
	}
	public void silentAddToPile(int i, Card c) {
		this.pile.get(i).addCard(c);
	}
	public CardPile getPile(int i) {
		return this.pile.get(i);
	}
	
	public void update(int delta) {
		for(Card c : this.hand) {
			if(c.updateMove()) {
				this.cardInMove = true;
				this.moveCard(delta, c);
			}
		}
		for(CardPile cp : this.pile) {
			for(Card c : cp.cards) {
				if(c.updateMove()) {
					this.cardInMove = true;
					this.moveCard(delta, c);
				}
			}
		}
	}
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
		if(!c.updateMove()) { this.cardInMove = false; }
	}
	
	private boolean inRange(int i, int j) {
		int k = 6;
		for(int x = 0; x < k; x++) {
			if(i+x == j) { return true; }
			if(i-x == j) { return true; }
		}
		return false;
	}
	
	/**
	 * Checks to see if a card is being moved / animation.
	 * @return Is card being moved?
	 */
	public boolean isCardMoving() { return this.cardInMove; }
	
	/**
	 * Moves all cards from a pile into the players active hand.
	 * @param i
	 */
	public void movePileToHand(int i) {
		for(Card c : this.pile.get(i).cards) {
			addCard(c);
		}
		this.pile.get(i).cards.clear();
	}
	/**
	 * Get the PID of player.
	 * @return
	 */
	public int getPID() {
		return this.pid;
	}
	/**
	 * Set the card orientation for drawing. Default is 0, no rotation.
	 * @param cardOrientation
	 */
	public void setCardOrientation(float cardOrientation) {
		this.cardOrientation = cardOrientation;
	}

	/**
	 * Draw the pile cards to the graphics object.
	 * @param g Graphics object.
	 */
	public void drawPlayerHand(SpriteBatch batch) {
		for(Card c : this.hand) {
			drawCard(batch,c);
		}
	}
	/**
	 * Prepares a specific card pile for drawing.
	 * @param i
	 * @param batch
	 */
	public void drawPlayerPile(int i, SpriteBatch batch) {
		for(Card c : this.pile.get(i).cards) {
			drawCard(batch,c);
		}
	}
	/**
	 * Draws the card to graphics batch.
	 * @param b
	 * @param c
	 */
	private void drawCard(SpriteBatch b, Card c) {
		b.draw(cd.drawCard(c), c.getCardXY().x, c.getCardXY().y, 0, 0, cd.getTileWidth(), cd.getTileHeight(), c.getcScaleXY().x, c.getcScaleXY().y, this.cardOrientation);
	}
	
	/**
	 * Gets a specific card based on it's number from the hand.
	 * @param i Zero based number of card in hand.
	 * @return Card object.
	 */
	public Card getInHandCard(int i) {
		return this.hand.get(i);
	}
	/**
	 * Get the suit value of card from the in hand card.
	 * @param i Zero based number of card in hand.
	 * @return int suit value.
	 */
	public int getInHandCardSuit(int i) {
		return this.hand.get(i).getSuit();
	}
	/**
	 * Get the internal card rank from an in hand card.
	 * @param i Zero based number of card in hand.
	 * @return
	 */
	public int getInHandCardRank(int i) {
		return this.hand.get(i).getRank();
	}
	
}
