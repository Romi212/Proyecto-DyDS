package view;

import presenter.SearchPresenter;
import presenter.SeriesPresenter;
import utils.WikiPage;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.ArrayList;

public class SearcherView {
    private JPanel searchPanel;
    private JTextField searchTextField;
    private JPopupMenu searchOptionsMenu;
    private JButton searchButton;
    private JTextPane searchResultTextPane;
    private JButton saveLocallyButton;
    private JSlider scoreSlideBar;
    private JButton setScoreButton;
    private JLabel noScoreFoundLabel;
    private JPanel noScorePanel;
    private SearchPresenter presenter;
    private WikiPage lastSearchedSeries;


    public SearcherView( SearchPresenter presenter) { this.presenter = presenter;}
    public void setUpView() {

        searchResultTextPane.setContentType("text/html");

        searchButton.addActionListener(e ->  { presenter.searchSeries();});

        scoreSlideBar.setVisible(false);
        noScorePanel.setVisible(false);

        saveLocallyButton.addActionListener(actionEvent -> {
            presenter.saveLocally();
        });

        setScoreButton.addActionListener(e -> presenter.setScore());
        scoreSlideBar.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting())  presenter.recordScore();
        });

        searchResultTextPane.setEditable(false);

        searchResultTextPane.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {

                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

            }
        });
    }
    public int getScore(){ return scoreSlideBar.getValue();}

    public void showResults(ArrayList<WikiPage> wikiPages){
        searchOptionsMenu = new JPopupMenu("Search Results");
        for(WikiPage wikiPage : wikiPages){
            wikiPage.getGraphicMenuItem().addActionListener(actionEvent -> {
                lastSearchedSeries = wikiPage;
                //TODO CAMBIAR????
                presenter.getSelectedExtract();

            });
            searchOptionsMenu.add(wikiPage.getGraphicMenuItem());
            searchOptionsMenu.show(searchResultTextPane, searchResultTextPane.getX(), searchResultTextPane.getY());
        }
    }
    public void setSearchResultTextPane(String text) {
        searchResultTextPane.setText(text);
        searchResultTextPane.setCaretPosition(0);
    }
    public void showScore(){
        scoreSlideBar.setVisible(true);
        noScorePanel.setVisible(false);
    }
    public String getSeriesName() { return searchTextField.getText(); }
    public Component getContentPane() { return searchPanel; }
    public void setScore(int i) { scoreSlideBar.setValue(i); }
    public WikiPage getLastSearchedSeries() { return lastSearchedSeries;    }

    public void setLasSearchedExtract(String text){
        lastSearchedSeries.setExtract(text);
    }
    public void setLastSearchedSeries(WikiPage lastSearchedSeries) { this.lastSearchedSeries = lastSearchedSeries;}
    public void showNoScore() {
        scoreSlideBar.setVisible(false);
        noScorePanel.setVisible(true);
    }

    public void setSearchTextField(String text) {
        searchTextField.setText(text);
    }

    public AbstractButton getSearchButton() {
        return searchButton;
    }

    public JPopupMenu getResults(){
        return searchOptionsMenu;
    }
    public String getSearchResultTextPane() {
        return searchResultTextPane.getText();
    }


}