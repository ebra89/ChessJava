package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.Table.MoveLog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StoriaDelGioco extends JPanel {

    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension STORIA_PANEL = new Dimension(100,400);

    StoriaDelGioco(){
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(STORIA_PANEL);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    void redo(final Board board,
              final MoveLog moveHistory){

        int correntRow = 0;
        this.model.clear();
        for(final Move move : moveHistory.getMoves()){
            final String moveText = move.toString();
            if(move.getMovedPezzo().getPezzoColore().isBianco()){
                this.model.setValueAt(moveText, correntRow, 0);
            }else if(move.getMovedPezzo().getPezzoColore().isNero()){
                this.model.setValueAt(moveText, correntRow,1);
                correntRow++;
            }
        }
        if(moveHistory.getMoves().size() > 0){
            final Move ultimaMossa = moveHistory.getMoves().get(moveHistory.size()-1);
            final String moveText = ultimaMossa.toString();

            if(ultimaMossa.getMovedPezzo().getPezzoColore().isBianco()) {
                this.model.setValueAt(moveText + calcolaScaccoAndScaccoMatoHash(board), correntRow , 0);
            }else if(ultimaMossa.getMovedPezzo().getPezzoColore().isNero()){
                this.model.setValueAt(moveText + calcolaScaccoAndScaccoMatoHash(board), correntRow -1,1);
            }
        }
        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calcolaScaccoAndScaccoMatoHash(final Board board) {
        if(board.correntPlayer().isScaccoMato()){
            return "#";
        }else if(board.correntPlayer().isInScacco()){
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel{
        private final List<Row> values;
        private static final String [] NOMI = {"BIANCHI", "NERI"};

        DataModel(){
            this.values = new ArrayList<>();
        }

        public void clear(){
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount(){
            if(this.values == null){
                return 0;
            }
            return this.values.size();
        }
        @Override
        public int getColumnCount(){
            return this.NOMI.length;
        }
        @Override
        public Object getValueAt(final int row, final int column){
            final Row correntRow = this.values.get(row);
            if(column == 0){
                return correntRow.getMosseBianchi();
            }else if( column == 1){
                return correntRow.getMosseNeri();
            }
                return null;
        }

        @Override
        public void setValueAt(final Object aValue,
                               final int row ,
                               final int column){

            final Row correntRow;
            if(this.values.size() <= row){
                correntRow = new Row();
                this.values.add(correntRow);
            }else{
                correntRow = this.values.get(row);
            }
            if(column == 0){
                correntRow.setMosseBianchi((String)aValue);
                fireTableCellUpdated(row, row);
            }else if(column == 1){
                correntRow.setMosseNeri((String)aValue);
                fireTableCellUpdated(row,column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column) {
            return Move.class;
        }
        @Override
        public String getColumnName(final int column){
            return NOMI[column];
        }
    }
    private static class Row {

        private String mosseBianchi;
        private String mosseNeri;

        Row(){
        }

        public String getMosseBianchi(){
            return this.mosseBianchi;
        }

        public String getMosseNeri(){
            return this.mosseNeri;
        }

        public void setMosseBianchi(final String mosseBianchi) {
            this.mosseBianchi = mosseBianchi;
        }

        public void setMosseNeri(final String mosseNeri) {
            this.mosseNeri = mosseNeri;
        }
    }
}
