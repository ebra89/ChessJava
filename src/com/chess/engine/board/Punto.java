package com.chess.engine.board;

import com.chess.engine.pezzi.Pezzo;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Punto {

    protected final int cordinatePunto;

    private static final Map<Integer, EmptyPunto > EMPTY_PUNTO_CACHE= createAllEmptyPunto();

    private static Map<Integer, EmptyPunto> createAllEmptyPunto() {

        final Map<Integer,EmptyPunto>emptyPuntoMap = new HashMap<>();
        for(int i = 0; i < BoardUtils.NUMERO_PUNTI ; i++){
           emptyPuntoMap.put(i,new EmptyPunto(i));
        }
        return ImmutableMap.copyOf(emptyPuntoMap);
    }

    public static Punto createPunto(final int cordinatePunto, final Pezzo pezzo){
        return pezzo != null ? new OccupatPunto(cordinatePunto,pezzo): EMPTY_PUNTO_CACHE.get(cordinatePunto);
    }

    private Punto(final int cordinatePunto){

        this.cordinatePunto = cordinatePunto;
    }

    public abstract boolean isPunOccupato();

    public abstract Pezzo getPezzo();
    
    public int getCordinatePunto(){

        return this.cordinatePunto;
    }


    public static final class EmptyPunto extends Punto{
        EmptyPunto(final int cordinate){

            super(cordinate) ;
        }

        @Override
        public String toString(){

            return "-";
        }
        @Override
        public boolean isPunOccupato(){

            return false;
        }

        @Override
        public Pezzo getPezzo() {

            return null;
        }
    }

    public static final class OccupatPunto extends Punto{

        private final Pezzo pezzoOnPunto;

        OccupatPunto(int cordinatePunto, Pezzo pezzoOnPunto){
            super(cordinatePunto);
            this.pezzoOnPunto = pezzoOnPunto;
        }

        @Override
        public String toString(){
            return getPezzo().getPezzoColore().isNero() ? getPezzo().toString().toLowerCase() :
                    getPezzo().toString();
        }
        @Override
        public boolean isPunOccupato() {

            return true;
        }

        @Override
        public Pezzo getPezzo() {

            return this.pezzoOnPunto;
        }
    }
}
