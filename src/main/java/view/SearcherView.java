package view;

import dyds.tvseriesinfo.fulllogic.DataBase;
import presenter.SeriesPresenter;
import utils.WikiPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearcherView {
    private JPanel searchPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JTextPane searchResultTextPane;
    private JButton saveLocallyButton;
    private SeriesPresenter presenter;
    String selectedResultTitle = null; //For storage purposes, it may not coincide with the searched term (see below)
    String text = ""; //Last searched text! this variable is central for everything


    public SearcherView( SeriesPresenter presenter) {

        this.presenter = presenter;


    }

        public void setUpView() {
            searchResultTextPane.setContentType("text/html");
            searchButton.addActionListener(e ->  { presenter.searchSeries();});
            saveLocallyButton.addActionListener(actionEvent -> {
                if(text != ""){
                    // save to DB  <o/
                  //  DataBase.saveInfo(selectedResultTitle.replace("'", "`"), text);  //Dont forget the ' sql problem
                    //selectSavedComboBox.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
                }
            });
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
                    presenter.getSelectedExtract(wikiPage);

                });
                searchOptionsMenu.add(wikiPage.getGraphicMenuItem());
                searchOptionsMenu.show(searchResultTextPane, searchResultTextPane.getX(), searchResultTextPane.getY());
            }
        }



        public void setSearchResultTextPane(String text) {
            this.text = text;
            searchResultTextPane.setText(text);
            searchResultTextPane.setCaretPosition(0);
        }

    public String getSeriesName() {
        return searchTextField.getText();
    }
    public Component getContentPane() {
        return searchPanel;
    }
}
