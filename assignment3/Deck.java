package assignment3;

import java.util.ArrayList;
import java.util.Collections;
/*
 * Creates a deck of Card objects and adds them to an array to simulate a deck of cards.
 */
public class Deck {
	
	private ArrayList<Card> cards = new ArrayList<>();

	
	//Create a deck of cards by instantiating 52 new Card objects.
	public Deck(){
		//Parameters: suit, orderInSuit, cardValue
		cards.add(new Card(Suit.DIAMONDS, 2, 2));
		cards.add(new Card(Suit.DIAMONDS, 3, 3));
		cards.add(new Card(Suit.DIAMONDS, 4, 4));
		cards.add(new Card(Suit.DIAMONDS, 5, 5));
		cards.add(new Card(Suit.DIAMONDS, 6, 6));
		cards.add(new Card(Suit.DIAMONDS, 7, 7));
		cards.add(new Card(Suit.DIAMONDS, 8, 8));
		cards.add(new Card(Suit.DIAMONDS, 9, 9));
		cards.add(new Card(Suit.DIAMONDS, 10, 10));
		cards.add(new Card(Suit.DIAMONDS, 11, 10));
		cards.add(new Card(Suit.DIAMONDS, 12, 10));
		cards.add(new Card(Suit.DIAMONDS, 13, 10));
		cards.add(new Card(Suit.DIAMONDS, 14, 11));
		cards.add(new Card(Suit.HEARTS, 2, 2));
		cards.add(new Card(Suit.HEARTS, 3, 3));
		cards.add(new Card(Suit.HEARTS, 4, 4));
		cards.add(new Card(Suit.HEARTS, 5, 5));
		cards.add(new Card(Suit.HEARTS, 6, 6));
		cards.add(new Card(Suit.HEARTS, 7, 7));
		cards.add(new Card(Suit.HEARTS, 8, 8));
		cards.add(new Card(Suit.HEARTS, 9, 9));
		cards.add(new Card(Suit.HEARTS, 10, 10));
		cards.add(new Card(Suit.HEARTS, 11, 10));
		cards.add(new Card(Suit.HEARTS, 12, 10));
		cards.add(new Card(Suit.HEARTS, 13, 10));
		cards.add(new Card(Suit.HEARTS, 14, 11));
		cards.add(new Card(Suit.CLUBS, 2, 2));
		cards.add(new Card(Suit.CLUBS, 3, 3));
		cards.add(new Card(Suit.CLUBS, 4, 4));
		cards.add(new Card(Suit.CLUBS, 5, 5));
		cards.add(new Card(Suit.CLUBS, 6, 6));
		cards.add(new Card(Suit.CLUBS, 7, 7));
		cards.add(new Card(Suit.CLUBS, 8, 8));
		cards.add(new Card(Suit.CLUBS, 9, 9));
		cards.add(new Card(Suit.CLUBS, 10, 10));
		cards.add(new Card(Suit.CLUBS, 11, 10));
		cards.add(new Card(Suit.CLUBS, 12, 10));
		cards.add(new Card(Suit.CLUBS, 13, 10));
		cards.add(new Card(Suit.CLUBS, 14, 11));
		cards.add(new Card(Suit.SPADES, 2, 2));
		cards.add(new Card(Suit.SPADES, 3, 3));
		cards.add(new Card(Suit.SPADES, 4, 4));
		cards.add(new Card(Suit.SPADES, 5, 5));
		cards.add(new Card(Suit.SPADES, 6, 6));
		cards.add(new Card(Suit.SPADES, 7, 7));
		cards.add(new Card(Suit.SPADES, 8, 8));
		cards.add(new Card(Suit.SPADES, 9, 9));
		cards.add(new Card(Suit.SPADES, 10, 10));
		cards.add(new Card(Suit.SPADES, 11, 10));
		cards.add(new Card(Suit.SPADES, 12, 10));
		cards.add(new Card(Suit.SPADES, 13, 10));
		cards.add(new Card(Suit.SPADES, 14, 11));
		
		
	}

//Shuffle the order of the deck.
public ArrayList<Card> getShuffledDeck(){
	Collections.shuffle(cards);
	return cards;
}
}