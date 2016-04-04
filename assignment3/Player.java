package assignment3;

import java.util.ArrayList;

public class Player {
	private ArrayList<Card> playersCards;
	private int winnings;
	private int currentWager;
	

	public void setCurrentWager(int wager){
		this.currentWager = wager;
	}
	public int getCurrentWager(){
		return currentWager;
	}
	public int getWinnings() {
		return winnings;
	}
	public void setWinnings(int winnings) {
		this.winnings = winnings;
	}
	
	//Constructor for Player sets the initial winnings
	public Player(int winnings) {
		super();
		
		this.winnings = winnings;
		this.playersCards = new ArrayList<>();
	}
	
	public void setPlayersCards(Card newCard){
		this.playersCards.add(newCard);
	}
	
	public void removePlayersCards(){
		this.playersCards.clear();
	}
	public ArrayList<Card> getPlayersCards(){
		return this.playersCards;
	}

	
	
}
