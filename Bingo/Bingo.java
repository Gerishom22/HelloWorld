//Gerishom Muholeza

import java.io.*;
import java.util.*;

class BingoCard { 
    String ID; // Card ID
    List<List<Integer>> grid = new ArrayList<>(); // 5x5 grid of numbers 
    
    //Constructor
    BingoCard(String ID, List<List<Integer>> grid) {
        this.ID = ID;
        this.grid = grid;
    }
    
    //Mark a number on the card
    void markNumber(int number) {
        for (List<Integer> row : grid) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == number) {
                    row.set(i, 0);
                }
            }
        }
    }
    
    //Check if the card has a Bingo
    boolean hasBingo() {
        List<Pattern> patterns = Arrays.asList( // List of Bingo patterns
            new Pattern(Pattern.Type.ROW),
            new Pattern(Pattern.Type.COLUMN),
            new Pattern(Pattern.Type.DIAGONAL)
        );

        for (Pattern pattern : patterns) { 
            if (pattern.matches(this)) { // Check if the card matches any of the patterns
                return true; // A Bingo is found
            }
        }
        return false; // No Bingo found
    }
    
    //Count the number of Bingos on the card
    int countBingos(List<Pattern> patterns) {
        int count = 0;
        for (Pattern pattern : patterns) { 
            if (pattern.matches(this)) { // Check if the card matches any of the patterns
                count++;
            }
        }
        return count; // Return the number of Bingos found
    }
    
    //Display the card
    void display() {
        System.out.println("Card " + ID);
        System.out.println("B\tI\tN\tG\tO");
        for (List<Integer> row : grid) { 
            for (int num : row) {
                System.out.print((num == 0 ? "X" : num) + "\t"); // Marked numbers are displayed as X
            }
            System.out.println();
        }
        System.out.println();
    }
}


class Pattern{

    // Types of Bingo patterns
    enum Type { 
        ROW, COLUMN, DIAGONAL, CUSTOM 
    }

    Type type;
    // List of (row, col) pairs for custom patterns
    List<int[]> customPattern;

    // Constructor for Normal patterns
    Pattern(Type type) {
        this.type = type;
        this.customPattern = new ArrayList<>();
    }

     // Constructor for custom patterns
    Pattern(List<int[]> customPattern) {
        this.type = Type.CUSTOM;
        this.customPattern = customPattern;
    }   
    
    // Check if the card matches the pattern
    boolean matches(BingoCard card) {
        switch (type) {
            case ROW:
                return checkRow(card);
            case COLUMN:
                return checkColumn(card);
            case DIAGONAL:
               return checkDiagonal(card);
            case CUSTOM:
                return checkCustom(card);
            default:
                return false;
        }
    } 
       
    //Rows case - Row Pattern - any fully marked row earns a Bingo
        private boolean checkRow(BingoCard card) {
            for (List<Integer> row : card.grid) {
                if (row.stream().allMatch(n -> n == 0)) return true; // If all numbers in the row are 0, it's a Bingo
            }
            return false;
        }
        
    //Columns case - Column Pattern - any  fully marked column earns a Bingo
    private boolean checkColumn(BingoCard card) {
            for (int i = 0; i < 5; i++) {
                boolean colBingo = true; // Assume the column is a Bingo
                for (List<Integer> row : card.grid) {
                    if (row.get(i) != 0) {
                        colBingo = false; // If any number in the column isn't 0, it's not a Bingo
                        break;
                    }
                }
                if (colBingo) return true; // If the column is a Bingo, return true
            }
            return false; // if not than no Bingo found
     }
      
    //Diagonal case - Diagonal Pattern - any fully marked diagonal earns a Bingo
     private boolean checkDiagonal(BingoCard card) { 
        // Check top-left to bottom-right diagonal
        boolean LDiag = true; 
        for (int i = 0; i < 5; i++) {
            if (card.grid.get(i).get(i) != 0) { // If any number in the diagonal isn't 0, it's not a Bingo
                LDiag = false;
                break;
            }
        }

        //Check top-right to bottom-left diagonal
        boolean RDiag = true;   
        for (int i = 0; i < 5; i++) {
            if (card.grid.get(i).get(4 - i) != 0) { 
                RDiag = false;
                break;
            }
        }
        return LDiag||RDiag; // If either diagonal is a Bingo, return true
    }

