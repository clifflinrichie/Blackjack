
public class Card {
    int suit;
    int value;
    int calcValue;
    
    // for user identification
    String actualSuit;
    String actualValue;

    // default constructor, no value (maybe joker)
    public Card() {
        this.suit = -1;
        this.value = -1;
    }

    public Card(int suit, int value) {
        this.suit = suit;
        this.value = value;
        translateCard();
    }

    public void printCard() {
        System.out.println(actualValue + " of " + actualSuit);
    }

    @Override
    public String toString() {
        String retVal = "";

        retVal = actualValue + " of " + actualSuit;
        return retVal;
    }

    public void translateCard() {
        if (suit == 1)
            actualSuit = "Clubs";
        if (suit == 2)
            actualSuit = "Diamonds";
        if (suit == 3) 
            actualSuit = "Hearts";
        if (suit == 4)
            actualSuit = "Spades";

        if (value == 11)
            actualValue = "Jack";
        else if (value == 12)
            actualValue = "Queen";
        else if (value == 13)
            actualValue = "King";
        else if (value == 1) 
            actualValue = "Ace";
        else 
            actualValue = Integer.toString(value);
        
        if (value > 10) {
            calcValue = 10;
        } else if (value == 1) {
            calcValue = 11;
        } else {
            calcValue = value;
        }
        
    }
}