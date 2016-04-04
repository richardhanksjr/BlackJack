package assignment3;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
/*This class functions as the dealer in a game of BlackJack.  The dealer has a player and a deck of cards from which to play.
 * playGame() is called from the Game class,which instantiates the player, dealer and the deck of cards.  The logic of the game 
 * can be roughly followed inside of playGame().  However, when it made more sense to finish out the logic in another method(called from 
 * playGame(), that strategy was taken.
 */
public class Dealer {
	//Dealer has a Deck from which to perform all tasks.
	private ArrayList<Card> deck;
	//private Hand dealerHand;
	private ArrayList<Card> dealersCards;
	//private ArrayList<Card> playersCards;
	private Player player;
	private Scanner scanner;
	//Keep track of the number of aces each time the number of aces is checked below.
	//NEEDS TO BE RESET AT THE END OF EACH HAND.  CURRENTLY BEING DONE IN askForHit(ArrayList<Card> card) (ie, single parameter version).
	private int oldAces = 0;
	private int dealerOldAces = 0;  //Also keep track of dealer aces.
	
	//Grab a new deck of shuffled cards from the constructor.
	public Dealer(ArrayList<Card> deck){
		this.deck = deck;
		this.dealersCards = new ArrayList<>();
		//this.playersCards = new ArrayList<>();
		this.scanner = new Scanner(System.in);
	}
	
	//This is the first call for getting the dealer to start his role and, subsequently, the player's.
	public void playGame(Player player){
		this.player = player; //Update the field	
		messageToPlayer("Let's play.");
		askForWager();
		dealCards();//Deal two cards to the player and two to the dealer.  Call to print out cards which graphically displays cards.
		displayCards("Your hand: ", player.getPlayersCards().get(0), player.getPlayersCards().get(1));//Display the player's initial cards
		displayCards("Dealer's card: ", dealersCards.get(0));//Display the dealer's initial card
		checkForBlackJack();//Check if the player has 21 and, if so, dealerPlay() and declareWinner().
		checkForSplit();//
		playerPlay();
	}

	//This is the main method for starting the play of the player.
	private void playerPlay(){
		ArrayList<Card> playerHand = new ArrayList<>();//create a new arraylist for keeping track of the players cards
		playerHand = askForHit(player.getPlayersCards(), true);//First run through the 3 option menu that has double down as an option
		//FROM HERE DOWN IS ONLY RUN IF THE PLAYER CHOOSES TO HIT FROM THE PREVIOUS askForHit.  IF HE CHOOSES TO STAND OR DOUBLE DOWN
		//GAMEPLAY ENDS HERE AND IS SENT TO THE DEALER (dealerPlay()) TO FINISH OUT BECAUSE NO MORE CARDS ARE NEEDED FOR THE PLAYER.
		playerHand = askForHit(playerHand);//Take the value from the time through the first menu. 
		dealerPlay(totalCards(playerHand));//If the player hasn't busted, send the players total to dealerPlay to run through the dealer's algorithm and return a winner.	
	}
	
	//Checks to see if the two cards are the same and, if so, asks if the player wants to split.
	private void checkForSplit(){
		if (player.getPlayersCards().get(0).equals(player.getPlayersCards().get(1))){
				char splitResponse = getYorNAnswer("Would you like to split?");
				if(splitResponse == 'Y'){
					messageToPlayer("Ok, that'll be " + player.getCurrentWager() + " dollars.");
					if(player.getCurrentWager() > player.getWinnings()){//If they don't have enough money to cover another bet, alert them and move on without splitting.
						messageToPlayer("Sorry, you don't have enough money to cover that!  You will not be able to split.");
						messageToPlayer("You have " + player.getWinnings() + " dollars left.");
					}else{
						player.setWinnings(player.getWinnings() - player.getCurrentWager());//Subtract the most recent wager from his total winnings
						player.setCurrentWager(player.getCurrentWager() *2);//If the player splits, he needs to double the amount of money he bid.
						messageToPlayer("You have " + player.getWinnings() + " dollars left.");
						dealPlayerTwoCards();//Deal player two more cards, one for index zero and one for index 1
						//Because the player will now be playing two hands, the hands are split into two and the rest of the game play on each will happen inside this method.
						ArrayList<Card> firstHand = new ArrayList<>();
						ArrayList<Card> secondHand = new ArrayList<>();
						//First hand is the first card of the initial array plus the first most recently added card.
						firstHand.add(player.getPlayersCards().get(0)); 
						firstHand.add(player.getPlayersCards().get(2));
						//Second hand is the other two cards.
						secondHand.add(player.getPlayersCards().get(1));
						secondHand.add(player.getPlayersCards().get(3));
						//The indexes from now on refer to the indexes in their NEW ArrayLists
						displayCards("First hand is: ", firstHand.get(0), firstHand.get(1));
						displayCards("Second hand is: ", secondHand.get(0), secondHand.get(1));
						//Onward with the game
						displayCards("OK, let's play the first hand: ", firstHand.get(0), firstHand.get(1));
						firstHand = askForHit(firstHand, "split");//pass the string "split" to call for a special case in askForHit
						displayCards("OK, now let's play the second hand: ", secondHand.get(0), secondHand.get(1));
						secondHand = askForHit(secondHand, "split");
						messageToPlayer("Dealer's turn.");
						dealerPlay(totalCards(firstHand), totalCards(secondHand));	
				}
			}else{
				messageToPlayer("OK, moving on.");
			}
		}
	}
	
