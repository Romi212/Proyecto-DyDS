import database.DataBase;
import model.APIs.SearchPageAPI;
import model.APIs.SearchSeriesAPI;
import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import org.junit.Before;
import org.junit.Test;
import presenter.SeriesPresenter;
import stub.DataBaseStub;
import stub.SearchPageAPIStub;
import stub.SearchSeriesAPIStub;
import view.ScoredView;
import view.SearcherView;
import view.StoredView;
import view.TVSeriesSearcherWindow;

import javax.swing.*;
import javax.swing.plaf.metal.MetalComboBoxButton;

import static org.junit.Assert.assertEquals;

public class integrationTest {

    SearcherView searcherView;
    TVSeriesSearcherWindow mainWindow;
    ScoredView scoredView;
    StoredView storedView;

    @Before
    public void setUp(){
        SearchSeriesModel searchModel = new SearchSeriesModel(new SearchSeriesAPIStub());
        SearchWikiPageModel wikiModel = new SearchWikiPageModel(new SearchPageAPIStub());
        DataBaseModel dataBaseModel = new DataBaseModel(new DataBaseStub());
        SeriesPresenter presenter = new SeriesPresenter(searchModel, wikiModel, dataBaseModel);
        mainWindow = new TVSeriesSearcherWindow(presenter);
        presenter.start(mainWindow);
        searcherView = mainWindow.getSearchView();
        storedView = mainWindow.getStoredView();
        scoredView = mainWindow.getScoredView();

    }

    @Test
    public void testSearchSeries() throws InterruptedException {
        searcherView.setSearchTextField("Breaking Bad");
        searcherView.getSearchButton().doClick();
        Thread.sleep(2000);
        JMenuItem firstItem = (JMenuItem) searcherView.getResults().getComponent(0);
        System.out.println(firstItem.getText());
        assertEquals(firstItem.getText(), "<html><font face=\"arial\">★Title1: snippet1");
    }

    @Test
    public void testSearchWikiPage() throws InterruptedException {
        searcherView.setSearchTextField("Breaking Bad");
        searcherView.getSearchButton().doClick();
        Thread.sleep(2000);
        JMenuItem firstItem = (JMenuItem) searcherView.getResults().getComponent(0);
        firstItem.doClick();
        Thread.sleep(2000);
        System.out.println(searcherView.getSearchResultTextPane());
        assertEquals(searcherView.getSearchResultTextPane(),"<html>\n" +
                "  <head>\n" +
                "    \n" +
                "  </head>\n" +
                "  <body>\n" +
                "    EXAMPLE EXTRACT<a href=\"https://en.wikipedia.org/?curid=1\">https://en.wikipedia.org/?curid=1</a>\n" +
                "  </body>\n" +
                "</html>\n");
    }

    @Test
    public void testShowSavedSeries() throws InterruptedException {
        mainWindow.getTabbedPane().setSelectedIndex(1);
        Thread.sleep(2000);
        assertEquals(storedView.getStoredSeries().getItemAt(0).toString(), "title");
    }

    @Test
    public void testShowSavedExtract() throws InterruptedException {
        mainWindow.getTabbedPane().setSelectedIndex(1);
        Thread.sleep(2000);
        storedView.getStoredSeries().setSelectedIndex(0);
        Thread.sleep(2000);
        assertEquals( "<html>\n" + "  <head>\n" +  "    \n" + "  </head>\n" +"  <body>\n" +"    <font face=\"arial\">EXAMPLE EXTRACT</font>\n" +
                "  </body>\n" +
                "</html>\n",
                storedView.getSelectedSavedExtract());
    }

    @Test
    public void testShowScoredSeries() throws InterruptedException {
        mainWindow.getTabbedPane().setSelectedIndex(2);
        Thread.sleep(2000);
        assertEquals("title",scoredView.getScoresTable().getValueAt(0, 0));
    }
}
