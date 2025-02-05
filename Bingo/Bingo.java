import java.io.*;
import java.util.*;

class BingoCard {
    String ID;
    List<List<Integer>> grid = new ArrayList<>();
    
    BingoCard(String ID, List<List<Integer>> grid) {
        this.ID = ID;
        this.grid = grid;
    }
    
    void markNumber(int number) {
        for (List<Integer> row : grid) {
            for (int i = 0; i < row.size(); i++) {
                if (row.get(i) == number) {
                    row.set(i, 0);
                }
            }
        }
    }
    
    boolean hasBingo() {
        // Check rows
        for (List<Integer> row : grid) {
            if (row.stream().allMatch(n -> n == 0)) return true;
        }
        
        // Check columns
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
        
        // Check diagonals
        if 
        (grid.get(0).get(0) == 0 
        && grid.get(1).get(1) == 0 
        && grid.get(2).get(2) == 0 
        && grid.get(3).get(3) == 0 
        && grid.get(4).get(4) == 0) 
        return true;

        if 
        (grid.get(0).get(4) == 0 
        && grid.get(1).get(3) == 0 
        && grid.get(2).get(2) == 0 
        && grid.get(3).get(1) == 0 
        && grid.get(4).get(0) == 0) 
        return true;
        
        return false;
    }
    
    void display() {
        System.out.println("Card " + ID);
        System.out.println("B	I	N	G	O");
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
        List<BingoCard> cards = loadCards("C:/Users/MuholezaGerishom/java/Bingo/BingoCards.txt.txt");
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();
        
        int numCards = 0;
        while (numCards < 1 || numCards > 4) {
            System.out.println("Choose 1 to 4 Bingo cards:");
            if (scanner.hasNextInt()) {
                numCards = scanner.nextInt();
                scanner.nextLine(); // Consume the newline
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i));
        }
        
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        Set<String> calledNumbers = new HashSet<>();
        while (true) {
            String call = generateCall(rand, calledNumbers);
            System.out.println("Number drawn: " + call);
            int calledNumber = Integer.parseInt(call.substring(1));
            
            for (BingoCard card : playerCards) {
                card.markNumber(calledNumber);
                card.display();
                if (card.hasBingo()) {
                    System.out.println("BINGO on Card " + card.ID + "! You win!");
                    scanner.close(); // Close the scanner before exiting
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
            if (line.startsWith("Card")) {
                if (!grid.isEmpty()) {
                    cards.add(new BingoCard(ID, new ArrayList<>(grid)));
                    grid.clear();
                }
                ID = line;
            } else {
                List<Integer> row = new ArrayList<>();
                for (String num : line.split(",")) {
                    row.add(Integer.parseInt(num.trim()));
                }
                grid.add(row);
            }
        }
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
        
        while (calledNumbers.contains(call)) {
            col = rand.nextInt(5);
            num = rand.nextInt(15) + 1 + (col * 15);
            call = letters[col] + num;
        }
        calledNumbers.add(call);
        return call;
    }
}
