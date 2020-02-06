package com.chess.engine.pezzi;

import com.chess.engine.Colore;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.JumpSoldato;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.chess.engine.board.Move.*;

public class Soldato extends Pezzo {


    private static final int[] CANDIDATE_VETTORI = {8, 16, 7, 9};

    public Soldato(final Colore pezzoColore,
                   final int pezzoPosizione) {

        super(TipoPezzo.soldato,pezzoPosizione, pezzoColore, true);
    }

    public Soldato(final Colore pezzoColore,
                   final int pezzoPosizione,
                   final boolean isPrimaMossa) {

        super(TipoPezzo.soldato,pezzoPosizione, pezzoColore, isPrimaMossa);
    }


    @Override
    public Collection<Move> calcolaLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int cordinateDiCandidatiOffset : CANDIDATE_VETTORI){
           final int cordinatePossibiliDestinazioni = this.pezzoPosizione + (this.colorePezzo.getDirezione()* cordinateDiCandidatiOffset);

            if(!BoardUtils.cordinateSonoValide(cordinatePossibiliDestinazioni)){
                continue;
            }
            if(cordinateDiCandidatiOffset == 8 && !board.getPunto(cordinatePossibiliDestinazioni).isPunOccupato()){
                if(this.colorePezzo.isPromotionSoldato(cordinatePossibiliDestinazioni)){
                    legalMoves.add(new PromotionSoldato(new MoveSoldato(board, this, cordinatePossibiliDestinazioni)));
                }else {
                    legalMoves.add(new MoveSoldato(board, this, cordinatePossibiliDestinazioni));
                }
            }else if(cordinateDiCandidatiOffset == 16 && this.isPrimaMossa() &&
                    ((BoardUtils.SETTIMO_RANK[this.pezzoPosizione] && this.getPezzoColore().isNero()) ||
                    (BoardUtils.SECONDO_RANK[this.pezzoPosizione] && this.getPezzoColore().isBianco()))){
                final int cordinateDestinazioniIndietro = this.pezzoPosizione + (this.colorePezzo.getDirezione() * 8);


                if(!board.getPunto(cordinateDestinazioniIndietro).isPunOccupato() &&
                   !board.getPunto(cordinatePossibiliDestinazioni).isPunOccupato()){
                   legalMoves.add(new JumpSoldato(board, this,cordinatePossibiliDestinazioni));
                }

            }else if (cordinateDiCandidatiOffset == 7 &&
                    !((BoardUtils.OTTAVA_COLONNA[this.pezzoPosizione] && this.colorePezzo.isBianco()) ||
                     (BoardUtils.PRIMA_COLONNA[this.pezzoPosizione] && this.colorePezzo.isNero()))
            ){
                if(board.getPunto(cordinatePossibiliDestinazioni).isPunOccupato()){
                    final Pezzo pezzoInCordinate = board.getPunto(cordinatePossibiliDestinazioni).getPezzo();
                    if(this.colorePezzo != pezzoInCordinate.getPezzoColore()) {
                        if (this.colorePezzo.isPromotionSoldato(cordinatePossibiliDestinazioni)) {
                            legalMoves.add(new PromotionSoldato(new AttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate)));
                        } else {
                            legalMoves.add(new AttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate));
                        }
                    }

                }else if(board.getEnPassentSoldato() != null){
                    if(board.getEnPassentSoldato().getPezzoPosizione() == (this.pezzoPosizione + (this.colorePezzo.getDirezioneOpposta()))){
                        final Pezzo pezzoInCordinate = board.getEnPassentSoldato();
                        if(this.colorePezzo != pezzoInCordinate.getPezzoColore()){
                            legalMoves.add(new SpecialAttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate));
                        }
                    }
                }

            }else if(cordinateDiCandidatiOffset == 9 &&
              !((BoardUtils.PRIMA_COLONNA[this.pezzoPosizione] && this.colorePezzo.isBianco()) ||
              (BoardUtils.OTTAVA_COLONNA[this.pezzoPosizione] && this.colorePezzo.isNero()))){
              if(board.getPunto(cordinatePossibiliDestinazioni).isPunOccupato()){
                    final Pezzo pezzoInCordinate = board.getPunto(cordinatePossibiliDestinazioni).getPezzo();
                    if(this.colorePezzo != pezzoInCordinate.getPezzoColore()) {
                        if (this.colorePezzo.isPromotionSoldato(cordinatePossibiliDestinazioni)) {
                            legalMoves.add(new PromotionSoldato(new AttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate)));
                        } else {
                            legalMoves.add(new AttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate));
                        }
                    }
              }else if(board.getEnPassentSoldato() != null){
              if(board.getEnPassentSoldato().getPezzoPosizione() == (this.pezzoPosizione - (this.colorePezzo.getDirezioneOpposta()))){
                      final Pezzo pezzoInCordinate = board.getEnPassentSoldato();
                      if(this.colorePezzo != pezzoInCordinate.getPezzoColore()){
                          legalMoves.add(new SpecialAttackMoveSoldato(board, this, cordinatePossibiliDestinazioni, pezzoInCordinate));
                      }
                  }
              }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }
    @Override
    public Soldato movePezzo(final Move move) {
        return new Soldato(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }
    @Override
    public String toString(){
        return TipoPezzo.soldato.toString();
    }

    public Pezzo getPromotionPezzo(){
        return new Regina(this.colorePezzo, this.pezzoPosizione, false);
    }
}
