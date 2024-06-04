package view;

import presenter.SeriesPresenter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class TVSeriesSearcherWindow {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;

    private ScoredView scoredView;

    private StoredView storedView;

    private SearcherView searcherView;

    private SeriesPresenter presenter;

    public TVSeriesSearcherWindow(SeriesPresenter presenter) {
        storedView = new StoredView(presenter);
        searcherView = new SearcherView(presenter);
        scoredView = new ScoredView(presenter);
        this.presenter = presenter;

    }
    public void showView(){
        JFrame frame = new JFrame("TV Series Info Repo");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.addTab("Search", searcherView.getContentPane());
        tabbedPane.addTab("Stored", storedView.getContentPane());
        tabbedPane.addTab("Scored", scoredView.getContentPane());

       // setUpSavedPanel();
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                presenter.changedTabs();
            }
        });

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

    public SearcherView getSearchView() {
        return searcherView;
    }

    public StoredView getStoredView() {
        return storedView;
    }

    public ScoredView getScoredView() { return scoredView; }

    public void showSearchPanel() {
        tabbedPane.setSelectedIndex(0);
    }

    public int getSelectedTab() {
        return tabbedPane.getSelectedIndex();
    }

    public void showSuccessMessage(Component panel, String message, String title) {
        JOptionPane.showMessageDialog(panel,message,title, JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorMessage(Component panel, String message, String title) {
        JOptionPane.showMessageDialog(panel, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
