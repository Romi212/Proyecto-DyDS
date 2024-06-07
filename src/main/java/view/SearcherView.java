package view;

import presenter.SearchPresenter;
import presenter.SeriesPresenter;
import utils.WikiPage;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.util.ArrayList;

public class SearcherView implements SearcherViewInterface {
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
    @Override
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
    @Override
    public int getScore(){ return scoreSlideBar.getValue();}

    @Override
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
    @Override
    public void setSearchResultTextPane(String text) {
        searchResultTextPane.setText(text);
        searchResultTextPane.setCaretPosition(0);
    }
    @Override
    public void showScore(){
        scoreSlideBar.setVisible(true);
        noScorePanel.setVisible(false);
    }
    @Override
    public String getSeriesName() { return searchTextField.getText(); }
    @Override
    public Component getContentPane() { return searchPanel; }
    @Override
    public void setScore(int i) { scoreSlideBar.setValue(i); }
    @Override
    public WikiPage getLastSearchedSeries() { return lastSearchedSeries;    }

    @Override
    public void setLasSearchedExtract(String text){
        lastSearchedSeries.setExtract(text);
    }
    @Override
    public void setLastSearchedSeries(WikiPage lastSearchedSeries) { this.lastSearchedSeries = lastSearchedSeries;}
    @Override
    public void showNoScore() {
        scoreSlideBar.setVisible(false);
        noScorePanel.setVisible(true);
    }

    @Override
    public void setSearchTextField(String text) {
        searchTextField.setText(text);
    }

    @Override
    public AbstractButton getSearchButton() {
        return searchButton;
    }

    @Override
    public JPopupMenu getResults(){
        return searchOptionsMenu;
    }
    @Override
    public String getSearchResultTextPane() {
        return searchResultTextPane.getText();
    }


}