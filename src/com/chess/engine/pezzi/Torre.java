
package com.chess.engine.pezzi;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorAttackMove;
import com.chess.engine.board.Punto;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Torre extends Pezzo {

    private static final int[] CANDIDATE_VETTORI = {-8, -1, 1, 8};

    public Torre(final Colore pezzoColore,
                 final int pezzoPosizione) {

        super(TipoPezzo.torre,pezzoPosizione, pezzoColore, true);
    }

    public Torre(final Colore pezzoColore,
                 final int pezzoPosizione,
                 final boolean isPrimaMossa) {

        super(TipoPezzo.torre,pezzoPosizione, pezzoColore, isPrimaMossa);
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
                        legalMoves.add(new Move.MajorMove(board, this,cordinatePossibiliDestinazioni));
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
    public Torre movePezzo(final Move move) {
        return new Torre(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }

    @Override
    public String toString(){
        return tipoPezzo.torre.toString();
    }

    private static boolean laPrimaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.PRIMA_COLONNA [posizioneCorrente] &&  (candidateOffset == -1);
    }
    private static boolean laOttavaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.OTTAVA_COLONNA [posizioneCorrente] &&  (candidateOffset == 1);
    }
}
