package com.ajeffcorrigan.games.tarabish;

import java.util.ArrayList;

import com.ajeffcorrigan.games.tarabish.cards.Card;
import com.ajeffcorrigan.games.tarabish.cards.CardDeck;
import com.ajeffcorrigan.games.tarabish.cards.CardPile;
import com.ajeffcorrigan.games.tarabish.cards.CardPlayer;



public class TarabishGameRules {
	/** Trump for this hand. */
	private int trump;
	/** Does trump need to be selected. */
	private boolean needtrump = true;
	/** Number of deals. */
	private int deals = 0;
	/** Trump broken? */
	private boolean trumpbroke = false;
	/** Dealer. */
	private int dealer;
	/** ID of player who picked trump. */
	private int pickedTrump;
	/** ID of last person to win hand. */
	private int lastwinner;

	private int team1score = 0;
	private int team2score = 0;
	
	public final int[] team1 = {0,2};
	public final int[] team2 = {1,3}; 
	
	private final int[] trumpweight = {6,0,1,2,7,5,8,3,4};
	private final int[] ntrumpweight = {8,0,1,2,3,7,4,5,6};
	
	public TarabishGameRules() {
		
	}
	
	/**
	 * Checks to see if trump needs to be selected?
	 * @return boolean Need trump.
	 */
	public boolean needTrump() { return this.needtrump; }
	
	/**
	 * Sets the trump value, and disables needtrump value.
	 * 0 = clubs, 1 = diamonds, 2 = hearts, 3 = spades.
	 * @param i
	 */
	public void setTrump(int i) {
		this.trump = i;
		this.needtrump = false;
	}
	
	/**
	 * Gets the name of the current trump.
	 * @return
	 */
	public String getTrumpName() {
		switch(this.trump) {
		case 0:
			return "Club";
		case 1:
			return "Diamond";
		case 2:
			return "Heart";
		case 3:
			return "Spade";
		default:
			return "Invalid";
		}
	}
	/**
	 * Returns int version of trump. 0 = clubs, 1 = diamonds, 2 = hearts, 3 = spades.
	 * @return
	 */
	public int getTrump() { return this.trump; }
	
	public void newDeal(CardDeck cd, ArrayList<CardPlayer> players) {
		int dealer = -1;
		int nxtpl = -1;
		for(int i = 0; i <= players.size(); i++) {
			if(players.get(i).isDealer()) {
				dealer = i;
				nxtpl = dealer - 1;
				break;
			}
		}
		if(dealer == 0) { nxtpl = 3; }
		//Deal to main hand.
		do {
			for(int j = 1; j <= 3; j++) {
				players.get(nxtpl).addCard(cd.cardStack.get(cd.cardStack.size()-1));
				cd.cardStack.remove(cd.cardStack.size()-1);
			}
			nxtpl -= 1;
			if(nxtpl == -1) { nxtpl = 3; }
		} while (players.get(dealer).sizeofHand() != 5);
		//Deal to kitty.
		do {
			for(int j = 1; j <= 3; j++) {
				players.get(nxtpl).addCardToPile(0, cd.cardStack.get(cd.cardStack.size()-1));
				cd.cardStack.remove(cd.cardStack.size()-1);
			}
			nxtpl -= 1;
			if(nxtpl == -1) { nxtpl = 3; }
		} while (cd.cardStack.size() != 0);
	}

	public int getDeals() {
		return deals;
	}

	public void setDeals(int deals) {
		this.deals = deals;
	}

	public boolean isTrumpbroke() {
		return trumpbroke;
	}

	public void setTrumpbroke(boolean trumpbroke) {
		this.trumpbroke = trumpbroke;
	}

	public int getDealer() {
		return dealer;
	}

	public void setDealer(int dealer) {
		this.dealer = dealer;
	}

	public int getTeam1score() {
		return team1score;
	}

	public void setTeam1score(int team1score) {
		this.team1score = team1score;
	}

	public int getTeam2score() {
		return team2score;
	}

	public void setTeam2score(int team2score) {
		this.team2score = team2score;
	}

	/**
	 * @return the pickedTrump
	 */
	public int getPickedTrump() {
		return pickedTrump;
	}

	/**
	 * @param pickedTrump the pickedTrump to set
	 */
	public void setPickedTrump(int pickedTrump) {
		this.pickedTrump = pickedTrump;
	}

	/**
	 * @return The Weight Trump
	 * @param i Rank of card based on CardDeck object.
	 */
	public int getTrumpWeight(int i) {
		return trumpweight[i];
	}

	/**
	 * @return The Non-Trump weight.
	 */
	public int getNonTrumpWeight(int i) {
		return ntrumpweight[i];
	}
	
	public int checkTrick(CardPile trick) {
		boolean hastrump = false;
		Card leadcard = null;
		for(Card c : trick.cards) { if(c.getSuit() == this.trump) { hastrump = true; } }
		if(hastrump) {
			for(Card c : trick.cards) {
				if(c.getSuit() == this.trump) {
					if(leadcard == null){ leadcard = c; }
					if(getTrumpWeight(c.getRank()) > getTrumpWeight(leadcard.getRank())) {
						leadcard = c;
					}
				}
			}
		} else {
			leadcard = trick.cards.get(0);
			for(Card c : trick.cards) {
				if(getNonTrumpWeight(c.getRank()) > getNonTrumpWeight(leadcard.getRank()) && leadcard.getSuit() == c.getSuit()) {
					leadcard = c;
				}
			}
		}
		return trick.getCardPlace(leadcard);
	}

	/**
	 * @return the lastwinner
	 */
	public int getLastwinner() {
		return lastwinner;
	}

	/**
	 * @param lastwinner the lastwinner to set
	 */
	public void setLastwinner(int lastwinner) {
		this.lastwinner = lastwinner;
	}
	
	
	
}
