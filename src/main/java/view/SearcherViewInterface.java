package view;

import utils.WikiPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public interface SearcherViewInterface {
    void setUpView();

    int getScore();

    void showResults(ArrayList<WikiPage> wikiPages);

    void setSearchResultTextPane(String text);

    void showScore();

    String getSeriesName();

    Component getContentPane();

    void setScore(int i);

    WikiPage getLastSearchedSeries();

    void setLasSearchedExtract(String text);

    void setLastSearchedSeries(WikiPage lastSearchedSeries);

    void showNoScore();

    void setSearchTextField(String text);

    AbstractButton getSearchButton();

    JPopupMenu getResults();

    String getSearchResultTextPane();
}
