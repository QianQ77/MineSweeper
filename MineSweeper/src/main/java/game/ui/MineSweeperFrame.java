package game.ui;

import javax.swing.*;

import game.MineSweeper;
import game.MineSweeper.CellState;
import game.MineSweeper.GameStatus;

import game.ui.MineSweeperCell;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MineSweeperFrame extends JFrame {

    MineSweeper mineSweeper;

    @Override
    protected void frameInit(){
        super.frameInit();

        mineSweeper = new MineSweeper();
        mineSweeper.mineSweeperInit();
        mineSweeper.placeAllMines();
        mineSweeper.markAjacent();

        GridLayout gridLayout = new GridLayout(mineSweeper.SIZE, mineSweeper.SIZE);
        setLayout(gridLayout);

        for (int i = 0; i < mineSweeper.SIZE; i++) {
            for (int j = 0; j < mineSweeper.SIZE; j++) {
                MineSweeperCell cell = new MineSweeperCell(i, j);
                cell.setSize(50, 50);
                getContentPane().add(cell);
                cell.addMouseListener(new CellClickedHandler());
            }
        }
    }

    public void restart(){
        mineSweeper.mineSweeperInit();
        mineSweeper.placeAllMines();
        mineSweeper.markAjacent();

        Component[] components = this.getContentPane().getComponents();
        for(Component component : components) {
            if (component instanceof MineSweeperCell) {
                MineSweeperCell theCell = (MineSweeperCell) component;
                theCell.setText("");
            }
        }
    }

    public static void main(String[] args){
        MineSweeperFrame frame = new MineSweeperFrame();

        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public void playAgain(MineSweeperCell cell, MouseEvent mouseEvent){
        cell = (MineSweeperCell) mouseEvent.getSource();
        int option = JOptionPane.showOptionDialog(cell, "Would you like to play again?",
                "Play Again?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        if (option == JOptionPane.YES_OPTION) {
            restart();
        }
        else {
            System.exit(0);
        }
    }

    private class CellClickedHandler implements MouseListener{

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}


        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            MineSweeperCell cell = (MineSweeperCell) mouseEvent.getSource();

            int row = cell.row;
            int column = cell.column;

            if (mineSweeper.getGameStatus() == GameStatus.INPROGRESS) {
                if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                    mineSweeper.exposeCell(row, column);

                    Component[] components = cell.getParent().getComponents();
                    for (Component component : components) {
                        if (component instanceof MineSweeperCell) {
                            MineSweeperCell theCell = (MineSweeperCell) component;
                            CellState cellState = mineSweeper.getCellState(theCell.row, theCell.column);
                            if (cellState == CellState.EXPOSED) {

                                if (mineSweeper.ajacent[theCell.row][theCell.column] != 0) {
                                    theCell.setText(mineSweeper.ajacent[theCell.row][theCell.column] + "");
                                } else {
                                    theCell.setText("-");
                                }
                            }
                        }
                    }
                } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {

                    mineSweeper.toggleCell(row, column);

                    if (mineSweeper.getCellState(row, column) == CellState.SEALED) {
                        cell.setText("√");
                    } else
                        cell.setText("");
                }
                if (mineSweeper.getGameStatus() == GameStatus.LOST) {
                    cell.setText("M");
                    JOptionPane.showMessageDialog(cell, "EXPLOSION! You Lose!");
                    playAgain(cell, mouseEvent);
                }

                if (mineSweeper.getGameStatus() == GameStatus.WON && mineSweeper.getGameStatus() != GameStatus.LOST) {
                    JOptionPane.showMessageDialog(cell, "Congratulations! You Win!");
                    playAgain(cell, mouseEvent);
                }
            }
        }
    }

}
