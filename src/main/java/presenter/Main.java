package presenter;

import database.DataBase;
import model.APIs.SearchPageAPI;
import model.APIs.SearchSeriesAPI;
import model.DataBaseModel;
import model.SearchSeriesModel;
import model.SearchWikiPageModel;
import view.TVSeriesSearcherWindow;

public class Main {
    public static void main(String[] args) {

        SearchSeriesModel searchModel = new SearchSeriesModel(new SearchSeriesAPI());
        SearchWikiPageModel wikiModel = new SearchWikiPageModel(new SearchPageAPI());
        DataBase dataBase = new DataBase();
        DataBaseModel dataBaseModel = new DataBaseModel(dataBase);
        SeriesPresenter presenter = new SeriesPresenter(searchModel, wikiModel, dataBaseModel);
        TVSeriesSearcherWindow mainWindow = new TVSeriesSearcherWindow(presenter);
        presenter.start(mainWindow);

    }
}
