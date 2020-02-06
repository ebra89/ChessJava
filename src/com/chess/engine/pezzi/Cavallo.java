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

public class Cavallo extends Pezzo {

    private final static int [] CANDIDATE_VETTORI = {-17, -15, -10, -6 , 6, 10, 15, 17};

    public Cavallo(final Colore colorePezzo ,
                   final int posizionePezzo) {

        super(TipoPezzo.cavallo,posizionePezzo, colorePezzo, true);
    }

    public Cavallo(final Colore colorePezzo,
                   final int posizionePezzo,
                   final boolean isPrimaMossa){
        super(TipoPezzo.cavallo, posizionePezzo , colorePezzo,isPrimaMossa);
    }
    @Override
    public Collection<Move> calcolaLegalMoves(final Board board) {

        final List<Move> legalMoves = new ArrayList<>();

        for(final int correntPossibili : CANDIDATE_VETTORI) {

            final int cordinatePossibiliDestinazioni = this.pezzoPosizione + correntPossibili;
            if (BoardUtils.cordinateSonoValide(cordinatePossibiliDestinazioni)){
                if(laPrimaColonnaEscluso(this.pezzoPosizione, correntPossibili)   ||
                   laSecondaColonnaEscluso(this.pezzoPosizione, correntPossibili) ||
                   laSettimaColonnaEscluso(this.pezzoPosizione, correntPossibili) ||
                   laOttavaColonnaescluso(this.pezzoPosizione, correntPossibili)){
                    continue;
                }
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
    public Cavallo movePezzo(final Move move) {
        return new Cavallo(move.getMovedPezzo().getPezzoColore(), move.getCordinateDestinazione());
    }

    @Override
    public String toString(){

        return tipoPezzo.cavallo.toString();
    }
    private static boolean laPrimaColonnaEscluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.PRIMA_COLONNA [posizioneCorrente] && (possibiliOffset == -17 || possibiliOffset == -10||
                possibiliOffset == 6 || possibiliOffset == 15);
    }
    
    private static boolean laSecondaColonnaEscluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.SECONDA_COLONNA [posizioneCorrente] && (possibiliOffset == -10 || possibiliOffset == 6);
    }

    private static boolean laSettimaColonnaEscluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.SETTIMA_COLONNA [posizioneCorrente] && (possibiliOffset == -6 || possibiliOffset == 10);
    }
    private static boolean laOttavaColonnaescluso(final int posizioneCorrente, final int possibiliOffset){
        return BoardUtils.OTTAVA_COLONNA [posizioneCorrente] && (possibiliOffset== -15 || possibiliOffset == -6 ||
                possibiliOffset == 10 || possibiliOffset == 17);
    }
}