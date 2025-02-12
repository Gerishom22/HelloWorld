import java.io.*;
import java.util.*;

class BingoCard {
    String ID; // Identifier for the Bingo card
    List<List<Integer>> grid = new ArrayList<>(); // 2D list representing the Bingo grid
    
    BingoCard(String ID, List<List<Integer>> grid) {
        this.ID = ID;
        this.grid = grid;
    }
    
    void markNumber(int number) {
        // Replace occurrences of the called number with 0 (marking it as checked)
        for (List<Integer> row : grid) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == number) {
                    row.set(i, 0);
                }
            }
        }
    }
    
    boolean hasBingo() {
        // Check if any row is fully marked (all zeros)
        for (List<Integer> row : grid) {
            if (row.stream().allMatch(n -> n == 0)) return true;
        }
        
        // Check if any column is fully marked
        for (int i = 0; i < 5; i++) {
            boolean colBingo = true;
            for (List<Integer> row : grid) {
                if (row.get(i) != 0) {
                    colBingo = false;
                    break;
                }
            }
            if (colBingo) return true;
        }
        
        // Check the two diagonal cases
        if (grid.get(0).get(0) == 0 
        && grid.get(1).get(1) == 0 
        && grid.get(2).get(2) == 0 
        && grid.get(3).get(3) == 0 
        && grid.get(4).get(4) == 0) 
            return true;

        if (grid.get(0).get(4) == 0 
        && grid.get(1).get(3) == 0 
        && grid.get(2).get(2) == 0 
        && grid.get(3).get(1) == 0 
        && grid.get(4).get(0) == 0) 
            return true;
        
        return false;
    }
    
    void display() {
        System.out.println("Card " + ID);
        System.out.println("B\tI\tN\tG\tO");
        for (List<Integer> row : grid) {
            for (int num : row) {
                System.out.print((num == 0 ? "X" : num) + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}

public class Bingo {
    public static void main(String[] args) throws IOException {
        List<BingoCard> cards = loadCards("BingoCards.txt"); // Load bingo cards from file
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        
        int numCards = 0;
        while (numCards < 1 || numCards > 4) {
            System.out.println("Choose 1 to 4 Bingo cards:");
            if (scanner.hasNextInt()) {
                numCards = scanner.nextInt();
                scanner.nextLine(); 
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); 
            }
        }

        // Select and shuffle Bingo cards
        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i));
        }
        
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        // Start drawing numbers
        Set<String> calledNumbers = new HashSet<>(); // Keep track of called numbers to avoid duplicates
        while (true) {
            String call = generateCall(rand, calledNumbers);
            System.out.println("Number drawn: " + call);
            int calledNumber = Integer.parseInt(call.substring(1)); // Extract the number part from the call
            
            for (BingoCard card : playerCards) {
                card.markNumber(calledNumber);
                card.display();
                if (card.hasBingo()) { // Check if a card has Bingo
                    System.out.println("BINGO on Card " + card.ID + "! You win!");
                    scanner.close(); 
                    return;
                }
            }
        }
    }
    
    private static List<BingoCard> loadCards(String filename) throws IOException {
        List<BingoCard> cards = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        String ID = "";
        List<List<Integer>> grid = new ArrayList<>();
        
        while ((line = br.readLine()) != null) {
            line = line.trim(); // Remove leading/trailing spaces
            if (line.isEmpty()) continue; // Skip empty lines
    
            if (line.startsWith("Card")) { // Detect card header
                if (!grid.isEmpty()) { // Save previous card if grid is not empty
                    cards.add(new BingoCard(ID, new ArrayList<>(grid)));
                    grid.clear();
                }
                ID = line; // Store card ID
            } else {
                List<Integer> row = new ArrayList<>();
                String[] numbers = line.split(",");
    
                for (String num : numbers) {
                    num = num.trim(); // Trim spaces
                    if (!num.isEmpty()) { // Ensure it's not an empty string
                        row.add(Integer.parseInt(num));
                    }
                }
    
                // Only add valid rows
                if (!row.isEmpty()) { 
                    grid.add(row);
                }
            }
        }

        // Add the last Bingo card
        if (!grid.isEmpty()) { 
            cards.add(new BingoCard(ID, grid));
        }
    
        br.close();
        return cards;
    }
    
    private static String generateCall(Random rand, Set<String> calledNumbers) {
        String[] letters = {"B", "I", "N", "G", "O"};
        int col = rand.nextInt(5);
        int num = rand.nextInt(15) + 1 + (col * 15);
        String call = letters[col] + num;

        // Ensure the number hasn't been called before
        while (calledNumbers.contains(call)) { 
            col = rand.nextInt(5);
            num = rand.nextInt(15) + 1 + (col * 15);
            call = letters[col] + num;
        }
        calledNumbers.add(call);
        return call;
    }
}
