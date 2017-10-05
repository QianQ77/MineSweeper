package game;


public class MineSweeper {
    public int SIZE = 10;
    public enum CellState {HIDDEN, EXPOSED, SEALED};
    public enum GameStatus {INPROGRESS, LOST, WON};
    public boolean mine[][] = new boolean[SIZE][SIZE];
    public int ajacent[][] = new int[SIZE][SIZE];

    CellState cellState[][] = new CellState[SIZE][SIZE];
    final int numberOfMines = 15;
    GameStatus gameStatus = GameStatus.INPROGRESS;

    public MineSweeper() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cellState[i][j] = CellState.HIDDEN;
                mine[i][j] = false;
            }
        }
    }

    public void mineSweeperInit(){
        mine  = new boolean[SIZE][SIZE];
        ajacent = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cellState[i][j] = CellState.HIDDEN;
                mine[i][j] = false;
            }
        }
        gameStatus = GameStatus.INPROGRESS;
    }

    public void placeAllMines() {
        int i = 1;
        while(i <= numberOfMines) {
            int row = (int) (Math.random() * SIZE);
            int column = (int)(Math.random() * SIZE);

            if(!mine[row][column]) {
                mine[row][column] = true;
                i++;
            }
        }
    }


    public void markAjacent(){
        for (int i = 0; i < mine.length; i++) {
            for (int j = 0; j < mine.length; j++) {
                if (!mine[i][j]) {
                    ajacent[i][j] = countMinesAround(i, j);
                }
            }
        }
    }

    public void exposeCell(int row, int column){
        if(getCellState(row, column) == CellState.HIDDEN && getGameStatus()!=GameStatus.LOST) {
            cellState[row][column] = CellState.EXPOSED;
            if(mine[row][column]){
                gameStatus = GameStatus.LOST;

            }else if(ajacent[row][column] == 0) {
                exposeNeighborsOf(row, column);
            }
        }
    }

    public void toggleCell(int row, int column){
        CellState state = getCellState(row, column);
        if(getGameStatus() != GameStatus.LOST){
            if(state == CellState.HIDDEN){
                cellState[row][column] = CellState.SEALED;
            }
            else if(state == CellState.SEALED) {
                cellState[row][column] = CellState.HIDDEN;
            }
        }
    }

    public CellState getCellState(int row, int column) {
        return cellState[row][column];
    }

    public void exposeNeighborsOf(int row, int column){
        for(int i = row-1; i <= row+1; i++){
            if(i >=0 && i <= SIZE-1){
                for(int j = column-1; j <= column+1; j++){
                    if(j >=0 && j <= SIZE-1){
                        if(i!=row || j!=column){
                            exposeCell(i, j);
                        }
                    }
                }
            }
        }
    }

    public int countMinesAround(int row, int column){
        int numberOfMinesFound = 0;

        for(int i = row-1; i <= row+1; i++){
            if(i >=0 && i <= SIZE-1){
                for(int j = column-1; j <= column+1; j++){
                    if(j >=0 && j <= SIZE-1){
                        if(i!=row || j!=column){
                            if(mine[i][j])
                                numberOfMinesFound++;
                        }
                    }
                }
            }
        }
        return numberOfMinesFound;
    }

    public GameStatus getGameStatus(){
        int sealedCells = 0;

        if(gameStatus == GameStatus.LOST)
            return GameStatus.LOST;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(cellState[i][j] == CellState.HIDDEN)
                    return GameStatus.INPROGRESS;
                if(cellState[i][j] == CellState.SEALED)
                    sealedCells ++;
            }
        }

        if(sealedCells == numberOfMines)
            return GameStatus.WON;
        else
            return GameStatus.INPROGRESS;
    }
}