	//The main method for play for the dealer.
	public void dealerPlay(int firstTotal){
		int dealerTotal = totalCards(dealersCards);
		displayCards("Dealer's cards: ", dealersCards.get(0), dealersCards.get(1));
		//Dealer plays his turn until he reaches 17.
		while(dealerTotal<17){
			
				messageToPlayer("Dealer has " + dealerTotal +". Dealing card: ");
				dealDealerOneCard();//Give the dealer one more card
				displayCards(dealersCards.get(dealersCards.size()-1));//Display that card to the screen
				dealerTotal = totalCards(dealersCards);
				messageToPlayer("Dealer has " + dealerTotal +".");
		}
		if(dealerTotal>21){
			messageToPlayer("Dealer busts! ");
		}else{
			messageToPlayer("Dealer stands at " + dealerTotal + ".");
		}
		//Determine the winner of game 1
		messageToPlayer("Results: ");
		if(dealerTotal>21 && firstTotal>21){//Both bust
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>21){//Dealer is over 21 but player isn't
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()*2);//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}else if(firstTotal>21){//Player busts
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		else if(dealerTotal== firstTotal){//No one busts, but there is a tie.
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>firstTotal){//Dealer wins
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.	
		}else if(dealerTotal<firstTotal) {//Player wins
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()*2);//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		
		//Game is over, see if player wants to play again.
		char playAgain = getYorNAnswer("Play again? Y/N");
		if(playAgain == 'Y'){
			player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
			player.removePlayersCards();//Clear out the players cards to start again.
			dealersCards.clear();//Clear out the dealers cards to start again.
			
			playGame(player);
		}else{
			messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
			
			//TODO The cards aren't being loaded properly on restart
			System.exit(0);//Close game.
		}
	}
	
