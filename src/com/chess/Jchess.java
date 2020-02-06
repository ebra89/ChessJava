package com.chess;

import com.chess.engine.board.Board;
import com.chess.gui.Table;

public class Jchess {


    public static void main(String[] args) {

        Board board = Board.creaStandardBoard();

        Table.get().show();
        System.out.println(board);
    }
}
