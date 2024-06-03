package stub;

import model.APIs.SearchPageAPInterface;
import utils.WikiPage;

public class SearchPageAPIStub implements SearchPageAPInterface {
    @Override
    public String getExtract(WikiPage wikiPage) {
        return "EXAMPLE EXTRA";
    }
}