	//Play the dealer's turn.  Takes in the total cards from the player after their turn, runs through the dealer's algorithm, and compares the results.
	//This is the overloaded method for comparing two totals from the player(As would be used in a split).
	public void dealerPlay(int firstTotal, int secondTotal){
		int dealerTotal = totalCards(dealersCards);
		displayCards("Dealer's cards: ", dealersCards.get(0), dealersCards.get(1));
		//Dealer plays his turn until he reaches 17.
		while(dealerTotal<17){
				messageToPlayer("Dealer has " + dealerTotal +". Dealing card: ");
				dealDealerOneCard();//Give the dealer one more card
				displayCards(dealersCards.get(dealersCards.size()-1));//Display that card to the screen
				dealerTotal = totalCards(dealersCards);
				messageToPlayer("Dealer has " + dealerTotal +".");
				if(dealerTotal>21){//As soon as the dealer is over 21 reset aces to 1.
					int ace = containsAce(dealersCards);//If the are over 21, check that they have an ace.  Ace holds the NUMBER of aces they have.
					if(ace == 0 || ace == dealerOldAces){ 
						//Do nothing; Dealer busts because they are over 21 and don't have any new aces.  They will exit this loop
						//because the total is over 17, and the rest of the logic should hold for comparing values.
						}else if (ace>0 && ace> dealerOldAces){//They do have ace(s) and there are more than there were the last time we checked.
								dealersCards.add(new Card(Suit.SPADES, 0, -10));//This card doesn't display, but is added to the deck to update the value of an ace from 11 to 1.)
								dealerOldAces++;//Increment the number of old aces for every new one we find.	
						messageToPlayer("Hard total: " + totalCards(dealersCards));
						}
				}		
		}
		if(dealerTotal>21){
			messageToPlayer("Dealer busts! ");
		}else{
			messageToPlayer("Dealer stands at " + dealerTotal + ".");
		}
		//Determine the winner of game 1
		messageToPlayer("First game results: ");
		if(dealerTotal>21 && firstTotal>21){//Both bust
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()/2);//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>21){//Dealer is over 21 but player isn't
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}else if(firstTotal>21){//Player busts
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		else if(dealerTotal== firstTotal){//No one busts, but there is a tie.
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()/2);//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>firstTotal){//Dealer wins
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.	
		}else if(dealerTotal<firstTotal) {//Player wins
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		//Determine a winner for the second game
		messageToPlayer("Second game results: ");
		if(dealerTotal>21 && secondTotal>21){//Both bust
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()/2);//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>21){//Dealer is over 21 but player isn't
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}else if(secondTotal>21){//Player busts
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		else if(dealerTotal== secondTotal){
			messageToPlayer("Push!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager()/2);//Give the player back his money.  Because this method is dealing with a split bet, only have of the total wager for this game is returned per hand.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");
		}else if(dealerTotal>secondTotal){
			messageToPlayer("You lose.");
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
			
		}else if(dealerTotal<secondTotal){
			messageToPlayer("You win!");
			player.setWinnings(player.getWinnings() + player.getCurrentWager());//The player gets his initial bet*2 back.  Because there is already twice as much bet as usual, just add the currentWager instead of x2.
			messageToPlayer("You have " + player.getWinnings() + " dollars left.");//Player doesn't get any money back or lose money after a loss.
		}
		//Game is over, see if player wants to play again.
		if(player.getWinnings() > 0){
			char playAgain = getYorNAnswer("Play again? Y/N");
			if(playAgain == 'Y'){
				player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
				player.removePlayersCards();//Clear out the players cards to start again.
				dealersCards.clear();//Clear out the dealers cards to start again.
				playGame(player);
			}else{
				messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
				System.exit(0);//Close game.
			}
		}else{
			messageToPlayer("I'm sorry, but you don't have enough money.");
				messageToPlayer("Game OVER!");
				System.exit(0);
			
		}
	}
	private void dealDealerOneCard(){
		if(deck.size()>1){
			dealersCards.add(deck.get(0));
			deck.removeAll(dealersCards);
			
		}else{
			Deck newDeck = new Deck();
			deck = newDeck.getShuffledDeck();
			dealersCards.add(deck.get(0));
			deck.removeAll(dealersCards);
		}
	}
	private ArrayList<Card> askForHit(ArrayList<Card> cards, boolean bust){
		ArrayList<Card> newCards = new ArrayList<>();
		int playerNextMove = 0;
		
			playerNextMove = hitMenu(bust);
			if(playerNextMove == 1){//Player chooses to stand.
				messageToPlayer("Player stands at " + totalCards(cards));
				dealerPlay(totalCards(cards));//If the player stands, we send the play off to the dealerPlay to finish and never return the ArrayList
			}else if(playerNextMove == 2){//Player chooses to hit.
				messageToPlayer("New card: ");
				dealPlayerOneCard();//Gets one card from the deck and adds it to the players cards(original array)
				//cards.add(player.getPlayersCards().get((player.getPlayersCards().size())-1));//Grab the last card in the players original array list(which is the one just added) and add it to the new array.
				displayCards(cards.get(cards.size()-1));
				messageToPlayer("Total of all cards: " + totalCards(cards));//Help the player by showing the updated total each time a new card is added.
				//This is the primary place to check if a player has gone over 21 (bust).
				if(totalCards(cards) > 21){
					int ace = containsAce(cards);//If the are over 21, check that they have an ace.  Ace holds the NUMBER of aces they have.
					if(ace == 0 || ace == oldAces){//They 
						messageToPlayer("Bust! Game Over.");
						if(player.getWinnings() > 0){//Check that that player has enough money to make another bet before prompting.
							char playAgain = getYorNAnswer("Play again? Y/N");
							if(playAgain == 'Y'){
								player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
								player.removePlayersCards();//Clear out the players cards to start again.
								dealersCards.clear();//Clear out the dealers cards to start again.
								playGame(player);
							}else{
								messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
								System.exit(0);//Close game.
							}
						}else{
							messageToPlayer("You are out of money. Game OVER!");
							System.exit(0);
						}
					}else if (ace>0 && ace> oldAces){//They do have ace(s) and there are more than there were the last time we checked.
						//For the number of aces they have.
							cards.add(new Card(Suit.SPADES, 0, -10));//This card doesn't display, but is added to the deck to update the value of an ace from 11 to 1.)
							oldAces++;//Increment the number of old aces for every new one we find.
					messageToPlayer("Hard total: " + totalCards(cards));
					}else{//They don't have any aces and their gameplay is over.
						if(player.getWinnings() > 0){//Check that that player has enough money to make another bet before prompting.
							char playAgain = getYorNAnswer("Play again? Y/N");
							if(playAgain == 'Y'){
								player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
								player.removePlayersCards();//Clear out the players cards to start again.
								dealersCards.clear();//Clear out the dealers cards to start again.
								
								playGame(player);
							}else{
								messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
								System.exit(0);//Close game.
							}
						}else{
							messageToPlayer("I'm sorry, but you don't have enough money.");
							messageToPlayer("Game OVER!");
							System.exit(0);
							
						}
						
					}
				}
				
			}else{//Player chooses to double down.
				messageToPlayer("Player chooses to double down.  That'll be " + player.getCurrentWager() + " dollars.");
				if(player.getWinnings()<player.getCurrentWager()){
					messageToPlayer("I'm sorry, you don't have enough money left to double down.");
				}else{
				player.setCurrentWager(player.getCurrentWager() *2); //If the player doubles down, that doubles the amount of his current wager.
				messageToPlayer("New card: ");
				dealPlayerOneCard();//Gets one card from the deck and adds it to the players cards(original array)
				//cards.add(player.getPlayersCards().get((player.getPlayersCards().size())-1));//Grab the last card in the players original array list(which is the one just added) and add it to the new array.
				displayCards(cards.get(cards.size()-1));
				messageToPlayer("Total of all cards: " + totalCards(cards));//Help the player by showing the updated total each time a new card is added.
				//This is the primary place to check if a player has gone over 21 (bust).
				if(totalCards(cards) > 21){
					int ace = containsAce(cards);//If the are over 21, check that they have an ace.  Ace holds the NUMBER of aces they have.
					if(ace == 0 || ace == oldAces){//They 
						messageToPlayer("Bust! Game Over.");
					}else if (ace>0 && ace> oldAces){//They do have ace(s) and there are more than there were the last time we checked.
						for (int i = 0; i<ace; i++){//For the number of aces they have.
							cards.add(new Card(Suit.SPADES, 0, -10));//This card doesn't display, but is added to the deck to update the value of an ace from 11 to 1.)
							oldAces++;//Increment the number of old aces for every new one we find.
						}
					messageToPlayer("Hard total: " + totalCards(cards));
					}
					
				}
				dealerPlay(totalCards(cards));//If the player selected double down, they only get the one card, so submit the total at this point to dealerPlay for the remainder of the game
				//and skip the return statement altogether.  
				}
			}
//		}while(playerNextMove != 1);//run through the menu once and then continue until they select stand (option 1)
		newCards = cards;
		return newCards;
	}
	//This plays the players moves until they stand, bust, or quit and then returns back the updated array of cards. 
	//This is an overloaded method that is used after the first decision(ie, there is no option to double down).
	private ArrayList<Card> askForHit(ArrayList<Card> cards, String split){
		ArrayList<Card> newCards = new ArrayList<>();
		int playerNextMove = 0;
		do{ 
			playerNextMove = hitMenu();
			if(playerNextMove == 1){//Player chooses to stand.
				messageToPlayer("Player stands at " + totalCards(cards));
			}else{
				messageToPlayer("New card: ");
				dealPlayerOneCard();//Gets one card from the deck and adds it to the players cards(original array)
				if(split == "split"){//This checks to see if the ArrayList used came from a split, in which case the ArrayList is different than playersCards.
					cards.add(player.getPlayersCards().get((player.getPlayersCards().size())-1));//Grab the last card in the players original array list(which is the one just added) and add it to the new array.
				}
				displayCards(cards.get(cards.size()-1));
				messageToPlayer("Total of all cards: " + totalCards(cards));//Help the player by showing the updated total each time a new card is added.
				//This is the primary place to check if a player has gone over 21 (bust).
				if(totalCards(cards) > 21){
					int ace = containsAce(cards);//If the are over 21, check that they have an ace.  Ace holds the NUMBER of aces they have.
					if(ace == 0 || ace == oldAces){//They 
						messageToPlayer("Bust! Game Over.");
						playerNextMove = 1;//Set to one so we exit the do/while loop.
					}else if (ace>0 && ace> oldAces){//They do have ace(s) and there are more than there were the last time we checked.
							cards.add(new Card(Suit.SPADES, 0, -10));//This card doesn't display, but is added to the deck to update the value of an ace from 11 to 1.)
							oldAces++;//Increment the number of old aces for every new one we find.
							messageToPlayer("Hard total: " + totalCards(cards));
					}
				}	
			}
		}while(playerNextMove != 1);//run through the menu once and then continue until they select stand (option 1)
		newCards = cards;
		oldAces = 0;//Since the player has now stood, their hand is over.  Set old aces back to 0 for the next hand.
		return newCards;
	}
	//Overloaded method that just takes an ArrayList<Card>. Useful when the ArrayList is the same as playersCards.
	private ArrayList<Card> askForHit(ArrayList<Card> cards){
		ArrayList<Card> newCards = new ArrayList<>();
		int playerNextMove = 0;
		do{ 
			playerNextMove = hitMenu();
			if(playerNextMove == 1){//Player chooses to stand.
				messageToPlayer("Player stands at " + totalCards(cards));
			}else{
				messageToPlayer("New card: ");
				dealPlayerOneCard();//Gets one card from the deck and adds it to the players cards(original array)
				displayCards(cards.get(cards.size()-1));
				messageToPlayer("Total of all cards: " + totalCards(cards));//Help the player by showing the updated total each time a new card is added.
				//This is the primary place to check if a player has gone over 21 (bust).
				if(totalCards(cards) > 21){
					int ace = containsAce(cards);//If the are over 21, check that they have an ace.  Ace holds the NUMBER of aces they have.
					if(ace == 0 || ace == oldAces){//They 
						messageToPlayer("Bust! Game Over.");
						playerNextMove = 1;//Set to one so we exit the do/while loop.
						if(player.getWinnings() > 0){//Check that that player has enough money to make another bet before prompting.
							char playAgain = getYorNAnswer("Play again? Y/N");
							if(playAgain == 'Y'){
								player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
								player.removePlayersCards();//Clear out the players cards to start again.
								dealersCards.clear();//Clear out the dealers cards to start again.
								playGame(player);
							}else{
								messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
								System.exit(0);//Close game.
							}
						}
					}else if (ace>0 && ace> oldAces){//They do have ace(s) and there are more than there were the last time we checked
							cards.add(new Card(Suit.SPADES, 0, -10));//This card doesn't display, but is added to the deck to update the value of an ace from 11 to 1.)
							oldAces++;//Increment the number of old aces for every new one we find.
					messageToPlayer("Hard total: " + totalCards(cards));
					}else{//They are over 21 and they don't have an ace. They bust.
						if(player.getWinnings() > 0){//Check that that player has enough money to make another bet before prompting.
							char playAgain = getYorNAnswer("Play again? Y/N");
							if(playAgain == 'Y'){
								player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
								player.removePlayersCards();//Clear out the players cards to start again.
								dealersCards.clear();//Clear out the dealers cards to start again.
								playGame(player);
							}else{
								messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
								System.exit(0);//Close game.
							}
						}else{
							messageToPlayer("I'm sorry, but you don't have enough money.");
							
								messageToPlayer("Game OVER!");
								System.exit(0);
							
						}
					}
				}
				
			}
		}while(playerNextMove != 1);//run through the menu once and then continue until they select stand (option 1)
		newCards = cards;
		oldAces = 0;//Since the player has now stood, their hand is over.  Set old aces back to 0 for the next hand.
		return newCards;
	}
	
	
	//Checks to see if there is an ace by looping through the array and comparing each value to 11 (the default value of an ace).
	private int containsAce(ArrayList<Card> cards){
		int ace = 0;
		for (Card card : cards) {
			if (card.getCardValue() == 11){
				ace += 1;
			}
		}
		return ace;
	}
	//Computes the total of all cards in a hand and returns the anser.
	private int totalCards(ArrayList<Card> cards){
		int cardTotal = 0;
		for (Card card : cards) {
			cardTotal += card.getCardValue();
		}
		return cardTotal;
	}
	
