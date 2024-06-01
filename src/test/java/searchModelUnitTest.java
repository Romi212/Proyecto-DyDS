import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import org.junit.Before;

public class searchModelUnitTest {

    DataBaseModel dataBaseModel ;
    SearchSeriesModel searchModel;
    SearchWikiPageModel wikiModel;



    @Before
    public void setUp(){
        dataBaseModel = new DataBaseModel();
        searchModel = new SearchSeriesModel();
        wikiModel = new SearchWikiPageModel();
    }


}
