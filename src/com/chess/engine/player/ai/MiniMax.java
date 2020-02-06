package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.player.MoveTransition;

public class MiniMax implements Movestrategy {

    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MiniMax(final int searchDepth){
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString(){

        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {

        final long startTime = System.currentTimeMillis();
        Move bestMove = null;

        int lowestSeenValue = Integer.MAX_VALUE;
        int highestSeenValue = Integer.MIN_VALUE;
        int correntValue;

        System.out.println(board.correntPlayer() + "Thinking with depth "+ this.searchDepth);
        int numMoves = board.correntPlayer().getLegalMoves().size();

        for(final Move move : board.correntPlayer().getLegalMoves()){

            final MoveTransition moveTransition = board.correntPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){

                correntValue = board.correntPlayer().getColore().isBianco() ?
                        min(moveTransition.getBoardTransition(), searchDepth-1):
                        max(moveTransition.getBoardTransition(), searchDepth -1);

                if(board.correntPlayer().getColore().isBianco() && correntValue >= highestSeenValue){
                    highestSeenValue = correntValue;
                    bestMove = move;

                }else if (board.correntPlayer().getColore().isNero() && correntValue <= lowestSeenValue){
                    lowestSeenValue = correntValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    public static boolean isEndOfGame(final Board board){
        return board.correntPlayer().isScaccoMato() ||
                board.correntPlayer().isStalMato();
    }


    public int min(final Board board,
                   final int depth){
        if(depth == 0 /* game over */ || isEndOfGame(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for(final Move move : board.correntPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.correntPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int correntValue = max(moveTransition.getBoardTransition(), depth -1);
                if(correntValue <= lowestSeenValue){
                    lowestSeenValue = correntValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board,
                   final int depth){
        if(depth == 0 /* game over */ || isEndOfGame(board)){
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for(final Move move : board.correntPlayer().getLegalMoves()){
            final MoveTransition moveTransition = board.correntPlayer().makeMove(move);
            if(moveTransition.getMoveStatus().isDone()){
                final int correntValue = min(moveTransition.getBoardTransition(), depth -1);
                if(correntValue >= highestSeenValue){
                    highestSeenValue = correntValue;
                }
            }
        }
        return highestSeenValue;
    }
}