   //Custom case - Custom Pattern - any custom pattern earns a Bingo
   private boolean checkCustom(BingoCard card) {
        for (int[] position : customPattern) {
            int row = position[0], col = position[1]; // Get the row and column of each position
            if (card.grid.get(row).get(col) != 0) { // If any position isn't 0, it's not a Bingo
                return false; 
            }
        }
        return true; // If all positions are 0, it's a Bingo
    }

}

class BingoManager {
    // Stores multiple Bingo patterns
    private List<List<int[]>> patterns; 

    // Constructor
    BingoManager() {
        this.patterns = new ArrayList<>();
    }

    // Add a new pattern (a list of (row, col) positions that must be marked)
    void addPattern(List<int[]> pattern) {
        patterns.add(pattern);
    }

    // Check if any of the stored patterns result in Bingo
    boolean isBingo(BingoCard card) {
        for (List<int[]> pattern : patterns) {
            if (checkPattern(card, pattern)) { // Check if the card matches any of the patterns
                return true;
            }
        }
        return false;
    }

    // Count the number of patterns that result in a Bingo
    int countBingos(BingoCard card) {
        int count = 0;
        for (List<int[]> pattern : patterns) { 
            if (checkPattern(card, pattern)) { // Check if the card matches any of the patterns
                count++;
            }
        }
        return count;
    }

    // Check if a specific pattern is fully marked (all positions must be 0)
    private boolean checkPattern(BingoCard card, List<int[]> pattern) {
        for (int[] pos : pattern) {
            int row = pos[0], col = pos[1];
            if (card.grid.get(row).get(col) != 0) {
                return false; // If any position isn't 0, this pattern isn't a Bingo
            }
        }
        return true; // If all positions are 0, this pattern is a Bingo
    }
}

// Main class for Bingo game - Chooese between manual and automatic mode
class BingoSelection {
     public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String mode; 

        while (true) { 
            System.out.println("Choose mode: (manual/automatic)");
            mode = scanner.nextLine().trim().toLowerCase(); 

            // Check if the input is valid
            if (mode.equals("manual") || mode.equals("automatic")) { 
                break; 
            } else {
                System.out.println("Invalid input. Please enter 'manual' or 'automatic'.");
            }
        }

        if (mode.equals("manual")) { // Start the game in manual mode
            System.out.println("Manual mode selected.");
            ManBingo.startGame();
        } else { // Start the game in automatic mode
            System.out.println("Automatic mode selected.");
            AutoBingo.startGame();  
        }

        scanner.close();
    }
}

// Helper class for Bingo game
class BingoHelper { 
    public static List<BingoCard> loadCards(String filename) throws IOException {
        List<BingoCard> cards = new ArrayList<>(); // List of Bingo cards
        BufferedReader br = new BufferedReader(new FileReader(filename)); // Read the file
        String line; // Read each line
        String ID = ""; // Card ID
        List<List<Integer>> grid = new ArrayList<>(); // 5x5 grid of numbers
        
        while ((line = br.readLine()) != null) {  
            line = line.trim(); // Remove leading/trailing whitespaces
            if (line.isEmpty()) continue; // Skip empty lines
            if (line.startsWith("Card")) { // Check if the line is a card ID
                if (!grid.isEmpty()) { // If the grid isn't empty, add the card to the list
                    cards.add(new BingoCard(ID, new ArrayList<>(grid))); 
                    grid.clear();                       
                }
                ID = line; 
            } else {
                List<Integer> row = new ArrayList<>(); // Create a new row
                String[] numbers = line.split(","); // Split the line by commas
    
                for (String num : numbers) { // Add each number to the row
                    num = num.trim(); 
                    if (!num.isEmpty()) { 
                        row.add(Integer.parseInt(num)); // Convert the number to an integer and add it to the row
                    }
                }
    
                if (!row.isEmpty()) {  
                    grid.add(row); // Add the row to the grid
                }
            }
        }

        if (!grid.isEmpty()) { 
            cards.add(new BingoCard(ID, grid)); // Add the last card to the list
        }
    
        br.close(); // Close the file
        return cards;
    }
    
