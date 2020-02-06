package com.chess.gui;

import com.chess.engine.Colore;
import com.chess.engine.player.Player;
import com.chess.gui.Table.PlayerType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSetUp extends JDialog {

    private PlayerType playerTypeBianco;
    private PlayerType playerTypeNero;
    private JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT= "Human";
    private static final String COMPUTER_TEXT = "Computer";

            GameSetUp(final JFrame frame,
                      final boolean modal){
                super(frame, modal);
                final JPanel myPanel = new JPanel(new GridLayout(0,1));
                final JRadioButton bianchiHumanButton = new JRadioButton(HUMAN_TEXT);
                final JRadioButton bianchiComputerButton = new JRadioButton(COMPUTER_TEXT);
                final JRadioButton neriHumanButton = new JRadioButton(HUMAN_TEXT);
                final JRadioButton neriComputerButton = new JRadioButton(COMPUTER_TEXT);
                bianchiHumanButton.setActionCommand(HUMAN_TEXT);
                final ButtonGroup grouppoBianchi = new ButtonGroup();
                grouppoBianchi.add(bianchiHumanButton);
                grouppoBianchi.add(bianchiComputerButton);
                bianchiHumanButton.setSelected(true);

                final ButtonGroup groupNeri = new ButtonGroup();
                groupNeri.add(neriHumanButton);
                groupNeri.add(neriComputerButton);
                neriHumanButton.setSelected(true);

                getContentPane().add(myPanel);
                myPanel.add(new JLabel("Bianchi"));
                myPanel.add(bianchiHumanButton);
                myPanel.add(bianchiComputerButton);
                myPanel.add(new JLabel("Neri"));
                myPanel.add(neriHumanButton);
                myPanel.add(neriComputerButton);

                myPanel.add(new JLabel("Cerca"));
                this.searchDepthSpinner = addLabeledSpinner(myPanel,"grado di difficolta", new SpinnerNumberModel(6,0 ,Integer.MAX_VALUE,1));


                final JButton cancelButton = new JButton("Cancel");
                final JButton okButton = new JButton("Ok");

                okButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                        playerTypeBianco = bianchiComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                        playerTypeNero = neriComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                        GameSetUp.this.setVisible(false);
                    }
                });

                cancelButton.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent actionEvent) {
                         System.out.println("Cancel");
                         GameSetUp.this.setVisible(false);
                    }
                });


                myPanel.add(okButton);
                myPanel.add(cancelButton);


                setLocationRelativeTo(frame);
                pack();
                setVisible(false);


            }
    void promptUser(){
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player){
                if(player.getColore() == Colore.BIANCO){
                    return getPlayerTypeBianco() == PlayerType.COMPUTER;
                }
                return getPlayerTypeNero() == PlayerType.COMPUTER;
    }


    PlayerType getPlayerTypeBianco() {

                return this.playerTypeBianco;
    }

    PlayerType  getPlayerTypeNero() {

                return this.playerTypeNero;
    }

    private static JSpinner addLabeledSpinner(final Container c,
                                              final String label,
                                              final SpinnerModel model){

                final JLabel l = new JLabel(label);
                c.add(l);
                final JSpinner spinner = new JSpinner(model);
                l.setLabelFor(spinner);
                c.add(spinner);
                return spinner;
    }

    int getSearchDepth(){
                return (Integer)this.searchDepthSpinner.getValue();
    }

}
