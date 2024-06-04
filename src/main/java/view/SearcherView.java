package view;

import presenter.SeriesPresenter;
import utils.WikiPage;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;

public class SearcherView {
    private JPanel searchPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JTextPane searchResultTextPane;
    private JButton saveLocallyButton;
    private JSlider scoreSlideBar;
    private JButton setScoreButton;
    private JLabel noScoreFoundLabel;
    private JPanel noScorePanel;
    private SeriesPresenter presenter;
    WikiPage lastSearchedSeries;
    String selectedResultTitle = null; //For storage purposes, it may not coincide with the searched term (see below)
    String text = ""; //Last searched text! this variable is central for everything


    public SearcherView( SeriesPresenter presenter) {

        this.presenter = presenter;


    }

        public void setUpView() {
            searchResultTextPane.setContentType("text/html");
            searchButton.addActionListener(e ->  { presenter.searchSeries();});
            scoreSlideBar.setVisible(false);
            noScorePanel.setVisible(false);

            saveLocallyButton.addActionListener(actionEvent -> {
                presenter.saveLocally();
            });

            setScoreButton.addActionListener(e -> {
                // Call the presenter method to set the score
                // You need to implement this method in the presenter
                presenter.setScore();
            });
            scoreSlideBar.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider)e.getSource();
                    if (!source.getValueIsAdjusting()) {

                        // Call the presenter method with the new score
                        presenter.recordScore();
                    }
                }
            });
        }


        public int getScore(){
            return scoreSlideBar.getValue();
        }
        public void setWorkingStatus() {
            for(Component c: this.searchPanel.getComponents()) c.setEnabled(false);
            searchResultTextPane.setEnabled(false);
        }

        public void setWatingStatus() {
            for(Component c: this.searchPanel.getComponents()) c.setEnabled(true);
            searchResultTextPane.setEnabled(true);
        }

        public void showResults(ArrayList<WikiPage> wikiPages){
            JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
            for(WikiPage wikiPage : wikiPages){
                wikiPage.getGraphicMenuItem().addActionListener(actionEvent -> {
                    selectedResultTitle = wikiPage.getTitle();
                    lastSearchedSeries = wikiPage;
                    presenter.getSelectedExtract(wikiPage);

                });
                searchOptionsMenu.add(wikiPage.getGraphicMenuItem());
                searchOptionsMenu.show(searchResultTextPane, searchResultTextPane.getX(), searchResultTextPane.getY());
            }
        }



        public void setSearchResultTextPane(String text) {
            lastSearchedSeries.setExtract(text);
            searchResultTextPane.setText(text);
            searchResultTextPane.setCaretPosition(0);
            //TODO cambiar esto de lugar

        }

    public void showScore(){
        scoreSlideBar.setVisible(true);
        noScorePanel.setVisible(false);

    }
    public String getSeriesName() {
        return searchTextField.getText();
    }
    public Component getContentPane() {
        return searchPanel;
    }

    public void setScore(int i) {
        scoreSlideBar.setValue(i);
    }

    public WikiPage getLastSearchedSeries() {

        return lastSearchedSeries;
    }


    public void setLastSearchedSeries(WikiPage lastSearchedSeries) {
        this.lastSearchedSeries = lastSearchedSeries;
    }
    public void showNoScore() {
        scoreSlideBar.setVisible(false);

        // Show the JLabel and JButton
        noScorePanel.setVisible(true);
    }
}
