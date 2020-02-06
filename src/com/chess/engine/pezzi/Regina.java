package com.chess.engine.pezzi;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Punto;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Regina extends Pezzo {

    private final static int [] CANDIDATE_VETTORI = {-9, -8, -7, -1, 1, 7, 8, 9};


    public Regina(final Colore pezzoColore,
                  final int pezzoPosizione) {

        super(TipoPezzo.regina,pezzoPosizione, pezzoColore, true);
    }

    public Regina(final Colore pezzoColore,
                  final int pezzoPosizione,
                  final boolean isPrimaMossa) {

        super(TipoPezzo.regina,pezzoPosizione, pezzoColore, isPrimaMossa);
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
    public Regina movePezzo(final Move move) {
        return new Regina(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }

    @Override
    public String toString(){
        return tipoPezzo.regina.toString();
    }

    private static boolean laPrimaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.PRIMA_COLONNA [posizioneCorrente] &&  (candidateOffset == -1 || candidateOffset == -9 || candidateOffset == 7);
    }
    private static boolean laOttavaColonnaEscluso(final int posizioneCorrente , int candidateOffset){
        return BoardUtils.OTTAVA_COLONNA [posizioneCorrente] &&  (candidateOffset == -7 || candidateOffset == 1 || candidateOffset == 9);
    }
}
