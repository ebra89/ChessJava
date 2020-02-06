package com.chess.engine.pezzi;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import java.util.Collection;

public abstract class Pezzo {

    protected final TipoPezzo tipoPezzo;
    protected final int pezzoPosizione;
    protected final Colore colorePezzo;
    protected final boolean isPrimaMossa;
    private final int cacheHashCod;

    Pezzo(final TipoPezzo tipoPezzo,
          final int pezzoPosizione,
          final Colore pezzoColore,
          final boolean isPrimaMossa){

        this.colorePezzo=pezzoColore;
        this.pezzoPosizione=pezzoPosizione;
        this.tipoPezzo = tipoPezzo;
        this.cacheHashCod = CompiutHashCode();
        this.isPrimaMossa=isPrimaMossa;
    }

    protected int CompiutHashCode(){                         // per non fare hashcode ogni volta !!
        int result = tipoPezzo.hashCode();
        result = 31 * result + colorePezzo.hashCode();
        result = 31 * result + pezzoPosizione;
        result = 31 * result + (isPrimaMossa ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof Pezzo)){
            return false;
        }
        final Pezzo otherPezzo = (Pezzo) other;
        return pezzoPosizione == otherPezzo.getPezzoPosizione() && tipoPezzo == otherPezzo.getTipoPezzo() &&
               colorePezzo == otherPezzo.getPezzoColore() && isPrimaMossa == otherPezzo.isPrimaMossa();
    }

    @Override
    public int hashCode(){
        return this.cacheHashCod;
    }

    public TipoPezzo getTipoPezzo(){
        return this.tipoPezzo;
    }

    public Colore getPezzoColore(){
        return this.colorePezzo;
    }

    public int getPezzoPosizione(){
        return this.pezzoPosizione;
    }
    public boolean isPrimaMossa(){
        return this.isPrimaMossa;
    }

    public abstract Collection<Move> calcolaLegalMoves(final Board board);

    public abstract Pezzo movePezzo(Move move);

    public int getPezzoValue(){
        return this.tipoPezzo.getPezzoValue();
    }

    public enum TipoPezzo {

        soldato( "P", 100){
            @Override
            public boolean isRe(){
                return false;
            }

            @Override
            public boolean isTorre(){
                return false;
            }
        },
        cavallo("N",300) {
            @Override
            public boolean isRe() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        },
        alfiere("B",300) {
            @Override
            public boolean isRe() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        },
        regina("Q", 900) {
            @Override
            public boolean isRe() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        },
        torre("R", 500) {
            @Override
            public boolean isRe() {
                return false;
            }

            @Override
            public boolean isTorre() {
                return true;
            }
        },
        re ("K", 1000){
            @Override
            public boolean isRe() {
                return true;
            }

            @Override
            public boolean isTorre() {
                return false;
            }
        };


        private String pezzoNome;
        private int pezzoValue;

        TipoPezzo(final String pezzoNome,
                  final int pezzoValue){

            this.pezzoNome = pezzoNome;
            this.pezzoValue = pezzoValue;
        }

        @Override
        public String toString(){

            return this.pezzoNome;
        }

        public abstract boolean isRe();

        public abstract boolean isTorre();

       public int getPezzoValue(){
           return this.pezzoValue;
       }
    }
}
