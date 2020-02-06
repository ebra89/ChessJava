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

public class PlayerNero extends Player{

    public PlayerNero(final Board board,
                      final Collection<Move> mosseLegaliStandardBianchi,
                      final Collection<Move> mosseLegaliStandardNeri ){
        super(board,mosseLegaliStandardNeri, mosseLegaliStandardBianchi);
    }

    @Override
    public Collection<Pezzo> getPezziAttivi() {
        return this.board.getPezziNeri();
    }

    @Override
    public Colore getColore() {
        return Colore.NERO;
    }

    @Override
    public Player getAvversario() {
        return this.board.playerBianco();
    }

    @Override
    protected Collection<Move> calcolaReCastles(final Collection<Move> playerLegals,
                                                final Collection<Move> avversarioLegals) {
        final List<Move> reCastles = new ArrayList<>();
        if(this.playerRe.isPrimaMossa() && !this.isInScacco()){
            //re nero castling
            if(!this.board.getPunto(5).isPunOccupato() &&
                !this.board.getPunto(6).isPunOccupato()){
                final Punto torrePunto = this.board.getPunto(7);
                if(torrePunto.isPunOccupato() && torrePunto.getPezzo().isPrimaMossa()){
                    if(Player.calcolaPuntoDiAttacco(5, avversarioLegals).isEmpty() &&
                            Player.calcolaPuntoDiAttacco(6, avversarioLegals).isEmpty() &&
                            torrePunto.getPezzo().getTipoPezzo().isTorre()){
                            reCastles.add(new KingSideCastelMove(this.board, this.playerRe, 6, (Torre)torrePunto.getPezzo(),
                             torrePunto.getCordinatePunto(), 5));
                    }
                }
            }
            if(!this.board.getPunto(1).isPunOccupato() &&
               !this.board.getPunto(2).isPunOccupato()&&
               !this.board.getPunto(3).isPunOccupato()){
                final Punto puntoTorre = this.board.getPunto(0);
                if(puntoTorre.isPunOccupato() && puntoTorre.getPezzo().isPrimaMossa() &&
                        Player.calcolaPuntoDiAttacco(2, avversarioLegals).isEmpty() &&
                        Player.calcolaPuntoDiAttacco(3,avversarioLegals).isEmpty() &&
                        puntoTorre.getPezzo().getTipoPezzo().isTorre()){
                        reCastles.add(new QueenSideCastelMove(this.board, this.playerRe, 2,(Torre)puntoTorre.getPezzo(),
                        puntoTorre.getCordinatePunto(), 3));
                }
            }
        }
        return ImmutableList.copyOf(reCastles);
    }
}
