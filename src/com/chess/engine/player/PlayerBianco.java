package com.chess.engine.player;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Punto;
import com.chess.engine.pezzi.Pezzo;
import com.chess.engine.pezzi.Torre;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class PlayerBianco extends Player{
    public PlayerBianco(final Board board,
                        final Collection<Move> mosseLegaliStandardBianchi,
                        final Collection<Move> mosseLegaliStandardNeri) {
        super(board,mosseLegaliStandardBianchi,mosseLegaliStandardNeri);
    }
    @Override
    public Collection<Pezzo> getPezziAttivi() {
        return this.board.getPezziBianchi();
    }
    @Override
    public Colore getColore() {
        return Colore.BIANCO;
    }
    @Override
    public Player getAvversario() {
        return this.board.playerNero();
    }
    @Override
    protected Collection<Move> calcolaReCastles(final Collection<Move> playerLegals,
                                                final Collection<Move> avversarioLegals) {
        final List<Move> reCastles = new ArrayList<>();
        if(this.playerRe.isPrimaMossa() && !this.isInScacco()){
           //re bianco arrocco
            if(!this.board.getPunto(61).isPunOccupato() &&
               !this.board.getPunto(62).isPunOccupato()){
                final Punto torrePunto = this.board.getPunto(63);
                if(torrePunto.isPunOccupato() && torrePunto.getPezzo().isPrimaMossa()){
                    if(Player.calcolaPuntoDiAttacco(61, avversarioLegals).isEmpty() &&
                       Player.calcolaPuntoDiAttacco(62, avversarioLegals).isEmpty() &&
                       torrePunto.getPezzo().getTipoPezzo().isTorre()){
                       reCastles.add(new KingSideCastelMove(this.board,
                                                                  this.playerRe, 62,
                                                                  (Torre) torrePunto.getPezzo(),
                                                                   torrePunto.getCordinatePunto(), 61));
                    }
                }
            }
            if(!this.board.getPunto(59).isPunOccupato()&&
            !this.board.getPunto(58).isPunOccupato()&&
            !this.board.getPunto(57).isPunOccupato()){
            final Punto puntoTorre = this.board.getPunto(56);
            if(puntoTorre.isPunOccupato() && puntoTorre.getPezzo().isPrimaMossa() &&
            Player.calcolaPuntoDiAttacco(58,avversarioLegals).isEmpty() &&
            Player.calcolaPuntoDiAttacco(59,avversarioLegals).isEmpty() &&
            puntoTorre.getPezzo().getTipoPezzo().isTorre()){
                reCastles.add(new QueenSideCastelMove(this.board, this.playerRe, 58,
                        (Torre)puntoTorre.getPezzo(),puntoTorre.getCordinatePunto(),59));
             }
           }
        }
        return ImmutableList.copyOf(reCastles);
    }
}
