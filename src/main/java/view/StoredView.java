package view;

import presenter.SeriesPresenter;
import presenter.StorePresenter;

import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class StoredView implements StoredViewInterface {
    private JPanel storedPanel;
    private JComboBox selectSavedComboBox;
    private JTextPane showSavedTextPane;
    private JButton linkButton;
    private StorePresenter presenter;


    public StoredView( StorePresenter presenter) {
        this.presenter = presenter;
    }


    @Override
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

    @Override
    public void setSelectSavedComboBox(Object[] titles){

        selectSavedComboBox.setModel(new DefaultComboBoxModel(titles));
        selectSavedComboBox.addActionListener(actionEvent -> presenter.getSavedExtract());
    }
    @Override
    public String getSeletedSavedTitle() {
        return selectSavedComboBox.getSelectedItem().toString();
    }
    @Override
    public void setSelectedExtract(String extract) {
        SwingUtilities.invokeLater(() -> {
            showSavedTextPane.setText(extract);
            showSavedTextPane.setCaretPosition(0);
        });
    }

    @Override
    public boolean existSelectedEntry() {
        return (selectSavedComboBox.getSelectedIndex() > -1);
    }
    @Override
    public void emptySavedTextPane() {
        showSavedTextPane.setText("");
        linkButton.setVisible(false);
    }
    @Override
    public String getSelectedSavedExtract() {
        return showSavedTextPane.getText();
    }
    @Override
    public JPanel getContentPane() {
        return storedPanel;
    }

    @Override
    public void setURL(String url) {
        linkButton.setText(url);
        linkButton.setVisible(true);
    }

    @Override
    public JComboBox getStoredSeries() {
        return selectSavedComboBox;
    }
}
