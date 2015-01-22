package com.ajeffcorrigan.games.tarabish.cards;

import com.badlogic.gdx.math.Vector2;

public class Card {
	
	/** Is the card face up or down. */
	private boolean faceup = false;
	/** Rank of card. 0=A, 10=J, 11=Q, 12=K */
	private int rank;
	/** Suit of card. 0=C, 1=D, 2=H, 3=S */
	private int suit;
	/** Is card a red card. */
	private boolean isRed;
	
	/** Vector location of the card. */
	private Vector2 cardXY;
	/** Vector location on sprite sheet for card. */
	private Vector2 tileXY;
	
	/** Determines if card is being held, mouse over, in hand, etc. */
	private boolean cardSel = false;
	/** Play weight. */
	private float playWeight = 0;
	
	/** Controls card movement state. */
	private boolean moveOnX = false;
	private boolean moveOnY = false;
	
	/** Destination location for card */
	private Vector2 moveXY = new Vector2();
	
	/** Spin card when animation? */
	private boolean spinCard = false;
	
	/** Scale of card. */
	private Vector2 cScaleXY = new Vector2(1,1);
	
	/** Card orientation. */
	private float orientation = 0f;

	/**
	 * Card constructor.
	 * @param rank Rank value of card.
	 * @param suit Suit value of the card.
	 * @param j Tile x axis location on sprite sheet.
	 * @param k Tile y axis location on sprite sheet.
	 */
	public Card(int rank, int suit, int tilex, int tiley) {
		this.rank = rank;
		this.suit = suit;
		this.tileXY = new Vector2(tilex,tiley);
		this.cardXY = new Vector2();
		if(this.suit == 1 || this.suit == 2) {
			this.isRed = true;
		} else {
			this.isRed = false;
		}
	}
	/**
	 * Card constructor.
	 * @param i Rank value of card.
	 * @param s Suit value of the card.
	 * @param j Tile x axis location on sprite sheet.
	 * @param k Tile y axis location on sprite sheet.
	 */
	public Card(int i, int s) {
		this.rank = i;
		this.suit = s;
		this.cardXY = new Vector2();
		if(this.suit == 1 || this.suit == 2) {
			this.isRed = true;
		} else {
			this.isRed = false;
		}
	}
	/**
	 * Card constructor.
	 * @param i Rank value of card.
	 * @param s Suit value of the card.
	 * @param j Tile x axis location on sprite sheet.
	 * @param k Tile y axis location on sprite sheet.
	 * @param x Card x axis location.
	 * @param y Card y axis location.
	 */
	public Card(int i, int s, int j, int k, float x, float y) {
		this.rank = i;
		this.suit = s;
		this.tileXY = new Vector2(j,k);
		this.cardXY = new Vector2(x, y);
		if(this.suit == 1 || this.suit == 2) {
			this.isRed = true;
		} else {
			this.isRed = false;
		}
	}
	
	/**
	 * Get x axis location of card.
	 * @return
	 */
	public float getCardX() { return this.cardXY.x; }
	/**
	 * Set the x axis location of the card.
	 * @param x
	 */
	public void setCardX(float x) { this.cardXY.x = x; }
	/**
	 * Get the y axis location of the card.
	 * @return
	 */
	public float getCardY() { return this.cardXY.y; }
	/**
	 * Set the y axis location of the card.
	 * @param y
	 */
	public void setCardY(float y) { this.cardXY.y = y; }
	/**
	 * Returns if the card is face up?
	 * @return
	 */
	public boolean isCardFaceUp() { return this.faceup; }
	/**
	 * Flip the card.
	 */
	public void flipCard() {
		if(this.faceup) {
			this.faceup = false;
		} else {
			this.faceup = true;
		}
	}
	/**
	 * Return the rank of the card.
	 * @return rank 0=A,6=J,7=Q,8=K
	 */
	public int getRank() { return this.rank; }
	/**
	 * Return the suit of card. 0=C, 1=D, 2=H, 3=S
	 * @return
	 */
	public int getSuit() { return this.suit; }
	/**
	 * Returns if the card is a red card.
	 * @return
	 */
	public boolean isCardRed() { return this.isRed; }
	/**
	 * Return the x axis location of the card on the sprite sheet.
	 * @return
	 */
	public int getTileX() { return (int)this.tileXY.x; }
	/**
	 * Return the y axis location of the card on the sprite sheet.
	 * @return
	 */
	public int getTileY() { return (int)this.tileXY.y;  }
	/**
	 * Gets the name of the suit.
	 * @return
	 */
	public String getSuitName() {
		switch(this.suit) {
			case 0:
				return "Clubs";
			case 1:
				return "Diamonds";
			case 2:
				return "Hearts";
			case 3:
				return "Spades";
			default:
				return "Invalid";
		}
	}
	/**
	 * Picks up the card to enable the card state.
	 */
	public void selCard() { this.cardSel = true; }
	/**
	 * Drops the card from a selected card state.
	 */
	public void dropCard() { this.cardSel = false; }
	/**
	 * Gets the current state of the card.
	 * @return
	 */
	public boolean getCardStat() { return this.cardSel; }
	
