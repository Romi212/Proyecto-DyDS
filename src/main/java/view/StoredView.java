package view;

import dyds.tvseriesinfo.fulllogic.DataBase;
import presenter.SeriesPresenter;
import utils.WikiPage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StoredView {
    private JPanel storedPanel;
    private JComboBox selectSavedComboBox;
    private JTextPane showSavedTextPane;

    private SeriesPresenter presenter;

    String selectedResultTitle = null; //For storage purposes, it may not coincide with the searched term (see below)
    String text = ""; //Last searched text! this variable is central for everything


    public StoredView( SeriesPresenter presenter) {

        this.presenter = presenter;



    }


    public void setUpView(){

        setUpComboBox();

        showSavedTextPane.setContentType("text/html");

        setUpPopupMenu();

    }

    private void setUpPopupMenu() {

        JPopupMenu storedInfoPopup = new JPopupMenu();

        JMenuItem deleteItem = new JMenuItem("Delete!");
        deleteItem.addActionListener(actionEvent -> { presenter.deleteSelectedExtract();  });
        storedInfoPopup.add(deleteItem);

        JMenuItem saveItem = new JMenuItem("Save Changes!");
        saveItem.addActionListener(actionEvent -> { presenter.saveExtractChanges();  });
        storedInfoPopup.add(saveItem);


        showSavedTextPane.setComponentPopupMenu(storedInfoPopup);

    }

    private void setUpComboBox() {

        presenter.initializeSavedPanel();

    }



    public void setSelectSavedComboBox(Object[] titles){

        selectSavedComboBox.setModel(new DefaultComboBoxModel(titles));
        selectSavedComboBox.addActionListener(actionEvent -> { System.out.println("Aprete el menu");presenter.showSelectedExtract();});

    }



    public String getSeletedSavedTitle() {
        return selectSavedComboBox.getSelectedItem().toString();
    }

    public void setSelectedExtract(String extract) {
        showSavedTextPane.setText(extract);
    }

    public boolean existSelectedEntry() {
        return (selectSavedComboBox.getSelectedIndex() > -1);
    }

    public void emptySavedTextPane() {
        showSavedTextPane.setText("");
    }

    public String getSelectedSavedExtract() {
        return showSavedTextPane.getText();
    }
    public JPanel getContentPane() {
        return storedPanel;
    }
}