    public static String generateCall(Random rand, Set<String> calledNumbers) {
        String[] letters = {"B", "I", "N", "G", "O"}; 
        int col, num; 
        String call; 
        
        do { // Generate a random Bingo call
            col = rand.nextInt(5); 
            num = rand.nextInt(15) + 1 + (col * 15); // Generate a random number in the range of the column
            call = letters[col] + num;
        } while (calledNumbers.contains(call)); // Check if the call has already been made

        calledNumbers.add(call); // Add the call to the set of called numbers
        return call;
    }
}

class AutoBingo {
    public static void startGame() throws IOException {
        List<BingoCard> cards = BingoHelper.loadCards("BingoCards.txt"); // Load Bingo cards from file
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        
        int numCards = 0;
        while (numCards < 1 || numCards > 4) { // Choose the number of Bingo cards
            System.out.println("Choose 1 to 4 Bingo cards:");
            if (scanner.hasNextInt()) {
                numCards = scanner.nextInt(); // Read the number of cards
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine();
            }
        }

        // Select the player's Bingo cards
        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i));
        }
        
        // Display the player's Bingo cards
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        // Start the automatic Bingo game
        System.out.println("Starting Automatic Bingo Game...");
        Set<String> calledNumbers = new HashSet<>();
        
        // Generate Bingo calls until a Bingo is found
        while (true) {
            String call = BingoHelper.generateCall(rand, calledNumbers); // Generate a random Bingo call
            System.out.println("Number drawn: " + call); // Display the call
            int calledNumber = Integer.parseInt(call.substring(1)); // Get the number from the call
            
            // Mark the number on the player's cards
            for (BingoCard card : playerCards) {
                card.markNumber(calledNumber);
                card.display();
                if (card.hasBingo()) {
                    System.out.println("BINGO on " + card.ID + "! You win!");
                    scanner.close();
                    return;
                }
            }
        }
    }
}

class ManBingo {
    public static void startGame() throws IOException {
        List<BingoCard> cards = BingoHelper.loadCards("BingoCards.txt");
        Scanner scanner = new Scanner(System.in);
        
        int numCards = 0;
        while (numCards < 1 || numCards > 4) { // Choose the number of Bingo cards
            System.out.println("Choose 1 to 4 Bingo cards:");
            if (scanner.hasNextInt()) {
                numCards = scanner.nextInt(); // Read the number of cards
                scanner.nextLine();
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine();
            }
        }

        // Select the player's Bingo cards
        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i)); 
        }
        
        // Display the player's Bingo cards
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        // Start the manual Bingo game
        System.out.println("Starting Manual Bingo Game:");
        Set<String> calledNumbers = new HashSet<>();

        // Enter Bingo calls until a Bingo is found
        while (true) { 
            System.out.println("Enter the called Bingo number or type 'Exit' to quit:");
            String call = scanner.nextLine().toUpperCase().trim(); 
            
            // Check if the game is exited
            if (call.equals("Exit")) {
                System.out.println("Game exited.");
                break;
            }
            
            // Check if the call is in the correct format
            if (!call.matches("[BINGO][0-9]{1,2}")) {
                System.out.println("Invalid format. Please enter a valid Bingo call (like B12, I25).");
                continue;
            }
            
            // Check if the call has already been made
            if (!calledNumbers.add(call)) {
                System.out.println("Number already called. Try a new number.");
                continue;
            }

            // Mark the number on the player's cards
            int calledNumber = Integer.parseInt(call.substring(1));
            
            // Mark the number on the player's cards
            for (BingoCard card : playerCards) {
                card.markNumber(calledNumber);
                card.display();
                if (card.hasBingo()) {
                    System.out.println("BINGO on " + card.ID + "! You win!");
                    scanner.close();
                    return;
                }
            }
        }
        
        scanner.close();
    }
}
