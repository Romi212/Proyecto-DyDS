package presenter;

import model.APIs.SearchPageAPI;
import model.APIs.SearchSeriesAPI;
import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;

public class Main {
    public static void main(String[] args) {

        SearchSeriesModel searchModel = new SearchSeriesModel(new SearchSeriesAPI());
        SearchWikiPageModel wikiModel = new SearchWikiPageModel(new SearchPageAPI());
        DataBaseModel dataBaseModel = new DataBaseModel();
        SeriesPresenter presenter = new SeriesPresenter(searchModel, wikiModel, dataBaseModel);
        presenter.start();

    }
}
