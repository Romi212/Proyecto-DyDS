package presenter;

import dyds.tvseriesinfo.fulllogic.DataBase;
import dyds.tvseriesinfo.fulllogic.SearchResult;
import model.SearchModel;
import view.SearchView;

import java.util.ArrayList;

public class SeriesPresenter {

    SearchModel model;
    SearchView view;

    Thread taskThread;

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

        taskThread = new Thread(() -> {
            String seriesName = view.getSeriesName();

            //TODO: Controlar vacia
            ArrayList<SearchResult> PagesFound = model.searchSeries(seriesName);

            view.showResults(PagesFound);
        });

        taskThread.start();

    }

    public void getSelectedExtract(SearchResult selectedResult){
        String extract = model.searchPageExtract(selectedResult);
        view.setSearchResultTextPane(extract);
    }
}
