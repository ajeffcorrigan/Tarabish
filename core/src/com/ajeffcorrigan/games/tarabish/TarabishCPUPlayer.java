package com.ajeffcorrigan.games.tarabish;

import java.util.ArrayList;
import java.util.List;

import com.ajeffcorrigan.games.tarabish.cards.Card;
import com.ajeffcorrigan.games.tarabish.cards.CardPile;
import com.ajeffcorrigan.games.tarabish.cards.CardPlayer;
import com.badlogic.gdx.Gdx;

public class TarabishCPUPlayer {
	
	private TarabishGameRules tgr;
	
	public TarabishCPUPlayer() {

	}

	public int pickTrump(CardPlayer cp, TarabishGameRules tgr, boolean force) {
		int trump = -1;
		int brain[] = { 0, 0, 0, 0 };
		int topthree[] = { 0, 0, 0, 0 };

		// Place initial weight on suit.
		for (Card c : cp.getCards()) {
			brain[c.getSuit()] += 1;
		}
		// Place weight based on suit and rank.
		for (Card c : cp.getCards()) {
			if (brain[c.getSuit()] == 1) {
				switch (c.getRank()) {
				case 6:
				case 4:
					brain[c.getSuit()] += 2;
					topthree[c.getSuit()] += 1;
					break;
				case 0:
					brain[c.getSuit()] += 1;
					topthree[c.getSuit()] += 1;
					break;
				}
			} else if (brain[c.getSuit()] == 2) {
				switch (c.getRank()) {
				case 6:
				case 4:
					brain[c.getSuit()] += 3;
					break;
				case 0:
					brain[c.getSuit()] += 2;
					break;
				case 5:
					brain[c.getSuit()] += 1;
					break;
				}
			} else if (brain[c.getSuit()] == 3) {
				switch (c.getRank()) {
				case 6:
				case 4:
				case 0:
					brain[c.getSuit()] += 3;
					break;
				case 5:
					brain[c.getSuit()] += 2;
					break;
				case 1:
				case 2:
				case 3:
				case 7:
				case 8:
					brain[c.getSuit()] += 1;
					break;
				}
			} else if (brain[c.getSuit()] >= 4) {
				switch (c.getRank()) {
				case 6:
				case 4:
				case 0:
					brain[c.getSuit()] += 4;
					break;
				case 5:
					brain[c.getSuit()] += 3;
					break;
				case 7:
				case 8:
					brain[c.getSuit()] += 2;
				case 1:
				case 2:
				case 3:
					brain[c.getSuit()] += 1;
					break;
				}
			}
		}
		// Check for bella.
		int bells = checkForBella(cp);
		if (bells != -1) {
			brain[bells] += 6;
		}
		// Check for a fifty run.
		int fifty = checkForFifty(cp);
		if (fifty != -1) {
			brain[fifty] += 8;
		}
		// Check for twenty run.
		int twenty = checkForTwenty(cp);
		if (twenty != -1) {
			brain[twenty] += 4;
		}

		int bestweight = 0;
		for (int j = 0; j < brain.length; j++) {
			for (int i = 0; i < brain.length; i++) {
				if (brain[i] >= 20) {
					return i;
				}
				if (brain[i] >= 15) {
					if (trump != i) {
						if (brain[i] > bestweight) {
							trump = i;
							bestweight = brain[i];
							break;
						}
					}
				}
				if (brain[i] > bestweight) {
					if (trump != i) {
						trump = i;
						bestweight = brain[i];
						break;
					}
				}
			}
		}
		Gdx.app.log(this.getClass().getSimpleName(), "Best weight: " + bestweight);
		int tempcount = 0;
		for (int i = 0; i < brain.length; i++) {
			if (i != trump) {
				if (bestweight - brain[i] >= 3) {
					tempcount += 1;
				}
			}
		}
		if (tempcount == 3) { return trump; }
		
		tempcount = 0;
		for (int i = 0; i < brain.length; i++) {
			if (i != trump) {
				if (bestweight - brain[i] > 1) {
					tempcount += 1;
				}
			}
		}
		
		if (tempcount >= 2 && topthree[trump] > 1) { return trump; }
		
		if (topthree[trump] == 3) { return trump; }
		for (int i = 0; i < brain.length; i++) {
			Gdx.app.log(this.getClass().getSimpleName(), "Suit:" + i
					+ " Weight:" + brain[i]);
		}
		if(force) {
			return trump;
		} else {
			return -1;
		}
	}

