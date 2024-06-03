import model.APIs.SearchPageAPI;
import model.APIs.SearchSeriesAPI;
import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import org.junit.Before;
import org.junit.Test;
import stub.SearchPageAPIStub;
import stub.SearchSeriesAPIStub;
import utils.WikiPage;

public class searchModelUnitTest {

    DataBaseModel dataBaseModel ;
    SearchSeriesModel searchModel;
    SearchWikiPageModel wikiModel;



    @Before
    public void setUp(){
        dataBaseModel = new DataBaseModel();
        searchModel = new SearchSeriesModel(new SearchSeriesAPIStub());
        wikiModel = new SearchWikiPageModel(new SearchPageAPIStub());
    }

    @Test
    public void testSearchSeries(){
        searchModel.searchSeries("Breaking Bad");
        assert searchModel.getSearchResults().get(0).getTitle().equals("Title1");
        assert searchModel.getSearchResults().get(1).getTitle().equals("Title2");
        assert searchModel.getSearchResults().get(2).getTitle().equals("Title3");
    }

    @Test
    public void testSearchWikiPage(){
        WikiPage wikiPage = new WikiPage("Title1","id1","snippet1");
        wikiModel.searchPageExtract(wikiPage);
        assert wikiModel.getExtract().equals("EXAMPLE EXTRA");
    }

}
