package view;

import presenter.SeriesPresenter;

import javax.swing.*;
import java.awt.*;

public class TVSeriesSearcherView {
    private JTabbedPane tabbedPane;
    private JPanel contentPane;

    private StoredView storedView;

    private SearcherView searcherView;

    public TVSeriesSearcherView(SeriesPresenter presenter) {
        storedView = new StoredView(presenter);
        searcherView = new SearcherView(presenter);

    }
    public void showView(){
        JFrame frame = new JFrame("TV Series Info Repo");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        tabbedPane.addTab("Search", searcherView.getContentPane());
        tabbedPane.addTab("Stored", storedView.getContentPane());

       // setUpSavedPanel();

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
}
