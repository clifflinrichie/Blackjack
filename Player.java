import java.util.*;

public class Player {
    ArrayList<Card> hand = new ArrayList<Card>();
    int coins; 
    boolean blackjack;

    public Player() {
        this.coins = 20;
        this.blackjack = false;
    }

    // personal view
    public void getHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(hand.get(i));
        }
    }

    // one card is always hidden
    public void showHand() {
        for (int i = 1; i < hand.size(); i++) {
            System.out.println(hand.get(i));
        }
    }

    public int handValue() {
        int total = 0;
        // this could be optimized but
        int aceCount = 0;
        boolean aceNotUsed = true;
        for (int i = 0; i < hand.size(); i++) {
            Card tempCard = hand.get(i);
            // if it's an ace, mark it known
            if (tempCard.value == 1) {
                aceCount++;
            }
            total += tempCard.calcValue;
        }

        // to account for multiple aces in various position in your hand
        while (total > 21 && aceCount > 0) {
            total = 0;
            for (int i = 0; i < hand.size(); i++) {
                Card tempCard = hand.get(i);
                // first ace converted back to 1
                if (tempCard.value == 1 && aceNotUsed) {
                    total += tempCard.value;
                    aceNotUsed = false;
                    aceCount--;
                } else {
                    total += tempCard.calcValue;
                }
            }
            aceNotUsed = true;
        }

        return total;
    }

    public void checkBlackJack() {
        if (hand.get(0).value == 1 && hand.get(1).value >= 10) {
            this.blackjack = true;
        } 
        if (hand.get(0).value >= 10 && hand.get(1).value == 1) {
            this.blackjack = true;
        } 
    }

    public void discardHand() {
        hand.clear();
        this.blackjack = false;
    }

}