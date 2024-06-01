package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import dyds.tvseriesinfo.fulllogic.DataBase;
import utils.WikiPage;
import presenter.SeriesPresenter;

public class SearchView {

  private SeriesPresenter presenter;
  private JTextField searchTextField;
  private JButton searchButton;
  private JPanel contentPane;
  private JTextPane searchResultTextPane;
  private JButton saveLocallyButton;
  private JTabbedPane tabbedPane1;
  private JPanel searchPanel;
  private JPanel storagePanel;
  private JComboBox selectSavedComboBox;

  private JTextPane showSavedTextPane;

  private LocallySavedView locallySavedView;


  DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
  String selectedResultTitle = null; //For storage purposes, it may not coincide with the searched term (see below)
  String text = ""; //Last searched text! this variable is central for everything


  public SearchView( SeriesPresenter presenter) {

    this.presenter = presenter;

    this.locallySavedView = new LocallySavedView();

  }

  private void setUpSearchPanel() {
    searchResultTextPane.setContentType("text/html");
    searchButton.addActionListener(e ->  { presenter.searchSeries();});
    saveLocallyButton.addActionListener(actionEvent -> {
      if(text != ""){
        // save to DB  <o/
        DataBase.saveInfo(selectedResultTitle.replace("'", "`"), text);  //Dont forget the ' sql problem
        selectSavedComboBox.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
      }
    });
  }

  private void setUpSavedPanel(){

    setUpComboBox();

    showSavedTextPane = locallySavedView.getShowSavedTextPane();

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


    locallySavedView.addPopup(storedInfoPopup);

  }

  private void setUpComboBox() {

    JComboBox selectSavedComboBox = new JComboBox<>();

    presenter.initializeSavedPanel();

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

  public void showView(){
    JFrame frame = new JFrame("TV Series Info Repo");
    frame.setContentPane(contentPane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    setUpSearchPanel();

    setUpSavedPanel();

    try {
      // Set System L&F
      UIManager.put("nimbusSelection", new Color(247,248,250));
      //UIManager.put("nimbusBase", new Color(51,98,140)); //This is redundant!

      for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }
    catch (Exception e) {
      System.out.println("Something went wrong with UI!");
    }
  }

  public String getSeriesName() {
    return searchTextField.getText();
  }

  public void setSearchResultTextPane(String text) {
    this.text = text;
    searchResultTextPane.setText(text);
    searchResultTextPane.setCaretPosition(0);
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
}
