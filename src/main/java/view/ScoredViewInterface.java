package view;

import utils.WikiPage;

import javax.swing.*;
import java.util.ArrayList;

public interface ScoredViewInterface {
    JPanel getContentPane();

    void deselectRows();

    void setUpView();

    void showSavedScores(ArrayList<WikiPage> scoredSeries);

    WikiPage getSelectedSeries();

    JTable getScoresTable();
}
