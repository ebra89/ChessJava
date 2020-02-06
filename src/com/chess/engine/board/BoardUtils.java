package com.chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardUtils{


    public static final boolean [] PRIMA_COLONNA = initColonna(0);
    public static final boolean [] SECONDA_COLONNA = initColonna(1);
    public static final boolean [] SETTIMA_COLONNA = initColonna(6);
    public static final boolean [] OTTAVA_COLONNA = initColonna(7);

    public static final boolean [] OTTAVA_RANK = initRow(0);
    public static final boolean [] SETTIMO_RANK = initRow(8);
    public static final boolean [] SESSTO_RANK = initRow(16);
    public static final boolean [] QUINTO_RANK = initRow(24);
    public static final boolean [] QUARTO_RANK = initRow(32);
    public static final boolean [] TERZO_RANK = initRow(40);
    public static final boolean [] SECONDO_RANK = initRow(48);
    public static final boolean [] PRIMO_RANK = initRow(56);


    public static final String [] ALGEBRIC_NOTITION = initializeAlgebricNotition();
    public static final Map<String, Integer> POSIZIONE_AL_CORDINATE = initializePosizioneAlCordinateMap();



    public static final int NUMERO_PUNTI = 64;
    public static final int NUMERO_PUNTI_PER_RIGA = 8;
    private static final int START_INDEX = 0;

    private BoardUtils(){
        throw new RuntimeException("la mossa non è possibile");

    }

    private static String [] initializeAlgebricNotition() {

         return new String[]{

                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"

        };
    }


    private static Map<String, Integer> initializePosizioneAlCordinateMap() {
        final Map<String, Integer> posizioneAlCordinate = new HashMap<>();
        for(int i = START_INDEX ; i < NUMERO_PUNTI ; i++){
            posizioneAlCordinate.put(ALGEBRIC_NOTITION[i], i);
        }
        return ImmutableMap.copyOf(posizioneAlCordinate);
    }


    private static boolean[] initColonna(int numeroColonna) {

        final boolean [] colonna = new boolean[NUMERO_PUNTI];

        do{
            colonna[numeroColonna] = true;
            numeroColonna += NUMERO_PUNTI_PER_RIGA;
        }while (numeroColonna < NUMERO_PUNTI);
        return colonna;
    }

    public static boolean[] initRow(int rowNumero){
        final boolean[] row = new boolean[NUMERO_PUNTI];
        do{
            row[rowNumero] = true;
            rowNumero ++;
        }while (rowNumero % NUMERO_PUNTI_PER_RIGA != 0);
        return row;
    }


    public static boolean cordinateSonoValide(int cordinate) {
        return cordinate >= START_INDEX && cordinate < NUMERO_PUNTI;
    }

    public static int getCordinateDelPosizione(final String posizione) {
        return POSIZIONE_AL_CORDINATE.get(posizione);
    }

    public static String getPosizioneAlCordinate(final int cordinate){
        return ALGEBRIC_NOTITION[cordinate];
    }
}
