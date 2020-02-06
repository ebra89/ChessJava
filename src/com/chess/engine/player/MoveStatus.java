package com.chess.engine.player;

public enum MoveStatus {

    DONE{
        @Override
        public boolean isDone() {
            return true;
        }
    },

    MOSSA_ILLEGALE {
        @Override
        public boolean isDone() {
            return false;
        }
    },

    LASCIA_PLAYER_INSCACCO {
        @Override
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();
}
