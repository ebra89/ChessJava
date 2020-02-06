package com.chess.gui;

import com.chess.engine.board.Move;
import com.chess.engine.pezzi.Pezzo;
import com.chess.gui.Table.MoveLog;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.imageio.ImageIO.read;

public class PezziPresiPanel extends JPanel {

    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color COLOR_PANEL = Color.white;
    private static final Dimension DIMENSIONE_PEZZI_PRESI_BOX= new Dimension(80,80);

    private final JPanel nordPanel;
    private final JPanel sudPanel;

    public PezziPresiPanel (){
        super(new BorderLayout());
        this.setBackground(COLOR_PANEL);
        this.setBorder(PANEL_BORDER);
        this.nordPanel = new JPanel(new GridLayout(8,4));
        this.sudPanel = new JPanel(new GridLayout(8,4));
        this.nordPanel.setBackground(COLOR_PANEL);
        this.sudPanel.setBackground(COLOR_PANEL);
        this.add(this.nordPanel, BorderLayout.NORTH);
        this.add(this.sudPanel, BorderLayout.SOUTH);
        setPreferredSize(DIMENSIONE_PEZZI_PRESI_BOX);
    }

    public void redo(final MoveLog moveLog){

        this.sudPanel.removeAll();
        this.nordPanel.removeAll();

        final List<Pezzo> pezziBianchiPresi = new ArrayList<>();
        final List<Pezzo> pezziNeriPresi = new ArrayList<>();

        for(final Move move : moveLog.getMoves()){
            if(move.isAttack()){
                final Pezzo pezzoPreso = move.getAttackedPezzo();
                if(pezzoPreso.getPezzoColore().isBianco()){
                    pezziBianchiPresi.add(pezzoPreso);
                }else if(pezzoPreso.getPezzoColore().isNero()){
                    pezziNeriPresi.add(pezzoPreso);
                }else {
                     throw new RuntimeException("errore");
                }
            }
        }

        Collections.sort(pezziBianchiPresi, new Comparator<Pezzo>() {
            @Override
            public int compare(Pezzo o1, Pezzo o2) {
                return Ints.compare(o1.getPezzoValue(), o2.getPezzoValue());
            }
        });

        Collections.sort(pezziNeriPresi, new Comparator<Pezzo>() {
            @Override
            public int compare(Pezzo o1, Pezzo o2) {
                return Ints.compare(o1.getPezzoValue(), o2.getPezzoValue());
            }
        });

        for(final Pezzo pezzipresi : pezziBianchiPresi){
            try{
                final BufferedImage image = read(new File("art/fancy/" + pezzipresi
                        .getPezzoColore()
                        .toString().substring(0,1)+ pezzipresi.toString()+ ".gif"));
               // final ImageIcon icon = new ImageIcon(image);
                //final JLabel imageLabel = new JLabel();
                this.sudPanel.add(new JLabel(new ImageIcon(image)));
            }catch (final IOException e){
                e.printStackTrace();
            }
        }

        for(final Pezzo pezzipresi : pezziNeriPresi){
            try{
                final BufferedImage image = ImageIO.read(new File("art/fancy/" + pezzipresi.getPezzoColore()
                        .toString().substring(0,1)+ pezzipresi.toString()+ ".gif"));
               // final ImageIcon icon = new ImageIcon(image);
                //final JLabel imageLabel = new JLabel();

                this.nordPanel.add(new JLabel(new ImageIcon(image)));
            }catch (final IOException e){
                e.printStackTrace();
            }
        }
        validate();
    }
}
