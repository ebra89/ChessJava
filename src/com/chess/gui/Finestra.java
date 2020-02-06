package com.chess.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Finestra {

    private JFrame gameFrame;
    private BoardPanel boardPanel;


    private static final Dimension LA_FINESTRA = new Dimension(700,700);
    private static final Dimension DIMENSIONE_CASELLE = new Dimension(10,10);

    Finestra(){
        this.gameFrame = new JFrame();
        this.boardPanel = new BoardPanel();
        this.gameFrame.setSize(LA_FINESTRA);
        this.gameFrame.setVisible(true);
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < 64; i++){
                final TilePanel tilesPanel = new TilePanel(this,i);
                this.boardTiles.add(tilesPanel);
                add(tilesPanel);
            }
            setPreferredSize(LA_FINESTRA);
            validate();
        }
    }

    private class TilePanel extends JPanel{
        private final int idTile;
        TilePanel(final BoardPanel boardPanel,
                  final int idTile){
            super(new GridLayout());
            this.idTile = idTile;
            setPreferredSize(DIMENSIONE_CASELLE);
        }
    }


    public static void main(String[] args) {
        Finestra finestra = new Finestra();
    }
}
