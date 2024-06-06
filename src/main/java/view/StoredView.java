package view;

import presenter.SeriesPresenter;
import presenter.StorePresenter;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class StoredView {
    private JPanel storedPanel;
    private JComboBox selectSavedComboBox;
    private JTextPane showSavedTextPane;
    private JButton linkButton;
    private StorePresenter presenter;


    public StoredView( StorePresenter presenter) {
        this.presenter = presenter;
    }


    public void setUpView(){
        setUpComboBox();
        setUpTextPane();
        setUpPopupMenu();
        setUpLinkButton();
    }

    private void setUpLinkButton() {
        linkButton.setVisible(false);
        linkButton.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI(linkButton.getText()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void setUpTextPane() { showSavedTextPane.setContentType("text/html");
    showSavedTextPane.setEditable(true);
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

    private void setUpComboBox() {  presenter.initializeSavedPanel();  }

    public void setSelectSavedComboBox(Object[] titles){

        selectSavedComboBox.setModel(new DefaultComboBoxModel(titles));
        selectSavedComboBox.addActionListener(actionEvent -> presenter.getSavedExtract());
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
        linkButton.setVisible(false);
    }
    public String getSelectedSavedExtract() {
        return showSavedTextPane.getText();
    }
    public JPanel getContentPane() {
        return storedPanel;
    }

    public void setURL(String url) {
        linkButton.setText(url);
        linkButton.setVisible(true);
    }

    public JComboBox getStoredSeries() {
        return selectSavedComboBox;
    }
}
