package assignment3;

public class Game {
	private Player player;
	private Dealer dealer;
	private Deck deck;
	
	
	
	//Method to start the game, initialize the objects, and call the private methods.
	public void startGame(){
		
				player = new Player(100);//Initialize a player object with an initial cash value of 100.
				deck = new Deck();//Initializing a deck object creates 52 cards and adds them to an ArayList<Card>
				dealer = new Dealer(deck.getShuffledDeck());//Create a dealer object and pass in the deck of 52 cards, SHUFFLED.
				dealer.playGame(player);//Call to the dealer to begin a game with the given player.
		
	}
}
