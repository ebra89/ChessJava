package com.chess.engine.board;

import com.chess.engine.Colore;
import com.chess.engine.pezzi.*;
import com.chess.engine.player.Player;
import com.chess.engine.player.PlayerBianco;
import com.chess.engine.player.PlayerNero;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

import static java.util.List.copyOf;

public class Board {

    private final List<Punto> gameBoard;
    private final Collection<Pezzo> pezziBianchi;
    private final Collection<Pezzo> pezziNeri;

    private final PlayerBianco playerBianco;
    private final PlayerNero playerNero;
    private final Player correntPlayer;

    private final  Soldato enPassentSoldato;


    private Board(final Builder builder){
        this.gameBoard = creaGameBoard(builder);
        this.pezziBianchi = calcolaPezziAttivi(this.gameBoard, Colore.BIANCO);
        this.pezziNeri = calcolaPezziAttivi(this.gameBoard, Colore.NERO);

        this.enPassentSoldato = builder.enPassantSoldato;

        final Collection<Move> mosseLegaliBianchi = calcolaLegalMoves(this.pezziBianchi);
        final Collection<Move> mosseLegaliNeri = calcolaLegalMoves(this.pezziNeri);

        this.playerBianco = new PlayerBianco(this, mosseLegaliBianchi, mosseLegaliNeri);
        this.playerNero = new PlayerNero (this, mosseLegaliBianchi, mosseLegaliNeri);

        this.correntPlayer = builder.prossimaMossaMaker.scegliPlayer(this.playerBianco, this.playerNero);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for(int i = 0; i < BoardUtils.NUMERO_PUNTI; i++){
            final String puntoText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", puntoText));
            if((i + 1 ) % BoardUtils.NUMERO_PUNTI_PER_RIGA == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player playerBianco(){
        return this.playerBianco;
    }

    public Player playerNero(){
        return this.playerNero;
    }

    public Soldato getEnPassentSoldato(){
        return this.enPassentSoldato;
    }

    public Player correntPlayer(){
        return this.correntPlayer;
    }
    public Collection<Pezzo> getPezziNeri (){
        return this.pezziNeri;
    }


    public Collection<Pezzo> getPezziBianchi (){
        return  this.pezziBianchi;
    }

    private Collection<Move> calcolaLegalMoves(final Collection<Pezzo> pezzi) {
        final List<Move> legalMoves = new ArrayList<>();
        for(final  Pezzo pezzo : pezzi){
            legalMoves.addAll(pezzo.calcolaLegalMoves(this));
        }
        return Collections.unmodifiableList(copyOf(legalMoves));                    //ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Pezzo> calcolaPezziAttivi(final List<Punto> gameBoard,
                                                 final Colore colore) {
        final List<Pezzo> pezziAttivi = new ArrayList<>();

        for(final Punto  punto : gameBoard){
            if(punto.isPunOccupato()){
                final Pezzo pezzo = punto.getPezzo();
                if(pezzo.getPezzoColore() == colore){
                    pezziAttivi.add(pezzo);
                }
            }
        }
        return Collections.unmodifiableList(copyOf(pezziAttivi));                                                               // ImmutableList.copyOf(pezziAttivi);
    }


    public Punto getPunto(final int cordinatePunto){

        return gameBoard.get(cordinatePunto);
    }

    private static List<Punto>creaGameBoard(final Builder builder){
        // hai creato una lista per utilizzare Collections!
         Punto [] punti = new Punto[BoardUtils.NUMERO_PUNTI];
         List<Punto> puntoList = new ArrayList<>();
        puntoList = puntoList;
        for(int i = 0;i < BoardUtils.NUMERO_PUNTI; i++){
            punti[i] = Punto.createPunto(i, builder.boardConfig.get(i));
        }
        return Collections.unmodifiableList(copyOf(puntoList));                    // ImmutableList.copyOf(punti);
    }

    public static Board creaStandardBoard(){
        final Builder builder = new Builder();
        //neri
        builder.setPezzo(new Torre(Colore.NERO, 0));
        builder.setPezzo(new Cavallo(Colore.NERO, 1));
        builder.setPezzo(new Alfiere(Colore.NERO, 2));
        builder.setPezzo(new Regina(Colore.NERO, 3));
        builder.setPezzo(new Re(Colore.NERO, 4));
        builder.setPezzo(new Alfiere(Colore.NERO, 5));
        builder.setPezzo(new Cavallo(Colore.NERO, 6));
        builder.setPezzo(new Torre(Colore.NERO, 7));
        builder.setPezzo(new Soldato(Colore.NERO, 8));
        builder.setPezzo(new Soldato(Colore.NERO, 9));
        builder.setPezzo(new Soldato(Colore.NERO, 10));
        builder.setPezzo(new Soldato(Colore.NERO, 11));
        builder.setPezzo(new Soldato(Colore.NERO, 12));
        builder.setPezzo(new Soldato(Colore.NERO, 13));
        builder.setPezzo(new Soldato(Colore.NERO, 14));
        builder.setPezzo(new Soldato(Colore.NERO, 15));

        // bianchi

        builder.setPezzo(new Soldato(Colore.BIANCO, 48));
        builder.setPezzo(new Soldato(Colore.BIANCO, 49));
        builder.setPezzo(new Soldato(Colore.BIANCO, 50));
        builder.setPezzo(new Soldato(Colore.BIANCO, 51));
        builder.setPezzo(new Soldato(Colore.BIANCO, 52));
        builder.setPezzo(new Soldato(Colore.BIANCO, 53));
        builder.setPezzo(new Soldato(Colore.BIANCO, 54));
        builder.setPezzo(new Soldato(Colore.BIANCO, 55));
        builder.setPezzo(new Torre(Colore.BIANCO, 56));
        builder.setPezzo(new Cavallo(Colore.BIANCO, 57));
        builder.setPezzo(new Alfiere(Colore.BIANCO, 58));
        builder.setPezzo(new Regina(Colore.BIANCO, 59));
        builder.setPezzo(new Re(Colore.BIANCO, 60));
        builder.setPezzo(new Alfiere(Colore.BIANCO, 61));
        builder.setPezzo(new Cavallo(Colore.BIANCO, 62));
        builder.setPezzo(new Torre(Colore.BIANCO, 63));

        //a bianco la prima mossa

        builder.setMossaMaker(Colore.BIANCO);
        return builder.build();
    }

    public Iterable<Move> getAllLegalMoves() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.playerBianco.getLegalMoves(),
                this.playerNero.getLegalMoves()));
    }



    public static class Builder{
        Map<Integer, Pezzo> boardConfig;
        Colore prossimaMossaMaker;
        Soldato enPassantSoldato;

        public Builder(){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPezzo(final Pezzo pezzo){
            this.boardConfig.put(pezzo.getPezzoPosizione(), pezzo);
            return this;
        }

        public Builder setMossaMaker(final Colore prossimaMossaMaker){
            this.prossimaMossaMaker = prossimaMossaMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }

        public void setEnPassantSoldato(Soldato enPassantSoldato) {

            this.enPassantSoldato = enPassantSoldato;
        }
    }
}
