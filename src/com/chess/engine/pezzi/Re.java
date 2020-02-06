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

public class Re extends Pezzo {

    private final static int [] CANDIDATE_MOVE_VECTOR_CORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Re(final Colore pezzoColore,
              final int pezzoPosizione) {
        super(TipoPezzo.re,pezzoPosizione, pezzoColore, true);
    }

    public Re(final Colore pezzoColore,
              final int pezzoPosizione,
              final boolean isPrimaMossa) {
        super(TipoPezzo.re,pezzoPosizione, pezzoColore, isPrimaMossa);
    }

    @Override
    public Collection<Move> calcolaLegalMoves(Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int cordinateDiCandidatiOffset : CANDIDATE_MOVE_VECTOR_CORDINATES){
          final int  cordinatePossibiliDestinazioni = this.pezzoPosizione + cordinateDiCandidatiOffset;

          if(laPrimaColonnaEscluso(this.pezzoPosizione, cordinateDiCandidatiOffset) ||
             laOttavaColonnaEscluso(this.pezzoPosizione, cordinateDiCandidatiOffset)){
              continue;
          }

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
              }
          }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Re movePezzo(final Move move) {
        return new Re(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }

    @Override
    public String toString(){
        return tipoPezzo.re.toString();
    }

    private static boolean laPrimaColonnaEscluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.PRIMA_COLONNA [posizioneCorrente] && (possibiliOffset == -9 || possibiliOffset == -1||
                possibiliOffset == 7);
    }

    private static boolean laOttavaColonnaEscluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.OTTAVA_COLONNA[posizioneCorrente] && (possibiliOffset == -7 || possibiliOffset == 1 ||
                possibiliOffset == 9);
    }



}
