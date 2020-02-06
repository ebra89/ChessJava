package com.chess.engine.board;

import com.chess.engine.pezzi.Pezzo;
import com.chess.engine.pezzi.Soldato;
import com.chess.engine.pezzi.Torre;

import static com.chess.engine.board.Board.*;

public abstract class Move {
    protected final Board board;
    protected final Pezzo movedPezzo;
    protected final int cordinateDestinazione;
    protected final boolean isPrimaMossa;

    public static final Move NULL_MOVE = new NullMove();

    private Move( final Board board,
                  final Pezzo movedPezzo,
                  final int cordinateDestinazione){
                  this.board = board;
                  this.movedPezzo = movedPezzo;
                  this.cordinateDestinazione = cordinateDestinazione;
                  this.isPrimaMossa = movedPezzo.isPrimaMossa();
    }

    private Move(final Board board,
                 final int cordinateDestinazione){

        this.board = board;
        this.cordinateDestinazione = cordinateDestinazione;
        this.movedPezzo = null;
        this.isPrimaMossa = false;
    }

    @Override
    public int hashCode(){
      final int prime = 31;
      int result = 1;
      result = prime * result + this.cordinateDestinazione;
      result = prime * result + this.movedPezzo.hashCode();
      result = prime * result * this.movedPezzo.getPezzoPosizione();
      return result;
    }

    @Override
    public boolean equals(final Object other){
       if(this == other){
           return true;
       }
       if(!(other instanceof Move)){
           return false;
       }
       final Move otherMove = (Move) other;
       return  getCorrentCordinate() == otherMove.getCorrentCordinate() &&
               getCordinateDestinazione() == otherMove.getCordinateDestinazione() &&
               getMovedPezzo().equals(otherMove.getMovedPezzo());

    }

    public int getCordinateDestinazione(){
       return this.cordinateDestinazione;
    }

    public int getCorrentCordinate(){
        return this.getMovedPezzo().getPezzoPosizione();
    }

    public Pezzo getMovedPezzo(){
        return this.movedPezzo;
    }

    public Board getBoard(){
        return this.board;
    }

    public boolean isAttack(){
       return false;
    }

    public boolean isCastlMove(){
       return false;
    }

    public Pezzo getAttackedPezzo(){
       return null;
    }

    public Board execute() {
        final Builder builder = new Builder();
        for(final Pezzo pezzo : this.board.correntPlayer().getPezziAttivi()){
            if(!this.movedPezzo.equals(pezzo)){
                builder.setPezzo(pezzo);
            }
        }
        for(final Pezzo pezzo : this.board.correntPlayer().getAvversario().getPezziAttivi()){
            builder.setPezzo(pezzo);
        }
        //la nuova board con le mosse aggiornate viene creato
        builder.setPezzo(this.movedPezzo.movePezzo(this));
        builder.setMossaMaker(this.board.correntPlayer().getAvversario().getColore());
        return builder.build();
    }


    public static final class MajorMove extends Move{         //cambiato qua!!

        public MajorMove(final Board board,
                         final Pezzo movedPezzo,
                         final int cordinateDestinazione) {
            super(board, movedPezzo, cordinateDestinazione);
        }

        @Override
        public boolean equals(final Object other){

            return this == other || other instanceof MajorMove && super.equals(other);
        }
        @Override
        public String toString(){
            return movedPezzo.getTipoPezzo().toString() + BoardUtils.getPosizioneAlCordinate(this.cordinateDestinazione);
        }
    }

    public static class AttackMove extends Move{
       final Pezzo attackedPezzo;
       public AttackMove(final Board board,
                         final Pezzo movedPezzo,
                         final int cordinateDestinazione,
                         final Pezzo attackedPezzo) {
            super(board, movedPezzo, cordinateDestinazione);
            this.attackedPezzo = attackedPezzo;
        }
        @Override
        public int hashCode(){
           return this.attackedPezzo.hashCode() + super.hashCode();
        }
        @Override
        public boolean equals(final Object other){
           if(this == other){
               return true;
           }
           if(!(other instanceof AttackMove)){
               return false;
           }
           final AttackMove otherAttackMove = (AttackMove) other;
           return super.equals(otherAttackMove) && getAttackedPezzo().equals(otherAttackMove.getAttackedPezzo());
        }

        @Override
        public boolean isAttack(){
           return true;
        }
        @Override
        public Pezzo getAttackedPezzo() {
           return this.attackedPezzo;
        }
    }

    public static class MajorAttackMove extends AttackMove{

