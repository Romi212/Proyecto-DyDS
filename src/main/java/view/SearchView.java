package view;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import dyds.tvseriesinfo.fulllogic.DataBase;
import dyds.tvseriesinfo.fulllogic.SearchResult;
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

  DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
  String selectedResultTitle = null; //For storage purposes, it may not coincide with the searched term (see below)
  String text = ""; //Last searched text! this variable is central for everything

  public SearchView( SeriesPresenter presenter) {

    this.presenter = presenter;

    // Carga los titulos guardados en la segunda pestaña
    selectSavedComboBox.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));

    // Establece que el texto de los JTextPane sea HTML
    searchResultTextPane.setContentType("text/html");
    showSavedTextPane.setContentType("text/html");


    // this is needed to open a link in the browser

    //textField1.addActionListener(actionEvent -> {System.out.println("ACCION!!!");});
   // System.out.println("TYPED!!!");
    searchTextField.addPropertyChangeListener(propertyChangeEvent -> {
              System.out.println("TYPED!!!");
    });

    //ToAlberto: They told us that you were having difficulties understanding this code,
    //Don't panic! We added several helpful comments to guide you through it ;)

    // From here on is where the magic happends: querying wikipedia, showing results, etc.
    searchButton.addActionListener(e -> new Thread(() -> {
              //This may take some time, dear user be patient in the meanwhile!
              setWorkingStatus();
              // get from service

              presenter.searchSeries();



              //Now you can keep searching stuff!
              setWatingStatus();
    }).start());

    saveLocallyButton.addActionListener(actionEvent -> {
      if(text != ""){
        // save to DB  <o/
        DataBase.saveInfo(selectedResultTitle.replace("'", "`"), text);  //Dont forget the ' sql problem
        selectSavedComboBox.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
      }
    });

    selectSavedComboBox.addActionListener(actionEvent -> showSavedTextPane.setText(textToHtml(DataBase.getExtract(selectSavedComboBox.getSelectedItem().toString()))));

    JPopupMenu storedInfoPopup = new JPopupMenu();

    JMenuItem deleteItem = new JMenuItem("Delete!");
    deleteItem.addActionListener(actionEvent -> {
        if(selectSavedComboBox.getSelectedIndex() > -1){
          DataBase.deleteEntry(selectSavedComboBox.getSelectedItem().toString());
          selectSavedComboBox.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
          showSavedTextPane.setText("");
        }
    });
    storedInfoPopup.add(deleteItem);

    JMenuItem saveItem = new JMenuItem("Save Changes!");
    saveItem.addActionListener(actionEvent -> {
        // save to DB  <o/
        DataBase.saveInfo(selectSavedComboBox.getSelectedItem().toString().replace("'", "`"), showSavedTextPane.getText());  //Dont forget the ' sql problem
        //comboBox1.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
    });
    storedInfoPopup.add(saveItem);

    showSavedTextPane.setComponentPopupMenu(storedInfoPopup);


  }


  private void setWorkingStatus() {
    for(Component c: this.searchPanel.getComponents()) c.setEnabled(false);
    searchResultTextPane.setEnabled(false);
  }

  private void setWatingStatus() {
    for(Component c: this.searchPanel.getComponents()) c.setEnabled(true);
    searchResultTextPane.setEnabled(true);
  }

  public void showResults(ArrayList<SearchResult> searchResults){
    JPopupMenu searchOptionsMenu = new JPopupMenu("Search Results");
    for(SearchResult searchResult : searchResults){
      searchResult.addActionListener(actionEvent -> {
        selectedResultTitle = searchResult.title;
        presenter.getSelectedExtract(searchResult);

      });
      searchOptionsMenu.add(searchResult);
    }
  }

  public void showView(){
    JFrame frame = new JFrame("TV Series Info Repo");
    frame.setContentPane(contentPane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

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
    searchResultTextPane.setText(text);
    searchResultTextPane.setCaretPosition(0);
  }

  public static String textToHtml(String text) {

    StringBuilder builder = new StringBuilder();

    builder.append("<font face=\"arial\">");

    String fixedText = text
            .replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..

    builder.append(fixedText);

    builder.append("</font>");

    return builder.toString();
  }
}
