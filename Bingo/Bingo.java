//Gerishom Muholeza

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
        
        for (List<Integer> row : grid) {
            if (row.stream().allMatch(n -> n == 0)) return true;
        }
        
        
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

class BingoSelection {
     public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String mode;

        while (true) {
            System.out.println("Choose mode: (manual/automatic)");
            mode = scanner.nextLine().trim().toLowerCase();

            if (mode.equals("manual") || mode.equals("automatic")) {
                break; 
            } else {
                System.out.println("Invalid input. Please enter 'manual' or 'automatic'.");
            }
        }

        if (mode.equals("manual")) {
            System.out.println("Manual mode selected.");
            ManBingo.startGame();
        } else {
            System.out.println("Automatic mode selected.");
            AutoBingo.startGame();  
        }

        scanner.close();
    }
}

class BingoHelper {
    public static List<BingoCard> loadCards(String filename) throws IOException {
        List<BingoCard> cards = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line;
        String ID = "";
        List<List<Integer>> grid = new ArrayList<>();
        
        while ((line = br.readLine()) != null) {
            line = line.trim(); 
            if (line.isEmpty()) continue; 
            if (line.startsWith("Card")) { 
                if (!grid.isEmpty()) { 
                    cards.add(new BingoCard(ID, new ArrayList<>(grid)));
                    grid.clear();
                }
                ID = line;
            } else {
                List<Integer> row = new ArrayList<>();
                String[] numbers = line.split(",");
    
                for (String num : numbers) {
                    num = num.trim();
                    if (!num.isEmpty()) { 
                        row.add(Integer.parseInt(num));
                    }
                }
    
                if (!row.isEmpty()) { 
                    grid.add(row);
                }
            }
        }

        if (!grid.isEmpty()) { 
            cards.add(new BingoCard(ID, grid));
        }
    
        br.close();
        return cards;
    }
    
    public static String generateCall(Random rand, Set<String> calledNumbers) {
        String[] letters = {"B", "I", "N", "G", "O"};
        int col, num;
        String call;
        
        do {
            col = rand.nextInt(5);
            num = rand.nextInt(15) + 1 + (col * 15);
            call = letters[col] + num;
        } while (calledNumbers.contains(call));

        calledNumbers.add(call);
        return call;
    }
}

class AutoBingo {
    public static void startGame() throws IOException {
        List<BingoCard> cards = BingoHelper.loadCards("BingoCards.txt");
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

        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i));
        }
        
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        System.out.println("Starting Automatic Bingo Game...");
        Set<String> calledNumbers = new HashSet<>();
        
        while (true) {
            String call = BingoHelper.generateCall(rand, calledNumbers);
            System.out.println("Number drawn: " + call);
            int calledNumber = Integer.parseInt(call.substring(1));
            
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

        List<BingoCard> playerCards = new ArrayList<>();
        Collections.shuffle(cards);
        for (int i = 0; i < numCards; i++) {
            playerCards.add(cards.get(i));
        }
        
        System.out.println("Your Bingo Cards:");
        for (BingoCard card : playerCards) {
            card.display();
        }
        
        System.out.println("Starting Manual Bingo Game:");
        Set<String> calledNumbers = new HashSet<>();

        while (true) {
            System.out.println("Enter the called Bingo number or type 'Exit' to quit:");
            String call = scanner.nextLine().toUpperCase().trim();
            
            if (call.equals("Exit")) {
                System.out.println("Game exited.");
                break;
            }
            
            if (!call.matches("[BINGO][0-9]{1,2}")) {
                System.out.println("Invalid format. Please enter a valid Bingo call (like B12, I25).");
                continue;
            }
            
            if (!calledNumbers.add(call)) {
                System.out.println("Number already called. Try a new number.");
                continue;
            }

            int calledNumber = Integer.parseInt(call.substring(1));
            
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
