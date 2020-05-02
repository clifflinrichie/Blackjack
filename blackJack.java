import java.util.*;

public class blackJack {
    public static Set<String> VALID_INPUTS = new HashSet<String>();
    // set globally so you can change how many rounds
    public static int roundLimit = 3;
    

    public blackJack() {
    }

    // creation of a new deck
    public static ArrayList<Card> createDeck() {
        ArrayList<Card> deck = new ArrayList<Card>();
        // Suit of every card
        for (int i = 0; i < 4; i++) {
            // value for every card
            for (int j = 0; j < 13; j++) {
                Card c = new Card(i+1, j+1);
                deck.add(c);
            }
        }
        return deck;
    }

    // just to see it's working
    public static void printDeck(ArrayList<Card> deck) {
        for(int i = 0; i < deck.size(); i++) {
            System.out.println(deck.get(i));
        }
    }

    public static ArrayList<Card> shuffleDeck(ArrayList<Card> deck, int totalSize) {
        ArrayList<Card> shuffled = new ArrayList<Card>();
        Random rand = new Random();

        // total size allows to shuffle decks of different sizes
        for (int i = 0; i < totalSize; i++) {
            int randomIndex = rand.nextInt(deck.size());
            shuffled.add(deck.get(randomIndex));
            deck.remove(randomIndex);
        }

        return shuffled;
    }

    public static void startingDraw(ArrayList<Card> deck, Player user) {
        // we assumed the deck has been shuffled, and that a deck exists
        if (deck.size() >= 1) {
            user.hand.add(deck.get(0));
            deck.remove(0);
        }
    }

    public static void hit(ArrayList<Card> deck, Player user) {
        if (deck.size() >= 1) {
            user.hand.add(deck.get(0));
            System.out.println("The card drawn was " + deck.get(0));
            deck.remove(0);
        }

    }

    public static void dealerPlay(ArrayList<Card> deck, Player dealer) {
        System.out.println("The dealer's hand is " + dealer.handValue());
        while (dealer.handValue() < 17) {
            hit(deck, dealer);
            System.out.println("The dealer's hand is " + dealer.handValue());
        }
    }

    public static boolean playerNotBusted(Player user) {
        return user.handValue() <= 21;
    }

    public static void commandList() {
        VALID_INPUTS.add("hit");
        VALID_INPUTS.add("stay");
        VALID_INPUTS.add("quit");
        VALID_INPUTS.add("help");

        VALID_INPUTS.add("h");
        VALID_INPUTS.add("s");
        VALID_INPUTS.add("q");
    }

    /*
    True if player wins, false if the dealer loses
    */
    public static boolean handComp(Player user, Player dealer) {
        if (dealer.handValue() > 21) {
            System.out.println("Dealer busted! You win!");
            return true;
        } 
        return user.handValue() - dealer.handValue() > 0;
    }

    public static void checkWinner(Player user, Player dealer) {
        boolean check = handComp(user, dealer);

        // player wins if they have blackjack and dealer does not
        if (user.blackjack && !dealer.blackjack) {
            System.out.println("BlackJack! Adding 10 coins...");
            user.coins += 10;
            dealer.coins -= 5;
        } 
        else if (check) {
            System.out.println("You won! Adding 5 coins...");
            user.coins += 5;
            dealer.coins -= 5;
        } else {
            System.out.println("You lost! Subtracting 5 coins...");
            user.coins -= 5;
            dealer.coins += 5;
        }
    }

    public static void winner(Player user, Player dealer) {
        if (user.coins == 0) {
            System.out.println("0 coins! You lose!");
        }
        if (dealer.coins == 0) {
            System.out.println("Dealer has no money left; you win!");
        }
        if (user.coins > dealer.coins) {
            System.out.println("Congrats, you won!");
            System.out.println("You won by " + Integer.toString(user.coins - dealer.coins) + " coins!");
        } else {
            System.out.println("Sorry, you've lost...");
            System.out.println("You lost by " + Integer.toString(dealer.coins - user.coins) + " coins!");
        }
    }

