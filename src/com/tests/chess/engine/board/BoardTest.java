package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {


    @Test
    public void initialBoard(){
        final Board board = Board.creaStandardBoard();
        assertEquals(board.correntPlayer().getLegalMoves().size(), 20);
        assertEquals(board.correntPlayer().getAvversario().getLegalMoves().size(),20);
        assertFalse(board.correntPlayer().isInScacco());
        assertFalse(board.correntPlayer().isScaccoMato());
        assertFalse(board.correntPlayer().isCasteled());
    //    assertTrue(board.correntPlayer().isKingSideCastledCapable());
   //     assertTrue(board.correntPlayer().isQueenSideCastledCapable());
        assertEquals(board.correntPlayer(), board.playerBianco());
        assertEquals(board.correntPlayer().getAvversario(), board.playerNero());
        assertFalse(board.correntPlayer().getAvversario().isInScacco());
        assertFalse(board.correntPlayer().getAvversario().isScaccoMato());
        assertFalse(board.correntPlayer().getAvversario().isCasteled());
     //   assertTrue(board.correntPlayer().getAvversario().isKingSideCastledCapable());
    //    assertTrue(board.correntPlayer().getAvversario().isQueenSideCastledCapable());
    //    assertEquals(new StandardBoardEvaluator().evaluate(board, 0), 0);

    }


}