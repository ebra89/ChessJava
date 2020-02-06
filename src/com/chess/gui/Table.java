package com.chess.gui;


import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Punto;
import com.chess.engine.pezzi.Pezzo;
import com.chess.engine.player.MoveTransition;
import com.chess.engine.player.ai.MiniMax;
import com.chess.engine.player.ai.Movestrategy;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class Table extends Observable {

    private final JFrame gameFrame;

    private final BoardPanel boardPanel;
    private final PezziPresiPanel pezziPresiPanel;
    private final StoriaDelGioco storiaDelGioco;
    private Board chessBoard;
    private Punto puntoPartenza;
    private Punto puntoDestinazione;
    private Pezzo humanMovedPezzo;
    private BoardDirection boardDirection;
    private boolean possibiliMosseLegali;
    private final MoveLog moveLog;
    private Move computerMove;
    private final GameSetUp gameSetUp;

    private final static Dimension LA_FINESTRA = new Dimension(700, 700);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension CASELLE_PANEL_DIMENSION = new Dimension(10,10);
    private static String defaultIconaPezzoPath = "art/fancy/";
    private final Color colorChiari = Color.cyan;
    private final Color colorScuri = Color.GRAY;

    private static final  Table INSTANCE = new Table();

    private Table(){
       this.gameFrame = new JFrame("JChess");
       this.gameFrame.setLayout(new BorderLayout());
       final JMenuBar tableMenuBar = createTableMenuBar();
       this.gameFrame.setJMenuBar(tableMenuBar);
       this.gameFrame.setSize(LA_FINESTRA);
       this.pezziPresiPanel = new PezziPresiPanel();
       this.storiaDelGioco = new StoriaDelGioco();
       this.moveLog = new MoveLog();
       this.addObserver(new TableGameAIwatcher());
       this.possibiliMosseLegali = false;
       this.chessBoard = Board.creaStandardBoard();
       this.boardPanel = new BoardPanel();
       this.gameSetUp = new GameSetUp(this.gameFrame, true);
       this.boardDirection = BoardDirection.NORMAL;
       this.gameFrame.add(this.pezziPresiPanel, BorderLayout.WEST);
       this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
       this.gameFrame.add(this.storiaDelGioco, BorderLayout.EAST);
       this.gameFrame.setVisible(true);
    }

    public static Table get(){
        return INSTANCE;
    }

    public void show(){
        Table.get().getMoveLog().clear();
        Table.get().getStoriaDelGioco().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getPezziPresiPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private GameSetUp getGameSetUp(){
        return this.gameSetUp;
    }

    private Board getGameBoard(){
        return this.chessBoard;
    }

    public void updateGameBoard(final Board board){
        this.chessBoard = board;
    }

    public void updateComputerMove(final Move move){
        this.computerMove = move;
    }

    private MoveLog getMoveLog(){
        return this.moveLog;
    }

    private StoriaDelGioco getStoriaDelGioco(){
        return this.storiaDelGioco;
    }

    private PezziPresiPanel getPezziPresiPanel(){
        return this.pezziPresiPanel;
    }

    private BoardPanel getBoardPanel(){
        return this.boardPanel;
    }

    public void moveMadeUpdate(final PlayerType playerType){
        setChanged();
        notifyObservers(playerType);
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(menuPreferenzeItem());
        tableMenuBar.add(creaOptionMenu());
        return tableMenuBar;
    }
    private JMenu createFileMenu(){
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("carica PGN File");

        openPGN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                System.out.println("apri il file pgn!!");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }                                       //metodo cambio menu

    private JMenu menuPreferenzeItem(){

        final JMenu menuPreferenze = new JMenu("Preferenze");
        final JMenuItem filpBoardMenuItem = new JMenuItem("cambia campo");
        filpBoardMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        menuPreferenze.add(filpBoardMenuItem);

        menuPreferenze.addSeparator();
        final JCheckBoxMenuItem possibiliMosseLegaliCheckBox = new JCheckBoxMenuItem("Suggerimenti", false);
        possibiliMosseLegaliCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                possibiliMosseLegali = possibiliMosseLegaliCheckBox.isSelected();
            }
        });
        menuPreferenze.add(possibiliMosseLegaliCheckBox);
        return menuPreferenze;
    }

    private JMenu creaOptionMenu(){
        final JMenu optionMenu = new JMenu("Options");
        final JMenuItem setUpGameMenuItem = new JMenuItem("Setup Gioco");
        setUpGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Table.get().getGameSetUp().promptUser();
                Table.get().setUpUpdate(Table.get().getGameSetUp());

            }
        });
        optionMenu.add(setUpGameMenuItem);
        return optionMenu;
    }

    private void setUpUpdate(final GameSetUp gameSetUp){
        setChanged();
        notifyObservers(gameSetUp);
    }

    private static class TableGameAIwatcher implements Observer{

        @Override
        public void update(final Observable o, final Object arg) {
            if(Table.get().getGameSetUp().isAIPlayer(Table.get().getGameBoard().correntPlayer()) &&
            !Table.get().getGameBoard().correntPlayer().isScaccoMato() &&
            !Table.get().getGameBoard().correntPlayer().isStalMato()){

            //crea AI thread
            // execute AI work

            final AIThinkTank thinkTank = new AIThinkTank();
            thinkTank.execute();
        }

        if(Table.get().getGameBoard().correntPlayer().isScaccoMato()){
            System.out.println("Game Over "+ Table.get().getGameBoard().correntPlayer()+ "in Scacco Matto!");
           }
        if(Table.get().getGameBoard().correntPlayer().isStalMato()){
            System.out.println("Game Over "+ Table.get().getGameBoard().correntPlayer()+ "e in stallo! ");
           }

        }
    }

    private static class AIThinkTank extends SwingWorker<Move, String>{


        private AIThinkTank(){
        }

        @Override
        protected Move doInBackground() throws Exception{
            final Movestrategy miniMax = new MiniMax(4);
            final Move bestMove = miniMax.execute(Table.get().getGameBoard());
            return bestMove;
        }
        @Override
        protected void done(){
            try {
                final Move bestMove = get();
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().correntPlayer().makeMove(bestMove).getBoardTransition());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getStoriaDelGioco().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getPezziPresiPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public enum BoardDirection {
        NORMAL{
            @Override
            List<PuntoPanel> travers(List<PuntoPanel> boardPunti) {
                return boardPunti;
            }

            @Override
            BoardDirection opposite(){
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<PuntoPanel> travers(List<PuntoPanel> boardPunti) {
                List<PuntoPanel> listReversed = new ArrayList<>();
                System.out.println("sono alla punto panel!!");
                Collections.reverse(boardPunti);
                return listReversed;
                //return Lists.reverse(boardPunti);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<PuntoPanel> travers(final List<PuntoPanel> boardPunti);
        abstract BoardDirection opposite();
    }



    public static class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move){
            this.moves.add(move);
        }
        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }
        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }

    }

    enum PlayerType{
        HUMAN,
        COMPUTER
    }


    private class BoardPanel extends JPanel{
        final List<PuntoPanel> boardPunti;

        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardPunti = new ArrayList<>();
            for(int i = 0; i < BoardUtils.NUMERO_PUNTI; i++){
                final PuntoPanel puntiPanel = new PuntoPanel(this, i);
                this.boardPunti.add(puntiPanel);
                add(puntiPanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {   //cambio campo
            removeAll();
            for(final PuntoPanel puntoPanel : boardDirection.travers(boardPunti)){
                puntoPanel.drawPunto(board);
                add(puntoPanel);
            }
            validate();
            repaint();
        }
    }
    private class PuntoPanel extends JPanel{

        private final int idPunti;

        PuntoPanel(final BoardPanel boardPanel,
                   final int idPunti){
            super(new GridLayout());
            this.idPunti = idPunti;
            setPreferredSize(CASELLE_PANEL_DIMENSION);
            dareColoreCaselle();
            dareIconaPezzoPunto(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    if(isRightMouseButton(e)){
                        puntoPartenza = null;
                        puntoDestinazione = null;
                        humanMovedPezzo = null;
                    }else if (isLeftMouseButton(e)){
                        if(puntoPartenza == null) {
                            puntoPartenza = chessBoard.getPunto(idPunti);
                            humanMovedPezzo = puntoPartenza.getPezzo();
                            if (humanMovedPezzo == null) {
                                puntoPartenza = null;
                            }
                        }else {
                            puntoDestinazione = chessBoard.getPunto(idPunti);
                            final Move move = Move.MoveFactory.createMove(chessBoard, puntoPartenza.getCordinatePunto(),
                                    puntoDestinazione.getCordinatePunto());
                            final MoveTransition transition = chessBoard.correntPlayer().makeMove(move);
                            if(transition.getMoveStatus().isDone()){
                                chessBoard = transition.getBoardTransition();
                                moveLog.addMove(move);
                            }
                            puntoPartenza = null;
                            puntoDestinazione = null;
                            humanMovedPezzo = null;
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                storiaDelGioco.redo(chessBoard, moveLog);
                                pezziPresiPanel.redo(moveLog);
                                if(gameSetUp.isAIPlayer(chessBoard.correntPlayer())){
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }
                @Override
                public void mousePressed(final MouseEvent e) {

                }

                @Override
                public void mouseReleased(final MouseEvent e) {

                }

                @Override
                public void mouseEntered(final MouseEvent e) {

                }

                @Override
                public void mouseExited(final MouseEvent e) {

                }
            });
            validate();
        }
        public void drawPunto(final Board board) {

            dareColoreCaselle();
            dareIconaPezzoPunto(board);
            possibiliMosse(board);
            validate();
            repaint();
        }
        private void dareIconaPezzoPunto (final Board board){
            this.removeAll();
            if(board.getPunto(this.idPunti).isPunOccupato()){
                try {
                    final BufferedImage imge =
                            ImageIO.read(new File(defaultIconaPezzoPath + board.getPunto(this.idPunti).
                                    getPezzo().getPezzoColore().toString().substring(0,1) +
                                    board.getPunto(this.idPunti).getPezzo().toString() + ".gif"));
                            add(new JLabel(new ImageIcon(imge)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void possibiliMosse(final Board board){
            if(possibiliMosseLegali){
                for(final Move move : mosseLegaliDelPezzo(board)){
                    if(move.getCordinateDestinazione() == this.idPunti){
                        try{
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> mosseLegaliDelPezzo (final Board board){
            if(humanMovedPezzo != null && humanMovedPezzo.getPezzoColore() == board.correntPlayer().getColore()){
                return humanMovedPezzo.calcolaLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void dareColoreCaselle() {
            if(BoardUtils.PRIMO_RANK[this.idPunti] ||
                    BoardUtils.TERZO_RANK[this.idPunti] ||
                    BoardUtils.QUINTO_RANK[this.idPunti] ||
                    BoardUtils.SETTIMO_RANK[this.idPunti]){
                setBackground(this.idPunti % 2 == 0 ? colorChiari : colorScuri);
            }else if(BoardUtils.SECONDO_RANK[this.idPunti] ||
                     BoardUtils.QUARTO_RANK[this.idPunti]  ||
                    BoardUtils.SESSTO_RANK[this.idPunti] ||
                    BoardUtils.OTTAVA_RANK[this.idPunti]){
                setBackground(this.idPunti % 2 != 0 ? colorChiari : colorScuri);
            }
        }
    }

}