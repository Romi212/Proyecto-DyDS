package stub;

import model.APIs.SearchPageAPInterface;
import model.APIs.SearchSeriesAPInterface;
import utils.WikiPage;

import java.util.ArrayList;

public class SearchSeriesAPIStub implements SearchSeriesAPInterface{


    @Override
    public ArrayList<WikiPage> searchSeries(String seriesName) {
        ArrayList<WikiPage> wikiPages = new ArrayList<>();
        WikiPage wikiPage = new WikiPage("Title1","id1","snippet1");
        WikiPage wikiPage2 = new WikiPage("Title2","id2","snippet2");
        WikiPage wikiPage3 = new WikiPage("Title3","id3","snippet3");
        wikiPages.add(wikiPage);
        wikiPages.add(wikiPage2);
        wikiPages.add(wikiPage3);
        return wikiPages;
    }
}
