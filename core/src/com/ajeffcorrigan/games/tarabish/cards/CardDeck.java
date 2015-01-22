package com.ajeffcorrigan.games.tarabish.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class CardDeck {

	/** Height of one sprite sheet tile. */
	private int tileHeight;
	/** Width of one sprite sheet tile. */
	private int tileWidth;
	/** Scaling of the tile size. */
	private float scaleDeck = 1f;
	/** Start XY location for cards. */
	private Vector2 startXY = new Vector2();
	/** Offset for cards in hand. */
	private Vector2 offsetXY = new Vector2(0,0);
	
	/** Image object for card back. */
	public TextureRegion cardback;
	/** TextureRegion of the card faces. */
	public TextureRegion cardface;
	
	/** Initial storage area for all cards. Basically a new deck to deal from. */
	public ArrayList<Card> cardStack = new ArrayList<Card>(52);
	/** ArrayList for active cards which can be picked up. */
	public ArrayList<Card> activeCards = new ArrayList<Card>(52);
	/**
	 * CardDeck constructor.
	 * @param s SpriteSheet object of card faces.
	 * @param width Width of the tile.
	 * @param height Height of the tile.
	 * @param cb Card back image.
	 */
	public CardDeck(Texture t, int width, int height, int rows, int cols, Texture cb) { 
		this.tileHeight = height;
		this.tileWidth = width;
		this.cardback = new TextureRegion(cb);
		this.cardface = new TextureRegion(t);
		for(int i = 0; i < rows; i++){ 
			for(int j = 0; j < cols; j++) { 
				cardStack.add(new Card(j,i,j * this.tileWidth,i * this.tileHeight)); 
			} 
		}
	}
	/**
	 * Return the card image from an index value.
	 * @param c
	 * @return
	 */
	public TextureRegion drawCard(int c) {
		if(cardStack.get(c).isCardFaceUp()) {
			this.cardface.setRegion(this.cardStack.get(c).getTileX(), this.cardStack.get(c).getTileY(), this.tileWidth, this.tileHeight);
			return this.cardface;
		} else {
			return this.cardback;
		}
		
	}
	/**
	 * Return the card image from a card object.
	 * @param c
	 * @return
	 */
	public TextureRegion drawCard(Card c) {
		if (c.isCardFaceUp()) {
			this.cardface.setRegion(c.getTileX(), c.getTileY(), this.tileWidth, this.tileHeight);
			return this.cardface;
		} else {
			return this.cardback;
		}
		
	}
	
	public void printCardDeck() {
		for(Card c : cardStack) {
			System.out.println("Card Suit: "+ c.getSuitName() + ", Rank: "+ c.getRank());
		}
	}
	/**
	 * Shuffles the deck.
	 */
	public void shuffleCards(){
		Collections.shuffle(cardStack);
	}
	
	public void moveToActive(Card c) {
		activeCards.add(c);
	}
	public void removeFromActive(Card c) {
		for(int i = 0; i < 52; i++) {
			if(activeCards.get(i).equals(c)) {
				activeCards.remove(i);
				cardStack.add(c);
				return;
			}
		}
	}
	/**
	 * Gets the sprite's tile height.
	 * @return
	 */
	public int getTileHeight() { return this.tileHeight; }
	/**
	 * Gets the sprite's tile width.
	 * @return
	 */
	public int getTileWidth() { return this.tileWidth; }
	/**
	 * Sets the cards starting x,y coords.
	 * @param x
	 * @param y
	 */
	public void setStartXY(float x, float y) {
		this.startXY.set(x, y);
		this.setCardsXY();
	}
	/**
	 * Add a card back to the card stack, face down.
	 * @param c
	 */
	public void addToStack(Card c) {
		c.setCardX(this.startXY.x);
		c.setCardY(this.startXY.y);
		if(c.isCardFaceUp()) { c.flipCard(); }
		this.cardStack.add(c);
	}
	/**
	 * Initializes the cards XY.
	 */
	private void setCardsXY() {
		for(Card c : cardStack) {
			c.setCardX(this.startXY.x);
			c.setCardY(this.startXY.y);
		}
	}
	/**
	 * Draws the card to graphics batch.
	 * @param b
	 * @param c
	 */
	public void drawCard(SpriteBatch b, Card c) {
		b.draw(drawCard(c), c.getCardXY().x, c.getCardXY().y, 30, 40, getTileWidth(), getTileHeight(), c.getcScaleXY().x, c.getcScaleXY().y, c.getOrientation());
	}
}
