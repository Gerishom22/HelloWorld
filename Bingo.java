import java.util.*;

//Gerishom Muholeza

class BingoCard{
    //List to store the Bingo card numberes
    List<Integer> num = new ArrayList<>();

     // Constructor to initialize Bingo Card
     BingoCard(Random rand) {
        List<Integer> possibleNum = new ArrayList<>();
        for (int i = 1; i <= 25; ++i) {
            possibleNum.add(i);
        }
        Collections.shuffle(possibleNum, rand); // Shuffle the numbers
        num.addAll(possibleNum.subList
        (0, 25));  // Take first 25 shuffled numbers
       
    }
}

public class Bingo {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
        System.out.println("Welcome to Bingo!");

        //Random number generator
        Random rand = new Random();

        // Create a new Bingo card for the Player
        BingoCard playerCard1 = new BingoCard(rand);
        BingoCard playerCard2 = new BingoCard(rand);
        BingoCard playerCard3 = new BingoCard(rand);
        BingoCard playerCard4 = new BingoCard(rand);

        //Main game loop
        while(true){
            
             // Generate a random number between 1 and 25
             int drawnNumber = generateRandomNumber(rand, 1, 25);
             System.out.println("Number drawn: " + drawnNumber);
 
            //Mark the drawn number on both player's cards
            drawNumberAndMark(playerCard1, playerCard2, playerCard3, playerCard4, drawnNumber);

            //Display Both Player's cards
            System.out.println("Player's Card 1: ");
            displayCard(playerCard1);
            System.out.println("Player's Card 2: ");
            displayCard(playerCard2);
            System.out.println("Player's Card 3: ");
            displayCard(playerCard3);
            System.out.println("Player's Card 4: ");
            displayCard(playerCard4);
            System.out.println("-----------------------");

               // Check for Bingo 1
               if (hasBingo(playerCard1)) {
                System.out.println("BINGO! You Win!");
                break; 
        
               }

              // Check for Bingo 2
              if (hasBingo(playerCard2)) {
                System.out.println("BINGO! You Win!");
                break; 
        
               }

               // Check for Bingo 3
               if (hasBingo(playerCard3)) {
                System.out.println("BINGO! You Win!");
                break; 
        
               }

               // Check for Bingo 4
               if (hasBingo(playerCard4)) {
                System.out.println("BINGO! You Win!");
                break; 
        
               }


            }
            scan.close();
            }


      // Generates a random number between min and max (inclusive)
    private static int generateRandomNumber(Random random, int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
  

     // Marks the drawn number on the player's card
     private static void drawNumberAndMark(BingoCard playerCard1, BingoCard playerCard2, BingoCard playerCard3, BingoCard playerCard4, int num) {
        for (int i = 0; i < playerCard1.num.size(); ++i) {
            if (playerCard1.num.get(i) == num) {
                playerCard1.num.set(i, 0); 
            }
        }
        for (int i = 0; i < playerCard2.num.size(); ++i) {
            if (playerCard2.num.get(i) == num) {
                playerCard2.num.set(i, 0); 
            }
        }
        for (int i = 0; i < playerCard3.num.size(); ++i) {
            if (playerCard3.num.get(i) == num) {
                playerCard3.num.set(i, 0); 
            }
        }
        for (int i = 0; i < playerCard4.num.size(); ++i) {
            if (playerCard4.num.get(i) == num) {
                playerCard4.num.set(i, 0); 
            }
        }
    }

     // Checks if the Bingo card has achieved a Bingo pattern 
     private static boolean hasBingo(BingoCard card) 
     { 
         for (int i = 0; i < 5; ++i) { 
             // Check rows for Bingo 
             if (card.num.get(i * 5) == 0
                 && card.num.get(i * 5 + 1) == 0
                 && card.num.get(i * 5 + 2) == 0
                 && card.num.get(i * 5 + 3) == 0
                 && card.num.get(i * 5 + 4) == 0) { 
                 return true; 
             } 
   
             // Check columns for Bingo 
             if (card.num.get(i) == 0
                 && card.num.get(i + 5) == 0
                 && card.num.get(i + 10) == 0
                 && card.num.get(i + 15) == 0
                 && card.num.get(i + 20) == 0) { 
                 return true; 
             } 
         } 
   
         // Check diagonals for Bingo 
         if ((card.num.get(0) == 0
              && card.num.get(6) == 0
              && card.num.get(12) == 0
              && card.num.get(18) == 0
              && card.num.get(24) == 0) 
             || (card.num.get(4) == 0
                 && card.num.get(8) == 0
                 && card.num.get(12) == 0
                 && card.num.get(16) == 0
                 && card.num.get(20) == 0)) { 
             return true; 
         } 
   
         // No Bingo pattern found 
         return false; 
     }

  
    // Displays the Bingo card
    private static void displayCard(BingoCard card) {
        for (int i = 0; i < card.num.size(); ++i) {
            System.out.print((card.num.get(i) != 0) ? card.num.get(i) : "X");
            System.out.print("\t");

            // Move to the next line after every 5 numbers
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }
    }
}