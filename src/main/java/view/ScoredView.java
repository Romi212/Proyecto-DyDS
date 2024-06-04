package view;

import presenter.SeriesPresenter;
import utils.WikiPage;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ScoredView {
    private JPanel scorePanel;
    private JTable scoresTable;
    private ArrayList<WikiPage> scoredSeries;
    private SeriesPresenter presenter;
    public ScoredView(SeriesPresenter presenter) {
        this.presenter = presenter;

    }
    public JPanel getContentPane() {
        return scorePanel;
    }

    public void setUpView(){
        presenter.getSavedScores();
        scoresTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = scoresTable.getSelectedRow();
                    if (selectedRow != -1) {
                        // Call the onRowSelected method in the presenter
                        presenter.onRowSelected();
                    }
                }
            }
        });

    }

    public void showSavedScores(ArrayList<WikiPage> scoredSeries) {
        // Create a new table model with two columns
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Series Name", "Score", "LastUpdated"}, 0){

            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        this.scoredSeries = scoredSeries;

        // Iterate over the scoredSeries list
        for (WikiPage series : scoredSeries) {
            // Add a row for each series with the series name and score
            model.addRow(new Object[]{series.getTitle(), series.getScore(), series.getLastUpdated()});
        }

        // Set the table model to the scoresTable
        scoresTable.setModel(model);

    }

    public WikiPage getSelectedSeries() {
        int selectedRow = scoresTable.getSelectedRow();

        return scoredSeries.get(selectedRow);
    }
}
