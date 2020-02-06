package com.tests.chess.engine.board;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.Movestrategy;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class TestFoolsMate {


    @Test
    public void testFoolsMate(){

        final Board board = Board.creaStandardBoard();
        final MoveTransition t1 = board.correntPlayer()
                .makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCordinateDelPosizione("f2"),
                        BoardUtils.getCordinateDelPosizione("f3")));


        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getBoardTransition()
                .correntPlayer()
                .makeMove(Move.MoveFactory.createMove(t1.getBoardTransition(), BoardUtils.getCordinateDelPosizione("e7"),
                        BoardUtils.getCordinateDelPosizione("e5")));

        assertTrue(t2.getMoveStatus().isDone());


        final MoveTransition t3 = t2.getBoardTransition()
                .correntPlayer()
                .makeMove(Move.MoveFactory.createMove(t2.getBoardTransition(), BoardUtils.getCordinateDelPosizione("g2"),
                        BoardUtils.getCordinateDelPosizione("g4")));


        assertTrue(t3.getMoveStatus().isDone());

        final Movestrategy movestrategy = new MiniMax(4);

      //  final Move aiMove = movestrategy.execute(t3.getBoardTransition());

        final Move bestMove = Move.MoveFactory.createMove(t3.getBoardTransition(), BoardUtils.getCordinateDelPosizione("d8"),
                BoardUtils.getCordinateDelPosizione("h4"));

      //  assertEquals(aiMove, bestMove);
    }

}
