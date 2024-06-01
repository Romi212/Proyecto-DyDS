package presenter;

import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import view.TVSeriesSearcherView;

public class Main {
    public static void main(String[] args) {
        SearchSeriesModel searchModel = new SearchSeriesModel();
        SearchWikiPageModel wikiModel = new SearchWikiPageModel();
        DataBaseModel dataBaseModel = new DataBaseModel();
        SeriesPresenter presenter = new SeriesPresenter(searchModel, wikiModel, dataBaseModel);
        presenter.start();

    }
}
