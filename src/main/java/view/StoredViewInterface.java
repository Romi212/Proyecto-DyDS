package view;

import javax.swing.*;

public interface StoredViewInterface {
    void setUpView();

    void setSelectSavedComboBox(Object[] titles);

    String getSeletedSavedTitle();

    void setSelectedExtract(String extract);

    boolean existSelectedEntry();

    void emptySavedTextPane();

    String getSelectedSavedExtract();

    JPanel getContentPane();

    void setURL(String url);

    JComboBox getStoredSeries();
}
