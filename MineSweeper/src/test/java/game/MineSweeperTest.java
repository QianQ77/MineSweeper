package game;

import org.junit.*;
import static org.junit.Assert.*;
import game.MineSweeper;
import game.MineSweeper.GameStatus;
import game.MineSweeper.CellState;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineSweeperTest {
    @Test
    public void testCanary(){
        assertTrue(true);
    }

    MineSweeper mineSweeper;
    boolean exposeNeighborsOfCalled = false;
    List<Integer> rowsAndColumns;

    class MineSweeperWithExposeCell extends MineSweeper{
        public void exposeCell(int row, int column){
            rowsAndColumns.add(row);
            rowsAndColumns.add(column);
        }
    }

    @Before
    public void SetUp(){
        mineSweeper = new MineSweeper();
        rowsAndColumns = new ArrayList<Integer>();
    }

    @Test
    public void exposeAnUnexposedCellTest(){
        mineSweeper.exposeCell(2, 5);

        assertEquals(MineSweeper.CellState.EXPOSED, mineSweeper.getCellState(2, 5));
    }

    @Test
    public void exposeAnExposedCellTest(){
        mineSweeper.exposeCell(2, 5);
        mineSweeper.exposeCell(2, 5);

        assertEquals(MineSweeper.CellState.EXPOSED, mineSweeper.getCellState(2, 5));
    }

    @Test
    public void exposeTwoCellTest(){
        mineSweeper.exposeCell(2, 0);
        mineSweeper.exposeCell(3, 5);

        assertEquals(MineSweeper.CellState.EXPOSED, mineSweeper.getCellState(2, 0));
        assertEquals(MineSweeper.CellState.EXPOSED, mineSweeper.getCellState(3, 5));
    }

    @Test
    public void exposeCellExposesItsNeighbors(){
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int column) {
                exposeNeighborsOfCalled = true;
            }

        };

        mineSweeper.ajacent[2][3] = 0;
        mineSweeper.mine[2][3] = false;
        mineSweeper.exposeCell(2, 3);

        assertTrue(exposeNeighborsOfCalled);
    }

    @Test
    public void callExposeCellTwiceTest(){
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int column){
                exposeNeighborsOfCalled = true;
            }
        };

        mineSweeper.exposeCell(2, 3);

        exposeNeighborsOfCalled = false;
        mineSweeper.exposeCell(2, 3);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void callExposeCellOnAjacent() {
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int column){
                exposeNeighborsOfCalled = true;
            }

        };

        mineSweeper.mine[2][3] = false;
        mineSweeper.ajacent[2][3] = 2;
        mineSweeper.exposeCell(2, 3);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void exposeNeighborsOfCallExpose(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(2, 3);

        assertEquals(Arrays.asList(1, 2, 1, 3, 1, 4, 2, 2, 2, 4, 3, 2, 3, 3, 3, 4), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfTopEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(0, 3);

        assertEquals(Arrays.asList(0, 2, 0, 4, 1, 2, 1, 3, 1, 4), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfRightEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(3, 9);

        assertEquals(Arrays.asList(2, 8, 2, 9, 3, 8, 4, 8, 4, 9), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfLeftEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(3, 0);

        assertEquals(Arrays.asList(2, 0, 2, 1, 3, 1, 4, 0, 4, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfBottomEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(9, 3);

        assertEquals(Arrays.asList(8, 2, 8, 3, 8, 4, 9, 2, 9, 4), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfTopLeftCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(0, 0);

        assertEquals(Arrays.asList(0, 1, 1, 0, 1, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfTopRightCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(0, 9);

        assertEquals(Arrays.asList(0, 8, 1, 8, 1, 9), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfBottomLeftCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(9, 0);

        assertEquals(Arrays.asList(8, 0, 8, 1, 9, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfBottomRightCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();

        mineSweeper.exposeNeighborsOf(9, 9);

        assertEquals(Arrays.asList(8, 8, 8, 9, 9, 8), rowsAndColumns);

    }


    @Test
    public void sealACellTest(){
        mineSweeper.toggleCell(2, 3);

        assertEquals(MineSweeper.CellState.SEALED, mineSweeper.getCellState(2, 3));
    }

    @Test
    public void exposeSealedCellTest(){
        mineSweeper.toggleCell(2, 3);

        mineSweeper.exposeCell(2, 3);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void toggleASealedCellTest(){
        mineSweeper.toggleCell(2, 0);
        mineSweeper.toggleCell(2, 0);

        assertEquals(MineSweeper.CellState.HIDDEN, mineSweeper.getCellState(2, 0));
    }

    @Test
    public void exposeAfterExposeMineTest(){
        mineSweeper.mine[2][3] = true;
        mineSweeper.exposeCell(2, 3);
        mineSweeper.exposeCell(3, 4);

        assertEquals(MineSweeper.CellState.HIDDEN, mineSweeper.getCellState(3, 4));
    }

    @Test
    public void sealAfterExposeMineTest(){
        mineSweeper.mine[2][3] = true;
        mineSweeper.exposeCell(2, 3);
        mineSweeper.toggleCell(0, 0);

        assertEquals(MineSweeper.CellState.HIDDEN, mineSweeper.getCellState(0, 0));
    }

    @Test
    public void isAnAdjacentTest() {
        mineSweeper.mine[0][0] = true;

        mineSweeper.markAjacent();

        assertEquals(mineSweeper.ajacent[1][1], 1);
    }


    @Test
    public void placeAllMinesTest() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                mineSweeper.mine[i][j] = false;
            }
        }

        int numberOfMinesPlaced = 0;
        mineSweeper.placeAllMines();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(mineSweeper.mine[i][j])
                    numberOfMinesPlaced++;
            }
        }

        assertEquals(mineSweeper.numberOfMines, numberOfMinesPlaced);
    }

    @Test
    public void gameInProgressWhenStarted(){
        assertEquals(GameStatus.INPROGRESS, mineSweeper.getGameStatus());
    }

    @Test
    public void gameLostWhenAMineExposed(){
        mineSweeper.mine[3][4] = true;
        mineSweeper.exposeCell(3, 4);

        assertEquals(GameStatus.LOST, mineSweeper.getGameStatus());
    }

    @Test
    public void gameWonTest(){
        mineSweeper.mineSweeperInit();
        mineSweeper.placeAllMines();
        mineSweeper.markAjacent();

        for (int i = 0; i < mineSweeper.SIZE; i++) {
            for (int j = 0; j < mineSweeper.SIZE; j++) {
                if(mineSweeper.mine[i][j])
                    mineSweeper.toggleCell(i, j);
                else
                    mineSweeper.exposeCell(i, j);
            }
        }

        assertEquals(GameStatus.WON, mineSweeper.getGameStatus());
    }

    @Test
    public void gameInProgressTest(){
        mineSweeper.cellState[3][4] = CellState.HIDDEN;

        assertEquals(GameStatus.INPROGRESS, mineSweeper.getGameStatus());
    }

    @Test
    public void sealAllCellTest(){

        for (int i = 0; i < mineSweeper.SIZE; i++) {
            for (int j = 0; j < mineSweeper.SIZE; j++) {
                mineSweeper.cellState[i][j] = CellState.SEALED;

                assertEquals(GameStatus.INPROGRESS, mineSweeper.getGameStatus());
            }
        }
    }
}