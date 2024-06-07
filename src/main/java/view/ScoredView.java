package view;

import presenter.ScorePresenter;
import presenter.SeriesPresenter;
import utils.WikiPage;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ScoredView extends PanelView implements ScoredViewInterface {
    private JPanel scorePanel;
    private JTable scoresTable;
    private ArrayList<WikiPage> scoredSeries;
    private ScorePresenter presenter;
    private DefaultTableModel model;
    public ScoredView(ScorePresenter presenter) {  this.presenter = presenter; }
    @Override
    public JPanel getContentPane() {
        return scorePanel;
    }
    @Override
    public void deselectRows(){
        scoresTable.clearSelection();
    }
    @Override
    public void setUpView(){
        scoresTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && scoresTable.getSelectedRow() > -1) {
                presenter.onRowSelected(); }
        });
        model = new DefaultTableModel(new Object[]{"Series Name", "Score", "LastUpdated"}, 0){
            @Override
            public boolean isCellEditable(int row, int column) {  return false; }
        };
        scoresTable.setModel(model);
    }
    @Override
    public void showSavedScores(ArrayList<WikiPage> scoredSeries) {
        this.scoredSeries = scoredSeries;
        model.setRowCount(0);
        for (WikiPage series : scoredSeries) {
            model.addRow(new Object[]{series.getTitle(), series.getScore(), series.getLastUpdated()});
        }
    }
    @Override
    public WikiPage getSelectedSeries() {
        int selectedRow = scoresTable.getSelectedRow();
            return scoredSeries.get(selectedRow);
    }

    @Override
    public JTable getScoresTable(){
        return scoresTable;
    }
}
