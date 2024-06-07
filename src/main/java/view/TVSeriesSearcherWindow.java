package view;

import presenter.SeriesPresenter;

import javax.swing.*;
import java.awt.*;

public class TVSeriesSearcherWindow {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private ScoredViewInterface scoredViewInterface;
    private StoredViewInterface storedViewInterface;
    private SearcherViewInterface searcherViewInterface;
    private SeriesPresenter presenter;

    public TVSeriesSearcherWindow(SeriesPresenter presenter) {
        storedViewInterface = new StoredView(presenter.getStorePresenter());
        searcherViewInterface = new SearcherView(presenter.getSearchPresenter());
        scoredViewInterface = new ScoredView(presenter.getScorePresenter());
        this.presenter = presenter;
    }

    public void showView() {
        JFrame frame = new JFrame("TV Series Info Repo");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.addTab("Search", searcherViewInterface.getContentPane());
        tabbedPane.addTab("Stored", storedViewInterface.getContentPane());
        tabbedPane.addTab("Scored", scoredViewInterface.getContentPane());

        tabbedPane.addChangeListener(e -> presenter.changedTabs());

        try {
            // TODO: CHeck this
            UIManager.put("nimbusSelection", new Color(247, 248, 250));
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong with UI!");
        }
    }

    public SearcherViewInterface getSearchView() {
        return searcherViewInterface;
    }

    public StoredViewInterface getStoredView() {
        return storedViewInterface;
    }

    public ScoredViewInterface getScoredView() {
        return scoredViewInterface;
    }

    public void showSearchPanel() {
        tabbedPane.setSelectedIndex(0);
    }

    public int getSelectedTab() {
        return tabbedPane.getSelectedIndex();
    }

    public void showSuccessMessage( String message) {
        JOptionPane.showMessageDialog(contentPane, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showErrorMessage( String message) {
        JOptionPane.showMessageDialog(contentPane, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean askConfirmation( String message) {
        int result = JOptionPane.showConfirmDialog(storedViewInterface.getContentPane(), message, "Warning", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public void setWorkingStatus() {
        disableComponents(contentPane);
        disableComponents(tabbedPane);
    }

    private void disableComponents(Container container) {
        for (Component c : container.getComponents()) {
            c.setEnabled(false);
        }
    }

    public void setWatingStatus() {
        enableComponents(contentPane);
        enableComponents(tabbedPane);
    }

    private void enableComponents(Container container) {
        for (Component c : container.getComponents()) {
            c.setEnabled(true);
        }
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }
}
