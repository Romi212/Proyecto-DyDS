package stub;

import model.APIs.SearchPageAPInterface;
import utils.WikiPage;

public class SearchPageAPIStub implements SearchPageAPInterface {
    @Override
    public WikiPage getExtract(WikiPage wikiPage) {
        wikiPage.setExtract("EXAMPLE EXTRACT");
        return wikiPage;
    }
}