	//Provides a menu from which the player can select their next game move.
	//This is an overloaded method that passes a boolean true as a paramenter and adds the option to double down.
		private int hitMenu(boolean bust){
			int playerChoice = 0;
			boolean loop = true;
			while(loop){//continue until a proper integer is entered.
				messageToPlayer("Make a move:\n(1)Stand\n(2)Hit\n(3)Double Down");
				try{
					playerChoice = scanner.nextInt();
					//If a correct number has been entered, turn loop to false and exit the while block.
					if (playerChoice == 1 || playerChoice == 2 || playerChoice == 3){
						loop = false;
					}
				}catch(InputMismatchException e){
					System.out.println("I'm sorry, please reenter a 1, 2, or 3");
					scanner.next();//Clear out the invalid input.
				}
			}
			return playerChoice;
			
		}
	//Provides a menu from which the player can select their next game move.
	private int hitMenu(){
		int playerChoice = 0;
		boolean loop = true;
		while(loop){//continue until a proper integer is entered.
			messageToPlayer("Make a move:\n(1) Stand\n(2)Hit");
			try{
				playerChoice = scanner.nextInt();
				//If a correct number has been entered, turn loop to false and exit the while block.
				if (playerChoice == 1 || playerChoice == 2){
					loop = false;
				}
			}catch(InputMismatchException e){
				System.out.println("I'm sorry, please reenter a 1 or 2");
				scanner.next();//Clear out the invalid input.
			}
		}
		return playerChoice;
		
	}
	
