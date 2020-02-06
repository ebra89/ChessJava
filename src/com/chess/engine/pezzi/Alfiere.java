package com.chess.engine.pezzi;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Punto;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Alfiere extends Pezzo {


    private final static int [] CANDIDATE_VETTORI = {-9, -7, 7, 9};

    public Alfiere(final Colore pezzoColore,
                   final int pezzoPosizione) {

        super( TipoPezzo.alfiere,pezzoPosizione, pezzoColore,true);
    }

    public Alfiere(final Colore pezzoColore,
                   final int pezzoPosizione,
                   final boolean isPrimaMossa) {
        super( TipoPezzo.alfiere,pezzoPosizione, pezzoColore,isPrimaMossa);
    }


    @Override
    public Collection<Move> calcolaLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int cordinateDiCandidatiOffset : CANDIDATE_VETTORI){
            int cordinatePossibiliDestinazioni = this.pezzoPosizione;
            while (BoardUtils.cordinateSonoValide(cordinatePossibiliDestinazioni)){
                if(laPrimaColonnaEscluso(cordinatePossibiliDestinazioni, cordinateDiCandidatiOffset) ||
                     laOttavaColonnaEscluso(cordinatePossibiliDestinazioni, cordinateDiCandidatiOffset)){
                    break;
                }

                cordinatePossibiliDestinazioni += cordinateDiCandidatiOffset;
                if(BoardUtils.cordinateSonoValide(cordinatePossibiliDestinazioni)){
                    final Punto possibiliDestinazione = board.getPunto(cordinatePossibiliDestinazioni);
                    if (!possibiliDestinazione.isPunOccupato()) {
                        legalMoves.add(new MajorMove(board, this,cordinatePossibiliDestinazioni));
                    } else {
                        final Pezzo pezzoInDestinazione = possibiliDestinazione.getPezzo();
                        final Colore colorePezzo = pezzoInDestinazione.getPezzoColore();

                        if (this.colorePezzo != colorePezzo) {
                            legalMoves.add(new MajorAttackMove(board, this, cordinatePossibiliDestinazioni, pezzoInDestinazione));
                        }
                        break;
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Alfiere movePezzo(final Move move) {
        return new Alfiere(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }

    @Override
    public String toString(){
        return tipoPezzo.alfiere.toString();
    }

    private static boolean laPrimaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.PRIMA_COLONNA [posizioneCorrente] && (candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean laOttavaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.OTTAVA_COLONNA [posizioneCorrente] && (candidateOffset == -7 || candidateOffset == 9);
    }
}
