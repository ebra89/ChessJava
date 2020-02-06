package com.chess.engine.player.ai;

import com.chess.engine.board.Board;
import com.chess.engine.pezzi.Pezzo;
import com.chess.engine.player.Player;

public final class StandardBoardEvaluator implements BoardEvaluator {

    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTEL_BONUS = 60;

    @Override
    public int evaluate(final Board board,
                        final int depth) {
        return scorePlayer(board, board.playerBianco(), depth) -
               scorePlayer(board, board.playerNero(), depth);
    }

    private int scorePlayer(final Board board,
                            final Player player,
                            final int depth){
        return pezzoValue(player) + mobility(player) + check(player) + checkmate(player, depth) + castled(player);
    }

    private static int castled(final Player player) {
        return player.isCasteled() ? CASTEL_BONUS : 0;
    }

    private static int checkmate(final Player player,
                                 final int depth) {
        return player.getAvversario().isScaccoMato() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int check(final Player player) {
        return player.getAvversario().isInScacco() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) {
        return player.getLegalMoves().size();
    }

    private static int pezzoValue(final Player player){
        int pezzoValueScore = 0;
        for(final Pezzo pezzo : player.getPezziAttivi()){
            pezzoValueScore += pezzo.getPezzoValue();
        }
        return pezzoValueScore;
    }
}
