package view;

import presenter.SeriesPresenter;

import javax.swing.*;
import java.awt.*;

public class TVSeriesSearcherWindow {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;
    private ScoredView scoredView;
    private StoredView storedView;
    private SearcherView searcherView;
    private SeriesPresenter presenter;

    public TVSeriesSearcherWindow(SeriesPresenter presenter) {
        storedView = new StoredView(presenter.getStorePresenter());
        searcherView = new SearcherView(presenter.getSearchPresenter());
        scoredView = new ScoredView(presenter.getScorePresenter());
        this.presenter = presenter;
    }

    public void showView() {
        JFrame frame = new JFrame("TV Series Info Repo");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.addTab("Search", searcherView.getContentPane());
        tabbedPane.addTab("Stored", storedView.getContentPane());
        tabbedPane.addTab("Scored", scoredView.getContentPane());

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

    public SearcherView getSearchView() {
        return searcherView;
    }

    public StoredView getStoredView() {
        return storedView;
    }

    public ScoredView getScoredView() {
        return scoredView;
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
        int result = JOptionPane.showConfirmDialog(storedView.getContentPane(), message, "Warning", JOptionPane.YES_NO_OPTION);
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
