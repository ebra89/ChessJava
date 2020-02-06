package com.chess.engine.player;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {

    private final Board boardTransition;
    private final Move move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board boardTransition,
                          final Move move,
                          final MoveStatus moveStatus){

        this.boardTransition = boardTransition;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus(){
        return this.moveStatus;
    }
    public Board getBoardTransition(){
        return this.boardTransition;
    }
}
