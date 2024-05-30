package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
import dyds.tvseriesinfo.fulllogic.SearchResult;
import model.SearchModel;
import view.SearchView;

import java.util.ArrayList;

public class SeriesPresenter {

    SearchModel model;
    SearchView view;

    public SeriesPresenter(SearchModel model) {
        this.model = model;
        model.setPresenter(this);
    }

    public void start(){
        view = new SearchView(this);
        view.showView();


        DataBase.loadDatabase();
        //DataBase.saveInfo("test", "sarasa");


        //System.out.println(DataBase.getExtract("test"));
        //System.out.println(DataBase.getExtract("nada"));
    }

    public void searchSeries() {
        String seriesName = view.getSeriesName();

        //Controlar vacia
        ArrayList<SearchResult> PagesFound = model.searchSeries(seriesName);

    }

    public void getSelectedExtract(SearchResult selectedResult){
        String extract = model.searchPageExtract(selectedResult);
        view.setSearchResultTextPane(extract);
    }
}