    /*
    At the start of each round, each player is dealt 2 cards. One of these cards is face up, one is face down
    In this extremely simplified form of blackjack, you can hit or stay. 
    (Different playstyles and betting will be implemented later on)
    Once your turn ends, the dealer goes, and you compare hands. Whoever has the higher hand wins!
    If it's bust (>21) you automatically lose, regardless if the dealer could've busted too.

    */
    public static void roundStart(ArrayList<Card> deck, Player user, Player dealer, int round) {
        boolean userTurn = true;
        Scanner input = new Scanner(System.in);
        
        // two cards per player
        startingDraw(deck, user);
        startingDraw(deck, dealer);
        startingDraw(deck, user);
        startingDraw(deck, dealer);

        user.checkBlackJack();
        dealer.checkBlackJack();

        System.out.println("Your hand is " + user.hand);
        System.out.println("The value of your hand is " + user.handValue());
        System.out.print("The dealer has ");
        dealer.showHand();

        // only one turn per round, bust check to see if the player is inelligble for play
        while (userTurn && playerNotBusted(user)) {
            String response = "";
            System.out.print("What would you like to do? (type 'help' for help): ");
            response = input.nextLine();
            response = response.toLowerCase();

            // ensure valid input
            while (!VALID_INPUTS.contains(response)) {
                System.out.println("Not valid input! Try again.");
                System.out.print("What would you like to do? (type 'help' for help): ");
                response = input.nextLine();
                response = response.toLowerCase();
            }

            if (response.equals("stay") || response.equals("s")) 
                userTurn = false;
            if (response.equals("help"))
                printHelp();
            if (response.equals("quit") || response.equals("q"))
                System.exit(0);
            // can hit as many times as you want
            if (response.equals("hit") || response.equals("h")) {
                // ongoing play in case deck runs out
                if (deck.size() == 0) {
                    deck = createDeck();
                    deck = shuffleDeck(deck, deck.size());
                }
                hit(deck, user);
                System.out.println("Your current hand is \n" + user.hand);
                System.out.println("The value of your hand is " + user.handValue());

            }

            
        }

        // if the player didn't bust, check hands
        if (playerNotBusted(user)) {
            System.out.print("The dealer's hand is ");
            System.out.println(dealer.hand);
            
            dealerPlay(deck, dealer);
            checkWinner(user, dealer);
        }

        // if the player busted
        if (!playerNotBusted(user)) {
            System.out.println("You busted! Subtracting 5 coins...");
            user.coins -= 5;
            dealer.coins += 5;
        }
       
        // overview
        System.out.println("Total coin counts: ");
        System.out.println("Player: " + user.coins);
        System.out.println("Dealer: " + dealer.coins);

        // Cleanup
        user.discardHand();
        dealer.discardHand();

        //input.close();
        if (round == roundLimit - 1) {
            input.close();
        }

    }

    public static void printHelp() {
        System.out.println(
            "You can either: "
            + "\n 1. Hit or h"
            + "\n 2. Stay or s"
            + "\n 3. Quit or q"

        );
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Blackjack! You will be playing against a dealer.");
        commandList();
        // create a new deck
        ArrayList<Card> deck = createDeck();
        System.out.println(deck.size());

        // proof the deck is shuffled
        deck = shuffleDeck(deck, deck.size());
        printDeck(deck);
        System.out.println(deck.size());

        // initializing player and dealer
        Player user = new Player();
        Player dealer = new Player();

        System.out.println(
            "The rules are as follows; your goal is to get to 21 as close as possible. On your turn, you can"
            + " either hit or stay. You can hit as many times as you want, but if you bust aka go over 21, you" 
            + " automatically lose. Once you stay, the dealer will go and you will compare hands. Whoever has the"
            + " higher hand will win. Run out of coins and you lose! Make the dealer run out of coins and you win!"
            + " There will be a total of 10 rounds.\n"
        );

        for (int i = 0; i < roundLimit; i++) {
            System.out.println("\nRound " + Integer.toString(i+1));
            roundStart(deck, user, dealer, i); 
            // to prevent card counting, shuffling deck when less than 10 cards
            if (deck.size() <= 10) {
                System.out.println("Reshuffling deck...");
                deck = createDeck();
                deck = shuffleDeck(deck, deck.size());
            }
            if (user.coins == 0 || dealer.coins == 0) {
                break;
            }
            System.out.println("Cards remaining in deck: " + Integer.toString(deck.size()));
            System.out.println("");

        }
        System.out.println("Game over!");
        winner(user, dealer);
        

    }
}