	private void dealPlayerOneCard(){
		if(deck.size()>1){
			player.setPlayersCards(deck.get(0));
			deck.removeAll(player.getPlayersCards());
			
		}else{
			Deck newDeck = new Deck();
			deck = newDeck.getShuffledDeck();
			player.setPlayersCards(deck.get(0));
			deck.removeAll(player.getPlayersCards());
		}
	}
	
	//Get the next two cards from the deck and add them to players cards.  This is called when a player splits their first hand.
	private void dealPlayerTwoCards(){
		if(deck.size()>1){
			player.setPlayersCards(deck.get(0));
			player.setPlayersCards(deck.get(1));
			deck.removeAll(player.getPlayersCards());
			
			
		}else{
			Deck newDeck = new Deck();
			deck = newDeck.getShuffledDeck();
			player.setPlayersCards(deck.get(0));
			player.setPlayersCards(deck.get(1));
			deck.removeAll(player.getPlayersCards());
		}
	}
	//Gets and validates a response to a question requiring Y/N answers.
	private char getYorNAnswer(String message){
			System.out.println(message);//Ask the question
			scanner.nextLine();
			String answer = scanner.nextLine();//Get the answer
			char charAnswer = answer.toUpperCase().charAt(0);//Convert to uppercase to ignore case and get the char at index 0
			while(charAnswer != 'Y' && charAnswer != 'N'){//Ask until a proper answer is given.
				System.out.println("I'm sorry, was that a yes or a no?");
				//scanner.nextLine();
				answer = scanner.nextLine();
				charAnswer = answer.toUpperCase().charAt(0);
			}
			if (charAnswer == 'Y'){
				charAnswer = 'Y';
			}else{
				charAnswer = 'N';
			}
			return charAnswer;	
	}
	//Checks to see if the player has reached a blackjack on his/her first turn.
	private void checkForBlackJack(){
		//Get the values of each card and addthem together.  If they equal 21, player has a blackjack.
		if(player.getPlayersCards().get(0).getCardValue() + player.getPlayersCards().get(1).getCardValue() == 21){
			messageToPlayer("BLACKJACK!! ");
			//If player has blackjack, check to see if the card the dealer is showing is an ace or a "10" card.  If not, player wins.
			if(dealersCards.get(0).getCardValue() != 14 && dealersCards.get(0).getCardValue() != 10){//Player wins because dealer cannot have a blackjack
				messageToPlayer("You win! Blackjack pays 3:2.");
				int payout = player.getCurrentWager() + (int)(1.5*player.getCurrentWager()+.5);//Calculate the payout amount based on a 3:2 payout.
				player.setWinnings(player.getWinnings() + payout);//pass the payout to the player to add to the total winnings.
				player.setCurrentWager(0);//Reset the player's current wager until the next hand.
			}else{//Dealer is showing either a 10 value or an ace, so turn over his other card and see if it's a blackjack
				messageToPlayer("Checking dealer's hand for BlackJack");
				dealDealerOneCard();//Give the dealer one more card
				displayCards(dealersCards.get(dealersCards.size()-1));//Display that card to the screen
				if(totalCards(dealersCards) == 21){
					messageToPlayer("Push!");
				}else{//Either the dealer has busted or he is short.  Either way, player wins.
					messageToPlayer("You win! Blackjack pays 3:2.");
					int payout = player.getCurrentWager() + (int)(1.5*player.getCurrentWager()+.5);//Calculate the payout amount based on a 3:2 payout.
					player.setWinnings(player.getWinnings() + payout);//pass the payout to the player to add to the total winnings.
					player.setCurrentWager(0);//Reset the player's current wager until the next hand.
				}
			}
			if(player.getWinnings() > 0){//Check that that player has enough money to make another bet before prompting.
				char playAgain = getYorNAnswer("Play again? Y/N");
				if(playAgain == 'Y'){
					player.setCurrentWager(0);//Reset the player's current wager before restarting.  Total "winnings" stays the same.
					player.removePlayersCards();//Clear out the players cards to start again.
					dealersCards.clear();//Clear out the dealers cards to start again.
					playGame(player);
				}else{
					messageToPlayer("Thanks for playing.  You have " + player.getWinnings() + " dollars.");
					System.exit(0);//Close game.
				}
			}else{
				messageToPlayer("I'm sorry, but you don't have enough money.");
					messageToPlayer("Game OVER!");
					System.exit(0);
				
			}
		}	
	}
	//Display the initial cards
	//Have display card take in a card as an argument, run the convertCardSuit to return a unicode string
	//Pass the int card value and the unicode string to the createAPlayingCard method to build a switch statement that
	//takes the value and runs ascii art for each of the 13 values inserting the unicode symbol "   " + unicode + "  "" etc
	private void displayCards(String message, Card card1, Card card2){
		System.out.println(message);
		String cardSuit1 =  convertCardSuit(card1.getSuit());
		createAPlayingCard(card1.getOrderInSuit(), cardSuit1);
		System.out.println("   +   ");
		String cardSuit2 =  convertCardSuit(card2.getSuit());
		createAPlayingCard(card2.getOrderInSuit(), cardSuit2);
		System.out.println("\n");		
	}

	
	//Overload the displayInitialCards method to accept a single Card parameter.  This is helpful because there are many times when only one card is displayed.
	private void displayCards(String message, Card card1){
		System.out.println(message);
		String cardSuit1 =  convertCardSuit(card1.getSuit());
		createAPlayingCard(card1.getOrderInSuit(), cardSuit1);
		System.out.println("\n");
	}
	//Overloaded method for displying only one card.
	private void displayCards(Card card1){
		String cardSuit1 =  convertCardSuit(card1.getSuit());
		createAPlayingCard(card1.getOrderInSuit(), cardSuit1);
		System.out.println("\n");
	}
	private void createAPlayingCard(int orderInDeck, String suit){
		switch(orderInDeck){
		case(14):
			System.out.println("-------");
			System.out.println("-A    -");
			System.out.println("-     -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-     -");
			System.out.println("-    A-");
			System.out.println("-------");
			break;
		case(13):
			System.out.println("-------");
			System.out.println("-K    -");
			System.out.println("-     -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-     -");
			System.out.println("-    K-");
			System.out.println("-------");
			break;
		case(12):
			System.out.println("-------");
			System.out.println("-Q    -");
			System.out.println("-     -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-     -");
			System.out.println("-    Q-");
			System.out.println("-------");
			break;
		case(11):
			System.out.println("-------");
			System.out.println("-J    -");
			System.out.println("-     -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-     -");
			System.out.println("-    J-");
			System.out.println("-------");
			break;
		case(3):
			System.out.println("-------");
			System.out.println("-3    -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-    3-");
			System.out.println("-------");
			break;
		case(2):
			System.out.println("-------");
			System.out.println("-2    -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-     -");
			System.out.println("-  "+suit+"  -");
			System.out.println("-    2-");
			System.out.println("-------");
			break;
		case(4):
			System.out.println("-------");
			System.out.println("-4    -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-     -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-    4-");
			System.out.println("-------");
			break;
		case(5):
			System.out.println("-------");
			System.out.println("-5    -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-  "+suit+"   -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-    5-");
			System.out.println("-------");
			break;
		case(6):
			System.out.println("-------");
			System.out.println("-6    -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-    6-");
			System.out.println("-------");
			break;
		case(7):
			System.out.println("-------");
			System.out.println("-7"+suit+" "+suit+"  -");
			System.out.println("-  "+suit+"   -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("-     7-");
			System.out.println("-------");
			break;
		case(8):
			System.out.println("-------");
			System.out.println("-8"+suit+" "+suit+" -");
			System.out.println("-  "+suit+"  -");
			System.out.println("- "+suit+" "+suit+" -");
			System.out.println("-  "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"8-");
			System.out.println("-------");
			break;
		case(9):
			System.out.println("-------");
			System.out.println("-9 "+suit+" "+suit+" -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("-  "+suit+"   -");
			System.out.println("- "+suit+" "+suit+" 9-");
			System.out.println("-------");
			break;
		case(10):
			System.out.println("-10"+suit+" "+suit+" -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"  -");
			System.out.println("- "+suit+" "+suit+"10-");
			System.out.println("-------");
			break;
		default:
			break;
		}
	}
	//Display the unicode version of the card for some authenticity.
	private String convertCardSuit(Suit suit){
		String convertedCardSuit= "";
		switch(suit){
		case DIAMONDS:
			convertedCardSuit = "\u2666";
			break;
		case HEARTS:
			convertedCardSuit = "\u2665";
			break;
		case CLUBS:
			convertedCardSuit = "\u2663";
			break;
		case SPADES:
			convertedCardSuit = "\u2660";
			break;
		
		}
		return convertedCardSuit;
	}
	
	//Converts the value (an int) into somethign that more closely resembles a card value (ie, Jack, Queen, etc.)
	private void askForWager(){
		//messageToPlayer("Please make a wager: ");
		//Ensure that the value entered is of the proper type (int).
		messageToPlayer("You have " + player.getWinnings() + " dollars.");
		int wager = Math.abs(getInt("Please make a wager: "));//getInt() returns a validated int. Get the absolute value in case
		//the user tries to enter a negative number to subtract from their total.
		if(player.getWinnings() == 0){//ie, they don't have enough money.
			messageToPlayer("I'm sorry, but you don't have enough money.");
				System.exit(0);
			
		}else if(player.getWinnings() < wager){
			//Keep going until they give you a wager within their winnings
			while(player.getWinnings()< wager){
				//messageToPlayer("Not enough money, buddy!  Make a smaller than " + player.getWinnings());
				wager = Math.abs(getInt("Not enough money, buddy! Make a bet smaller than " + player.getWinnings() + " dollars."));//Check for a proper int
			}
		}
		player.setWinnings(player.getWinnings()-wager);
		player.setCurrentWager(player.getCurrentWager()+wager);

	}
	
	//Method for checking the proper input of an int and that it is greater than 0 (bids should be at least 1 dollar).
	private int getInt(String message){
		int wager = 0; //local wager holder
		while(wager<1){
			try{
				System.out.println(message);
				//Get the absolute value of the number entered in case they try to enter a negative number in an attempt to subtract their money.
				wager = scanner.nextInt();
				if(wager<1){
					System.out.println("Youd need to wager at least $1.00. What is your wager?");
				}
				
			}catch(InputMismatchException e){
				System.err.println("Input must be a whole number integer.\n");
				scanner.next();//Collect the "wrong" input so we can try again.
			}
		}
		return wager;
	}
	//Checks to see if the player has 21
	//Outputs a message to the terminal from the dealer with an extra \n for formatting.
	private void messageToPlayer(String message){
		System.out.println(message);
		System.out.println();
	}
	
	//The result of this method is the initial cards are dealt to the player and the dealer.
	private void dealCards(){
		//get the player's cards
		//Check to see if there are enough cards and, if not, get a new shuffled deck.	
		if(deck.size()>1){
			player.setPlayersCards(deck.get(0));
			player.setPlayersCards(deck.get(1));	
		}else{
			Deck newDeck = new Deck();
			deck = newDeck.getShuffledDeck();
			player.setPlayersCards(deck.get(0));
			player.setPlayersCards(deck.get(1));
		}
		//Remove those cards from the deck.
		deck.removeAll(player.getPlayersCards());
		if(deck.size()>1){
			 dealersCards.add(deck.get(0));
			 dealersCards.add(deck.get(1));
		}else{
			Deck newDeck = new Deck();
			deck = newDeck.getShuffledDeck();
			dealersCards.add(deck.get(0));
			dealersCards.add(deck.get(1));
		}
		deck.removeAll(dealersCards);//Remove those cards from the deck	
	}
}