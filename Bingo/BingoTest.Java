import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

class BingoTest {
    private BingoCard bingoCard;
    private BingoManager bingoManager;
    
    private void markRow(int row) {
        for (int i = 0; i < 5; i++) {
            bingoCard.markNumber(bingoCard.getNumber(row, i));
        }
    }

    private void markColumn(int col) {
        for (int i = 0; i < 5; i++) {
            bingoCard.markNumber(bingoCard.getNumber(i, col));
        }
    }

    private void markDiagonal(boolean topRight) {
        for (int i = 0; i < 5; i++) {
            bingoCard.markNumber(bingoCard.getNumber(i, topRight ? 4 - i : i));
        }
    }

    private void markAll() {
        for (int i = 0; i < 5; i++) {
            markRow(i);
        }
    }

    private void markTShape() {
        markRow(0);
        markColumn(2);
    }

    private void markSquare() {
        markRow(0);
        markRow(4);
        markColumn(0);
        markColumn(4);
    }

    @BeforeEach
    void setUp() {
        List<List<Integer>> grid = new ArrayList<>();
        grid.add(Arrays.asList(1, 2, 3, 4, 5));
        grid.add(Arrays.asList(6, 7, 8, 9, 10));
        grid.add(Arrays.asList(11, 12, 13, 14, 15));
        grid.add(Arrays.asList(16, 17, 18, 19, 20));
        grid.add(Arrays.asList(21, 22, 23, 24, 25));
        bingoCard = new BingoCard("TestCard", grid);
        bingoManager = new BingoManager();
    }

    //Pattern Test1
    @Test
    void testRowPattern_FirstRowFull() {
        markRow(0);
        assertTrue(new Pattern(Pattern.Type.ROW).matches(bingoCard));
    }

    //Pattern Test2
    @Test
    void testRowPattern_ThirdRowFull() {
        markRow(2);
        assertTrue(new Pattern(Pattern.Type.ROW).matches(bingoCard));
    }

    //Pattern Test3
    @Test
    void testRowPattern_LastRowFull() {
        markRow(4);
        assertTrue(new Pattern(Pattern.Type.ROW).matches(bingoCard));
    }

    //Pattern Test4
    @Test
    void testRowPattern_FirstRowExceptLastColumn() {
        markRow(0);
        bingoCard.unmarkNumber(5);
        assertFalse(new Pattern(Pattern.Type.ROW).matches(bingoCard));
    }

    //Pattern Test5
    @Test
    void testColumnPattern_FirstColumnFull() {
        markColumn(0);
        assertTrue(new Pattern(Pattern.Type.COLUMN).matches(bingoCard));
    }

    //Pattern Test6
    @Test
    void testColumnPattern_FourthColumnFull() {
        markColumn(3);
        assertTrue(new Pattern(Pattern.Type.COLUMN).matches(bingoCard));
    }

    //Pattern Test7
    @Test
    void testColumnPattern_LastColumnFull() {
        markColumn(4);
        assertTrue(new Pattern(Pattern.Type.COLUMN).matches(bingoCard));
    }

    //Pattern Test8
    @Test
    void testColumnPattern_FirstRowMarked() {
        markRow(0);
        assertFalse(new Pattern(Pattern.Type.COLUMN).matches(bingoCard));
    }

    //Pattern Test9
    @Test
    void testColumnPattern_SecondColumnExceptSecondRow() {
        markColumn(1);
        bingoCard.unmarkNumber(7);
        assertFalse(new Pattern(Pattern.Type.COLUMN).matches(bingoCard));
    }

    //Pattern Test10
    @Test
    void testDiagonalPattern_TopRightToBottomLeft() {
        markDiagonal(true);
        assertTrue(new Pattern(Pattern.Type.DIAGONAL).matches(bingoCard));
    }

    //Pattern Test11
    @Test
    void testDiagonalPattern_TopLeftToBottomRight() {
        markDiagonal(false);
        assertTrue(new Pattern(Pattern.Type.DIAGONAL).matches(bingoCard));
    }

    //Pattern Test12
    @Test
    void testDiagonalPattern_SecondRowFull() {
        markRow(1);
        assertFalse(new Pattern(Pattern.Type.DIAGONAL).matches(bingoCard));
    }

    //Pattern Test13
    @Test
    void testCustomPattern_TShape() {
        markTShape();
        assertTrue(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //Pattern Test14
    @Test
    void testCustomPattern_TShapeFullCard() {
        markAll();
        assertTrue(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //Pattern Test15
    @Test
    void testCustomPattern_TShapeMissingMiddle() {
        markTShape();
        bingoCard.unmarkNumber(13);
        assertFalse(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //Pattern Test16
    @Test
    void testCustomPattern_Square() {
        markSquare();
        assertTrue(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //Pattern Test17
    @Test
    void testCustomPattern_SquareMissingBottomRight() {
        markSquare();
        bingoCard.unmarkNumber(25);
        assertFalse(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //Pattern Test18
    @Test
    void testCustomPattern_SquareMissingTopLeft() {
        markSquare();
        bingoCard.unmarkNumber(1);
        assertFalse(new Pattern(Pattern.Type.CUSTOM).matches(bingoCard));
    }

    //BingoManager Test1
    @Test
    void testBingoManager_SingleRow() {
        markRow(0);
        assertEquals(1, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test2
    @Test
    void testBingoManager_ThreeRows() {
        markRow(0);
        markRow(2);
        markRow(4);
        assertEquals(3, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test3
    @Test
    void testBingoManager_ThreeRowsAndLastColumn() {
        markRow(0);
        markRow(2);
        markRow(4);
        markColumn(4);
        assertEquals(3, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test4
    @Test
    void testBingoManager_ThreeRowsANdLastRowAndLastColumnFull() {
        markRow(0);
        markRow(2);
        markRow(4);
        markColumn(4);
        markRow(4);
        assertEquals(4, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test5
    @Test
    void testBingoManager_RowPatternAndColumnPatternAndDiagonalPattern_FullCardMarked() {
        markAll();
        assertEquals(12, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test6
    @Test
    void testBingoManager_RowPatternAndColumnPatternAndDiagonalPatternAndCustomPatternTShapeAndSquare_FullCardMarked() {
        markAll();
        markTShape();
        markSquare();
        assertEquals(14, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test7
    @Test
    void testBingoManager_RowPatternAndColumnPatternAndDiagonalPatternAndCustomPatternTShapeAndSquare_FullCardMarkedExceptTopRightCorner() {
        markAll();
        markTShape();
        markSquare();
        bingoCard.unmarkNumber(1);
        assertEquals(9, bingoManager.countBingos(bingoCard));
    }
   
    //BingoManager Test8
    @Test
    void testBingoManager_RowPatternAndColumnPatternAndDiagonalPatternAndCustomPatternTShapeAndSquare_FullCardMarkedExcept2ndRow2ndColumn() {
        markAll();
        markTShape();
        markSquare();
        bingoCard.unmarkNumber(7);
        assertEquals(11, bingoManager.countBingos(bingoCard));
    }

    //BingoManager Test9
    @Test
    void testBingoManager_RowPatternAndColumnPatternAndDiagonalPatternAndCustomPatternTShapeAndSquare_FullCardMarkedExcept3rdRow2ndColumn() {
        markAll();
        markTShape();
        markSquare();
        bingoCard.unmarkNumber(12);
        assertEquals(12, bingoManager.countBingos(bingoCard));
    }


}