	/**
	 * Sets the card playable card weight; usually between 0 - 100.
	 */
	public void setPlayWeight(float f) { this.playWeight = f; }
	/**
	 * Returns the current play weight for the card.
	 */
	public float getPlayWeight() { return this.playWeight; }
	
	
	/**
	 * Return the card's XY axis locations.
	 * @return Vector2f Card locations.
	 */
	public Vector2 getCardXY() { return this.cardXY; }
	/**
	 * Set the card's XY axis locations.
	 * @param x float X axis location.
	 * @param y float Y axis location.
	 */
	public void setCardXY(float x, float y) { this.cardXY.set(x, y); }
	/**
	 * Set's the destination location for card.
	 * @param x float X axis destination location.
	 * @param y float Y axis destination location.
	 */
	public void setMoveXY(float x, float y) { 
		this.moveXY.set(x, y);
		if(this.moveXY.x != this.cardXY.x) { this.moveOnX = true; }
		if(this.moveXY.y != this.cardXY.y) { this.moveOnY = true; }
	}
	/**
	 * Gets the destination XY location for card.
	 * @return Vector2f XY location for card to move to.
	 */
	public Vector2 getMoveXY() { return this.moveXY; }
	/**
	 * Quick check if any move updates are needed.
	 * @return boolean Does card need update?
	 */
	public boolean updateMove() {
		if(this.moveOnX || this.moveOnY) { return true; } else { return false; }
	}
	/**
	 * Gets the Move on X axis value.
	 * @return boolean Does card need X axis movement.
	 */
	public boolean getMoveOnX() { return this.moveOnX; }
	/**
	 * Sets the Move on X axis value.
	 * @param b boolean Sets the move on X axis value.
	 */
	public void setMoveOnX(boolean b) { this.moveOnX = b; }
	/**
	 * Gets the Move on Y axis value.
	 * @return boolean Does card need Y axis movement.
	 */
	public boolean getMoveOnY() { return this.moveOnY; }
	/**
	 * Sets the Move on Y axis value.
	 * @param b boolean Sets the move on Y axis value.
	 */
	public void setMoveOnY(boolean b) { this.moveOnY = b; }
	/**
	 * Get the scaling for the card.
	 * @return
	 */
	public Vector2 getcScaleXY() {
		return cScaleXY;
	}
	/**
	 * Set the scaling of the card.
	 * @param cScaleXY
	 */
	public void setcScaleXY(Vector2 cScaleXY) {
		this.cScaleXY = cScaleXY;
	}
	/**
	 * @return the orientation
	 */
	public float getOrientation() {
		return orientation;
	}
	/**
	 * @param orientation the orientation to set
	 */
	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}
	/**
	 * @param x Should card spin when animated?
	 */
	public void setSpin(boolean x) { this.spinCard = x; }
	/**
	 * @return Should the card spin in animation?
	 */
	public boolean getSpin() { return this.spinCard; }
}