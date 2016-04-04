package assignment3;

public class Card {
	private Suit suit;
	private int cardValue;
	private int orderInSuit;
	
	public Card(Suit suit, int orderInSuit, int cardValue) {
		super();
		this.suit = suit;
		this.cardValue = cardValue;
		this.orderInSuit = orderInSuit;
	}
	
	public int getOrderInSuit(){
		return orderInSuit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cardValue;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (cardValue != other.cardValue)
			return false;
		return true;
	}

	public Suit getSuit() {
		return suit;
	}
	public void setSuit(Suit suit) {
		this.suit = suit;
	}
	public int getCardValue() {
		return cardValue;
	}
	public void setCardValue(int cardValue) {
		this.cardValue = cardValue;
	}
	
}