        public MajorAttackMove(final Board board,
                               final Pezzo movedPezzo,
                               final int cordinateDestinazione,
                               final Pezzo pezzoAttecked) {
            super(board, movedPezzo, cordinateDestinazione, pezzoAttecked);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof  MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString(){
            return movedPezzo.getTipoPezzo() + BoardUtils.getPosizioneAlCordinate(this.cordinateDestinazione);
        }
    }

    public static final class PromotionSoldato extends Move {

        final Move decoratedMove;
        final Soldato promotedSoldato;

        public PromotionSoldato(final Move decoratedMove) {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPezzo(), decoratedMove.getCordinateDestinazione());
            this.decoratedMove = decoratedMove;
            this.promotedSoldato = (Soldato)decoratedMove.getMovedPezzo();
        }

        @Override
        public int hashCode(){
            return decoratedMove.hashCode() + (31 * promotedSoldato.hashCode());
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof PromotionSoldato && super.equals(other);
        }

        @Override
        public Pezzo getAttackedPezzo(){
            return this.decoratedMove.getAttackedPezzo();
        }

        @Override
        public boolean isAttack(){
            return this.decoratedMove.isAttack();
        }

        @Override
        public Board execute(){
            final Board soldatoMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Builder();
            for(final Pezzo pezzo : soldatoMovedBoard.correntPlayer().getPezziAttivi()){
                if(!this.promotedSoldato.equals(pezzo)){
                    builder.setPezzo(pezzo);
                }
            }
            for(final Pezzo pezzo : soldatoMovedBoard.correntPlayer().getAvversario().getPezziAttivi()){
                builder.setPezzo(pezzo);
            }
            builder.setPezzo(this.promotedSoldato.getPromotionPezzo().movePezzo(this));
            builder.setMossaMaker(soldatoMovedBoard.correntPlayer().getColore());
            return builder.build();
        }

        @Override
        public String toString(){
            return "";
        }
    }

    public static final class MoveSoldato extends Move {
        public MoveSoldato(final Board board,
                          final Pezzo movePezzo,
                          final int cordinateDestinazione) {
            super(board, movePezzo, cordinateDestinazione);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof MoveSoldato && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPosizioneAlCordinate(this.cordinateDestinazione);
        }
    }

    public static class AttackMoveSoldato extends AttackMove {
        public AttackMoveSoldato(final Board board,
                                final Pezzo movePezzo,
                                final int cordinateDestinazione,
                                final Pezzo attackedPezzo) {
            super(board, movePezzo, cordinateDestinazione, attackedPezzo);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof AttackMoveSoldato && super.equals(other);
        }

        @Override
        public String toString(){
            return BoardUtils.getPosizioneAlCordinate(this.movedPezzo.getPezzoPosizione()).substring(0,1)+ "x" +
                    BoardUtils.getPosizioneAlCordinate(this.cordinateDestinazione);
        }
    }
    public static final class SpecialAttackMoveSoldato extends AttackMoveSoldato {
        public SpecialAttackMoveSoldato(final Board board,
                                        final Pezzo movePezzo,
                                        final int cordinateDestinazione,
                                        final Pezzo attackedPezzo) {
            super(board, movePezzo, cordinateDestinazione, attackedPezzo);
        }

