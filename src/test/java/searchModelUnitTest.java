import model.APIs.SearchPageAPI;
import model.APIs.SearchSeriesAPI;
import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import org.junit.Before;
import org.junit.Test;
import stub.DataBaseStub;
import stub.SearchPageAPIStub;
import stub.SearchSeriesAPIStub;
import utils.WikiPage;

public class searchModelUnitTest {

    DataBaseModel dataBaseModel ;
    SearchSeriesModel searchModel;
    SearchWikiPageModel wikiModel;



    @Before
    public void setUp(){
        dataBaseModel = new DataBaseModel(new DataBaseStub());
        searchModel = new SearchSeriesModel(new SearchSeriesAPIStub());
        wikiModel = new SearchWikiPageModel(new SearchPageAPIStub());
    }

    //TESTS DE SEARCHMODEL
    @Test
    public void testSearchSeries(){
        try {
            searchModel.searchSeries("Breaking Bad");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert searchModel.getSearchResults().get(0).getTitle().equals("Title1");
        assert searchModel.getSearchResults().get(1).getTitle().equals("Title2");
        assert searchModel.getSearchResults().get(2).getTitle().equals("Title3");
    }

    //TESTS DE WIKIMODEL
    @Test
    public void testSearchWikiPage() {
        WikiPage wikiPage = new WikiPage("Title1", "001", "snippet1");
        try {
            wikiModel.searchPageExtract(wikiPage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assert wikiModel.getExtract().equals("EXAMPLE EXTRACT");
        assert wikiModel.getUrl().equals("https://en.wikipedia.org/?curid=001");
    }

    //TESTS DE DATABASEMODEL
    @Test
    public void testGetSavedSeries(){
        dataBaseModel.getSavedTitles();
        assert dataBaseModel.getSavedTitlesList()[0].equals("title");
    }
    @Test
    public void testGetSavedExtract(){
        dataBaseModel.getSavedExtract("title");
        assert dataBaseModel.getExtract().equals("EXAMPLE EXTRACT");
    }

    @Test
    public void testGetSavedScoredSeries(){
        dataBaseModel.getScoredSeries();
        assert dataBaseModel.getScoredSeriesList().get(0).getTitle().equals("title");
    }

    @Test
    public void testGetScore(){
        assert dataBaseModel.getScore("title") == 0;

    }
}
