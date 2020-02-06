package com.chess.engine.player;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pezzi.Pezzo;
import com.chess.engine.pezzi.Re;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player {

    protected final Board board;
    protected final Re playerRe;
    protected final Collection<Move> legalMoves;
    private final boolean isInScacco;


    Player(final Board board,
           final Collection<Move> legalMoves,
           final Collection<Move> controMossa){

        this.board = board;
        this.playerRe = reStabilita();
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calcolaReCastles(legalMoves, controMossa)));
        this.isInScacco = !Player.calcolaPuntoDiAttacco(this.playerRe.getPezzoPosizione(), controMossa).isEmpty();
    }

    public Re getPlayerRe(){
        return this.playerRe;
    }
    public Collection<Move> getLegalMoves(){
        return this.legalMoves;
    }
    protected static Collection<Move> calcolaPuntoDiAttacco(int pezzoPosizione, Collection<Move> moves) {
         final List<Move> attackMoves = new ArrayList<>();
         for(final Move move : moves){
             if(pezzoPosizione == move.getCordinateDestinazione()){
                 attackMoves.add(move);
             }
         }
         return ImmutableList.copyOf(attackMoves);
    }

    private Re reStabilita() {
        for(final Pezzo pezzo : getPezziAttivi()){
            if (pezzo.getTipoPezzo().isRe()) {
                return (Re)pezzo;
            }
        }
        throw new RuntimeException("la mossa non e consentita!!!");
    }

    public boolean isMoveLegal(final Move move){
        return this.legalMoves.contains(move);
    }

    public boolean isInScacco(){

        return this.isInScacco;
    }

    public boolean isScaccoMato(){
        return this.isInScacco && !haAltreMossa();
    }

    public boolean isStalMato(){
        return !this.isInScacco && !haAltreMossa();
    }

    public boolean isCasteled(){
        return false;
    }


    protected boolean haAltreMossa() {
        for(final Move move : this.legalMoves){
            final MoveTransition transition = makeMove(move);
            if(transition.getMoveStatus().isDone()){
                return true;
            }
        }
        return false;
    }
    //todo non e finita
    public MoveTransition makeMove(final Move move){
        if(!isMoveLegal(move)) {
            return new MoveTransition(this.board, move, MoveStatus.MOSSA_ILLEGALE);
        }
            final Board transitionBoard = move.execute();

            final Collection <Move> attaccoAlRe = Player.calcolaPuntoDiAttacco(transitionBoard.correntPlayer().
                            getAvversario().getPlayerRe().getPezzoPosizione(),
                            transitionBoard.correntPlayer().getLegalMoves());
            if(!attaccoAlRe.isEmpty()){
                return new MoveTransition(this.board, move, MoveStatus.LASCIA_PLAYER_INSCACCO);
        }
            return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
    }


    public abstract Collection<Pezzo>getPezziAttivi();
    public abstract Colore getColore();
    public abstract Player getAvversario();
    protected abstract Collection<Move> calcolaReCastles(Collection<Move> playerLegals, Collection<Move> avversarioLegals);

}