        @Override
        public boolean equals(final Object other){
            return this == other || other instanceof SpecialAttackMoveSoldato && super.equals(other);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Pezzo pezzo : this.board.correntPlayer().getPezziAttivi()){
                if(!this.movedPezzo.equals(pezzo)){
                    builder.setPezzo(pezzo);
                }
            }
            for(final Pezzo pezzo : this.board.correntPlayer().getAvversario().getPezziAttivi()){
                if(!pezzo.equals(this.getAttackedPezzo())){
                    builder.setPezzo(pezzo);
                }
            }
            builder.setPezzo(this.movedPezzo.movePezzo(this));
            builder.setMossaMaker(this.board.correntPlayer().getAvversario().getColore());
            return builder.build();
        }
    }
    public static final class JumpSoldato extends Move {
        public JumpSoldato(final Board board,
                           final Pezzo movePezzo,
                           final int cordinateDestinazione) {
            super(board, movePezzo, cordinateDestinazione);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for(final Pezzo pezzo : this.board.correntPlayer().getPezziAttivi()){
                if(!this.movedPezzo.equals(pezzo)){
                    builder.setPezzo(pezzo);
                }
            }
            for(final Pezzo pezzo : this.board.correntPlayer().getAvversario().getPezziAttivi()){
                builder.setPezzo(pezzo);
            }
            final Soldato movedSoldato = (Soldato)this.movedPezzo.movePezzo(this);
            builder.setPezzo(movedSoldato);
            builder.setEnPassantSoldato(movedSoldato);
            builder.setMossaMaker(this.board.correntPlayer().getAvversario().getColore());
            return builder.build();
        }

        @Override
        public String toString(){
            return BoardUtils.getPosizioneAlCordinate(this.cordinateDestinazione);
        }
    }
    static abstract class CastelMove extends Move {

       private final Torre castleTorre;
       private final int castleTorreStart;
       private final int castlTorreDestinazione;

       public CastelMove(final Board board,
                          final Pezzo movePezzo,
                          final int cordinateDestinazione,
                          final Torre castleTorre,
                          final int castleTorreStart,
                          final int castlTorreDestinazione) {
            super(board, movePezzo, cordinateDestinazione);
            this.castleTorre = castleTorre;
            this.castleTorreStart = castleTorreStart;
            this.castlTorreDestinazione = castlTorreDestinazione;
        }

        public Torre getCastleTorre(){
            return this.castleTorre;
        }

        @Override
        public boolean isCastlMove(){
            return true;
        }

        @Override
        public Board execute() {
                                                                        // metodo move
            final Builder builder = new Builder();
            for (final Pezzo pezzo : this.board.correntPlayer().getPezziAttivi()) {
                if (!this.movedPezzo.equals(pezzo) && !this.castleTorre.equals(pezzo)) {
                    builder.setPezzo(pezzo);
                }
            }
            for (final Pezzo pezzo : this.board.correntPlayer().getAvversario().getPezziAttivi()) {
                builder.setPezzo(pezzo);
            }
            builder.setPezzo(this.movedPezzo.movePezzo(this));
            builder.setPezzo(new Torre(this.castleTorre.getPezzoColore(), this.castlTorreDestinazione));
            builder.setMossaMaker(this.board.correntPlayer().getAvversario().getColore());
            return builder.build();
        }
        @Override
        public int hashCode(){
           final int prime = 31;
           int result = super.hashCode();
           result = prime * result + this.castleTorre.hashCode();
           result = prime * result + this.cordinateDestinazione;
           return result;
       }

       @Override
        public boolean equals (final Object other){
           if( this == other){
               return true;
           }
           if(!(other instanceof CastelMove)){
               return false;
           }
           final CastelMove otherCastelMove = (CastelMove)other;
           return super.equals(otherCastelMove) && this.castleTorre.equals(otherCastelMove.getCastleTorre());
       }



    }
    public static final class KingSideCastelMove extends CastelMove {
        public KingSideCastelMove(final Board board,
                                  final Pezzo movePezzo,
                                  final int cordinateDestinazione,
                                  final Torre castleTorre,
                                  final int castleTorreStart,
                                  final int castlTorreDestinazione) {
            super(board, movePezzo, cordinateDestinazione, castleTorre,castleTorreStart, castlTorreDestinazione);
        }

        @Override
        public boolean equals(final Object other){
           return this == other || other instanceof KingSideCastelMove && super.equals(other);
        }

        @Override
        public String toString(){
            return "o-o";
        }
    }

    public static final class QueenSideCastelMove extends CastelMove {
        public QueenSideCastelMove(final Board board,
                                   final Pezzo movePezzo,
                                   final int cordinateDestinazione,
                                   final Torre castleTorre,
                                   final int castleTorreStart,
                                   final int castlTorreDestinazione) {
            super(board, movePezzo, cordinateDestinazione, castleTorre, castleTorreStart, castlTorreDestinazione);
        }

        @Override
        public boolean equals(final Object other){
           return this == other || other instanceof QueenSideCastelMove && super.equals(other);
        }

        @Override
        public String toString(){
            return "o-o-o";
        }
    }

    public static final class NullMove extends Move {
        public NullMove() {

            super(null, 65);
        }
        @Override
        public Board execute() {
            throw new RuntimeException("non si puo eseguire null move");
        }

        @Override
        public int getCorrentCordinate(){
            return -1;
        }
    }

    public static class MoveFactory {

        private MoveFactory() {
            throw new RuntimeException("non istanziabile!");
        }
        public static Move createMove(final Board bord,
                                      final int correntCordinate,
                                      final int cordinateDestinazione) {
            for (final Move move : bord.getAllLegalMoves()) {
                if (move.getCorrentCordinate() == correntCordinate &&
                    move.getCordinateDestinazione() == cordinateDestinazione) {
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}