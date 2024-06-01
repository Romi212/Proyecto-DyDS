package presenter;

import model.SearchSeriesModel;
import model.SearchWikiPageModel;

public class Main {
    public static void main(String[] args) {
        SearchSeriesModel searchModel = new SearchSeriesModel();
        SearchWikiPageModel wikiModel = new SearchWikiPageModel();
        SeriesPresenter presenter = new SeriesPresenter(searchModel, wikiModel);
        presenter.start();
    }
}
