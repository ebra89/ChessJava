package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerBianco;
import com.chess.engine.player.PlayerNero;

public enum Colore {

    BIANCO {
        @Override
       public int getDirezione() {
            return -1;
        }
        @Override
        public int getDirezioneOpposta(){
            return 1;
        }

        @Override
        public boolean isNero() {

            return false;
        }

        @Override
        public boolean isBianco() {

            return true;
        }

        @Override
        public boolean isPromotionSoldato(int posizione) {
            return BoardUtils.OTTAVA_RANK[posizione];
        }

        @Override
        public Player scegliPlayer(final PlayerBianco playerBianco, final PlayerNero playerNero){
            return playerBianco;
        }
    },
    NERO {
        @Override
       public  int getDirezione() {
            return 1;
        }

        @Override
        public int getDirezioneOpposta(){
            return -1;
        }

        @Override
        public boolean isNero() {
            return true;
        }

        @Override
        public boolean isBianco() {
            return false;
        }

        @Override
        public boolean isPromotionSoldato(int posizione) {
            return BoardUtils.PRIMO_RANK[posizione];
        }

        @Override
        public Player scegliPlayer(final PlayerBianco playerBianco, final PlayerNero playerNero) {
            return playerNero;
        }
    };

   public abstract int getDirezione();
   public abstract int getDirezioneOpposta();
   public abstract boolean isNero();
   public abstract boolean isBianco();

   public abstract boolean isPromotionSoldato(int posizione);
   public abstract Player scegliPlayer(PlayerBianco playerBianco, PlayerNero playerNero);
}