	public Card playCard(CardPlayer cp, TarabishGameRules tgr, CardPile trick) {
		Card playc = null;
		this.tgr = tgr;
		if(trick.cards.size() == 0) {
			int highestnt = -1;
			for(int j = 0; j < cp.getCards().size(); j++) {
				if(cp.getCards().get(j).getSuit() != tgr.getTrump()) {
					if(highestnt == -1) { 
						highestnt = j; 
					} else {
						if(tgr.getNonTrumpWeight(cp.getInHandCardRank(j)) > tgr.getNonTrumpWeight(cp.getInHandCardRank(highestnt))) {
							highestnt = j;
						}
					}
				}
			}
			if(highestnt == -1) {
				for(int j = 0; j < cp.getCards().size(); j++) {
					if(cp.getCards().get(j).getSuit() != tgr.getTrump()) {
						if(highestnt == -1) { 
							highestnt = j; 
						} else {
							if(tgr.getTrumpWeight(cp.getInHandCardRank(j)) > tgr.getTrumpWeight(cp.getInHandCardRank(highestnt))) {
								highestnt = j;
							}
						}
					}
				}
			}
			return cp.getInHandCard(highestnt);
		} else if(trick.cards.size() == 1) {
			List<Card> beatablec = new ArrayList<Card>();
			int tsuit = trick.cards.get(0).getSuit();
			if(tsuit == tgr.getTrump()){
				int trank = tgr.getTrumpWeight(trick.cards.get(0).getRank());
			} else {
				int trank = tgr.getNonTrumpWeight(trick.cards.get(0).getRank());
				for(Card c : cp.getCards()) {
					if(c.getSuit() == tsuit && tgr.getNonTrumpWeight(c.getRank()) > trank) {
						beatablec.add(c);
					}
				}
				if(beatablec.size() > 0) {
					int topranks = 0;
					for(Card c : beatablec) {
						switch(tgr.getNonTrumpWeight(c.getRank())) {
						case 8:
						case 7:
							topranks += 1;
							break;
						}
					}
					if(topranks == 2) {
						for(Card c : beatablec) {
							if(tgr.getNonTrumpWeight(c.getRank()) == 7) {
								playc = c;
							}
						}
					} else {
						playc = beatablec.get(0);
						for(Card c : beatablec) {
							if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
								playc = c;
							}
						}
					}
				} else {
					for(Card c : cp.getCards()) {
						if(c.getSuit() == tsuit) {
							playc = c;
						}
					}
					if(playc == null) {
						for(Card c : cp.getCards()) {
							if(c.getSuit() == tgr.getTrump()) {
								beatablec.add(c);
							}
						}
						if(beatablec.size() > 0) {
							tgr.setTrumpbroke(true);
							for(Card c : beatablec) {
								if(playc == null) {
									playc = c;
								} else {
									if(tgr.getTrumpWeight(playc.getRank()) > this.tgr.getTrumpWeight(c.getRank())) {
										playc = c;
									}
								}
							}
						} else {
							for(Card c : cp.getCards()) {
								if(playc == null) { playc = c; } else {
									if(this.tgr.getNonTrumpWeight(playc.getRank()) > this.tgr.getNonTrumpWeight(c.getRank())) {
										playc = c;
									}
								}
							}
						}
					}
				}
			}
			return playc;
		} else if(trick.cards.size() == 2) {
			List<Card> beatablec = new ArrayList<Card>();
			Card teamc = trick.cards.get(0);
			Card tobeat = this.trickCardToBeat(trick);
			boolean teamw;
			if(teamc == tobeat) {
				teamw = true;
			} else {
				teamw = false;
			}
			int tsuit = tobeat.getSuit();
			if(teamw) {
				//Process if team is winning with a trump card.
				if(teamc.getSuit() == tgr.getTrump()){
					for(Card c : cp.getCards()) {
						if(c.getSuit() == tgr.getTrump() && tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(teamc.getRank())){
							beatablec.add(c);
						}
					}
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(playc == null) { playc = c; }
							if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
								playc = c;
							}
						}
					} else {
						for(Card c : cp.getCards()) {
							if(playc == null && c.getSuit() == tgr.getTrump()) { playc = c; }
						}
						if(playc == null) {
							if(tgr.getTrumpWeight(teamc.getRank()) >= 5) {
								for(Card c : cp.getCards()) {
									if(playc == null) { playc = c; }
									if(c.getSuit() != tgr.getTrump()) {
										if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									} else {
										if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}
							}	
						}
					}
				} else {
					//Teammate is winning with non trump card.
					//Get cards the same suit as winning card.
					for(Card c : cp.getCards()) {
						if(c.getSuit() == tobeat.getSuit()) { beatablec.add(c); }
					}
					//If don't have any same non trump suits; check for available trump cards.
					if(beatablec.size() == 0) {
						for(Card c : cp.getCards()) {
							if(c.getSuit() == tgr.getTrump()) { beatablec.add(c); }
						}
					}
					int topnt = 0;
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(c.getSuit() != tgr.getTrump()) {
								if(tgr.getNonTrumpWeight(c.getRank()) >= 6) { topnt++; }
							}
						}
					}
					//Process decision based on how good winning card is.
					if(tgr.getNonTrumpWeight(tobeat.getRank()) <= 6 && tgr.getNonTrumpWeight(tobeat.getRank()) >= 3) {
						if(beatablec.size() > 0) {
							if(topnt > 1) {
								if(beatablec.size() < 4) {
									for(Card c : beatablec) {
										if(tgr.getNonTrumpWeight(c.getRank()) >= 6) {
											playc = c;
										}
									}
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) < 6) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 4 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}
							}
						}
					} else if(tgr.getNonTrumpWeight(tobeat.getRank()) > 6) {
						if(beatablec.size() > 0) {
							if(topnt >= 1) {
								for(Card c : beatablec) {
									if(playc == null) { playc = c; }
									if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) <= 5) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 3 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}								
							}
						}
					} else if(tgr.getNonTrumpWeight(tobeat.getRank()) <= 2) {
						if(beatablec.size() > 0) {
							if(topnt >= 1) {
								for(Card c : beatablec) {
									if(playc == null) { playc = c; }
									if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) <= 6) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 4 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}								
							}
						}	
					}
					if(beatablec.size() == 0) {
						for(Card c : cp.getCards()) {
							if(playc == null) { playc = c; }
							if(tgr.getNonTrumpWeight(tobeat.getRank()) >= 5) {
								if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
							} else {
								if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
							}
						}
					}
					
				}
			} else {
				//TODO: Play to win since team mates cards isn't taking trick so far.
				if(tobeat.getSuit() == tgr.getTrump()) {
					for(Card c : cp.getCards()) {
						if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(tobeat.getRank())) {
							beatablec.add(c);
						}
					}
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(tobeat.getRank()) && tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(tobeat.getRank()) + 3) {
								if(playc == null) { playc = c; }
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
						if(playc == null) {
							for(Card c : beatablec) {
								if(playc == null) { playc = c;}
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
					} else {
						for(Card c : cp.getCards()) {
							if(c.getSuit() == tgr.getTrump()) {
								if(playc == null) { playc = c; }
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
						if(playc == null) {
							for(Card c : cp.getCards()) {
								if(trick.cards.get(0).getSuit() != tgr.getTrump()) {
									if(c.getSuit() == trick.cards.get(0).getSuit()) {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								} else {
									if(playc == null) { playc = c;}
									if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
									}
								}

							}
						}
					}
				}
			}
			return playc;
		} else if(trick.cards.size() == 3) {
			List<Card> beatablec = new ArrayList<Card>();
			Card teamc = trick.cards.get(1);
			Card tobeat = this.trickCardToBeat(trick);
			boolean teamw;
			if(teamc == tobeat) {
				teamw = true;
			} else {
				teamw = false;
			}
			int tsuit = tobeat.getSuit();
			if(teamw) {
				//Process if team is winning with a trump card.
				if(teamc.getSuit() == tgr.getTrump()){
					for(Card c : cp.getCards()) {
						if(c.getSuit() == tgr.getTrump() && tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(teamc.getRank())){
							beatablec.add(c);
						}
					}
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(playc == null) { playc = c; }
							if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
								playc = c;
							}
						}
					} else {
						for(Card c : cp.getCards()) {
							if(playc == null && c.getSuit() == tgr.getTrump()) { playc = c; }
						}
						if(playc == null) {
							if(tgr.getTrumpWeight(teamc.getRank()) >= 5) {
								for(Card c : cp.getCards()) {
									if(playc == null) { playc = c; }
									if(c.getSuit() != tgr.getTrump()) {
										if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									} else {
										if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}
							}	
						}
					}
				} else {
					//Teammate is winning with non trump card.
					//Get cards the same suit as winning card.
					for(Card c : cp.getCards()) {
						if(c.getSuit() == tobeat.getSuit()) { beatablec.add(c); }
					}
					//If don't have any same non trump suits; check for available trump cards.
					if(beatablec.size() == 0) {
						for(Card c : cp.getCards()) {
							if(c.getSuit() == tgr.getTrump()) { beatablec.add(c); }
						}
					}
					int topnt = 0;
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(c.getSuit() != tgr.getTrump()) {
								if(tgr.getNonTrumpWeight(c.getRank()) >= 6) { topnt++; }
							}
						}
					}
					//Process decision based on how good winning card is.
					if(tgr.getNonTrumpWeight(tobeat.getRank()) <= 6 && tgr.getNonTrumpWeight(tobeat.getRank()) >= 3) {
						if(beatablec.size() > 0) {
							if(topnt > 1) {
								if(beatablec.size() < 4) {
									for(Card c : beatablec) {
										if(tgr.getNonTrumpWeight(c.getRank()) >= 6) {
											playc = c;
										}
									}
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) < 6) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 4 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}
							}
						}
					} else if(tgr.getNonTrumpWeight(tobeat.getRank()) > 6) {
						if(beatablec.size() > 0) {
							if(topnt >= 1) {
								for(Card c : beatablec) {
									if(playc == null) { playc = c; }
									if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) <= 5) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 3 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}								
							}
						}
					} else if(tgr.getNonTrumpWeight(tobeat.getRank()) <= 2) {
						if(beatablec.size() > 0) {
							if(topnt >= 1) {
								for(Card c : beatablec) {
									if(playc == null) { playc = c; }
									if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
								}
							} else {
								for(Card c : beatablec) {
									if(c.getSuit() == tgr.getTrump()) {
										if(playc == null) { playc = c; }
										if(tgr.getTrumpWeight(c.getRank()) <= 6) {
											if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) { playc = c; }
										}
									} else {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) > 4 && tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								}								
							}
						}	
					}
					if(beatablec.size() == 0) {
						for(Card c : cp.getCards()) {
							if(playc == null) { playc = c; }
							if(tgr.getNonTrumpWeight(tobeat.getRank()) >= 5) {
								if(tgr.getNonTrumpWeight(c.getRank()) > tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
							} else {
								if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) { playc = c; }
							}
						}
					}
					
				}
			} else {
				//TODO: Play to win since team mates cards isn't taking trick so far.
				if(tobeat.getSuit() == tgr.getTrump()) {
					for(Card c : cp.getCards()) {
						if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(tobeat.getRank())) {
							beatablec.add(c);
						}
					}
					if(beatablec.size() > 0) {
						for(Card c : beatablec) {
							if(tgr.getTrumpWeight(c.getRank()) > tgr.getTrumpWeight(tobeat.getRank()) && tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(tobeat.getRank()) + 3) {
								if(playc == null) { playc = c; }
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
						if(playc == null) {
							for(Card c : beatablec) {
								if(playc == null) { playc = c;}
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
					} else {
						for(Card c : cp.getCards()) {
							if(c.getSuit() == tgr.getTrump()) {
								if(playc == null) { playc = c; }
								if(tgr.getTrumpWeight(c.getRank()) < tgr.getTrumpWeight(playc.getRank())) {
									playc = c;
								}
							}
						}
						if(playc == null) {
							for(Card c : cp.getCards()) {
								if(trick.cards.get(0).getSuit() != tgr.getTrump()) {
									if(c.getSuit() == trick.cards.get(0).getSuit()) {
										if(playc == null) { playc = c; }
										if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
										}
									}
								} else {
									if(playc == null) { playc = c;}
									if(tgr.getNonTrumpWeight(c.getRank()) < tgr.getNonTrumpWeight(playc.getRank())) {
											playc = c;
									}
								}

							}
						}
					}
				}
			}
		}
		return playc;
	}
	
	private boolean isValidMove(Card c, CardPlayer cp, CardPile trick) {
		return true;
	}
	/**
	 * If the trick is larger than 1 card; this function decides the card which needs to be beaten.
	 * @param trick
	 * @return
	 */
	private Card trickCardToBeat(CardPile trick) {
		Card tobeat = null;
		if(trick.cards.get(0).getSuit() == this.tgr.getTrump()) {
			for(Card c : trick.cards) {
				if(tobeat == null) { tobeat = c; } else {
					if(this.tgr.getTrumpWeight(tobeat.getRank()) < this.tgr.getTrumpWeight(c.getRank())) {
						tobeat = c;
					}
				}
			}
		} else {
			tobeat = trick.cards.get(0);
			for(Card c : trick.cards) {
				if(this.tgr.getNonTrumpWeight(tobeat.getRank()) < this.tgr.getNonTrumpWeight(c.getRank()) && c.getSuit() == trick.cards.get(0).getSuit()) {
					tobeat = c;
				} else if(c.getSuit() == this.tgr.getTrump()) {
					if(tobeat.getSuit() != this.tgr.getTrump()) {
						tobeat = c;
					} else {
						if(this.tgr.getTrumpWeight(tobeat.getRank()) < this.tgr.getTrumpWeight(c.getRank())) {
							tobeat = c;
						}
					}
				}
			}
		}
		return tobeat;
	}
	
	private int checkForTwenty(CardPlayer cp) {
		boolean possible = false;
		int run = 0;
		Card tempc;
		for (Card c : cp.getCards()) {
			if (c.getRank() <= 5) {
				tempc = c;
				possible = true;
				do {
					for (Card tc : cp.getCards()) {
						if (tc.getRank() - 1 == tempc.getRank()
								&& tc.getSuit() == tempc.getSuit()) {
							tempc = tc;
							run += 1;
							possible = true;
							break;
						} else {
							possible = false;
						}
					}
				} while (possible == true && run < 3);
				if (run == 3) {
					return tempc.getSuit();
				}
			}
		}
		return -1;
	}

	private int checkForFifty(CardPlayer cp) {
		boolean possible = false;
		int run = 0;
		Card tempc;
		for (Card c : cp.getCards()) {
			if (c.getRank() <= 5) {
				tempc = c;
				possible = true;
				do {
					for (Card tc : cp.getCards()) {
						if (tc.getRank() - 1 == tempc.getRank()
								&& tc.getSuit() == tempc.getSuit()) {
							tempc = tc;
							run += 1;
							possible = true;
							break;
						} else {
							possible = false;
						}
					}
				} while (possible == true && run < 4);
				if (run == 4) {
					return tempc.getSuit();
				}
			}
		}
		return -1;

	}

	private int checkForBella(CardPlayer cp) {
		for (Card c : cp.getCards()) {
			if (c.getRank() == 7 || c.getRank() == 8) {
				for (Card tc : cp.getCards()) {
					if (c.getRank() == 7) {
						if (tc.getRank() == 8 && tc.getSuit() == c.getSuit()) {
							return c.getSuit();
						}
					} else {
						if (tc.getRank() == 7 && tc.getSuit() == c.getSuit()) {
							return c.getSuit();
						}
					}
				}
			}
		}
		return -1;
	}

}
